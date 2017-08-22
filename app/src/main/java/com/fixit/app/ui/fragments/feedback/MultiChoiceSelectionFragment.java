package com.fixit.app.ui.fragments.feedback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fixit.app.R;
import com.fixit.app.ifs.feedback.ChoiceSelection;
import com.fixit.app.ui.styleholders.ButtonStyleHolder;
import com.fixit.core.ui.adapters.MultiSelectRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostyantin on 8/20/2017.
 */

public class MultiChoiceSelectionFragment extends ChoiceSelectionFragment implements View.OnClickListener, MultiSelectRecyclerAdapter.OnMultiSelectItemChangeListener {

    private RecyclerView rvSelections;
    private Button btnDone;

    private ChoiceSelection.Choices mSelections;
    private MultiSelectRecyclerAdapter mSelectionsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        Context context = getContext();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;

        rvSelections = new RecyclerView(context);
        rvSelections.setLayoutManager(new LinearLayoutManager(context));
        mRoot.addView(rvSelections, lp);

        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnDone = new Button(context);
        btnDone.setOnClickListener(this);
        btnDone.setVisibility(View.INVISIBLE);
        btnDone.setText(getString(R.string.done));
        new ButtonStyleHolder(context).applyStyle(btnDone);
        mRoot.addView(btnDone, btnLp);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSelections = getSelectionsBuilder().toChoices();
        mSelectionsAdapter = new MultiSelectRecyclerAdapter(getContext(),  mSelections.toSelectItems()) {
            @Override
            protected int getLayoutRes() {
                return R.layout.list_item_multiselect_dark;
            }
        };
        mSelectionsAdapter.setSelectItemChangeListener(this);
        rvSelections.setAdapter(mSelectionsAdapter);
    }

    @Override
    public void onClick(View v) {
        List<Object> selections = mSelections.extractChoiceValues(mSelectionsAdapter.getSelectedItems());
        onSelectionMade(selections);
    }

    @Override
    public void onSelectItemChanged(MultiSelectRecyclerAdapter.SelectItem selectItem) {
        if(mSelectionsAdapter.getSelectedItemCount() > 0) {
            btnDone.setVisibility(View.VISIBLE);
        } else {
            btnDone.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSelectionLimitReached(int limit) {
        // there is not limit.
    }

}
