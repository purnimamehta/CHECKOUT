package com.app.checkout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tempBarCode;
            tempBarCode = extras.getString("barcode");
            Log.d("Test barcode passing", tempBarCode);
        } else {
            Log.d("Test barcode passing", "extras are null!");
        }

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

    /**
     * Represents an asynchronous item retrieval task using the barcode and storeId
     */
    public class ItemRetrievalTask extends AsyncTask<String, Void, Boolean> {

//        private final String mEmail;
//        private final String mPassword;

        ItemRetrievalTask() {
//            mEmail = email;
//            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(String... uri) {
            //Not done yet
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

}
