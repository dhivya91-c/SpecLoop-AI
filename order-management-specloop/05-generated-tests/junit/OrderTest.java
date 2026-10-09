package com.salesmanager.test.order.specloop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.persistence.Column;

import org.junit.Test;

import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;

public class OrderTest {

    @Test
    public void orderDefaultsToOrderTypeAndStoresStatusAndTotal() {
        Order order = new Order();

        order.setStatus(OrderStatus.ORDERED);
        order.setTotal(new BigDecimal("12.34"));

        assertNotNull(order.getOrderType());
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(new BigDecimal("12.34"), order.getTotal());
    }

    @Test
    public void customerEmailColumnIsRequiredAndBounded() throws Exception {
        Column column = Order.class.getDeclaredField("customerEmailAddress")
                .getAnnotation(Column.class);

        assertNotNull(column);
        assertFalse(column.nullable());
        assertEquals(50, column.length());
    }
}