package com.youthlive.youthlive.Activitys;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.OTP;
import com.youthlive.youthlive.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreatePassword extends AppCompatActivity {
    Button btncreate;
    EditText userid,password;
    String userid1,Pass="",User,str;

    String CREATEapi="http://nationproducts.in/youthlive/api/create_password.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userid1=settings.getString("userid","");
        btncreate=(Button)findViewById(R.id.btncreate);
        userid=(EditText)findViewById(R.id.userid);
        password=(EditText)findViewById(R.id.password);
        SharedPreferences pass = getSharedPreferences("mypref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pass.edit();
        editor.putString("password",Pass);
        editor.commit();
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pass=password.getText().toString().trim();
                User=userid.getText().toString().trim();
                if(Pass.equals("")){  Toast.makeText(CreatePassword.this, "enter password", Toast.LENGTH_SHORT).show();}
                else if(User.equals("")){  Toast.makeText(CreatePassword.this, "enter Userid", Toast.LENGTH_SHORT).show();}
                else{
                    createpassword();}
            }
        });
    }
    public void createpassword()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATEapi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if (!status.equals("0"))
                    {
                        JSONObject user = jObj.getJSONObject("data");
                        userid1=user.getString("userId");
                        Pass=user.getString("password");
                        Toast.makeText(CreatePassword.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        //String verificationCode=jObj.getString("message");
                        Intent i=new Intent(CreatePassword.this,UserInformation.class);
                        startActivity(i);

                    }else {

                        str=jObj.getString("message");
                        Toast.makeText(CreatePassword.this, str, Toast.LENGTH_SHORT).show();
                    }

                   /* Intent intent=new Intent(getApplicationContext(),CreatePassword.class);
                    PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreatePassword.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userid1);
                params.put("password",Pass);
                return params;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(CreatePassword.this);
        requestQueue.add(stringRequest);
    }
}
