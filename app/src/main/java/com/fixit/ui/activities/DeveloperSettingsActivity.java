package com.fixit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.BaseApplication;
import com.fixit.app.R;
import com.fixit.config.AppConfig;
import com.fixit.controllers.OrderController;
import com.fixit.data.OrderData;
import com.fixit.feedback.OrderFeedbackFlowManager;
import com.fixit.notifications.OrderNotificationManager;
import com.fixit.utils.GlobalPreferences;

/**
 * Created by Kostyantin on 7/5/2017.
 */

public class DeveloperSettingsActivity extends BaseDeveloperSettingsActivity {

    private final static EditableConfiguration[] EDITABLE_CONFIGURATIONS = {
            new EditableConfiguration(AppConfig.KEY_SERVER_API_BASE_URL, ConfigurationType.STRING)
    };

    @Override
    public EditableConfiguration[] getEditableConfigurations() {
        for(EditableConfiguration configuration : EDITABLE_CONFIGURATIONS) {
            Object value = null;
            switch (configuration.type) {
                case STRING:
                    value = AppConfig.getString(this, configuration.key, null);
                    break;
                case INT:
                    value = AppConfig.getInteger(this, configuration.key, null);
                    break;
                case BOOLEAN:
                    value = AppConfig.getBoolean(this, configuration.key, null);
                    break;
            }
            configuration.setValue(value);
        }
        return EDITABLE_CONFIGURATIONS;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.layout.layout_search_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.developer_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_ovderrides:
                AppConfig.Overrides.clearOverrides(this);
                notifySettingsChanged();

                return true;
            case R.id.restart:
                ((BaseApplication) getApplication()).onDeveloperSettingsChanged();
                restartApp(false);

                return true;
            case R.id.send_order_notification:
                new MaterialDialog.Builder(this)
                        .items("immediate", "delayed")
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        sendOrderNotification(OrderFeedbackFlowManager.FLOW_CODE_IMMEDIATE);
                                        return true;
                                    case 1:
                                        sendOrderNotification(OrderFeedbackFlowManager.FLOW_CODE_DELAYED);
                                        return true;
                                }
                                return false;
                            }
                        })
                        .positiveText(R.string.done)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendOrderNotification(int flowCode) {
        String orderId = GlobalPreferences.getLastOrderId(this);
        if(!TextUtils.isEmpty(orderId)) {
            OrderController orderController = new OrderController((BaseApplication) getApplication(), this);
            OrderData latestOrder = orderController.getLatestOrder();

            if(latestOrder != null) {
                OrderNotificationManager.registerOrderFeedbackNotification(this, latestOrder.getId(), true, flowCode);
            } else {
                notifyUser("Could not find order with id " + orderId + ", create a new order and try again");
            }
        } else {
            notifyUser("Cannot send notification without creating at least one order");
        }
    }

    @Override
    public void onSettingChanged(EditableConfiguration configuration) {
        switch (configuration.type) {
            case BOOLEAN:
                AppConfig.Overrides.override(this, configuration.key, configuration.getValue().asBoolean());
                break;
            case INT:
                AppConfig.Overrides.override(this, configuration.key, configuration.getValue().asInteger());
                break;
            case STRING:
                AppConfig.Overrides.override(this, configuration.key, configuration.getValue().asString());
                break;
        }
    }

    @Override
    public Class<?> getLoginActivity() {
        return null;
    }

    @Override
    public void restartApp(boolean skipSplash) {
        startActivity(new Intent(this, SplashActivity.class));
        finishAffinity();
    }
}
