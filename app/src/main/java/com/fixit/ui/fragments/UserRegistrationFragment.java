package com.fixit.ui.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fixit.app.R;
import com.fixit.controllers.UserController;
import com.fixit.data.UserAccountDetails;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.Constants;

/**
 * Created by konstantin on 5/14/2017.
 */

public class UserRegistrationFragment extends BaseFragment<UserController>
    implements View.OnClickListener {

    private UserRegistrationInteractionsListener mListener;
    private ViewHolder mView;

    private String existingEmail = null;

    private static class ViewHolder {
        final EditText etFirstName;
        final EditText etLastName;
        final EditText etEmail;

        ViewHolder(View v) {
            etFirstName = (EditText) v.findViewById(R.id.et_first_name);
            etLastName = (EditText) v.findViewById(R.id.et_last_name);
            etEmail = (EditText) v.findViewById(R.id.et_email);
        }

        void populate(UserAccountDetails userAccountDetails) {
            String firstName = userAccountDetails.getFirstName();
            if(!TextUtils.isEmpty(firstName) && !firstName.equals("null")) {
                etFirstName.setText(firstName);
            }

            String lastName = userAccountDetails.getLastName();
            if(!TextUtils.isEmpty(lastName) && !lastName.equals("null")) {
                etLastName.setText(lastName);
            }

            String email = userAccountDetails.getEmail();
            if(!TextUtils.isEmpty(email) && !email.equals("null")) {
                etEmail.setText(email);
            }
        }

        String getFirstName() {
            return etFirstName.getText().toString();
        }

        String getLastName() {
            return etLastName.getText().toString();
        }

        String getEmail() {
            return etEmail.getText().toString();
        }

        boolean validate(String existingEmail) {
            boolean valid = true;
            Resources resources = etFirstName.getResources();

            String firstName = etFirstName.getText().toString();
            if(TextUtils.isEmpty(firstName)) {
                etFirstName.setError(resources.getString(R.string.error_empty_field));
                valid = false;
            }

            String lastName = etLastName.getText().toString();
            if(TextUtils.isEmpty(lastName)) {
                etLastName.setError(resources.getString(R.string.error_empty_field));
                valid = false;
            }

            String email = etEmail.getText().toString();
            if(TextUtils.isEmpty(email)) {
                etEmail.setError(resources.getString(R.string.error_empty_field));
                valid = false;
            } else if(!CommonUtils.isValidEmail(email)) {
                etEmail.setError(resources.getString(R.string.invalid_email));
                valid = false;
            } else if(existingEmail != null && email.equals(existingEmail)) {
                etEmail.setError(resources.getString(R.string.email_already_exists));
                valid = false;
            }

            return valid;
        }
    }

    public static UserRegistrationFragment newInstance(UserAccountDetails userAccountDetails, boolean emailAlreadyExists) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_LOGIN_DETAILS, userAccountDetails);
        args.putBoolean(Constants.ARG_EMAIL_EXISTS, emailAlreadyExists);
        UserRegistrationFragment fragment = new UserRegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_registration, container, false);

        mView = new ViewHolder(v);
        v.findViewById(R.id.btn_submit).setOnClickListener(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        UserAccountDetails userAccountDetails = args.getParcelable(Constants.ARG_LOGIN_DETAILS);
        mView.populate(userAccountDetails);

        if(args.getBoolean(Constants.ARG_EMAIL_EXISTS)) {
            mView.etEmail.setError(getString(R.string.email_already_exists));
            existingEmail = userAccountDetails.getEmail();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof UserRegistrationInteractionsListener) {
            mListener = (UserRegistrationInteractionsListener) context;
        } else {
            throw new IllegalStateException("context must implement " + UserRegistrationInteractionsListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener.updateAccountDetails(mView.getFirstName(), mView.getLastName(), mView.getEmail());
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit) {
            if(mView.validate(existingEmail)) {
                if(mListener != null) {
                    mListener.onLoggedIn(mView.getFirstName(), mView.getLastName(), mView.getEmail(), "");
                }
            }
        }
    }

    public interface UserRegistrationInteractionsListener {
        void updateAccountDetails(String firstName, String lastName, String email);
        void onLoggedIn(String firstName, String lastName, String email, String avatarUrl);
    }
}
