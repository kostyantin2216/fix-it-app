package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.core.controllers.RegistrationController;
import com.fixit.core.ui.activities.BaseActivity;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.utils.CommonUtils;

/**
 * Created by konstantin on 5/15/2017.
 */

public class TelephoneVerificationFragment extends BaseFragment<RegistrationController>
        implements View.OnClickListener,
                   RegistrationController.TelephoneVerificationCallback,
                   BaseActivity.OnBackPressListener {

    private final static String TELEPHONE_NUMBER_PREFIX = "+972 ";

    private TelephoneVerificationListener mListener;

    private ViewHolder mView;

    private int mVerificationCode = -1;

    private enum ViewState {
        TELEPHONE_INPUT,
        CODE_INPUT
    }

    private static class ViewHolder implements TextWatcher {
        final TextView tvHint;
        final ProgressBar pbLoader;
        final TextInputLayout telephoneContainer;
        final EditText etTelephone;
        final TextInputLayout codeContainer;
        final EditText etCode;
        final Button btnSubmit;

        ViewState currentState;

        ViewHolder(View v, View.OnClickListener onClickListener) {
            currentState = ViewState.TELEPHONE_INPUT;

            tvHint = (TextView) v.findViewById(R.id.tv_hint);
            pbLoader = (ProgressBar) v.findViewById(R.id.loader);
            telephoneContainer = (TextInputLayout) v.findViewById(R.id.container_telephone);
            etTelephone = (EditText) v.findViewById(R.id.et_telephone);
            codeContainer = (TextInputLayout) v.findViewById(R.id.container_code);
            etCode = (EditText) v.findViewById(R.id.et_verification_code);
            btnSubmit = (Button) v.findViewById(R.id.btn_submit);

            etTelephone.setText(TELEPHONE_NUMBER_PREFIX);
            etTelephone.addTextChangedListener(this);

            btnSubmit.setOnClickListener(onClickListener);
        }

        void setState(ViewState state) {
            currentState = state;

            switch (currentState) {
                case TELEPHONE_INPUT:
                    tvHint.setText(R.string.number_verification_desc);
                    telephoneContainer.setVisibility(View.VISIBLE);
                    codeContainer.setVisibility(View.GONE);
                    btnSubmit.setText(R.string.verify);
                    break;
                case CODE_INPUT:
                    tvHint.setText(R.string.verification_code_desc);
                    telephoneContainer.setVisibility(View.GONE);
                    codeContainer.setVisibility(View.VISIBLE);
                    btnSubmit.setText(R.string.continue_);
                    break;
            }
        }

        void showLoader() {
            pbLoader.setVisibility(View.VISIBLE);
            btnSubmit.setClickable(false);
        }

        void hideLoader() {
            pbLoader.setVisibility(View.INVISIBLE);
            btnSubmit.setClickable(true);
        }

        String getString(@StringRes int resId) {
            return tvHint.getResources().getString(resId);
        }

        boolean validate(int verificationCode) {
            boolean valid = true;
            if(currentState == ViewState.TELEPHONE_INPUT) {
                String telephone = etTelephone.getText().toString();
                if(TextUtils.isEmpty(telephone)) {
                    etTelephone.setError(getString(R.string.error_empty_field));
                    valid = false;
                } else if(!CommonUtils.isValidPhoneNumber(telephone)) {
                    etTelephone.setError(getString(R.string.invalid_telephone));
                    valid = false;
                }
            } else {
                String verificationCodeInput = etCode.getText().toString();
                if(TextUtils.isEmpty(verificationCodeInput)) {
                    etCode.setError(getString(R.string.error_empty_field));
                    valid = false;
                } else if(!verificationCodeInput.equals(String.valueOf(verificationCode))) {
                    etCode.setError(getString(R.string.invalid_verification_code));
                    valid = false;
                }
            }
            return valid;
        }

        String getTelephone() {
            return etTelephone.getText().toString();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if(!s.toString().startsWith(TELEPHONE_NUMBER_PREFIX)) {
                etTelephone.setText(TELEPHONE_NUMBER_PREFIX);
                Selection.setSelection(etTelephone.getText(), TELEPHONE_NUMBER_PREFIX.length());
            }
        }
    }

    public static TelephoneVerificationFragment newInstance() {
        return new TelephoneVerificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_telephone_verification, container, false);

        mView = new ViewHolder(v, this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TelephoneVerificationListener) {
            mListener = (TelephoneVerificationListener) context;
        } else {
            throw new IllegalStateException("context must implement " + TelephoneVerificationListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit) {
            if(mView.validate(mVerificationCode)) {
                if (mView.currentState == ViewState.TELEPHONE_INPUT) {
                    verifyPhoneNumber();
                } else {
                    completeVerification();
                }
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if(mView.currentState == ViewState.CODE_INPUT) {
            mView.setState(ViewState.TELEPHONE_INPUT);
            return true;
        }
        return false;
    }

    private void verifyPhoneNumber() {
        mView.showLoader();
        getController().verifyTelephone(mView.getTelephone(), this);
    }

    private void completeVerification() {
        if(mListener != null) {
            mView.showLoader();
            mListener.onTelephoneNumberVerified(mView.getTelephone());
        }
    }

    @Override
    public void onVerificationCodeSent(int verificationCode) {
        mView.hideLoader();
        mVerificationCode = verificationCode;
        mView.setState(ViewState.CODE_INPUT);
    }

    @Override
    public void onVerificationError(int code, String error) {
        mView.hideLoader();
        mView.etTelephone.setError(error);
    }

    public interface TelephoneVerificationListener {
        void onTelephoneNumberVerified(String telephone);
    }
}
