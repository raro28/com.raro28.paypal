package com.raro28;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.paypal.orders.Order;
import com.paypal.payments.Refund;
import com.raro28.paypal.Orders;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String LINK_FORMAT = "{0} => {1}:{2}";

    private void run() throws IOException {
        Consumer<com.paypal.orders.LinkDescription> ordersLinkConsumer = link -> LOGGER.log(Level.INFO, LINK_FORMAT,
                new Object[] { link.rel(), link.method(), link.href() });
        Consumer<com.paypal.payments.LinkDescription> paymentsLinkConsumer = link -> LOGGER.log(Level.INFO, LINK_FORMAT,
                new Object[] { link.rel(), link.method(), link.href() });

        // If call returns body in response, you can get the de-serialized version by
        // calling result() on the response
        Order createdOrder = Orders.create();
        LOGGER.log(Level.INFO, "Order ID: {0}", createdOrder.id());
        createdOrder.links().forEach(ordersLinkConsumer);

        LOGGER.log(Level.INFO,
                "Copy approve link and paste it in browser. Login with buyer account and follow the instructions.\nOnce approved hit enter...");
        System.in.read();

        Order capturedOrder = Orders.capture(createdOrder.id());
        String capturedOrderId = capturedOrder.purchaseUnits().get(0).payments().captures().get(0).id();
        LOGGER.log(Level.INFO, "Capture ID:  {0}", capturedOrderId);
        capturedOrder.purchaseUnits().get(0).payments().captures().get(0).links().forEach(ordersLinkConsumer);

        LOGGER.log(Level.INFO, "Hit enter to refund the order");
        System.in.read();

        Refund refund = Orders.refund(capturedOrderId);
        LOGGER.log(Level.INFO, "Refund ID: {0}", refund.id());
        refund.links().forEach(paymentsLinkConsumer);
    }

    public static void main(String[] args) throws IOException {
        App app = new App();

        app.run();

        LOGGER.info("end");
    }
}
