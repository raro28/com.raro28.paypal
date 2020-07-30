package com.raro28.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PaypalClient {

    private final PayPalEnvironment environment;

    private final PayPalHttpClient client;

    public PaypalClient() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");
        this.environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);

        this.client = new PayPalHttpClient(this.environment);
    }

    public PayPalHttpClient getClient() {
        return this.client;
    }
}