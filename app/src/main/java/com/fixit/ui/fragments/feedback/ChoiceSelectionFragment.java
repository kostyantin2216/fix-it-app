package com.fixit.ui.fragments.feedback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.controllers.ActivityController;
import com.fixit.ui.fragments.BaseFragment;
import com.fixit.utils.Constants;
import com.fixit.feedback.ChoiceSelection;

/**
 * Created by Kostyantin on 8/20/2017.
 */

public class ChoiceSelectionFragment extends BaseFragment<ActivityController> {

    public final static int SINGLE_CHOICE = 0;
    public final static int MULTI_CHOICE = 1;
    public final static int INPUT_CHOICE = 2;

    protected LinearLayout mRoot;

    private ChoiceSelectionListener mListener;
    private int mSelectionCode;

    public static ChoiceSelectionFragment newInstance(int type, String title, int selectionCode) {
        ChoiceSelectionFragment fragment;
        switch (type) {
            case SINGLE_CHOICE:
                fragment = new SingleChoiceSelectionFragment();
                break;
            case MULTI_CHOICE:
                fragment = new MultiChoiceSelectionFragment();
                break;
            case INPUT_CHOICE:
                fragment = new InputChoiceSelectionFragment();
                break;
            default:
                throw new IllegalArgumentException("Invalid choice selection fragment type(" + type + ")");
        }
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
        View v = inflater.inflate(R.layout.fragment_choice_selection, container, false);

        mRoot = (LinearLayout) v.findViewById(R.id.root);

        String title = getArguments().getString(Constants.ARG_TITLE);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ChoiceSelectionListener) {
            mListener = (ChoiceSelectionListener) context;
        } else {
            throw new IllegalStateException(context.getClass().getName() + " must implement "
                    + ChoiceSelectionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    protected void onSelectionMade(Object selection) {
        if(mListener != null) {
            mListener.onSelectionMade(mSelectionCode, selection);
        }
    }

    protected ChoiceSelection.Builder getSelectionsBuilder() {
        return mListener.getSelections(mSelectionCode);
    }

    public interface ChoiceSelectionListener {
        ChoiceSelection.Builder getSelections(int selectionCode);
        void onSelectionMade(int selectionCode, Object selection);
    }

}
