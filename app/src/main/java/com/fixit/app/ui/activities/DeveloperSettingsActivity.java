package com.fixit.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fixit.app.R;
import com.fixit.core.BaseApplication;
import com.fixit.core.config.AppConfig;
import com.fixit.core.ui.activities.BaseDeveloperSettingsActivity;

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
                    value = AppConfig.getInt(this, configuration.key, null);
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
        setToolbar(R.layout.layout_toolbar);
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
            default:
                return super.onOptionsItemSelected(item);
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
        //
        startActivity(new Intent(this, SplashActivity.class));
        finishAffinity();
    }
}
