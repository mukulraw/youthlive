package com.youthlive.youthlive;
import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.youthlive.youthlive.Activitys.UserInformation;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.Response.FacebookResponse;
import com.youthlive.youthlive.Response.TwiterResponse;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


import static com.youthlive.youthlive.R.id.Signup;

public class Register extends AppCompatActivity {
    EditText counrt_code, Phone_no;
    Button signup_button;
    int flag=0;
    CallbackManager callbackManager;
    private String fbUserID;
    SessionManager session;
    //private TwitterAuthClient client;
    private int twiterUserID;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static  final  int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=12;
    RelativeLayout relative;

    private static final String TWITTER_KEY = "LBbbEwhJEotJqr3hfXlRHGtUk";
    private static final String TWITTER_SECRET = "RQL5V4FKdtqMLdWs6DkldiCoM7bkN4szL5s8oZKEHXXmHARWNR";

    private static final String BASEESIGNUP_URL = "http://nationproducts.in/youthlive/api/sign_up.php";
    private static final String BASEOTP_URL = "http://nationproducts.in/youthlive/api/sign_up.php";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final String KEY_COUNTRY = "Contery";
    private static final String KEY_PASS = "passs";
    private static final String TAG = "ERROR";
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    private static final String OTP = "otp";
    String Counter_code, Phonenumber ,Msg,opt_Phone,str,verificationCode,userId;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    String jsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_register);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        checkLocationPermission1();
        builder =new AlertDialog.Builder(Register.this);
        counrt_code = findViewById(R.id.counrt_code);
        relative=findViewById(R.id.relative);
        Phone_no = findViewById(R.id.phone_no);
        signup_button = findViewById(Signup);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersign();
            }
        });
    }
    private void usersign() {
        showpDialog();
        Counter_code = counrt_code.getText().toString().trim();
        Phonenumber = Phone_no.getText().toString().trim();
        if (Counter_code.equals("")||Phonenumber.equals(""))
        {
            builder.setTitle("Somethimg went wrong..");
            builder.setMessage("Please fill all the fields...");
            Displayalart("input_error");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEESIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status= jObj.getString("status");
                    if (!status.equals("0")){
                        JSONObject user = jObj.getJSONObject("data");
                        Counter_code = user.getString("countryCode");
                        Phonenumber = user.getString("phone");
                        verificationCode = user.getString("verificationCode");
                        str=jObj.getString("message");
                        userId=user.getString("userId");
                        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userid",userId);
                        editor.commit();

                        Intent intent=new Intent(getApplicationContext(),OTP.class);
                        intent.putExtra("verificationCode",verificationCode);
                        intent.putExtra("userId",userId);
                        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
                        SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(Phonenumber, null, verificationCode, pi,null);
                        //Toast.makeText(getApplicationContext(), "Message Sent successfully!",Toast.LENGTH_LONG).show();

                        //OTP();
                    }else{
                        str=jObj.getString("message");
                        Toast.makeText(Register.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("countryCode", Counter_code);
                params.put("phone",Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void OTP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEOTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject data = obj.getJSONObject("data");
                    String code = data.getString("verificationCode");
                    Toast.makeText(Register.this , code , Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*Intent intent=new Intent(Register.this,OTP.class);
                intent.putExtra("userId",userId);
                intent.putExtra("phone",Phonenumber);
                intent.putExtra("countryCode",Counter_code);
                startActivity(intent);*//**/
                StringRequest sr=new StringRequest(Request.Method.POST, "http://nationproducts.in/youthlive/api/sign_up.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("countryCode", Counter_code);
                        params.put("phone",Phonenumber);
                        return params;
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", Phonenumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Displayalart(final String code) {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (code.equals("input_error")) {
                    counrt_code.setText("");
                    Phone_no.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public boolean checkLocationPermission1() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
                // Show an expanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
