package com.licorne.sample.businesscardreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    TextView txtScanResult;
    Button btnScan;
    JSONObject jObj = null;
    String json = "";
    String scanResult;
    String serverResponse=null;
    ProgressBar loading;
    public ProgressDialog pDialog;

    //create JSON parser Object
    JSONParser jParser = new JSONParser();
    //server url
    private static String getEmployeeInformationUrl;
    //declare employee JSON variables
    private final String TAG_ID = "id";
    private final String TAG_LASTNAME = "lastname";
    private final String TAG_FIRSTNAME = "firstname";

    //declare JSONArray
    JSONArray employeeInformation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtScanResult=(TextView)findViewById(R.id.txtScanResult);
        btnScan=(Button)findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void scan(int code) {
        Intent intentScan = new Intent(Settings.ZXING_SCAN_INTENT);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);

        startActivityForResult(intentScan, code);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Settings.ZXING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(Settings.ZXING_TEXT, intent.getStringExtra("SCAN_RESULT"));
                    obj.put(Settings.ZXING_FORMAT, intent.getStringExtra("SCAN_RESULT_FORMAT"));
                    obj.put(Settings.ZXING_CANCELLED, false);
                    Log.e("SCAN RESULT", intent.getStringExtra("SCAN_RESULT"));

                    scanResult=intent.getStringExtra("SCAN_RESULT");
                    new LoadEmployeeInformation().execute();

                } catch (JSONException e) {
                    //Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(Settings.ZXING_TEXT, "");
                    obj.put(Settings.ZXING_FORMAT, "");
                    obj.put(Settings.ZXING_CANCELLED, true);
                } catch (JSONException e) {
                    //Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
            } else {
                //this.error(new PluginResult(PluginResult.Status.ERROR), this.callback);
            }
        }
    }

    @Override
    public void onClick(View view) {
        scan(Settings.ZXING_REQUEST_CODE);
    }

    /*
	 * Load employee information from server
	 */
    class LoadEmployeeInformation extends AsyncTask<String, String, String> {
        String success;
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            getEmployeeInformationUrl="http://your_domain/get_information.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", scanResult));
            JSONObject json = jParser.makeHttpRequest(getEmployeeInformationUrl, "GET", params);

            if(json!=null){
                try {
                    // Json from server is not null
                    // verify if information are got
                    if(json.getString("success").equals("1")){
                        JSONArray result=json.getJSONArray("information");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);

                            // get information
                            String id = c.getString(TAG_ID);
                            String lastname = c.getString(TAG_LASTNAME);
                            String firstname = c.getString(TAG_FIRSTNAME);
                            serverResponse=id+"\n"+lastname+"\n"+firstname;
                        }
                    }
                } catch (JSONException e) {
                    Log.e("error: ", e.getMessage());
                }
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if(serverResponse!=null){
                        txtScanResult.setText(serverResponse);
                    }else{
                        txtScanResult.setText("this employee ID does not exist!!!");
                    }
                }

            });

        }

    }
}
