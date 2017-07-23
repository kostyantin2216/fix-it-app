package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.app.ui.styleholders.ButtonStyleHolder;
import com.fixit.core.controllers.ActivityController;
import com.fixit.core.ui.fragments.BaseFragment;
import com.fixit.core.ui.styleholders.StyleHolder;
import com.fixit.core.utils.Constants;
import com.fixit.core.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 7/19/2017.
 */
public class SingleChoiceSelectionFragment extends BaseFragment<ActivityController> implements View.OnClickListener {

    private SingleChoiceSelectionListener mListener;
    private LinearLayout mRoot;

    private int mSelectionCode;

    public static SingleChoiceSelectionFragment newInstance(String title, int selectionCode) {
        SingleChoiceSelectionFragment fragment = new SingleChoiceSelectionFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TITLE, title);
        args.putInt(Constants.ARG_SELECTION_CODE, selectionCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectionCode = getArguments().getInt(Constants.ARG_SELECTION_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_choice_selection, container, false);

        mRoot = (LinearLayout) v.findViewById(R.id.root);

        String title = getArguments().getString(Constants.ARG_TITLE);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Selection> selections = mListener.getSelections(mSelectionCode).selections;

        Context context = getContext();
        StyleHolder<Button> styleHolder = new ButtonStyleHolder(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int buttonMargin = UIUtils.dpToPx(context, 6);
        lp.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        for(Selection selection : selections) {
            Button btnSelection = new Button(context);
            btnSelection.setText(selection.display);
            btnSelection.setTag(selection.value);
            btnSelection.setOnClickListener(this);
            styleHolder.applyStyle(btnSelection);
            mRoot.addView(btnSelection, lp);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SingleChoiceSelectionListener) {
            mListener = (SingleChoiceSelectionListener) context;
        } else {
            throw new IllegalStateException(context.getClass().getName() + " must implement "
                        + SingleChoiceSelectionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            mListener.onSelectionMade(mSelectionCode, v.getTag());
        }
    }

    public interface SingleChoiceSelectionListener {
        SelectionBuilder getSelections(int selectionCode);
        void onSelectionMade(int selectionCode, Object selection);
    }

    private static class Selection {
        final Object value;
        final String display;

        private Selection(Object value, String display) {
            this.value = value;
            this.display = display;
        }
    }

    public static class SelectionBuilder {
        private final List<Selection> selections = new ArrayList<>();

        public SelectionBuilder add(String display, Object value) {
            selections.add(new Selection(value, display));
            return this;
        }
    }

}
