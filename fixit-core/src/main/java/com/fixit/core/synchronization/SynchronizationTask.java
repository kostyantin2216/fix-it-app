package com.fixit.core.synchronization;

import android.content.Context;
import android.os.Process;

import com.fixit.core.data.DataModelObject;
import com.fixit.core.data.Profession;
import com.fixit.core.database.CommonDAO;
import com.fixit.core.factories.DAOFactory;
import com.fixit.core.rest.apis.SynchronizationServiceAPI;
import com.fixit.core.rest.requests.data.SynchronizationRequestData;
import com.fixit.core.rest.responses.APIResponse;
import com.fixit.core.rest.responses.APIResponseHeader;
import com.fixit.core.rest.responses.data.SynchronizationResponseData;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationTask extends Thread {

    private final static String[] SYNCHRONIZATION_TARGETS = new String[] {
            Profession.class.getSimpleName().toLowerCase()
    };

    private final SynchronizationCallback mCallback;
    private final SynchronizationHistory mHistory;
    private final SynchronizationServiceAPI mServiceApi;
    private final DAOFactory mDaoFactory;

    public SynchronizationTask(Context context, SynchronizationServiceAPI api, DAOFactory daoFactory, SynchronizationCallback callback) {
        this.mServiceApi = api;
        this.mCallback = callback;
        this.mDaoFactory = daoFactory;
        this.mHistory = new SynchronizationHistory(context);
    }

    public boolean isReadyForSynchronization() {
        return false;// TODO: change to: mHistory.isReadyForSync;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Map<String, Set<SynchronizationAction>> history = mHistory.getHistory();
        boolean isFirstSynchronization = history.isEmpty();

        if(isFirstSynchronization) {
            // Tell the server what data this version of the app supports.
            for(String target : SYNCHRONIZATION_TARGETS) {
                history.put(target, Collections.<SynchronizationAction>emptySet());
            }
        }

        try {
            SynchronizationRequestData requestData = new SynchronizationRequestData(mHistory.getLastUpdate(), history);
            APIResponse<SynchronizationResponseData> response = mServiceApi.synchronize(requestData).execute().body();

            APIResponseHeader header = response.getHeader();
            if(header.hasErrors()) {
                mCallback.onSynchronizationError(header.getErrors().get(0).getDescription(), null);
            } else {
                synchronize(response.getData());
            }
        } catch (IOException e) {
            mCallback.onSynchronizationError("Could not connect to server", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void synchronize(SynchronizationResponseData responseData) {
        List<SynchronizationResult> syncResults = responseData.getSynchronizationResults();
        for(SynchronizationResult syncResult : syncResults) {
            if(syncResult.isSupported()) {
                CommonDAO<DataModelObject> dao = mDaoFactory.createDaoForDmoName(syncResult.getName());
                if (dao != null) {
                    SynchronizationResult.Result[] results = syncResult.getResults();
                    for (SynchronizationResult.Result result : results) {
                        SynchronizationAction action = result.getAction();
                        switch (action.getActionEnum()) {
                            case OVERRIDE:
                                dao.truncate();
                            case INSERT:
                                dao.insert(result.getData());
                                break;
                            case UPDATE:
                                dao.updateAll(result.getData());
                                break;
                            case DELETE:
                                Set<Long> ids = result.getIds();
                                for (Long id : ids) {
                                    dao.delete(String.valueOf(id));
                                }
                                break;
                        }
                    }
                }
            }
        }
        mHistory.update(syncResults);
        mCallback.onSynchronizationComplete();
    }

    public interface SynchronizationCallback {
        void onSynchronizationComplete();
        void onSynchronizationError(String msg, Throwable t);
    }

}
