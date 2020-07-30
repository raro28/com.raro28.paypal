package com.raro28;

import com.paypal.core.PayPalHttpClient;
import com.raro28.paypal.PaypalClient;

public class App 
{

    private final PaypalClient payPalClient;

    private App() {
        this.payPalClient = new PaypalClient();
    }

    private void run(){
        PayPalHttpClient client = this.payPalClient.getClient();
    }

    public static void main( String[] args )
    {
        App app = new App();

        app.run();

        System.out.println("end");
    }
}
