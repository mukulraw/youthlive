package com.youthlive.youthlive;

import android.Manifest;
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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.youthlive.youthlive.Activitys.CreatePassword;
import com.youthlive.youthlive.Activitys.UserInformation;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.PendingIntent.getActivity;
import static com.youthlive.youthlive.Signin.MyPREFERENCES;
import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity {

    Button create, login;
    CallbackManager mCallbackManager;
    ImageView facebook_login, googleLogin , twitter_login;
    public static final String mypreference = "mypref";
    private ProgressDialog pDialog;
    String msg, loginid;
    SessionManager session;
    ProgressBar progress;

    String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CAMERA
    };

    GoogleSignInClient mGoogleSignInClient;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private int RC_SIGN_IN = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
                                        Log.d("LoginJSONActivity", object.toString());
                                        try {
                                            JSONObject json = response.getJSONObject();
                                            final String name = json.getString("name");
                                            final String email = json.getString("email");
                                            String id = json.getString("id");
                                            Log.d("FACEBOOKNAME", name + " Email > " + email);
                                            Log.d("FACEBOOKNAME", name + " Email > " + id);
                                            //   String profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                            String image = "https://graph.facebook.com/" + id + "/picture?type=large";



                                            progress.setVisibility(View.VISIBLE);

                                            final bean b = (bean) getApplicationContext();

                                            final Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.BASE_URL)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                                            Call<loginResponseBean> call = cr.socialSignIn(id , email);

                                            call.enqueue(new Callback<loginResponseBean>() {
                                                @Override
                                                public void onResponse(Call<loginResponseBean> call, Response<loginResponseBean> response) {

                                                    if (response.body().getData().getUserName().length() > 0)
                                                    {

                                                        Toast.makeText(Login.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Login.this , MainActivity.class);
                                                        startActivity(intent);

                                                        SharedPreferences sharedpreferences = Login.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString("userid", response.body().getData().getUserId());
                                                        editor.commit();

                                                    }
                                                    else
                                                    {

                                                        Toast.makeText(Login.this , "Please update your info" , Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Login.this, UserInformation.class);
                                                        intent.putExtra("userId" , response.body().getData().getUserId());
                                                        startActivity(intent);
                                                        SharedPreferences sharedpreferences = Login.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString("userid", response.body().getData().getUserId());
                                                        editor.commit();
                                                        finish();

                                                    }


                                                    progress.setVisibility(View.GONE);


                                                }

                                                @Override
                                                public void onFailure(Call<loginResponseBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });



                                            //new FacebookloginAsyncTask().execute(email);
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


/*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("asdasd", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("asdasd", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Asdasd", "printHashKey()", e);
        }

*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);*/


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Login.this));


        if(!hasPermissions(this , PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this , PERMISSIONS , REQUEST_CODE_ASK_PERMISSIONS);
        }


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
                signIn();

            }
        });
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });

        twitter_login = (ImageView)findViewById(R.id.twitter);
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        create = (Button) findViewById(R.id.create);
        login = (Button) findViewById(R.id.log_first);

        progress = (ProgressBar)findViewById(R.id.progress);

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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS)
        {
            if (
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
                    )
            {



            }
            else
            {
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WAKE_LOCK)
                        ) {

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                }
                //permission is denied (and never ask again is  checked)
                //shouldShowRequestPermissionRationale will return false
                else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
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

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            progress.setVisibility(View.VISIBLE);

            final bean b = (bean) getApplicationContext();

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AllAPIs cr = retrofit.create(AllAPIs.class);


            Call<loginResponseBean> call = cr.socialSignIn(account.getId() , account.getEmail());

            call.enqueue(new Callback<loginResponseBean>() {
                @Override
                public void onResponse(Call<loginResponseBean> call, Response<loginResponseBean> response) {

                    if (response.body().getData().getUserName().length() > 0)
                    {

                        Toast.makeText(Login.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this , MainActivity.class);
                        startActivity(intent);

                        SharedPreferences sharedpreferences = Login.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userid", response.body().getData().getUserId());
                        editor.commit();

                    }
                    else
                    {

                        Toast.makeText(Login.this , "Please update your info" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, UserInformation.class);
                        intent.putExtra("userId" , response.body().getData().getUserId());
                        startActivity(intent);
                        SharedPreferences sharedpreferences = Login.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userid", response.body().getData().getUserId());
                        editor.commit();
                        finish();

                    }


                    progress.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<loginResponseBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });



        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("asdas", "signInResult:failed code=" + e.getStatusCode());

        }
    }

}
