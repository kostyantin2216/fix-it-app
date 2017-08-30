package com.fixit.ui.helpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import com.fixit.app.R;
import com.fixit.data.Tradesman;
import com.fixit.ui.fragments.TradesmanProfileFragment;
import com.fixit.ui.fragments.TradesmanReviewFragment;

/**
 * Created by konstantin on 8/9/2017.
 */

public class TradesmanActionHandler {

    public static void showTradesman(FragmentManager fragmentManager, Tradesman tradesman) {
        TradesmanProfileFragment fragment = TradesmanProfileFragment.newInstance(tradesman, false);
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_holder, fragment)
                .commit();
    }

    public static void reviewTradesman(Context context, FragmentManager fragmentManager, Tradesman tradesman) {
        TradesmanReviewFragment fragment = TradesmanReviewFragment.newInstance(tradesman, ContextCompat.getColor(context, R.color.transparent_black_EE));
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_out_left, R.anim.enter_from_left, R.anim.exit_out_right)
                .add(R.id.fragment_holder, fragment)
                .commit();
    }
}
