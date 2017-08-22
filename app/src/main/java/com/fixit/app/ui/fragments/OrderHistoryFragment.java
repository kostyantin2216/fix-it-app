package com.fixit.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixit.app.R;
import com.fixit.app.ui.adapters.groups.OrderGroup;
import com.fixit.app.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.core.controllers.OrderController;
import com.fixit.core.data.Order;
import com.fixit.core.ui.fragments.BaseFragment;
import com.xwray.groupie.GroupAdapter;

import java.util.Arrays;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderHistoryFragment extends BaseFragment<OrderController>  {

    private OrderedTradesmanInteractionHandler tradesmanInteractionHandler;

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_recycler_list, container, false);

        v.findViewById(R.id.root).setBackgroundResource(R.drawable.bg_corner_nuts);

        TextView tvEmptyList = (TextView) v.findViewById(R.id.tv_empty_list);
        tvEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_text));

        Order[] orders = getController().getAllOrders();

        if(orders != null && orders.length > 0) {
            tvEmptyList.setVisibility(View.GONE);
            Arrays.sort(orders, Order.CREATION_DATE_COMPARATOR);

            GroupAdapter groupAdapter = new GroupAdapter();


            for (Order order : orders) {
                groupAdapter.add(new OrderGroup(order, tradesmanInteractionHandler));
            }

            RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(groupAdapter);
            rv.setVisibility(View.VISIBLE);
        } else {
            tvEmptyList.setText(R.string.no_orders_made_yet);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(tradesmanInteractionHandler == null) {
            tradesmanInteractionHandler = new OrderedTradesmanInteractionHandler();
        }

        if(context instanceof OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener) {
            tradesmanInteractionHandler.setListener((OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener) context);
        } else {
            throw new IllegalStateException("context must implement "
                    + OrderedTradesmanInteractionHandler.OrderedTradesmanInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        tradesmanInteractionHandler.setListener(null);
    }


}
