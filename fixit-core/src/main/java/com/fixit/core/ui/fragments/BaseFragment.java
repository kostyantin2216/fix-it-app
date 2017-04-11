package com.fixit.core.ui.fragments;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.fixit.core.controllers.ActivityController;


/**
 * Created by konstantin on 2/20/2017.
 */

public abstract class BaseFragment<C extends ActivityController> extends Fragment {

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

    public void hideKeyboard() {
        if(mListener != null) {
            mListener.hideKeyboard(getView().getRootView().getWindowToken());
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

    public interface BaseFragmentInteractionsListener<C> {
        public C getController();
        public void startChrome(String url);
        public void notifyUser(String msg);
        public void hideKeyboard(IBinder windowToken);
    }

}
