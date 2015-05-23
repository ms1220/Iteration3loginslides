/*

Claritas: ‘Clarity through innovation’

Project: SocBox

Module: Login Activity

Code File Name: Iteration3loginslides

Description: this is the functionality and activity that the user experiences whilst they log into
the SocBox system

Initial Authors: Mark Stonehouse

Change History:

Version: 0.1

Author: Mark Stonehouse

Change: Created original version

Date: 23/05/2015

Traceabilty:

Tag: D/LPF/01-16

Note: This file is still work in progress.

 Todo: Add input for video file to be played and retrieve it from it's file.

    Add ability to create and play video in specified sub-frame.

    Add User control.

*/
package com.iteration2.mark.iteration3loginslides;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.widget.PopupWindow.OnDismissListener;

public class MainActivity extends ActionBarActivity {

    View popUpView;
    View popUpView2;
    PopupWindow popUp;
    PopupWindow popUp2;
    public String passEntered;
    public String mailEntered;
    public TextView text;
    // url of server php files
    // JSON parser class
    JSONParserForDatabase jsonParser = new JSONParserForDatabase();

    private static final String url_login = "http://192.168.1.85/Database_connection_php/db_check_userlogin.php";
    private static final String url_create_account = "http://192.168.1.85/Database_connection_php/db_login_create_account.php";

    // JSON Node names
    private static final String TAG_SUCCEEDED = "succeeded";
    private static final String TAG_USERDATA = "user_data";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_DEBUGMESSAGE = "debugMessage";

    public void displayPopUp(View view){

        LayoutInflater layoutInflater = (LayoutInflater)
                MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpView = layoutInflater.inflate(R.layout.popup, null);
        popUp = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popUpView2 = layoutInflater.inflate(R.layout.popup2, null);
        popUp2 = new PopupWindow(popUpView2, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);

        DismissPopUp listener = new DismissPopUp(popUp2);
        popUp.setOnDismissListener(listener);

        ColorDrawable background = new ColorDrawable( -1);
        popUp.setBackgroundDrawable(background);
        popUp.setOutsideTouchable(false);

        ColorDrawable background2 = new ColorDrawable( -12303292);
        background2.setAlpha(125);
        popUp2.setBackgroundDrawable(background2);
        popUp2.setTouchable(false);
        popUp2.setFocusable(false);

        EditText editText = (EditText) findViewById(R.id.passBox);
        passEntered = editText.getText().toString();

        EditText editTextMail = (EditText) findViewById(R.id.mailBox);
        mailEntered = editTextMail.getText().toString();

        text = (TextView)popUpView.findViewById(R.id.popUpText);

        if((passEntered.matches(""))&&(!mailEntered.matches(""))){
            //loginMessage = "password not entered";
            text.setText("empty pass real mail");
        }
        else if((mailEntered.matches(""))&&(!passEntered.matches(""))){
            //loginMessage = "E-Mail not entered";
            text.setText("empty mail real pass");
        }
        else if((mailEntered.matches(""))&&(passEntered.matches(""))){
            text.setText("all fields null");
        }
        else if((!mailEntered.matches(""))&&(!passEntered.matches(""))){
            text.setText("NOT NULL");

        }

        popUp2.showAtLocation(view,1,0,0);
        popUp.showAtLocation(view,0,250,250);
       }

    public void dismissPopUp(View view){
        if (text.getText() == "NOT NULL"){
            new Login().execute("LOGIN");
        }
        else{
            popUp.dismiss();
            popUp2.dismiss();
        }
    }

    /*public void goToCreate(){
        Intent intent = new Intent(MainActivity.this, SecondSlide.class);
        startActivity(intent);
    }*/

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scroll[] = new int[2];
            w.getLocationOnScreen(scroll);
            float x = event.getRawX() + w.getLeft() - scroll[0];
            float y = event.getRawY() + w.getTop() - scroll[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
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
    /**
     * Background Async Task to Get complete product details
     * */
    class Login extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateStatusMessage("Accessing Database");
        }

        /**
         * Accessing DB in background thread
         * */
        protected String doInBackground(String... params) {

            int succeeded;

            try {
                // Building Parameters
                String debugMessage;
                JSONObject json;

                List<NameValuePair> dataToSend = new ArrayList<NameValuePair>();
                dataToSend.add(new BasicNameValuePair("username", mailEntered));
                dataToSend.add(new BasicNameValuePair("password", passEntered));

                if(params[0].equals("CREATE"))
                {
                    // Send the new username and password to the database by making HTTP POST request
                    // A POST request is used as we are writing to the database
                    json = jsonParser.makeHttpRequest(url_create_account, "POST", dataToSend);
                    // check your log for json response
                    Log.d("User login status", json.toString());
                }
                else
                {
                    // Check the username and password against the database by making HTTP GET request
                    // A GET request is used as we are merely retrieving information and not writing to the database
                    json = jsonParser.makeHttpRequest(url_login, "GET", dataToSend);
                    // check your log for json response
                    Log.d("User login status", json.toString());
                }

                //The returned json onject should be an array in the form [Succeeded: (value), debugMessage (message) Login details: [username: (username string), password: (password string)]]

                // json success tag
                succeeded = json.getInt(TAG_SUCCEEDED);
                if (succeeded == 1) {
                    debugMessage= json.getString(TAG_DEBUGMESSAGE);
                    updateStatusMessage(debugMessage);
                    // successfully received login details
                    JSONArray userDataArray = json.getJSONArray(TAG_USERDATA); // JSON Array
                    // get first user login details from JSON Array
                    JSONObject userData = userDataArray.getJSONObject(0);

                    SharedPreferences userLoginDetails = getSharedPreferences("userlogindetails", 0);
                    SharedPreferences.Editor editor = userLoginDetails.edit();
                    editor.putString("username", userData.getString(TAG_USERNAME));
                    editor.putString("password", userData.getString(TAG_PASSWORD));
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, LoggedIn.class);
                    //Start the activity
                    startActivity(intent);

                }else{
                    // user login details incorrect
                    debugMessage= json.getString(TAG_DEBUGMESSAGE);

                    updateStatusMessage(debugMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                updateStatusMessage("Error: connection problems");
            }
            return null;
        }

        //This is the function to update UI objects from the background thread. If you try to do so directly from
        //a background thread it will crash
        private void updateStatusMessage(final String statusMessage) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //statusText = (TextView)findViewById(R.id.statusText);
                                //statusText.setText(statusMessage);
                            }
                        });
                    } catch (final Exception ex) {
                        Log.i("Error:","Exception when updating status message in UI thread");
                    }
                }
            }.start();
        }
}}