package com.fixit.factories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.SparseArray;

import com.fixit.caching.CacheCallback;
import com.fixit.caching.TradesmanCache;
import com.fixit.data.JobReason;
import com.fixit.data.Order;
import com.fixit.data.OrderData;
import com.fixit.data.Profession;
import com.fixit.data.Tradesman;
import com.fixit.database.JobReasonDAO;
import com.fixit.database.ProfessionDAO;
import com.fixit.rest.APIError;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kostyantin on 8/27/2017.
 */

public class OrderFactory {

    private final TradesmanCache mTradesmanCache;
    private final ProfessionDAO mProfessionDao;
    private final JobReasonDAO mJobReasonDao;

    private final Object mLock = new Object();

    private OrderGenerator mOrderGenerator;

    public OrderFactory(TradesmanCache tradesmanCache, ProfessionDAO professionDao, JobReasonDAO jobReasonDao) {
        mTradesmanCache = tradesmanCache;
        mProfessionDao = professionDao;
        mJobReasonDao = jobReasonDao;
    }

    public void createOrders(Context context, OrderData[] orderData, OrderFactoryCallback orderFactoryCallback) {
        mOrderGenerator = new OrderGenerator(context, orderData, orderFactoryCallback);
        mOrderGenerator.start();
    }

    public void cleanGenerator() {
        if(mOrderGenerator != null) {
            mOrderGenerator.cancel();
            mOrderGenerator = null;
        }
    }

    public interface OrderFactoryCallback extends GeneralServiceErrorCallback {
        void onOrdersCreated(Order[] orders);
    }

    private class OrderGenerator extends Thread implements CacheCallback<Tradesman> {

        private final WeakReference<Context> contextReference;
        private final OrderData[] orderData;
        private final OrderFactoryCallback callback;
        private final Handler handler;

        private final SparseArray<Tradesman> mappedTradesmen = new SparseArray<>();
        private final SparseArray<JobReason> mappedJobReasons = new SparseArray<>();
        private final SparseArray<Profession> mappedProfessions = new SparseArray<>();

        private volatile boolean cancelled = false;

        public OrderGenerator(Context context, OrderData[] orderData, OrderFactoryCallback callback) {
            this.contextReference = new WeakReference<>(context);
            this.orderData = orderData;
            this.callback = callback;
            this.handler = new Handler(Looper.getMainLooper());
        }

        public void cancel() {
            this.cancelled = true;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            FILog.i(Constants.LOG_TAG_ORDER_FACTORY, "loading " + orderData.length + " orders");

            Set<String> tradesmenIds = new HashSet<>();
            Set<String> professionIds = new HashSet<>();
            Set<String> jobReasonIds = new HashSet<>();
            for(OrderData orderData : this.orderData) {
                for(String tradesman : orderData.getTradesmen()) {
                    tradesmenIds.add(tradesman);
                }
                professionIds.add(String.valueOf(orderData.getProfessionId()));
                for(long jobReason : orderData.getJobReasons()) {
                    jobReasonIds.add(String.valueOf(jobReason));
                }
            }

            if(!cancelled) {
                Context context = contextReference.get();
                if (context != null) {
                    mTradesmanCache.get(context, this, tradesmenIds.toArray(new String[tradesmenIds.size()]));

                    if(jobReasonIds.size() > 0) {
                        JobReason[] jobReasons = mJobReasonDao.findIn(JobReasonDAO.KEY_ID, jobReasonIds.toArray(new String[jobReasonIds.size()]));
                        for (JobReason jobReason : jobReasons) {
                            mappedJobReasons.put((int) jobReason.getId(), jobReason);
                        }
                    }

                    Profession[] professions = mProfessionDao.findIn(ProfessionDAO.KEY_ID, professionIds.toArray(new String[professionIds.size()]));
                    synchronized (mLock) {
                        for (Profession profession : professions) {
                            mappedProfessions.put((int) profession.getId(), profession);
                        }
                    }

                    if(isReadyForGeneration()) {
                        generateOrders();
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onUnexpectedErrorOccurred("No context while generating orders", null);
                        }
                    });
                }
            }
        }

        @Override
        public void onResult(final Tradesman[] results) {
            if(!cancelled) {
                new Thread() {
                    @Override
                    public void run() {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                        synchronized (mLock) {
                            for (Tradesman tradesman : results) {
                                mappedTradesmen.put(tradesman.get_id().hashCode(), tradesman);
                            }
                        }

                        if(isReadyForGeneration()) {
                            generateOrders();
                        }
                    }
                }.start();
            }
        }

        private boolean isReadyForGeneration() {
            synchronized (mLock) {
                return !cancelled && mappedTradesmen.size() > 0 && mappedProfessions.size() > 0;
            }
        }

        private void generateOrders() {
            final Order[] orders = new Order[orderData.length];
            for (int i = 0; i < orders.length; i++) {

                if (cancelled) {
                    break;
                }

                OrderData orderData = this.orderData[i];

                String[] tradesmenIds = orderData.getTradesmen();
                Tradesman[] tradesmen = new Tradesman[tradesmenIds.length];
                for (int j = 0; j < tradesmen.length; j++) {
                    tradesmen[j] = mappedTradesmen.get(tradesmenIds[j].hashCode());
                }

                long[] jobReasonsIds = orderData.getJobReasons();
                JobReason[] jobReasons = new JobReason[jobReasonsIds.length];
                for (int j = 0; j < jobReasons.length; j++) {
                    jobReasons[j] = mappedJobReasons.get((int) jobReasonsIds[j]);
                }

                Profession profession = mappedProfessions.get((int) orderData.getProfessionId());

                orders[i] = new Order(orderData.get_id(), orderData.getLocation(), profession, tradesmen, jobReasons, orderData.getComment(), orderData.getCreatedAt());
            }

            if (!cancelled) {
                handler.post(() -> callback.onOrdersCreated(orders));
            }
        }

        @Override
        public void onUnexpectedErrorOccurred(final String msg, final Throwable t) {
            if(!cancelled) {
                handler.post(() -> callback.onUnexpectedErrorOccurred(msg, t));
            }
        }

        @Override
        public void onAppServiceError(final List<APIError> errors) {
            if(!cancelled) {
                handler.post(() -> callback.onAppServiceError(errors));
            }
        }

        @Override
        public void onServerError() {
            if(!cancelled) {
                handler.post(() -> callback.onServerError());
            }
        }
    }

}
