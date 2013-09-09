package net.ralphpina.example.brendanappsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    TextView textView;
    EditText editTextID;
    EditText editTextName;
    Button buttonGetCustomers;
    Button buttonPostInfo;
    Button buttonDelete;
    Button buttonPutInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        editTextID = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonGetCustomers = (Button) findViewById(R.id.buttonGetCustomers);
        buttonPostInfo = (Button) findViewById(R.id.buttonPostInfo);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonPutInfo = (Button) findViewById(R.id.buttonPutInfo);

        buttonGetCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHttpClient httpClient = new AndroidHttpClient("http://api.XXXXXX.com/androidapp/index.php/customers/");
                httpClient.get("", null, new AsyncCallback() {

                    @Override
                    public void onComplete(HttpResponse httpResponse) {

                        try {
                            JSONArray array = new JSONArray(httpResponse.getBodyAsString());
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                sb.append(obj.getString("id") + " " + obj.getString("name") + "\n");
                            }
                            textView.setText(sb.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        textView.setText("There was an error posting");
                        e.printStackTrace();
                    }
                });
            }
        });

        buttonPostInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidHttpClient httpClient = new AndroidHttpClient("http://api.XXXXXX.com/androidapp/index.php/customers/");
                httpClient.setMaxRetries(5);
                ParameterMap params = httpClient.newParams()
                        .add("id", editTextID.getText().toString())
                        .add("name", editTextName.getText().toString());
                httpClient.post("", params, new AsyncCallback() {
                    @Override
                    public void onComplete(HttpResponse httpResponse) {
                        try {
                            JSONObject response = new JSONObject(httpResponse.getBodyAsString());
                            boolean success = response.getJSONObject("success").getString("code").equals("200");
                            if (success) {
                                textView.setText("New customer posted: id = " + editTextID.getText().toString() +
                                                 " name = " + editTextName.getText().toString());
                            }
                        } catch (JSONException e) {
                            textView.setText("There was an error posting your data");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        textView.setText("There was an error posting your data");
                        e.printStackTrace();
                    }
                });
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidHttpClient httpClient = new AndroidHttpClient("http://api.XXXXXX.com/androidapp/index.php/customers/");
                httpClient.delete(editTextID.getText().toString(), null, new AsyncCallback() {
                    @Override
                    public void onComplete(HttpResponse httpResponse) {
                        try {
                            JSONObject response = new JSONObject(httpResponse.getBodyAsString());
                            boolean success = response.getJSONObject("success").getString("code").equals("200");
                            if (success) {
                                textView.setText("Customer deleted: id = " + editTextID.getText().toString());
                            }
                        } catch (JSONException e) {
                            textView.setText("There was an error deleting the customer");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        textView.setText("There was an error deleting the customer");
                        e.printStackTrace();
                    }
                });
            }
        });


        buttonPutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("I was not able to get this working :(");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
