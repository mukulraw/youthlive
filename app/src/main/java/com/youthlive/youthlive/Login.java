package com.youthlive.youthlive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.youthlive.youthlive.DBHandler.SessionManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity {

    Button create, login;
    CallbackManager mCallbackManager;
    ImageView facebook_login, googleLogin;
    public static final String mypreference = "mypref";
    private ProgressDialog pDialog;
    String msg, loginid;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        Log.d("LoginActivity", response.toString());
                                        Log.d("LoginJSONActivity", object.toString());
                                        try {
                                            JSONObject json = response.getJSONObject();
                                            final String name = json.getString("name");
                                            final String email = json.getString("email");
                                            String id = json.getString("id");
                                            Log.d("FACEBOOKNAME", name + " Email > " + email);
                                            Log.d("FACEBOOKNAME", name + " Email > " + email);
                                            //   String profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                            String image = "https://graph.facebook.com/" + id + "/picture?type=large";
                                            new FacebookloginAsyncTask().execute(email);
                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginManager.getInstance().logOut();
        try {
            getUserinfo();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        session = new SessionManager(getApplicationContext());
        googleLogin = findViewById(R.id.ggleLogin);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent xyz = new Intent(getApplicationContext(), Live.class);
                startActivity(xyz);

            }
        });
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        create = (Button) findViewById(R.id.create);
        login = (Button) findViewById(R.id.log_first);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                Login.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alredyacco = new Intent(Login.this, Signin.class);
                startActivity(alredyacco);
                Login.this.overridePendingTransition(R.anim.outt_anim, R.anim.out_anim);

            }
        });
    }

    public void Alredyaccount(View view) {
        Intent alredyacco = new Intent(Login.this, Signin.class);
        startActivity(alredyacco);

    }

    public void getUserinfo() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest nd = MessageDigest.getInstance("SHA");
            nd.update(signature.toByteArray());
            Log.d("KeyHash:-", Base64.encodeToString(nd.digest(), Base64.DEFAULT));
        }
    }

    private class FacebookloginAsyncTask extends AsyncTask<String, Void, Void> {    //////today list asyntask
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            //String name=params[0];
            String email = params[0];
            // String image=params[0];
            String encoded = "null";

           /* URL imageURL = null;
            try {
                imageURL = new URL(image);
                Bitmap imagee = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                // Bitmap myBitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagee.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String url = "http://nationproducts.in/youthlive/api/socialsign_up.php";
            ArrayList<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<org.apache.http.NameValuePair>();
            //  nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("name", name));
            nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("email", email));
            // nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("image", encoded));

            ServiceHandler sh = new ServiceHandler();
            String userdata = sh.makeServiceCall(url,
                    ServiceHandler.POST, nameValuePairs);
            /*if (userdata != null)
                try {
                    JSONObject jsonObj = new JSONObject(userdata);
                    String strstatus=jsonObj.getString("status");
                    if (strstatus.equals("1")){
                        JSONObject obj2=jsonObj.getJSONObject("data");
                        msg=jsonObj.getString("message");
                        loginid=obj2.getString("userId");
                    }else{
                        msg=jsonObj.getString("message");
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                        }
                    });
                    e.printStackTrace();
                }
            else {
                Toast.makeText(getApplicationContext(), "No Data To Display", Toast.LENGTH_SHORT).show();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
          /*  SharedPreferences sharedpreferences = Login.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userId", loginid);
            editor.commit();
            String  userId = sharedpreferences.getString("userId", "");*/

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new View(this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
