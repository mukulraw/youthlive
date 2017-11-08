package com.youthlive.youthlive.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.youthlive.youthlive.MainActivity;
import com.youthlive.youthlive.OTP;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.Register;
import com.youthlive.youthlive.Signin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformation extends AppCompatActivity {
    String UserInfo = "http://nationproducts.in/youthlive/api/update_user_info.php";
    EditText user_name, BirthDay, Biodata;
    private static final int DATE_PICKER_ID = 112;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int RESULT_LOAD_IMG = 1;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    String Username, Birthdate, BioData, gender, userid1="", Female, str, encodedImage = "", imgDecodableString, pImage = "";
    RadioButton radioMale, radioFemale;
    RadioGroup rg;
    CircleImageView userimage;
    AlertDialog.Builder builder;
    Button signup_button;
    ImageView uploadpic;
    private int year, month, day;
    Uri imageuri, selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_user_information);
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userid1 = settings.getString("userid", "");
        builder = new AlertDialog.Builder(UserInformation.this);
        user_name = (EditText) findViewById(R.id.user_name);
        BirthDay = (EditText) findViewById(R.id.password_edit);
        rg = (RadioGroup) findViewById(R.id.radioSex);
        Biodata = (EditText) findViewById(R.id.conform_passwor);
        signup_button = (Button) findViewById(R.id.signup_button);
        userimage = findViewById(R.id.userimage);
        uploadpic = findViewById(R.id.uploadpic);
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        BirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = user_name.getText().toString().trim();
                Birthdate = BirthDay.getText().toString().trim();
                BioData = Biodata.getText().toString().trim();
                //  int selectedId = rg.getCheckedRadioButtonId();
                //   rg = (RadioGroup) findViewById(selectedId);
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int position) {
                        radioMale = (RadioButton) findViewById(R.id.radioMale);
                        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
                        switch (position) {
                            case R.id.radioMale:
                                gender = "Male";
                                break;
                            case R.id.radioFemale:
                                gender = "Female";
                                break;
                            default:
                                break;
                        }
                    }
                });
                if (Username.equals("") || Birthdate.equals("") || BioData.equals(" ")) {
                    builder.setTitle("Somethimg went wrong..");
                    builder.setMessage("Please fill all the fields...");
                    Displayalart("input_error");
                }
               /* else if (Birthdate.equals(""))
                {
                    Toast.makeText(UserInformation.this, "enter birthday", Toast.LENGTH_SHORT).show();
                }

                else if (BioData.equals(""))
                {

                }*/

                else {
                    filldata();
                }
            }
        });
    }

    private void filldata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    if (!status.equals("0")) {
                        JSONObject obj2 = jObj.getJSONObject("data");
                        userid1 = obj2.getString("userId");
                        Username = obj2.getString("userName");
                        Birthdate = obj2.getString("birthday");
                        pImage = obj2.getString("image");
                        gender = obj2.getString("gender");
                        BioData = obj2.getString("bio");
                        Toast.makeText(UserInformation.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UserInformation.this, Signin.class);
                        startActivity(i);
                    } else {
                        str = jObj.getString("message");
                        Toast.makeText(UserInformation.this, str, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserInformation.this, error.toString(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userName", Username);
                params.put("gender", "male");
                params.put("birthday", Birthdate);
                params.put("bio", BioData);
                params.put("userId", userid1);
                params.put("image", encodedImage);
               /* String imageurl = "http://nationproducts.in/youthlive/upload/user/" + pImage;
                if (!pImage.equals(null)) {
                    Picasso.with(UserInformation.this)
                            .load(imageurl).noFade()
                            .into(userimage);
                } else if (pImage.equals(null)) {
                    Picasso.with(UserInformation.this)
                            .load(R.drawable.face).noFade();
                }*/
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
                    user_name.setText("");
                    BirthDay.setText("");
                    Biodata.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            BirthDay.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
            Birthdate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        }
    };

    private void SelectImage() {
        final CharSequence[] items = {"Take Photo from Camera",
                "Choose from Gallery",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserInformation.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.cameraicon);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo from Camera")) {
                    Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getpic.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                    startActivityForResult(getpic, MY_PERMISSIONS_REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && resultCode != 0 && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //  bitmap=layout_to_image.convert_layout();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            userimage.setImageBitmap(photo);
        }
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap Image = BitmapFactory.decodeStream(imageStream);

                Image = getResizedBitmap(Image, 400);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Image.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //  image_profile=(HeartImageView) findViewById(R.id.image_profile);
                // Set the Image in ImageView after decoding the String
                userimage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                userimage.setImageBitmap(Image);
               /* SharedPreferences sharedpreferences1 = profile.this.getSharedPreferences("MyPrefs", 1);
                SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                editor1.putString("loginid", String.valueOf(Image));
                editor1.commit();*/

            } else {
              /*  Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(UserInformation.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(UserInformation.this, ALLOW_KEY)) {
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(UserInformation.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(UserInformation.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(UserInformation.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
        }
    }
    private void showSettingsAlert() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserInformation.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(UserInformation.this);
                    }
                });

        alertDialog.show();
    }

    private void showAlert() {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserInformation.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(UserInformation.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }
}
