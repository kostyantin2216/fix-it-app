package com.fixit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.app.R;
import com.fixit.controllers.ReviewController;
import com.fixit.data.Review;
import com.fixit.data.Tradesman;
import com.fixit.ui.components.CancelableEditText;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.fixit.utils.GlobalPreferences;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by konstantin on 7/23/2017.
 */

public class TradesmanReviewFragment extends BaseFragment<ReviewController>
        implements View.OnClickListener,
                   View.OnFocusChangeListener,
                   ReviewController.LoadReviewsCallback {

    private Tradesman tradesman;
    private ViewHolder mView;

    private TradesmanReviewListener mListener;

    private boolean newReview = true;

    public static TradesmanReviewFragment newInstance(Tradesman tradesman, @Nullable Integer backgroundColor) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_TRADESMAN, tradesman);
        if(backgroundColor != null) {
            args.putInt(Constants.ARG_BACKGROUND_COLOR, backgroundColor);
        }
        TradesmanReviewFragment fragment = new TradesmanReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tradesman = getArguments().getParcelable(Constants.ARG_TRADESMAN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tradesman_review, container, false);

        Integer backgroundColor = null;
        Bundle args = getArguments();
        if(args.containsKey(Constants.ARG_BACKGROUND_COLOR)) {
            backgroundColor = args.getInt(Constants.ARG_BACKGROUND_COLOR);
        }

        mView = new ViewHolder(v, this, this, backgroundColor);
        mView.populate(tradesman);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoader(getString(R.string.checking_review_history));
        getController().loadReviewForTradesmanByUser(tradesman, this);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        if(reviews != null && !reviews.isEmpty()) {
            mView.showExistingReview(reviews.get(0));
            newReview = false;
        }
        hideLoader();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_submit:
                Review review = mView.fieldsToReview();
                review.setTradesmanId(tradesman.get_id());
                review.setUserId(GlobalPreferences.getUserId(getContext()));
                review.setOnDisplay(true);
                review.setCreatedAt(new Date());

                ReviewController controller = getController();
                if(controller != null) {
                    if (newReview) {
                        controller.saveReview(tradesman, review);
                    } else {
                        controller.updateReview(review);
                    }
                } else {
                    FILog.w("Unable to obtain Review Controller in TradesmanReviewFragment", getContext());
                }

                if(mListener != null) {
                    mListener.onTradesmanReviewed(newReview, review);
                }
                break;
            default:
                handleReviewInput(viewId);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            handleReviewInput(v.getId());
        }
    }

    private void handleReviewInput(int viewId) {
        switch (viewId) {
            case R.id.et_review_title:
                promptInput(viewId, R.string.review_title, R.string.review_title_hint, mView.etReviewTitle, true);
                break;
            case R.id.et_review_content:
                promptInput(viewId, R.string.review_content, R.string.review_content_hint, mView.etReview, false);
                break;
        }
    }

    public void promptInput(final int fromViewId, @StringRes int titleResId, @StringRes int hintResId, final EditText etInput, boolean limitInput) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .title(titleResId)
                .content(getString(hintResId))
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        if(limitInput) {
            builder.inputRangeRes(2, 40, R.color.colorError);
        }
        MaterialDialog materialDialog = builder.input("", etInput.getText(), (dialog, input) -> {
                    etInput.setText(input);
                    mView.onTextInput(fromViewId);
                }).build();

        if(!limitInput) {
            EditText editText = materialDialog.getInputEditText();
            editText.setSingleLine(false);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        }

        materialDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TradesmanReviewListener) {
            mListener = (TradesmanReviewListener) context;
        } else {
            throw new IllegalStateException(context.getClass().getName() + " must implement "
                        + TradesmanReviewListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public interface TradesmanReviewListener {
        void onTradesmanReviewed(boolean isNewReview, Review review);
    }

    public static class ViewHolder implements RatingBar.OnRatingBarChangeListener, CancelableEditText.OnCancelTextListener {
        final TextView tvStatus;
        final ImageView ivTradesmanLogo;
        final TextView tvCompanyName;
        final RatingBar rbRating;
        final CancelableEditText etReviewTitle;
        final CancelableEditText etReview;
        final Button btnSubmit;

        ViewHolder(View v, View.OnFocusChangeListener focusChangeListener, View.OnClickListener clickListener, Integer backgroundColor) {
            tvStatus = (TextView) v.findViewById(R.id.tv_review_status);
            ivTradesmanLogo = (ImageView) v.findViewById(R.id.iv_tradesman_logo);
            tvCompanyName = (TextView) v.findViewById(R.id.tv_company_name);
            rbRating = (RatingBar) v.findViewById(R.id.rb_rating);
            etReviewTitle = (CancelableEditText) v.findViewById(R.id.et_review_title);
            etReview = (CancelableEditText) v.findViewById(R.id.et_review_content);
            btnSubmit = (Button) v.findViewById(R.id.btn_submit);

            etReviewTitle.setOnCancelTextListener(this);
            etReview.setOnCancelTextListener(this);
            etReviewTitle.setOnFocusChangeListener(focusChangeListener);
            etReview.setOnFocusChangeListener(focusChangeListener);
            etReviewTitle.setOnClickListener(clickListener);
            etReview.setOnClickListener(clickListener);
            btnSubmit.setOnClickListener(clickListener);
            rbRating.setOnRatingBarChangeListener(this);

            if(backgroundColor != null) {
                v.findViewById(R.id.root).setBackgroundColor(backgroundColor);
            }
        }

        void populate(Tradesman tradesman) {
            Picasso.with(ivTradesmanLogo.getContext()).load(tradesman.getLogoUrl()).into(ivTradesmanLogo);
            tvCompanyName.setText(tradesman.getCompanyName());
        }

        void showExistingReview(Review review) {
            tvStatus.setText(tvStatus.getResources().getString(R.string.update_existing_review));
            rbRating.setRating(review.getRating());
            etReviewTitle.setText(review.getTitle());
            etReview.setText(review.getContent());
            validateSubmitButton();
        }

        Review fieldsToReview() {
            Review review = new Review();
            review.setRating(rbRating.getRating());
            review.setTitle(etReviewTitle.getText().toString());
            review.setContent(etReview.getText().toString());
            return review;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            validateSubmitButton();
        }

        @Override
        public void onTextCancelled(CancelableEditText cancelableEditText) {
            validateSubmitButton();
        }

        void onTextInput(int viewId) {
            validateSubmitButton();
        }

        void validateSubmitButton() {
            if(rbRating.getRating() > 0
                    && etReviewTitle.getText().toString().trim().length() > 0
                    && etReview.getText().toString().trim().length() > 0) {
                btnSubmit.setVisibility(View.VISIBLE);
            } else {
                btnSubmit.setVisibility(View.INVISIBLE);
            }
        }
    }

}
