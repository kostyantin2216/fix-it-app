package com.fixit.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.Order;
import com.fixit.data.Tradesman;
import com.fixit.utils.UIUtils;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;

/**
 * Created by konstantin on 8/7/2017.
 */

public class OngoingOrderView extends LinearLayout implements View.OnClickListener {

    private final static int DEFAULT_MAX_TRADESMEN_TO_SHOW = 3;

    private Order order;
    private OrderedTradesmanInteractionHandler handler;

    private final TextView tvTitle;

    private final int maxTradesmenToShow;

    public OngoingOrderView(Context context) {
        this(context, null, 0);
    }

    public OngoingOrderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OngoingOrderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        int padding = UIUtils.dpToPx(context, 4);
        setPadding(padding, padding, padding, padding);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OngoingOrderView);

        maxTradesmenToShow = ta.getInteger(R.styleable.OngoingOrderView_maxTradesmenToShow, DEFAULT_MAX_TRADESMEN_TO_SHOW);

        ta.recycle();

        tvTitle = new TextView(getContext());
        tvTitle.setText(R.string.ongoing_order);
        tvTitle.setLayoutParams(
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.large_text_size));
        addView(tvTitle);
    }

    public void setInteractionHandler(OrderedTradesmanInteractionHandler handler) {
        this.handler = handler;
    }

    public OrderedTradesmanInteractionHandler getInteractionHandler() {
        return handler;
    }

    public void setOrder(Order order) {
        this.order = order;
        tvTitle.setText(getResources().getString(R.string.format_ongoing_order, order.getProfession().getName()));
        invalidateTradesmen();
    }

    public String getOrderId() {
        return order == null ? null : order.getId();
    }

    private void invalidateTradesmen() {
        Tradesman[] tradesmen = order.getTradesmen();
        int limit = tradesmen.length > maxTradesmenToShow ? maxTradesmenToShow : tradesmen.length;
        for(int i = 0; i < limit; i++) {
            View v = getChildAt(i + 1);
            if(v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ordered_tradesman, this, false);
                addView(v, i + 1);
            }

            Tradesman tradesman = tradesmen[i];

            TextView tvCompanyName = (TextView) v.findViewById(R.id.tv_company_name);
            tvCompanyName.setText(tradesman.getCompanyName());

            ImageView ivMore = (ImageView) v.findViewById(R.id.iv_more);
            ivMore.setOnClickListener(this);
            ivMore.setTag(i);

            ImageView ivCall = (ImageView) v.findViewById(R.id.iv_call);
            ivCall.setOnClickListener(this);
            ivCall.setTag(i);

            ImageView ivMsg = (ImageView) v.findViewById(R.id.iv_msg);
            ivMsg.setOnClickListener(this);
            ivMsg.setTag(i);
        }
    }

    @Override
    public void onClick(View v) {
        final Tradesman tradesman = order.getTradesmen()[(int) v.getTag()];

        handler.onClick(v, tradesman);
    }


}
