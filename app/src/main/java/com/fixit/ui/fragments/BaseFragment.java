package com.fixit.ui.fragments;

import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fixit.controllers.ActivityController;
import com.fixit.rest.APIError;
import com.fixit.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.ui.activities.BaseActivity;

import java.util.List;


/**
 * Created by konstantin on 2/20/2017.
 *
 * Delegates common interactions to {@link BaseActivity}
 *
 */
public abstract class BaseFragment<C extends ActivityController> extends Fragment
    implements GeneralServiceErrorCallback {

    private BaseFragmentInteractionsListener<C> mListener;

    @Nullable
    public C getController() {
        if(mListener != null) {
            return mListener.getController();
        }
        return null;
    }

    public void startChrome(String url) {
        if(mListener != null) {
            mListener.startChrome(url);
        }
    }

    public void notifyUser(String msg) {
        if(mListener != null) {
            mListener.notifyUser(msg);
        }
    }

    public void notifyUser(String msg, View v) {
        if(mListener != null) {
            mListener.notifyUser(msg, v);
        }
    }

    public void showLoader(String message) {
        if(mListener != null) {
            mListener.showLoader(message);
        }
    }

    public void hideLoader() {
        if(mListener != null) {
            mListener.hideLoader();
        }
    }

    public void hideKeyboard(View rootView) {
        if(mListener != null) {
            mListener.hideKeyboard(rootView.getWindowToken());
        }
    }

    public void copyToClipboard(String label, String text) {
        if(mListener != null) {
            mListener.copyToClipboard(label, text);
        }
    }


    public boolean composeEmail(String[] addresses, String subject) {
        if(mListener != null) {
            return mListener.composeEmail(addresses, subject);
        }
        return false;
    }

    @Override
    public void onUnexpectedErrorOccurred(String msg, Throwable t) {
        if(mListener != null) {
            mListener.onUnexpectedErrorOccurred(msg, t);
        }
    }

    @Override
    public void onAppServiceError(List<APIError> errors) {
        if(mListener != null) {
            mListener.onAppServiceError(errors);
        }
    }

    @Override
    public void onServerError() {
        if(mListener != null) {
            mListener.onServerError();
        }
    }

    public void showError(String error) {
        if(mListener != null) {
            mListener.showError(error);
        }
    }

    public void showPrompt(String prompt) {
        if(mListener != null) {
            mListener.showPrompt(prompt);
        }
    }

    public void setToolbar(Toolbar toolbar) {
        setToolbar(toolbar, false);
    }

    public void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled) {
        if(mListener != null) {
            mListener.setToolbar(toolbar, homeAsUpEnabled);
        }
    }

    public void setToolbarTitle(String title) {
        if(mListener != null) {
            mListener.setToolbarTitle(title);
        }
    }

    public void restartApp(boolean skipSplash) {
        if(mListener != null) {
            mListener.restartApp(skipSplash);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof BaseFragmentInteractionsListener) {
            mListener = (BaseFragmentInteractionsListener) context;
        } else {
            throw new RuntimeException("Activity of " + BaseFragment.class.getName() + " must implement "
                    + BaseFragmentInteractionsListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface BaseFragmentInteractionsListener<C> extends GeneralServiceErrorCallback {
        C getController();
        void showError(String displayMsg);
        void showPrompt(String displayMsg);
        void showLoader(String message);
        void hideLoader();
        void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled);
        void setToolbarTitle(String title);
        void startChrome(String url);
        void notifyUser(String msg);
        void notifyUser(String msg, View v);
        void hideKeyboard(IBinder windowToken);
        void copyToClipboard(String label, String text);
        boolean composeEmail(String[] addresses, String subject);
        void restartApp(boolean skipSplash);
    }

}
