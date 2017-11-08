package com.youthlive.youthlive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.youthlive.youthlive.Activitys.FollowingActivity;
import com.youthlive.youthlive.Activitys.MessaageActivity;
import com.youthlive.youthlive.Activitys.MyVlog;
import com.youthlive.youthlive.Activitys.PersonalInfo;
import com.youthlive.youthlive.Activitys.RattingActivity;
import com.youthlive.youthlive.DBHandler.SessionManager;
import com.youthlive.youthlive.Handler.CrudHandler;
import com.youthlive.youthlive.Marchmallowpermission.MarshMallowPermission;
import com.youthlive.youthlive.Response.ChangeCoverImageResponse;
import com.youthlive.youthlive.Response.ProfileDetails;
import com.youthlive.youthlive.Response.UpdateProfileResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {
    TextView messagechate,followingAct,ratting_act,personal_info,vlogActivity;
    ImageView choose_file,ivBlurProfile,profile_image1;
    LinearLayout cover_image,profile_image;
    public static final int GALLEY_REQUEST_CODE_CUSTOMER = 10;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri realUri ;
    HashMap<String, String> user;
    String userID;
    Bitmap bitmap;
    SessionManager session;
    CircleImageView profile_imagee;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        choose_file=view.findViewById(R.id.choose_file);
        messagechate=view.findViewById(R.id.messagechatee);
        ratting_act=view.findViewById(R.id.ratting_act);
        ivBlurProfile=(ImageView)view.findViewById(R.id.ivBlurProfile1);
        profile_imagee=view.findViewById(R.id.profile_image);
        vlogActivity=view.findViewById(R.id.vlogActivity);
        session=new SessionManager(getActivity());
        user= session.getUserDetails();
        userID = user.get(SessionManager.USER_ID);
        updateProfile();
        vlogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MyVlog.class);
                startActivity(intent);
            }
        });
        personal_info=view.findViewById(R.id.personal_info);
        personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PersonalInfo.class);
                startActivity(intent);


            }
        });
        ratting_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RattingActivity.class);
                startActivity(intent);

            }
        });

        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDialog();
            }
        });
        followingAct=view.findViewById(R.id.followingAct);
        followingAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),FollowingActivity.class);
                startActivity(intent);

            }
        });
        messagechate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MessaageActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
    public void ChooseDialog()
    {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profile_coverdialog);
        cover_image=dialog.findViewById(R.id.cover_image);
        profile_image=dialog.findViewById(R.id.profile_image);
        cover_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                coverProfileUpdate();
            }

        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coverProfileUpdate();

            }
        });

        dialog.show();
    }
    private int coverProfileUpdate() {
        boolean result= MarshMallowPermission.checkPermission(getActivity());
        if(result) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
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
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });
            myAlertDialog.show();
        }
        return 0;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivBlurProfile.setImageBitmap(photo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, "Title", null);
                realUri = Uri.parse(path);

                try {
                    realUri = Uri.parse(getPath(realUri));

                    responseCoverImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ivBlurProfile.setImageBitmap(photo);
                //   Toast.makeText(RegisterRider.this, ""+realUri, Toast.LENGTH_SHORT).show();
            } else if (resultCode == getActivity().RESULT_OK && requestCode == GALLEY_REQUEST_CODE_CUSTOMER) {
                if (data.getData() != null) {
                    try {
                        Log.d("TAG", "not cust");
                        realUri = data.getData();
                        // Get real path to make File
                        realUri = Uri.parse(getPath(data.getData()));
                        bitmap = BitmapFactory.decodeFile(realUri.getPath());
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        //Log.d(TAG, "Image path :- " + realUri);
                        responseCoverImage();
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                    ivBlurProfile.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(getActivity(), "plaese select another image", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
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
    private void responseCoverImage() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "Please wait", "Loading...", false, false);
        RestAdapter adapter=new RestAdapter.Builder().setEndpoint(Constant.ROOT_URL).build();
        CrudHandler.CoverImageUpdate coverImageUpdate=adapter.create(CrudHandler.CoverImageUpdate.class);
        coverImageUpdate.changeCoverImage(attechCoverImg(), new Callback<ChangeCoverImageResponse>() {
            @Override
            public void success(ChangeCoverImageResponse changeCoverImageResponse, Response response) {
                try {
                    //Toast.makeText(getActivity(), "" + changeCoverImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (changeCoverImageResponse.getStatus() == 1) {
                        Log.d("====> Cover Image", changeCoverImageResponse.getMessage());
                        //resfresh data updating cover image
                        pd.dismiss();
                        Toast.makeText(getActivity(),changeCoverImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (changeCoverImageResponse.getStatus() == 0) {
                        Toast.makeText(getActivity(), "" + changeCoverImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                pd.dismiss();
                try {
                    Log.d("====> Error Image", error.getMessage());
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Network Problem.....");
                    builder.setMessage(error.getMessage());
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);*/
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }
    private MultipartTypedOutput attechCoverImg() {
        MultipartTypedOutput multipartTypedOutput=new MultipartTypedOutput();
        try {
            multipartTypedOutput.addPart("userId",new TypedString(userID));
            multipartTypedOutput.addPart("image",makeFile(realUri.toString()));
        }
        catch (Exception e){e.printStackTrace();}
        return multipartTypedOutput;
    }
    private void responseProfileimage()
    {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "Please wait", "Loading...", false, false);
        RestAdapter adapter=new RestAdapter.Builder().setEndpoint(Constant.ROOT_URL).build();
        CrudHandler.UpdateProfile updateProfile=adapter.create(CrudHandler.UpdateProfile.class);
        updateProfile.updateViewProfile(attechCoverImg1(), new Callback<UpdateProfileResponse>() {
            @Override
            public void success(UpdateProfileResponse changeprofileimage, Response response) {
                try {
                    //Toast.makeText(getActivity(), "" + changeCoverImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (changeprofileimage.getStatus() == 1) {
                        Log.d("====> Cover Image", changeprofileimage.getMessage());
                        //resfresh data updating cover image
                        pd.dismiss();
                        Toast.makeText(getActivity(),changeprofileimage.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (changeprofileimage.getStatus() == 0) {
                        Toast.makeText(getActivity(), "" + changeprofileimage.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pd.dismiss();
                try {
                    Log.d("====> Error Image", error.getMessage());
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Network Problem.....");
                    builder.setMessage(error.getMessage());
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);*/
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }
    private MultipartTypedOutput attechCoverImg1() {
        MultipartTypedOutput multipartTypedOutput=new MultipartTypedOutput();
        try {
            multipartTypedOutput.addPart("userId",new TypedString(userID));
            multipartTypedOutput.addPart("image",makeFile(realUri.toString()));
        }
        catch (Exception e){e.printStackTrace();}
        return multipartTypedOutput;
    }

    public void updateProfile() {
        RestAdapter adapter=new RestAdapter.Builder().setEndpoint(Constant.ROOT_URL).build();
        final CrudHandler.ProfileView profileDetails=adapter.create(CrudHandler.ProfileView.class);
        profileDetails.createViewProfile(attech(), new Callback<ProfileDetails>() {
            @Override
            public void success(ProfileDetails profileDetailses, Response response) {
                if (profileDetailses.getStatus() == 1) {
                    String coverImg = profileDetailses.getData().getCover_image();
                    String proImg = profileDetailses.getData().getImage();
                    String followers = profileDetailses.getData().getFollowers();
                    String following = profileDetailses.getData().getFollowing();
                    String name = profileDetailses.getData().getFname();
                    String edu = profileDetailses.getData().getEducation();
                    String gender = profileDetailses.getData().getGender();
                    String dob = profileDetailses.getData().getDob();
                    String loc = profileDetailses.getData().getLocation();
                    String about = profileDetailses.getData().getAbout_me();
                    //data save share prefrences..........
                    session.createProfile(followers, following);
                    session.updateSession(proImg, name, edu, gender, dob, loc, about);
                    session.createCoverImage(coverImg);
                    HashMap<String, String> userPro= session.getUserProfile();
                    String ShareCoverImg=userPro.get(SessionManager.COVER_PROFILE);
                    String shareProfile = userPro.get(SessionManager.USER_PROFILE);
                    String  shareName = userPro.get(SessionManager.USER_NAME);
                    String shareDOB = userPro.get(SessionManager.DOB);
                    String shareAbout = userPro.get(SessionManager.ABOUT);
                    String shareLocation=userPro.get(SessionManager.LOCATION);
                    String shareFollowers = userPro.get(SessionManager.FOLLOWER);
                    String shareFollowing = userPro.get(SessionManager.FOLLOWING);
                    String shareCoverImg=userPro.get(SessionManager.COVER_PROFILE);
                    try{
                        Picasso.with(getActivity()).load(ShareCoverImg).
                                placeholder(R.drawable.bg_profile).error(R.drawable.bg_profile).into(ivBlurProfile);

                    }
                    catch (Exception e)
                    {e.printStackTrace();}
                    Picasso.with(getActivity()).load(shareProfile).placeholder(R.drawable.bg_profile)
                            .error(R.drawable.bg_profile).into(profile_imagee);
                   /* tvFollowers.setText(shareFollowers);
                    tvFollowing.setText(shareFollowing);
                    tvNameProfile.setText(shareName);
                    tvBrithdayProfile.setText(shareDOB);
                    tvLocationProfile.setText(shareLocation);
                    tvAboutProfile.setText(shareAbout);*/

                    Log.d("====>ImageUrl", "" + shareProfile);
                    Log.d("====>Name", "" + shareName);
                    Log.d("====>Follower", "" + shareFollowers);
                    Log.d("====>Following", "" + shareFollowing);
                    Log.d("====>CoverImage", "" + shareCoverImg);

                } else if (profileDetailses.getStatus() == 0) {
                    Log.d("====>No more data", "" + profileDetailses.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.d("===>Error", "" + error);
                    Toast.makeText(getActivity(), "network error!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MultipartTypedOutput attech() {
        MultipartTypedOutput multipartTypedOutput=new MultipartTypedOutput();
        try{
            multipartTypedOutput.addPart("userId",new TypedString(userID));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return multipartTypedOutput;
    }

}
