package com.youthlive.youthlive;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.Activitys.UserInformation;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signin extends AppCompatActivity {
    EditText phone_number, password_login, phoneno;
    Button Login;
    SessionManager session;
    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "userSession";
    private static final String BASEELOGIN_URL = "http://nationproducts.in/youthlive/api/mobile_signin.php";
    private static final String KEY_NUM = "number";
    private static final String BASEOTP_URL = "http://nationproducts.in/youthlive/api/sign_up.php";
    private static final String KEY_PASS = "passs";
    String User_number, User_passwords, Counter_code, Phonenumber = "", verificationCode, str, userId, pass1 = "";
    AlertDialog.Builder builder;
    Button login;
    LinearLayout cancel_layout, cancel_ok;
    ProgressBar progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        builder = new AlertDialog.Builder(Signin.this);
        phone_number = findViewById(R.id.phone_number);
        password_login = findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.login);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId = settings.getString("userid", "");
        pass1 = settings.getString("password", "");

        progress = (ProgressBar) findViewById(R.id.progress);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();

            }
        });
    }

    public void showCustomDialog(View view) {
        forgotdialog();
    }

    private void forgotdialog() {
        final Dialog dialog = new Dialog(Signin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgote_customedialog);
        cancel_layout = dialog.findViewById(R.id.cancel_layout);
        phoneno = (EditText) dialog.findViewById(R.id.txtphoneno);
        cancel_ok = dialog.findViewById(R.id.txtok);
        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneno.getText().toString().equals("")) {
                    Toast.makeText(Signin.this, "enter phone number", Toast.LENGTH_SHORT).show();
                }
                forgotPassword();
            }
        });
    }


    public void login() {

        String phone = phone_number.getText().toString();
        String password = password_login.getText().toString();

        if (phone.length() > 0) {

            if (password.length() > 0) {


                progress.setVisibility(View.VISIBLE);

                bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<loginResponseBean> call = cr.signIn(phone , password);

                call.enqueue(new Callback<loginResponseBean>() {
                    @Override
                    public void onResponse(Call<loginResponseBean> call, retrofit2.Response<loginResponseBean> response) {

                        Toast.makeText(Signin.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                        Intent Inbt = new Intent(Signin.this, MainActivity.class);
                        startActivity(Inbt);
                        SharedPreferences sharedpreferences = Signin.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userid", response.body().getData().getUserId());
                        editor.commit();
                        session.createLoginSession(Signin.this, userId);

                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<loginResponseBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } else {
                password_login.setError("Invalid Password");
            }

        } else {
            phone_number.setError("Invalid Phone");
        }


    }


    private void userloginn() {

        User_number = phone_number.getText().toString().trim();
        User_passwords = password_login.getText().toString().trim();
        if (User_number.equals("") || User_passwords.equals("")) {
            builder.setTitle("Somethimg went wrong..");
            builder.setMessage("Please fill all the fields...");
            Displayalart("input_error");
        } else {
            showpDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASEELOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        if (!status.equals(0)) {
                            JSONObject obj2 = jObj.getJSONObject("data");
                            userId = obj2.getString("userId");
                            // str = jObj.getString("message");
                            Phonenumber = obj2.getString("phone");
                            User_passwords = obj2.getString("password");
                            Intent Inbt = new Intent(Signin.this, MainActivity.class);
                            startActivity(Inbt);
                            SharedPreferences sharedpreferences = Signin.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userid", userId);
                            editor.commit();
                            session.createLoginSession(Signin.this, userId);

                        } else {
                            str = jObj.getString("message");
                            Toast.makeText(Signin.this, str, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hidepDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", User_number);
                    params.put("password", pass1);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void forgotPassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://nationproducts.in/youthlive/api/sign_up.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Log.d(TAG, response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals("0")) {
                        JSONObject user = jObj.getJSONObject("data");
                        Counter_code = user.getString("countryCode");
                        Phonenumber = user.getString("phone");
                        verificationCode = user.getString("verificationCode");
                        str = jObj.getString("message");
                        userId = user.getString("userId");
                        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userid", userId);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), OTP.class);
                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(Phonenumber, null, verificationCode, pi, null);
                    } else {
                        str = jObj.getString("message");
                        Toast.makeText(Signin.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("countryCode", Counter_code);
                params.put("phone", Phonenumber);
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
                // Log.d(TAG, response.toString());
                Intent intent = new Intent(Signin.this, Signin.class);
                intent.putExtra("userId", userId);
                intent.putExtra("phone", Phonenumber);
                intent.putExtra("countryCode", Counter_code);
                startActivity(intent);
                StringRequest sr = new StringRequest(Request.Method.POST, "http://nationproducts.in/youthlive/api/sign_up.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("countryCode", Counter_code);
                        params.put("phone", Phonenumber);
                        return params;
                    }
                };
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Signin.this, error.toString(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
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
                    phone_number.setText("");
                    password_login.setText("");
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
}
