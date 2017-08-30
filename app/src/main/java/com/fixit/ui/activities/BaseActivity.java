package com.fixit.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.config.AppConfig;
import com.fixit.controllers.ActivityController;
import com.fixit.app.R;
import com.fixit.rest.APIError;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.ui.fragments.BaseFragment;
import com.fixit.ui.fragments.ErrorFragment;
import com.fixit.utils.Constants;
import com.fixit.utils.GlobalPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kostyantin on 12/21/2016.
 */

public abstract class BaseActivity<C extends ActivityController> extends AppCompatActivity
        implements BaseFragment.BaseFragmentInteractionsListener,
        GeneralServiceErrorCallback,
                   ActivityController.UiCallback {

    private C mController;
    private MaterialDialog mLoaderDialog;

    private Set<OnBackPressListener> mBackPressListeners;
    private ActivityBackPressPrompt mBackPressPrompt;
    private LoginRequester mLoginRequester;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mController = createController();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isNetworkConnected()) {
            showError(ErrorFragment.ErrorType.NO_NETWORK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.RC_LOGIN) {
            boolean success = resultCode == RESULT_OK;
            mLoginRequester.loginComplete(success, success ? data.getExtras() : null);
            mLoginRequester = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!AppConfig.isProduction(this)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.development_settings, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(itemId == R.id.open_developer_settings) {
            openDeveloperSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDeveloperSettings() {
        // Override if needed.
    }

    public abstract C createController();

    public C getController() {
        return mController;
    }

    @Override
    public void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        }
    }

    public Toolbar findToolbar(@NonNull ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view.getClass().getName().equals("android.support.v7.widget.Toolbar")
                    || view.getClass().getName().equals("android.widget.Toolbar")) {
                return (Toolbar) view;
            } else if (view instanceof ViewGroup) {
                return findToolbar((ViewGroup) view);
            }
        }
        return null;
    }

    @Override
    public void setToolbarTitle(String title) {
        throw new UnsupportedOperationException("must override this method if you need to use it");
    }

    @Override
    public void startChrome(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);

            try {
                startActivity(intent);
            } catch(ActivityNotFoundException innerEx) {
                // No web browser apps installed.

                notifyUser(getString(R.string.no_internet_browser));
            }
        }
    }

    @Override
    public void hideKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        notifyUser(getString(R.string.copied_format, label));
    }

    @Override
    public boolean composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    /**
     *  Override for enabling/disabling user notifications.
     */
    public boolean notifyPossible() {
        return true;
    }

    @Override
    public void notifyUser(String msg) {
        notifyUser(msg, getWindow().getDecorView());
    }

    @Override
    public void notifyUser(String msg, View v) {
        if(notifyPossible()) {
            Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(AppConfig.getColor(this, AppConfig.KEY_COLOR_PRIMARY_DARK, Color.BLACK));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.large_text_size));
            textView.setMaxLines(5);
            snackbar.show();
        }
    }

    // USER MANAGEMENT
    public abstract Class<?> getLoginActivity();

    public boolean isUserRegistered() {
        return !TextUtils.isEmpty(GlobalPreferences.getUserId(this));
    }

    public void requestLogin(LoginRequester requester, @Nullable Bundle data) {
        mLoginRequester = requester;

        Intent intent = new Intent(this, getLoginActivity());
        if(data != null) {
            intent.putExtras(data);
        }
        startActivityForResult(intent, Constants.RC_LOGIN);
    }

    public interface LoginRequester {
        void loginComplete(boolean success, @Nullable Bundle data);
    }

    // ERROR HANDLING
    // ===========================

    @Override
    public void onAppServiceError(List<APIError> errors) {
        showError(ErrorFragment.ErrorType.GENERAL.createBuilder(this).apiError(errors).build());
    }

    @Override
    public void onServerError() {
        showError(ErrorFragment.ErrorType.SERVER_UNAVAILABLE);
    }

    @Override
    public void onUnexpectedErrorOccurred(String msg, Throwable t) {
        ErrorFragment.ErrorParamsBuilder builder = ErrorFragment.ErrorType.GENERAL.createBuilder(this).log(msg);
        if(t != null) {
            builder.cause(t);
        }
        showError(builder.build());
    }

    public void showError() {
        showError(ErrorFragment.ErrorType.GENERAL);
    }

    @Override
    public void showError(String error) {
        showError(ErrorFragment.ErrorType.GENERAL, error);
    }

    public void showError(ErrorFragment.ErrorType errorType) {
        showError(errorType.createBuilder(this).build());
    }

    public void showError(ErrorFragment.ErrorType errorType, String msg) {
        showError(errorType.createBuilder(msg).build());
    }

    public void showError(ErrorFragment.ErrorParams params) {
        hideLoader();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, ErrorFragment.newInstance(params))
                .commitAllowingStateLoss();
    }

    @Override
    public void showPrompt(String message) {
        showError(ErrorFragment.ErrorType.PROMPT.createBuilder(message).build());
    }

    // LOADER
    // ==========================

    public void showLoader() {
        showLoader(getString(R.string.loading));
    }

    @Override
    public void showLoader(String message) {
        showLoader(message, false);
    }

    public void showLoader(String message, boolean cancelable) {
        if(mLoaderDialog == null) {
            mLoaderDialog = new MaterialDialog.Builder(this)
                    .title(R.string.please_wait)
                    .content(message)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .cancelable(cancelable)
                    .show();
        } else {
            mLoaderDialog.setContent(message);
            mLoaderDialog.show();
        }
    }

    public void hideLoader() {
        if(mLoaderDialog != null) {
            mLoaderDialog.dismiss();
            mLoaderDialog = null;
        }
    }

    // QUESTION PROMPTS
    // ============================

    public void askQuestion(String question, QuestionResult result) {
        askQuestion(question, getString(R.string.yes), getString(R.string.no), result);
    }

    public void askQuestion(String question, String yesText, String noText, final QuestionResult result) {
        new MaterialDialog.Builder(this)
                .content(question)
                .positiveText(yesText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        result.onQuestionAnswered(true);
                    }
                })
                .negativeText(noText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        result.onQuestionAnswered(false);
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.onQuestionCancelled();
                    }
                })
                .show();
    }

    public interface QuestionResult {
        void onQuestionAnswered(boolean answeredYes);
        void onQuestionCancelled();
    }

    // FRAGMENT INTERACTIONS
    // ====================

    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackEntryCount = fm.getBackStackEntryCount();
        for(int i = 0; i < backStackEntryCount; ++i) {
            fm.popBackStack();
        }
    }

    @Nullable
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass) {
        return getFragment(tag, fragmentClass, false, 0);
    }

    @Nullable
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass, boolean createIfNotExist) {
        return getFragment(tag, fragmentClass, createIfNotExist, android.R.id.content);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends Fragment> T getFragment(String tag, Class<T> fragmentClass, boolean createIfNotExist, int containerViewId) {
        FragmentManager fm = getSupportFragmentManager();

        T fragment = (T) fm.findFragmentByTag(tag);
        if(createIfNotExist && fragment == null) {
            fragment = (T) Fragment.instantiate(this, fragmentClass.getCanonicalName());

            fm.beginTransaction()
                    .add(containerViewId, fragment, tag)
                    .commit();
        }

        return fragment;
    }


    // BACK PRESS
    // ==========================

    @Override
    public void onBackPressed() {
        boolean handled = false;

        if(mBackPressListeners != null) {
            for(OnBackPressListener backPressListener : mBackPressListeners) {
                if(backPressListener.onBackPressed()) {
                    handled = true;
                }
            }
        }

        if(!handled) {
            if(mBackPressPrompt == null) {
                super.onBackPressed();
            } else {
                askQuestion(mBackPressPrompt.content, mBackPressPrompt.yesText, mBackPressPrompt.noText, new QuestionResult() {
                    @Override
                    public void onQuestionAnswered(boolean answeredYes) {
                        if(answeredYes) {
                            BaseActivity.super.onBackPressed();
                        }
                    }

                    @Override
                    public void onQuestionCancelled() { }
                });
            }
        }
    }

    public void registerOnBackPressListener(OnBackPressListener listener) {
        if(mBackPressListeners == null) {
            mBackPressListeners = new HashSet<>();
        }
        mBackPressListeners.add(listener);
    }

    public void unregisterOnBackPressListener(OnBackPressListener listener) {
        if(mBackPressListeners != null) {
            mBackPressListeners.remove(listener);

            if(mBackPressListeners.size() == 0) {
                mBackPressListeners = null;
            }
        }
    }

    public void clearOnBackPressListeners() {
        mBackPressListeners = null;
    }

    public boolean hasBackPressListeners() {
        return mBackPressListeners != null && !mBackPressListeners.isEmpty();
    }

    public void setActivityBackPressPrompt(ActivityBackPressPrompt prompt) {
        this.mBackPressPrompt = prompt;
    }

    public interface OnBackPressListener {
        boolean onBackPressed();
    }

    public static class ActivityBackPressPrompt {
        private final String content;
        private final String yesText;
        private final String noText;

        public ActivityBackPressPrompt(String content, String yesText, String noText) {
            this.content = content;
            this.yesText = yesText;
            this.noText = noText;
        }
    }

}
