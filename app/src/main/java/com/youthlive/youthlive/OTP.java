package com.youthlive.youthlive;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.Activitys.CreatePassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTP extends AppCompatActivity {
    private static final String OTPBASEVERFRY_URL="http://nationproducts.in/youthlive/api/varify_code.php";
    private static final String OTPBASERESEND_URL="http://nationproducts.in/youthlive/api/resend_code.php";

    private static final String TAG = "RESPONSE";
    EditText verify_otpnumber;
    String VerifyNumber1,userId,countryCode,phone,str,varifycode,verificationCode;
    TextView regeneratepassword;
    private ProgressDialog pDialog;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        checkLocationPermission1();
        verify_otpnumber=(EditText)findViewById(R.id.verify_otpnumber);
        varifycode=getIntent().getStringExtra("verificationCode");
        userId=getIntent().getStringExtra("userId");
        // phone=getIntent().getStringExtra("phone");
        // countryCode=getIntent().getStringExtra("countryCode");
        regeneratepassword=(TextView)findViewById(R.id.regeneratepassword);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        verify = (Button)findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyNumber1=verify_otpnumber.getText().toString().trim();
                if(VerifyNumber1.equals("")){ Toast.makeText(OTP.this, "enter otp number", Toast.LENGTH_SHORT).show();}
                else{
                    VerifyOtp();}
            }
        });
        regeneratepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, OTPBASERESEND_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status= jObj.getString("status");
                            if (!status.equals("0"))
                            {
                                Toast.makeText(OTP.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                                varifycode=jObj.getString("verificationCode");
                                Intent intent=new Intent(getApplicationContext(),OTP.class);
                                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
                                SmsManager sms=SmsManager.getDefault();
                                sms.sendTextMessage(phone, null, verificationCode, pi,null);
                            }
                            else
                            {
                                str=jObj.getString("message");
                                Toast.makeText(OTP.this, str, Toast.LENGTH_SHORT).show();
                            }

//                            Toast.makeText(getApplicationContext(), "Message Sent successfully!",
//                                    Toast.LENGTH_LONG).show();
//                            Intent i=new Intent(OTP.this,MainActivity.class);
//                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OTP.this, error.toString(), Toast.LENGTH_SHORT).show();
                        hidepDialog();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone", phone);
                        params.put("countryCode",countryCode);
                        params.put("userId", userId);
                        params.put("code",varifycode);
                        return params;
                    }
                } ;
                RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
                requestQueue.add(stringRequest);
            }
        });

    }
    private void VerifyOtp() {
        VerifyNumber1=verify_otpnumber.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OTPBASEVERFRY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status= jObj.getString("status");
                    if (!status.equals("0"))
                    {
                        Toast.makeText(OTP.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(OTP.this,CreatePassword.class);
                        startActivity(i);
                    }
                    else
                    {
                        str=jObj.getString("message");
                        Toast.makeText(OTP.this, str, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OTP.this, error.toString(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("code",varifycode);
                return params;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
