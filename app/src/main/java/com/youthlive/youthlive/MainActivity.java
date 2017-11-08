package com.youthlive.youthlive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youthlive.youthlive.Handler.CrudHandler;
import com.youthlive.youthlive.Response.AddPostImageResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final String BASEVIDEO_URL = "http://nationproducts.in/youthlive/api/add_video.php";
    private static final String TAG = "ERROR";
    String str,userId;
    public static final int GALLEY_REQUEST_CODE_CUSTOMER = 10;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static MediaRecorder mediaRecorder;
    String formattedDate;
    Toolbar toolbar;
    private ProgressDialog pDialog;
    DrawerLayout drawer;
    ImageButton online , vlog , live , timeline , profile;
    String mediaPath, mediaPath1;
    ImageView imgView;
    private Uri fileUri;
    private Uri realUri ;
    Bitmap bitmap;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        online = (ImageButton) findViewById(R.id.live);
        vlog = (ImageButton)findViewById(R.id.vlog);
        live = (ImageButton)findViewById(R.id.golive);
        timeline = (ImageButton)findViewById(R.id.timeline);
        profile = (ImageButton)findViewById(R.id.profile);
        SharedPreferences settings = getSharedPreferences("mypref", MODE_PRIVATE);
        userId=settings.getString("userid","");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this , GoLive.class);
                startActivity(intent);
            }
        });


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd" , "online clicked");

                toolbar.setTitle("Live Room");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Live frag1 = new Live();
                ft.replace(R.id.replace , frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });


        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd" , "online clicked");

                toolbar.setTitle("Timeline");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Timeline frag1 = new Timeline();
                ft.replace(R.id.replace , frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd" , "online clicked");

                toolbar.setTitle("Profile");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Profile frag1 = new Profile();
                ft.replace(R.id.replace , frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });
        vlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("asd" , "vlog clicked");

                toolbar.setTitle("Vlog");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

                Vlog frag1 = new Vlog();
                ft.replace(R.id.replace , frag1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

            }
        });
        toolbar.setTitle("Live Room");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Live frag1 = new Live();
        ft.replace(R.id.replace , frag1);
        //ft.addToBackStack(null);
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notifications:

                break;
            case R.id.video:
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
    private void dialogCamera() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MainActivity.this);
        myAlertDialog.setTitle("Uploade Your Pictures");
        myAlertDialog.setMessage("How do you want to set your picture?");
        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(openGallery, "Open Gallery"), GALLEY_REQUEST_CODE_CUSTOMER);
            }
        });
        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        myAlertDialog.show();
    }

    //multipart image..............................
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //dialog =ProgressDialog.show(Activity.this, "", "loading...",false,true);
        try {
            if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                if (resultCode == MainActivity.RESULT_OK) {
                    realUri = Uri.parse(fileUri.getPath());
                    imgPostServices();
                } else if (resultCode == MainActivity.RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "Video capture failed.", Toast.LENGTH_LONG).show();

                }
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //    ivProfileEditProfile.setImageBitmap(photo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), photo, "Title", null);
                realUri = Uri.parse(path);

                try {
                    realUri = Uri.parse(getPath(realUri));
                    //  ivProfileEditProfile.setImageURI(data.getData());
                    imgPostServices();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(RegisterRider.this, ""+realUri, Toast.LENGTH_SHORT).show();
            } else if (resultCode == MainActivity.RESULT_OK && requestCode == GALLEY_REQUEST_CODE_CUSTOMER) {
                if (data.getData() != null) {
                    try {
                        Log.d("TAG", "not cust");
                        realUri = data.getData();
                        // Get real path to make File
                        realUri = Uri.parse(getPath(data.getData()));
                        bitmap = BitmapFactory.decodeFile(realUri.getPath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        imgPostServices();
                        //Log.d(TAG, "Image path :- " + realUri);
                        //   ivProfileEditProfile.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "plaese select another image", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String getPath(Uri uri) throws Exception {
        // this method will be used to get real path of Image chosen from gallery.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor =MainActivity.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private TypedFile makeFile(String uri) {
        // this will make file which is required by Retrofit.
        File file = new File(uri);
//        File photo = new File(file,  "Pic.image");
        TypedFile typedFile = new TypedFile("",file);
        Log.d("show", String.valueOf(file));
        Log.d("show", String.valueOf(uri));
        return typedFile;
    }
    //video uploading here.,.............................................
    /** Create a file Uri for saving an image or video */
    /** Create a File for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
     /*    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
       mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
       mediaRecorder.setVideoEncodingBitRate(690000);
       mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
       mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(640, 480);*/
        return Uri.fromFile(getOutputMediaFile(type));

    }
    private static File getOutputMediaFile(int type){
        // Check that the SDCard is mounted
        //   File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "JUMP Video");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/youyhlive/" +"/Send Video/");
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                // Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.", Toast.LENGTH_LONG).show();
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        // Create a media file name
        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());
        File mediaFile;
        if(type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }
        return mediaFile;
    }
    private void imgPostServices(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        formattedDate = df.format(c.getTime());
        final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...", false, false);
        RestAdapter adapter=new RestAdapter.Builder().setEndpoint(Constant.ROOT_URL).build();
        CrudHandler.AddPostImage addPostImage=adapter.create(CrudHandler.AddPostImage.class);
        addPostImage.addPost(attechPostImg(), new retrofit.Callback<AddPostImageResponse>() {
            @Override
            public void success(AddPostImageResponse addPostImageResponse, retrofit.client.Response response) {
                if (addPostImageResponse.getStatus() == 1) {
                    Toast.makeText(MainActivity.this, "" + addPostImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    pd.dismiss();
                    Log.d("====> Error Image", error.getMessage());
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Network Problem.....");
                    builder.setMessage(error.getMessage());
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    pd.dismiss();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MultipartTypedOutput attechPostImg() {

        Log.d("video test",""+realUri.toString());
        MultipartTypedOutput multipartTypedOutput=new MultipartTypedOutput();
        try{
            multipartTypedOutput.addPart("userId",new TypedString("94"));
           // multipartTypedOutput.addPart("date",new TypedString(formattedDate));
            // multipartTypedOutput.addPart("photo",makeFile(realUri.toString()));
          //  multipartTypedOutput.addPart("post",makeFile(realUri.toString()));
            multipartTypedOutput.addPart("video",makeFile(realUri.toString()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return multipartTypedOutput;
    }

}
