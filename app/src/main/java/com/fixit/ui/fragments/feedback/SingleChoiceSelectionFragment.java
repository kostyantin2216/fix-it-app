package com.fixit.ui.fragments.feedback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fixit.utils.UIUtils;
import com.fixit.feedback.ChoiceSelection;
import com.fixit.ui.styleholders.ButtonStyleHolder;

import java.util.List;

/**
 * Created by konstantin on 7/19/2017.
 */
public class SingleChoiceSelectionFragment extends ChoiceSelectionFragment implements View.OnClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ChoiceSelection> selections = getSelectionsBuilder().build();

        Context context = getContext();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int buttonMargin = UIUtils.dpToPx(context, 6);
        lp.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        for(ChoiceSelection selection : selections) {
            Button btnSelection = new Button(context);
            btnSelection.setText(selection.display);
            btnSelection.setTag(selection.value);
            btnSelection.setOnClickListener(this);
            new ButtonStyleHolder(context).applyStyle(btnSelection);
            mRoot.addView(btnSelection, lp);
        }
    }

    @Override
    public void onClick(View v) {
        onSelectionMade(v.getTag());
    }

}
