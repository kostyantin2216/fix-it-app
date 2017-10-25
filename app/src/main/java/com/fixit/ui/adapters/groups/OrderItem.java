package com.fixit.ui.adapters.groups;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.data.Order;
import com.fixit.utils.DateUtils;
import com.fixit.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * Created by konstantin on 8/9/2017.
 */

public class OrderItem extends Item<OrderItem.OrderViewHolder> implements ExpandableItem, View.OnClickListener {

    private final Order order;
    private ExpandableGroup expandableGroup;

    public OrderItem(Order order) {
        this.order = order;
    }

    @NonNull
    @Override
    public OrderViewHolder createViewHolder(@NonNull View itemView) {
        return new OrderViewHolder(itemView);
    }

    @Override
    public void bind(@NonNull OrderViewHolder viewHolder, int position) {
        viewHolder.populate(order);
        viewHolder.itemView.setTag(viewHolder);
        viewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void unbind(@NonNull OrderViewHolder holder) {
        super.unbind(holder);
        holder.itemView.setOnClickListener(null);
        holder.itemView.setTag(null);
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_order;
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        expandableGroup = onToggleListener;
    }

    @Override
    public void onClick(View v) {
        if(expandableGroup != null) {
            expandableGroup.onToggleExpanded();
            ((OrderViewHolder) v.getTag()).toggleIndicator(expandableGroup.isExpanded());
        }
    }

    static class OrderViewHolder extends ViewHolder {

        final ImageView ivProfessionBg;
        final ImageView ivExpandIndicator;
        final TextView tvProfessionLine;
        final TextView tvTradesmenLine;
        final TextView tvLocationLine;
        final TextView tvDateLine;

        OrderViewHolder(@NonNull View v) {
            super(v);

            ivProfessionBg = (ImageView) v.findViewById(R.id.iv_profession_bg);
            ivExpandIndicator = (ImageView) v.findViewById(R.id.iv_expand_indicator);
            tvProfessionLine = (TextView) v.findViewById(R.id.tv_profession_line);
            tvTradesmenLine = (TextView) v.findViewById(R.id.tv_tradesmen_line);
            tvLocationLine = (TextView) v.findViewById(R.id.tv_location_line);
            tvDateLine = (TextView) v.findViewById(R.id.tv_date_line);
        }

        void populate(Order order) {
            String imgUrl = order.getProfession().getImageUrl();
            if(!TextUtils.isEmpty(imgUrl)) {
                Picasso.with(itemView.getContext()).load(imgUrl).fit().into(ivProfessionBg);
            } else {
                Picasso.with(itemView.getContext()).load(R.drawable.bg_plumber).fit().into(ivProfessionBg);
            }

            Resources res = itemView.getResources();

            tvProfessionLine.setText(order.getProfession().getName());
            tvTradesmenLine.setText(res.getString(R.string.format_ordered_x_tradesmen, order.getTradesmen().length));
            tvLocationLine.setText(res.getString(R.string.format_at_x, order.getJobLocation().getGoogleAddress()));
            tvDateLine.setText(DateUtils.dateToString(DateUtils.FORMAT_DMY_AT_HM, order.getCreatedAt()));
        }

        void toggleIndicator(boolean isExpanded) {
            UIUtils.rotate180Degrees(ivExpandIndicator, isExpanded);
        }
    }
}
