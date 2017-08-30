package com.fixit.ui.fragments.feedback;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fixit.app.R;
import com.fixit.ui.styleholders.ButtonStyleHolder;

/**
 * Created by Kostyantin on 8/20/2017.
 */

public class InputChoiceSelectionFragment extends ChoiceSelectionFragment implements View.OnClickListener {

    private EditText etInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;

        etInput = new EditText(context);
        etInput.setTextColor(Color.WHITE);
        mRoot.addView(etInput, lp);

        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btnDone = new Button(context);
        btnDone.setOnClickListener(this);
        btnDone.setText(getString(R.string.continue_));
        new ButtonStyleHolder(context).applyStyle(btnDone);
        mRoot.addView(btnDone, btnLp);

        return v;
    }

    @Override
    public void onClick(View v) {
        onSelectionMade(etInput.getText().toString().trim());
        hideKeyboard(mRoot);
    }
}
