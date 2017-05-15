package com.fixit.core.ui.fragments;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fixit.core.controllers.ActivityController;
import com.fixit.core.general.UnexpectedErrorCallback;
import com.fixit.core.rest.APIError;
import com.fixit.core.rest.callbacks.GeneralServiceErrorCallback;
import com.fixit.core.rest.callbacks.ServiceErrorCallback;

import java.util.List;


/**
 * Created by konstantin on 2/20/2017.
 *
 * Delegates common interactions to {@link com.fixit.core.ui.activities.BaseActivity}
 *
 */
public abstract class BaseFragment<C extends ActivityController> extends Fragment
    implements GeneralServiceErrorCallback {

    private BaseFragmentInteractionsListener<C> mListener;

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

    public void hideKeyboard(View rootView) {
        if(mListener != null) {
            mListener.hideKeyboard(rootView.getWindowToken());
        }
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
        void setToolbar(Toolbar toolbar, boolean homeAsUpEnabled);
        void startChrome(String url);
        void notifyUser(String msg);
        void hideKeyboard(IBinder windowToken);
    }

}
