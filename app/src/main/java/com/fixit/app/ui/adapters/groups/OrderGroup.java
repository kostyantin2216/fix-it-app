package com.fixit.app.ui.adapters.groups;

import com.fixit.app.ui.helpers.OrderedTradesmanInteractionHandler;
import com.fixit.core.data.Order;
import com.fixit.core.data.Tradesman;
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
