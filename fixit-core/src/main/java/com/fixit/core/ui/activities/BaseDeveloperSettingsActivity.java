package com.fixit.core.ui.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fixit.core.R;
import com.fixit.core.config.AppConfig;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.ui.adapters.DeveloperSettingsAdapter;
import com.fixit.core.utils.CommonUtils;

/**
 * Created by Kostyantin on 7/3/2017.
 */

public abstract class BaseDeveloperSettingsActivity extends BaseActivity<ActivityController> implements DeveloperSettingsAdapter.DeveloperSettingsChangedListener {

    private LinearLayout mRoot;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_list);

        mRoot = (LinearLayout) findViewById(R.id.root);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setVisibility(View.VISIBLE);

        notifySettingsChanged();
    }

    public void setToolbar(@LayoutRes int toolbarRes) {
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(toolbarRes, mRoot, false);
        mRoot.addView(toolbar, 0);
        setToolbar(toolbar, true);
    }

    public abstract EditableConfiguration[] getEditableConfigurations();

    public void notifySettingsChanged() {
        DeveloperSettingsAdapter adapter = new DeveloperSettingsAdapter(getEditableConfigurations());
        adapter.setSettingChangedListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public ActivityController createController() {
        return null;
    }

    public enum ConfigurationType {
        STRING,
        INT,
        BOOLEAN;

        public boolean isBoolean() {
            return this == BOOLEAN;
        }

        public ConfigurationValue wrapValue(Object value) {
            if(value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }

            if(isOfType(value)) {
                switch (this) {
                    case STRING:
                        return new ConfigurationValue<>((String) value, this);
                    case BOOLEAN:
                        return new ConfigurationValue<>((Boolean) value, this);
                    case INT:
                        return new ConfigurationValue<>((Integer) value, this);
                }
            }
            throw new IllegalArgumentException("value must be of type " + this);
        }

        public boolean isOfType(Object value) {
            if(value != null) {
                switch (this) {
                    case STRING:
                        return value instanceof String;
                    case BOOLEAN:
                        return value instanceof Boolean;
                    case INT:
                        return value instanceof Integer;
                }
            }
            return false;
        }

        public int getInputType() {
            switch (this) {
                case INT:
                    return InputType.TYPE_CLASS_NUMBER;
                default:
                    return InputType.TYPE_CLASS_TEXT;
            }
        }
    }

    public static class EditableConfiguration {
        public final String key;
        public final ConfigurationType type;

        private ConfigurationValue value;

        public EditableConfiguration(String key, ConfigurationType type) {
            this.key = key;
            this.type = type;
        }

        public void setValue(Object value) {
            this.value = type.wrapValue(value);
        }

        public <T> ConfigurationValue<T> getValue() {
            return value;
        }
    }

    public static class ConfigurationValue<T> {
        private final T value;
        private final ConfigurationType type;

        ConfigurationValue(T value, ConfigurationType type) {
            this.value = value;
            this.type = type;
        }

        public T get() {
            return  value;
        }

        public Boolean asBoolean() {
            if (type != ConfigurationType.BOOLEAN) {
                throw new IllegalStateException("Cannot return a Boolean, " + EditableConfiguration.class.getName() + " instance is of type " + type);
            }
            return (Boolean) value;
        }

        public Integer asInteger() {
            if (type != ConfigurationType.INT) {
                throw new IllegalStateException("Cannot return a Integer, " + EditableConfiguration.class.getName() + " instance is of type " + type);
            }
            return (Integer) value;
        }

        public String asString() {
            if (type != ConfigurationType.STRING) {
                throw new IllegalStateException("Cannot return a String, " + EditableConfiguration.class.getName() + " instance is of type " + type);
            }
            return (String) value;
        }
    }

}
