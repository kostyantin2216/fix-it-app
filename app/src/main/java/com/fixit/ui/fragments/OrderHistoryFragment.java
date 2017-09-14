package com.fixit.ui.fragments;

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
import com.fixit.controllers.OrderController;
import com.fixit.data.Order;
import com.fixit.ui.activities.BaseActivity;
import com.fixit.ui.adapters.groups.OrderGroup;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.xwray.groupie.GroupAdapter;

import java.util.Arrays;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderHistoryFragment extends BaseFragment<OrderController> implements OrderController.OrderCallback, BaseActivity.LoginRequester {

    private OrderedTradesmanInteractionHandler tradesmanInteractionHandler;

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    private TextView tvEmptyList;
    private RecyclerView rvOrders;

    private boolean ordersLoaded = false;
    private boolean userRegistered = false;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRegistered = isUserRegistered();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_recycler_list, container, false);

        v.findViewById(R.id.root).setBackgroundResource(R.drawable.bg_corner_nuts);

        tvEmptyList = (TextView) v.findViewById(R.id.tv_empty_list);
        tvEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_text));

        rvOrders = (RecyclerView) v.findViewById(R.id.recycler);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!userRegistered) {
            requestLogin(getString(R.string.login_for_order_history), null, null, this);
        } else if(!ordersLoaded) {
            getController().loadOrderHistory(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(!ordersLoaded && userRegistered) {
            getController().cleanOrderFactory();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        tradesmanInteractionHandler.setListener(null);
    }

    @Override
    public void onOrdersLoaded(Order[] orders) {
        ordersLoaded = true;
        hideLoader();
        getController().cleanOrderFactory();
        if(orders != null && orders.length > 0) {
            tvEmptyList.setVisibility(View.GONE);
            Arrays.sort(orders, Order.CREATION_DATE_COMPARATOR);

            GroupAdapter groupAdapter = new GroupAdapter();

            for (Order order : orders) {
                groupAdapter.add(new OrderGroup(order, tradesmanInteractionHandler));
            }

            rvOrders.setAdapter(groupAdapter);
            rvOrders.setVisibility(View.VISIBLE);
        } else {
            updateEmptyListText();
        }
    }

    @Override
    public void loginComplete(boolean success, @Nullable Bundle data) {
        if(success) {
            userRegistered = true;
            getController().loadOrderHistory(this);
        } else {
            getActivity().finish();
        }
    }

    private void updateEmptyListText() {
        if(userRegistered) {
            tvEmptyList.setText(R.string.no_orders_made_yet);
        } else {
            tvEmptyList.setText(R.string.login_for_order_history);
        }
    }
}
