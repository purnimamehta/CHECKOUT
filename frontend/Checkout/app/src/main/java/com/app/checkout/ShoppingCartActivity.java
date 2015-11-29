package com.app.checkout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingCartActivity extends ActionBarActivity {

    private ArrayList<Item> items = new ArrayList<Item>();
    private double numItems = 0;
    private double totalCost = 0;
    private static final String REQUEST_CHECKOUT = "http://ec2-52-27-64-11.us-west-2.compute.amazonaws.com:5000/checkout?";
    private static final String REQUEST_PRICE = "http://ec2-52-27-64-11.us-west-2.compute.amazonaws.com:5000/price?";
    private static String responsePrice = "0";
    private static String responseItemName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Button checkoutButton = (Button) findViewById(R.id.button_Checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConfirmPage();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tempBarCode;
            tempBarCode = extras.getString("barcode");
            if (tempBarCode != null) {
                Log.d("Test barcode passing", tempBarCode);
                ProductDetailsTask productDetailsTask = new ProductDetailsTask(tempBarCode);
                Log.d("Price request string", constructRequestWithBarCodeAndStoreID(REQUEST_PRICE, tempBarCode));
                productDetailsTask.execute(constructRequestWithBarCodeAndStoreID(REQUEST_PRICE, tempBarCode));

                ProductNameTask productNameTask = new ProductNameTask(tempBarCode);
                productNameTask.execute("http://ec2-52-27-64-11.us-west-2.compute.amazonaws.com:5000/name?store=wmrtmdv&upc=" + tempBarCode);


                if (responsePrice == "0") {
                    ItemRetrievalTask itemRetrievalTask = new ItemRetrievalTask("50");
                    itemRetrievalTask.execute(constructRequestWithPrice(REQUEST_CHECKOUT, "50"));
                } else {
                    ItemRetrievalTask itemRetrievalTask = new ItemRetrievalTask(responsePrice);
                    itemRetrievalTask.execute(constructRequestWithPrice(REQUEST_CHECKOUT, responsePrice));
                }

                try {
                    Thread.sleep(3000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                TextView itemPrice1 = (TextView) findViewById(R.id.tv_price1);
                Log.d("setText", responsePrice);
                itemPrice1.setText(responsePrice);
                TextView itemName1 = (TextView) findViewById(R.id.tv_itemName1);
                itemName1.setText(responseItemName);

            } else {
                Log.d("Test barcode passing", "tempBarCode was null!");
            }

        } else {
            Log.d("Test barcode passing", "extras are null!");
        }

    }

    private String constructRequestWithPrice(String originalRequest, String price) {
        return originalRequest + "price=" + price;
    }

    private String constructRequestWithBarCodeAndStoreID(String originalRequest, String barcode) {
        return originalRequest + "store=wmrtmdv&" + "upc=" + barcode;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }

    public void incrementNumItems() {
        numItems++;
    }

    public void decrementNumItems() {
        numItems--;
    }

    public void addTotalCost(double itemPrice) {
        totalCost += itemPrice;
    }

    public void subtractTotalCost(double itemPrice) {
        totalCost -= itemPrice;
    }

    private class Item {
        private String barCode;
        private double price;
        private String name;
        private String storeId;
    }

    public void goToConfirmPage(){
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous item retrieval task using the barcode and storeId
     */
    public class ItemRetrievalTask extends AsyncTask<String, Void, Boolean> {

//        private final String mEmail;
//        private final String mPassword;
        private final String price;

        ItemRetrievalTask(String price) {
//            mEmail = email;
//            mPassword = password;
            this.price = price;
        }

        @Override
        protected Boolean doInBackground(String... uri) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    Log.d("TaskSuccess", "Connection OKAY!");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();
                    Log.d("HttpResponseString", responseString);
                    out.close();
                    //..more logic
                } else{
                    //Closes the connection.
                    Log.d("TaskFailed", "Connection NOT good...");
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            Log.d("DEBUG", "Before returning true at the end of doInBackground");
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            Log.d("DEBUG", "Entering onPostExecute");
//            mAuthTask = null;
//            showProgress(true);
//
//            if (success) {
//                //finish(); //This line will close the current activity.
//                Log.d("DEBUG", "In onPostExecute, logged in success and exiting!");
//                startActivity(new Intent(LoginAccountActivity.this, BarCodeActivity.class));
//
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }

    public class ProductDetailsTask extends AsyncTask<String, Void, Boolean> {

        //        private final String mEmail;
//        private final String mPassword;
        private final String barcode;
        private String storeID;

        ProductDetailsTask(String barcode) {
            this.barcode = barcode;
            this.storeID = "val";
        }

        @Override
        protected Boolean doInBackground(String... uri) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    Log.d("TaskSuccess", "Connection OKAY!");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();
                    Log.d("HttpResponseString", responseString);
                    responsePrice = responseString;
                    out.close();
                    //..more logic
                } else{
                    //Closes the connection.
                    Log.d("TaskFailed", "Connection NOT good...");
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            Log.d("DEBUG", "Before returning true at the end of doInBackground");
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            Log.d("DEBUG", "Entering onPostExecute");
//            mAuthTask = null;
//            showProgress(true);
//
//            if (success) {
//                //finish(); //This line will close the current activity.
//                Log.d("DEBUG", "In onPostExecute, logged in success and exiting!");
//                startActivity(new Intent(LoginAccountActivity.this, BarCodeActivity.class));
//
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }

    public class ProductNameTask extends AsyncTask<String, Void, Boolean> {

        private final String barcode;
        private String storeID;

        ProductNameTask(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Boolean doInBackground(String... uri) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    Log.d("TaskSuccess", "Connection OKAY!");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();
                    Log.d("HttpResponseString", responseString);
                    responseItemName = responseString;
                    out.close();
                    //..more logic
                } else{
                    //Closes the connection.
                    Log.d("TaskFailed", "Connection NOT good...");
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            Log.d("DEBUG", "Before returning true at the end of doInBackground");
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            Log.d("DEBUG", "Entering onPostExecute");
//            mAuthTask = null;
//            showProgress(true);
//
//            if (success) {
//                //finish(); //This line will close the current activity.
//                Log.d("DEBUG", "In onPostExecute, logged in success and exiting!");
//                startActivity(new Intent(LoginAccountActivity.this, BarCodeActivity.class));
//
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
        }
    }

}
