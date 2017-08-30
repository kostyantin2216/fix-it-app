package com.fixit.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.app.R;
import com.fixit.ui.activities.BaseDeveloperSettingsActivity.EditableConfiguration;
import com.fixit.ui.adapters.DeveloperSettingsAdapter.ConfigurationViewHolder;
import com.fixit.ui.adapters.animations.RecyclerItemAnimation;
import com.fixit.utils.CommonUtils;
import com.fixit.ui.adapters.CommonRecyclerAdapter.CommonViewHolder;
/**
 * Created by Kostyantin on 7/3/2017.
 */

public class DeveloperSettingsAdapter extends CommonRecyclerAdapter<EditableConfiguration, ConfigurationViewHolder> {

    private DeveloperSettingsChangedListener mChangedListener;

    public DeveloperSettingsAdapter(EditableConfiguration[] items) {
        super(((RecyclerItemAnimation) null), items);
        setNotifyOnClick(true);
    }

    public void setSettingChangedListener(DeveloperSettingsChangedListener listener) {
        mChangedListener = listener;
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, EditableConfiguration item) {
        final int adapterPos = vh.getAdapterPosition();
        switch (item.type) {
            case STRING:
            case INT:
                String strValue = item.getValue().get().toString();
                new MaterialDialog.Builder(vh.itemView.getContext())
                        .content(item.key)
                        .inputType(item.type.getInputType())
                        .input(strValue, strValue, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                EditableConfiguration item = getAdapterItem(adapterPos);
                                String value = input.toString();
                                switch (item.type) {
                                    case STRING:
                                        item.setValue(value);
                                        onSettingChanged(adapterPos, item);
                                        break;
                                    case INT:
                                        if(CommonUtils.isNumber(value)) {
                                            item.setValue(Integer.parseInt(value));
                                            onSettingChanged(adapterPos, item);
                                        } else {
                                            Toast.makeText(dialog.getContext(), "value must be a number", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }
                            }
                        })
                        .build()
                        .show();
                break;
            case BOOLEAN:
                Boolean boolValue = (Boolean) item.getValue().get();
                item.setValue(!boolValue);
                onSettingChanged(adapterPos, item);
                break;
        }
    }

    private void onSettingChanged(int adapterPos, EditableConfiguration configuration) {
        notifyItemChanged(adapterPos);
        if(mChangedListener != null) {
            mChangedListener.onSettingChanged(configuration);
        }
    }

    @Override
    public ConfigurationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_developer_setting, viewGroup, false);
        ConfigurationViewHolder viewHolder = new ConfigurationViewHolder(itemView);

        attachItemClickListener(itemView, viewHolder);

        return viewHolder;
    }

    public interface DeveloperSettingsChangedListener {
        void onSettingChanged(EditableConfiguration configuration);
    }

    static class ConfigurationViewHolder extends CommonViewHolder<EditableConfiguration> {
        private final TextView tvKey;
        private final TextView tvValue;
        private final CheckBox cbValue;

        ConfigurationViewHolder(View itemView) {
            super(itemView);

            tvKey = (TextView) itemView.findViewById(R.id.tv_key);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
            cbValue = (CheckBox) itemView.findViewById(R.id.cb_value);
        }

        @Override
        public void populate(EditableConfiguration config) {
            tvKey.setText(config.key);

            if(config.type.isBoolean()) {
                tvValue.setText("");
                cbValue.setVisibility(View.VISIBLE);
                Boolean value = (Boolean) config.getValue().get();
                cbValue.setChecked(value);
            } else {
                tvValue.setText(config.getValue().get().toString());
                cbValue.setVisibility(View.GONE);
            }
        }

        public void setChecked(Boolean checked) {
            this.cbValue.setChecked(checked);
        }
    }

}
