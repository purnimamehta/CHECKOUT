package com.app.checkout;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/* ********************************************************************
    @Author: Sagar                 @Date: 2015 - 11 - 28
    *Class to handle all Barcode Scanning
 * ********************************************************************/

public class BarCodeActivity extends ActionBarActivity {
    private String barcode; // Variable to hold inputted barcode scan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something"); // Optional- take out or change if needed
        integrator.setOrientationLocked(false); // Scanning can be done in any orientation
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }
    // TODO: Get rid of method after success in connecting to ShoppingCartActivity
    public void scanBarcodeCustomLayout(View view) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        barcode = result.getContents(); // Store scanned value here
        // Code for sending barcode variable to ShoppingCartActivity
        Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class); // create new Intent for sending to ShoppingCartActivity
        intent.putExtra("barcode", barcode); // Add barcode variable to 'extra' in the Intent
        startActivity(intent);

        //TODO: Delete commented code after ShoppingCartActivity works
        /*
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Weird");
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
        */

        // TODO: Add following code block to ShoppingCartActivity after set up
        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getString("barcode");
        }
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
