package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.config.AppConfig;
import com.fixit.controllers.ActivityController;

/**
 * Created by Kostyantin on 8/17/2017.
 */

public class AboutFragment extends BaseFragment<ActivityController> implements View.OnClickListener {

    private String contactEmail;
    private String versionInfo;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        contactEmail = AppConfig.getString(context, AppConfig.KEY_EMAIL_FOR_SUPPORT, "info@fixxit.co.za");
        versionInfo = AppConfig.getVersionInfo(context).toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

        TextView tvContactUs = (TextView) v.findViewById(R.id.tv_contact_us_at);
        tvContactUs.setText(getString(R.string.contact_us_at, contactEmail));
        tvContactUs.setOnClickListener(this);

        TextView tvVersion = (TextView) v.findViewById(R.id.tv_version);
        tvVersion.setText(versionInfo);
        tvVersion.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contact_us_at:
                String[] addresses = new String[] {contactEmail};
                String subject = getString(R.string.inquiry_from_app_version, versionInfo);
                boolean emailComposed = composeEmail(addresses, subject);
                if(!emailComposed) {
                    String label = getString(R.string.fixxit_contact_email);
                    copyToClipboard(label, contactEmail);
                }
                break;
            case R.id.tv_version:
                String label = getString(R.string.fixxit_version);
                copyToClipboard(label, versionInfo);
                break;
            default:
                throw new IllegalArgumentException("unsupported view click");
        }
    }
}
