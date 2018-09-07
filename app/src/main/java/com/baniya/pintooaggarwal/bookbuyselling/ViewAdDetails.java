package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.DialogInterface;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.AppCompatButton;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import retrofit2.Callback;
        import retrofit2.Response;

import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewAdDetails extends AppCompatActivity implements PaytmPaymentTransactionCallback {


    private TextView title;
    private TextView price;
    private TextView description;
    private TextView name;
    private TextView location;
    private AppCompatButton delete;
   // private AppCompatButton fabmessage;
    private LinearLayout layout;
    private Toolbar toolbar;
    FloatingActionButton fab1,message;



    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad_details);
        if(! Utility.isNetworkAvailable(this)){


            Toast.makeText(this,
                    "Please check internet connection", Toast.LENGTH_SHORT).show();

        }
        Intent intent = getIntent();

        final AdUploadInfo adUploadInfo = (AdUploadInfo) intent.getSerializableExtra("AdDetails");
      //  final ProfileInfo profileInfo = (ProfileInfo) intent.getSerializableExtra("ProfileDetails");

        user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference(adUploadInfo.getUserId()).child(adUploadInfo.getAdId());
       // databaseReference2 = FirebaseDatabase.getInstance().getReference(profileInfo.getUserId()).child(profileInfo.getAdId());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        name = (TextView) findViewById(R.id.name);
        layout = (LinearLayout)findViewById(R.id.layout);
        location = (TextView)findViewById(R.id.location);

        delete = (AppCompatButton) findViewById(R.id.delete);
      //  fabmessage = (AppCompatButton)findViewById(R.id.fabmessage);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        message = (FloatingActionButton)findViewById(R.id.fabmessage);
        title.setText(adUploadInfo.adTitle);
        price.setText(adUploadInfo.adPrice);
        description.setText(adUploadInfo.adDescription);
        location.setText(adUploadInfo.adlocation);
        name.setText(adUploadInfo.adName);
       // location.setText(profileInfo.ProfileLocation);

//        if(user.getUid().equals(profileInfo.getUserId())){
//            delete.setVisibility(View.VISIBLE);
//        }
//        else{
//
//            fab.setVisibility(View.VISIBLE);
//        }

        if(user.getUid().equals(adUploadInfo.getUserId())){
            delete.setVisibility(View.VISIBLE);
        }
        else{
            message.setVisibility(View.VISIBLE);
            fab1.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dial = new Intent();
                dial.setAction("android.intent.action.DIAL");
                dial.setData(Uri.parse("tel:"+adUploadInfo.adPhone));
                startActivity(dial);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailid = new Intent(ViewAdDetails.this, ChatActivity.class);
                emailid.setData(Uri.parse("email:"+adUploadInfo.adEmail));
                startActivity(emailid);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 generateCheckSum();


            }
        });
        databaseReference.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            int i =0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    View v = layout.getChildAt(i);
                    i=i+1;
                    if (v instanceof ImageView) {
                        Picasso.with(getApplicationContext())
                                .load(postSnapshot.getValue().toString())
                                .placeholder(R.drawable.image_processing)
                                .error(R.drawable.no_image)
                                .fit()
                                .centerInside()
                                .into((ImageView) v);
                        v.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! Utility.isNetworkAvailable(ViewAdDetails.this)){


                    Toast.makeText(ViewAdDetails.this,
                            "Please check internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(ViewAdDetails.this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.delete_confirmation))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseReference.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                            StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(postSnapshot.getValue().toString());
                                            photoRef.delete();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference.removeValue();
                                Toast.makeText(getApplicationContext(), "Ad may be take while until being deleted", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })      .setNegativeButton(android.R.string.no, null).show();
            }});

    }
    public void imageClick(View v){

        String IdAsString = v.getResources().getResourceName(v.getId());
        final String idNumber=IdAsString.substring(IdAsString.length()-1);

        databaseReference.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
            int i =0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    i=i+1;
                    if (i== Integer.valueOf(idNumber)){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(postSnapshot.getValue().toString()), "image/*");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void generateCheckSum()
    {
        //getting the tax amount first.
        String txnAmount = price.getText().toString().trim();

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

}

