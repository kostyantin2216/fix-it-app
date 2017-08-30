package com.fixit.ui.adapters.groups;

import com.fixit.data.Order;
import com.fixit.data.Tradesman;
import com.fixit.ui.helpers.OrderedTradesmanInteractionHandler;
import com.xwray.groupie.ExpandableGroup;

/**
 * Created by konstantin on 8/8/2017.
 */

public class OrderGroup extends ExpandableGroup {

    public OrderGroup(Order order, OrderedTradesmanInteractionHandler tradesmanInteractionHandler) {
        super(new OrderItem(order));

        for(Tradesman tradesman : order.getTradesmen()) {
            add(new TradesmanItem(tradesman, tradesmanInteractionHandler));
        }
    }

}
