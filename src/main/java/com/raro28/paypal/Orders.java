package com.raro28.paypal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.payments.Refund;
import com.paypal.payments.RefundRequest;
import com.paypal.payments.CapturesRefundRequest;
import com.paypal.payments.Money;

public class Orders {

    private static final PaypalClient CLIENT = new PaypalClient();

    private Orders() {

    }

    public static Order create() throws IOException {
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value("100.00")));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        // Call API with your client and get a response for your call
        HttpResponse<Order> response = CLIENT.getClient().execute(request);

        return response.result();
    }

    public static Order capture(String approvedOrderId) throws IOException {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(approvedOrderId);

        // Call API with your client and get a response for your call
        HttpResponse<Order> response = CLIENT.getClient().execute(request);

        // If call returns body in response, you can get the de-serialized version by
        // calling result() on the response
        order = response.result();

        return order;
    }

    public static Refund refund(String capturedOrderId) throws IOException {
        RefundRequest refundRequest = new RefundRequest();
        Money money = new Money();
        money.currencyCode("USD");
        money.value("100.00");
        refundRequest.amount(money);

        CapturesRefundRequest request = new CapturesRefundRequest(capturedOrderId);
        request.prefer("return=representation");
        request.requestBody(refundRequest);
        HttpResponse<Refund> response = CLIENT.getClient().execute(request);

        return response.result();
    }
}