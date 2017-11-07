package com.fixit.synchronization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.fixit.config.AppConfig;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationHistory {

    private final static String PREF_GROUP_SYNC_HISTORY = "sync_history_prefs";
    private final static String PREF_LAST_SYNC = "pref_last_sync";
    private final static String PREF_PREFIX_SYNC_ACTIONS = "pref_sync_actions_for_";
    private final static String PREF_PREFIX_ACTION_DATE = "pref_";

    private final Map<String, Set<SynchronizationAction>> synchronizationHistory = new HashMap<>();
    private final SharedPreferences preferences;
    private final Date lastSyncDate;
    private final String[] supportedTables;
    public final boolean isReadyForSync;


    public SynchronizationHistory(Context context, String[] supportedTables) {
        this.preferences = context.getSharedPreferences(PREF_GROUP_SYNC_HISTORY, Context.MODE_PRIVATE);
        this.supportedTables = supportedTables;

        long syncMinIntervalMs = AppConfig.getInteger(context, AppConfig.KEY_SYNCHRONIZATION_MIN_INTERVAL_MS, 3600000);
        long lastSyncTime = preferences.getLong(PREF_LAST_SYNC, 0);
        long syncAfter = lastSyncTime + syncMinIntervalMs;

        lastSyncDate = new Date(lastSyncTime);
        isReadyForSync = System.currentTimeMillis() > syncAfter;

        if(isReadyForSync) {
            FILog.i(Constants.LOG_TAG_SYNCHRONIZATION, "Ready for synchronization");
            load();
        } else {
            FILog.i(Constants.LOG_TAG_SYNCHRONIZATION, "Not ready for synchronization");
        }
    }

    public Map<String, Set<SynchronizationAction>> getHistory() {
        return synchronizationHistory;
    }

    private void load() {
        for (String table : supportedTables) {
            Set<String> actions = preferences.getStringSet(PREF_PREFIX_SYNC_ACTIONS + table, null);

            if (actions != null && actions.size() > 0) {
                Set<SynchronizationAction> actionHistory = new HashSet<>();
                for (String action : actions) {
                    Date actionDate = new Date(preferences.getLong(PREF_PREFIX_ACTION_DATE + action + "_for_" + table, 0L));

                    actionHistory.add(new SynchronizationAction(action, actionDate));
                }
                synchronizationHistory.put(table, actionHistory);
            } else {
                synchronizationHistory.put(table, Collections.<SynchronizationAction>emptySet());
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    public void update(List<SynchronizationResult> results) {
        SharedPreferences.Editor editor = preferences.edit();

        for(SynchronizationResult result : results) {
            if(result.isSupported()) {
                String table = result.getName();

                SynchronizationResult.Result[] resultData = result.getResults();
                Set<String> actions = preferences.getStringSet(PREF_PREFIX_SYNC_ACTIONS + table, null);

                // only initialize actions when needed.
                if(actions == null) {
                    actions = new HashSet<>(resultData.length);
                }

                for (SynchronizationResult.Result data : resultData) {
                    SynchronizationAction action = data.getAction();
                    String actionName = action.getAction();
                    actions.add(actionName);
                    editor.putLong(PREF_PREFIX_ACTION_DATE + actionName + "_for_" + table, action.getDate().getTime());
                }
                editor.putStringSet(PREF_PREFIX_SYNC_ACTIONS + table, actions);
            }
        }
        editor.putLong(PREF_LAST_SYNC, new Date().getTime());

        // should already be in background thread so just commit.
        editor.commit();
    }

    public Date getLastUpdate() {
        return lastSyncDate;
    }

    public static void clear(Context context, boolean apply) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_GROUP_SYNC_HISTORY, Context.MODE_PRIVATE).edit().clear();
        if(apply) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
