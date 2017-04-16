package com.fixit.core.synchronization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.fixit.core.config.AppConfig;

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
    private final static String PREF_TABLE_NAMES = "pref_table_names";
    private final static String PREF_PREFIX_SYNC_ACTIONS = "pref_sync_actions_for_";
    private final static String PREF_PREFIX_ACTION_DATE = "pref_";

    private final Map<String, Set<SynchronizationAction>> synchronizationHistory = new HashMap<>();
    private final SharedPreferences preferences;
    private final Date lastSyncDate;
    public final boolean isReadyForSync;


    public SynchronizationHistory(Context context) {
        this.preferences = context.getSharedPreferences(PREF_GROUP_SYNC_HISTORY, Context.MODE_PRIVATE);

        long syncMinIntervalMs = AppConfig.getInt(context, AppConfig.KEY_SYNCHRONIZATION_MIN_INTERVAL_MS, 3600000);
        long lastSyncTime = preferences.getLong(PREF_LAST_SYNC, 0);
        long syncAfter = lastSyncTime + syncMinIntervalMs;

        lastSyncDate = new Date(lastSyncTime);
        isReadyForSync = System.currentTimeMillis() > syncAfter;

        if(isReadyForSync) {
            load();
        }
    }

    public Map<String, Set<SynchronizationAction>> getHistory() {
        return synchronizationHistory;
    }

    private void load() {
        Set<String> tables = preferences.getStringSet(PREF_TABLE_NAMES, null);

        if (tables != null) {
            for (String table : tables) {
                Set<String> actions = preferences.getStringSet(PREF_PREFIX_SYNC_ACTIONS + table, null);

                if (actions != null) {
                    Set<SynchronizationAction> actionHistory = new HashSet<>();
                    for (String action : actions) {
                        Date actionDate = new Date(preferences.getLong(PREF_PREFIX_ACTION_DATE + action + "_for_" + table, 0L));

                        actionHistory.add(new SynchronizationAction(action, actionDate));
                    }
                    synchronizationHistory.put(table, actionHistory);
                }
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    public void update(List<SynchronizationResult> results) {
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> tables = new HashSet<>(results.size());
        for(SynchronizationResult result : results) {
            if(result.isSupported()) {
                String table = result.getName();
                tables.add(table);

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
        editor.putStringSet(PREF_TABLE_NAMES, tables);

        editor.putLong(PREF_LAST_SYNC, new Date().getTime());
        // should already be in background thread so just commit.
        editor.commit();
    }

    public Date getLastUpdate() {
        return lastSyncDate;
    }

}
