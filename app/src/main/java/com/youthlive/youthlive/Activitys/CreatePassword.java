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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.OTP;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreatePassword extends AppCompatActivity {
    Button btncreate;
    EditText userid,password;
    String userid1,Pass="",User,str;

    ProgressBar progress;

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

        progress = (ProgressBar)findViewById(R.id.progress);

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                create();

            }
        });
    }



    public void create()
    {

        String pass = userid.getText().toString();
        String ret = password.getText().toString();

        if (pass.length() > 0)
        {

            if (Objects.equals(ret, pass))
            {


                progress.setVisibility(View.VISIBLE);

                bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<loginResponseBean> call = cr.createPassword(getIntent().getStringExtra("userId") , pass);

                call.enqueue(new Callback<loginResponseBean>() {
                    @Override
                    public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {


                        if (Objects.equals(response.body().getStatus(), "1"))
                        {

                            Toast.makeText(CreatePassword.this , "Password created, Please update your Info" , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreatePassword.this, UserInformation.class);
                            intent.putExtra("userId" , getIntent().getStringExtra("userId"));
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(CreatePassword.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        }


                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<loginResponseBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            }
            else
            {
                password.setError("Password did not match");
            }

        }
        else
        {
            userid.setError("Invalid Password");
        }

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
