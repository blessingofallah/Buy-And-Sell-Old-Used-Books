package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Utils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

  //  private ImageView profile;
    private EditText name;
    private Toolbar toolbar;
    //private TextView toolbar_title;
    private EditText address;
    private EditText phoneNo;
    private EditText location;
    private AppCompatButton submit;
    private AppCompatButton cancel;
    private AppCompatButton payment;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
   // FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences mSettings;
    String uid;

    AdView adView;
   // private ImageButton profileImage;
  //  private static final int Gallery_Request = 234;
   // private Uri imageuri = null;
    //FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_profile);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        phoneNo=(EditText)findViewById(R.id.phone);
        location=(EditText)findViewById(R.id.location);
        submit = (AppCompatButton) findViewById(R.id.submit);
        cancel = (AppCompatButton) findViewById(R.id.cancel);
        //payment = (AppCompatButton)findViewById(R.id.pay);
        //profileImage =(ImageButton) findViewById(R.id.profileImage);
       // toolbar = (Toolbar) findViewById(R.id.toolbar3);
       // setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent,Gallery_Request);
//
//            }
//        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        //storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToFirebase();
            }
        });
//        payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateCheckSum();
//            }
//        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        mSettings= this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        String personname = mSettings.getString("name","");
        String personAddress = mSettings.getString("address","");
        String personMobile = mSettings.getString("mobile","");
        String personLocation=mSettings.getString("location","");
        name.setText(personname);
        address.setText(personAddress);
        phoneNo.setText(personMobile);
        location.setText(personLocation);
        if (user != null) {

            uid = user.getUid();

        }

        if(! Utility.isNetworkAvailable(this)){

            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validate()
    {
        boolean valid = true;

        String personname = name.getText().toString();
        String mobile= phoneNo.getText().toString();
        String personAddress = address.getText().toString();
        String personLocation = location.getText().toString();

        if (mobile.isEmpty() || mobile.length() < 6) {
            phoneNo.setError("Please enter valid number");
            valid = false;
        } else {
            phoneNo.setError(null);
        }


        if(personAddress.isEmpty()||personAddress.length()<10) {

            address.setError("Please enter description not less than 10 letters");
            valid = false;
        } else {
            address.setError(null);

        }
        if(personname.isEmpty()) {

            name.setError("Please enter your name");
            valid = false;
        } else {
            name.setError(null);

        }
        if(personLocation.isEmpty()) {

            location.setError("Please enter your name");
            valid = false;
        } else {

            location.setError(null);

        }

        return valid ;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
//        if(requestCode==Gallery_Request && resultCode==RESULT_OK && data.getData()!=null)
//        {
//            imageuri = data.getData();
//            profileImage.setImageURI(imageuri);
//        }

    }

    private void submitToFirebase() {

//        if(imageuri!=null)
//        {
//            StorageReference filepath = storageReference.child("Profile Images").child(imageuri.getLastPathSegment());
//            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
////                    Uri downloaduri=taskSnapshot.getDownloadUrl();
////                    DatabaseReference profiledata = databaseReference.push();
////                    profiledata.child("Profile Data").setValue(downloaduri.toString());
//                  //  String downloadUrl = taskSnapshot.getDownloadUrl().toString();
//
//                //    databaseReference.child(uid).child(String.valueOf(profileid)).child("images").push().setValue(downloadUrl);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(ProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }


        if(!validate()){

            Toast.makeText(getApplicationContext(),"Please check the inputs again",Toast.LENGTH_SHORT).show();
            return;
        }else if(! Utility.isNetworkAvailable(this)){

            Toast.makeText(this,
                    "Please check internet connection and try again", Toast.LENGTH_SHORT).show();
            return;
        }

        mSettings.edit().putString("name",name.getText().toString()).apply();
        mSettings.edit().putString("address",address.getText().toString()).apply();
        mSettings.edit().putString("mobile",phoneNo.getText().toString()).apply();
        mSettings.edit().putString("location",location.getText().toString()).apply();
        // Generate a reference to a new location and add some data using push()
        DatabaseReference pushedPostRef = databaseReference.push();
        // Get the unique ID generated by a push()
        final String profileid = pushedPostRef.getKey();
//        if(imageuri!=null)
//        {
//            StorageReference filepath = storageReference.child("Profile Images").child(imageuri.getLastPathSegment());
//            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
////                    Uri downloaduri=taskSnapshot.getDownloadUrl();
////                    DatabaseReference profiledata = databaseReference.push();
////                    profiledata.child("Profile Data").setValue(downloaduri.toString());
//                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
//
//                    databaseReference.child(uid).child(String.valueOf(profileid)).child("images").push().setValue(downloadUrl);
//                    Toast.makeText(ProfileActivity.this, "Done.", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                    Toast.makeText(ProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }

        ProfileInfo profileInfo = new ProfileInfo(
                name.getText().toString()
                ,address.getText().toString()
                ,phoneNo.getText().toString()
                ,location.getText().toString()
        );
        databaseReference.child(uid).child(String.valueOf(profileid)).setValue(profileInfo);
       // Toast.makeText(HomeActivity, "done", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
//    private void generateCheckSum()
//    {
//        //getting the tax amount first.
//        String txnAmount ="250";
//
//        //creating a retrofit object.
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        //creating the retrofit api service
//        Api apiService = retrofit.create(Api.class);
//
//        //creating paytm object
//        //containing all the values required
//        final Paytm paytm = new Paytm(
//                Constants.M_ID,
//                Constants.CHANNEL_ID,
//                txnAmount,
//                Constants.WEBSITE,
//                Constants.CALLBACK_URL,
//                Constants.INDUSTRY_TYPE_ID
//        );
//
//        //creating a call object from the apiService
//        Call<Checksum> call = apiService.getChecksum(
//                paytm.getmId(),
//                paytm.getOrderId(),
//                paytm.getCustId(),
//                paytm.getChannelId(),
//                paytm.getTxnAmount(),
//                paytm.getWebsite(),
//                paytm.getCallBackUrl(),
//                paytm.getIndustryTypeId()
//        );
//
//        //making the call to generate checksum
//        call.enqueue(new Callback<Checksum>() {
//            @Override
//            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
//
//                //once we get the checksum we will initiailize the payment.
//                //the method is taking the checksum we got and the paytm object as the parameter
//                initializePaytmPayment(response.body().getChecksumHash(), paytm);
//            }
//
//            @Override
//            public void onFailure(Call<Checksum> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void initializePaytmPayment(String checksumHash, Paytm paytm) {
//
//        //getting paytm service
//        PaytmPGService Service = PaytmPGService.getStagingService();
//
//        //use this when using for production
//        //PaytmPGService Service = PaytmPGService.getProductionService();
//
//        //creating a hashmap and adding all the values required
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("MID", Constants.M_ID);
//        paramMap.put("ORDER_ID", paytm.getOrderId());
//        paramMap.put("CUST_ID", paytm.getCustId());
//        paramMap.put("CHANNEL_ID", paytm.getChannelId());
//        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
//        paramMap.put("WEBSITE", paytm.getWebsite());
//        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
//        paramMap.put("CHECKSUMHASH", checksumHash);
//        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
//
//
//        //creating a paytm order object using the hashmap
//        PaytmOrder order = new PaytmOrder(paramMap);
//
//        //intializing the paytm service
//        Service.initialize(order, null);
//
//        //finally starting the payment transaction
//        Service.startPaymentTransaction(this, true, true, this);
//
//    }
//
//    //all these overriden method is to detect the payment result accordingly
//    @Override
//    public void onTransactionResponse(Bundle bundle) {
//
//        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void networkNotAvailable() {
//        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void clientAuthenticationFailed(String s) {
//        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void someUIErrorOccurred(String s) {
//        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onErrorLoadingWebPage(int i, String s, String s1) {
//        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onBackPressedCancelTransaction() {
//        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onTransactionCancel(String s, Bundle bundle) {
//        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
//    }

}
