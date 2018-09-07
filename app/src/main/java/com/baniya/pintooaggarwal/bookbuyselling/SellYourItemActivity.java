package com.baniya.pintooaggarwal.bookbuyselling;


        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.TypedArray;
        import android.net.Uri;
        import android.provider.SyncStateContract;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.AppCompatButton;
        import android.support.v7.widget.Toolbar;
        import android.util.TypedValue;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;


        import java.util.ArrayList;

        import java.util.UUID;

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
        import com.squareup.picasso.Picasso;

        import  com.baniya.pintooaggarwal.bookbuyselling.imagelib.activities.GalleryActivity;
        import  com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants;
        import  com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Image;
        import  com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Params;

public class SellYourItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView addPhoto;
    private  int selectedColor;
    private ImageView imageView;
    private LinearLayout layout;
    private ArrayList<Image> imagesList;
    private ArrayList<Long> selectedIdsBackup;
    private TextView photoText;

    private EditText title;
    private EditText price;
    private EditText description;
    private EditText name;
    private EditText input_email;
    private EditText mobile_number;
    private EditText location;
    private AppCompatButton submit;
    private Spinner mspinner;
    private ArrayAdapter<CharSequence> adapter;
    private String uid;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    SharedPreferences mSettings;
    private Toolbar toolbar;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_your_item);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_sell);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mspinner=(Spinner)findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mspinner.setAdapter(adapter);
        mspinner.setOnItemSelectedListener(this);


        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);
        name = (EditText) findViewById(R.id.name);
        input_email = (EditText) findViewById(R.id.input_email);
        mobile_number = (EditText) findViewById(R.id.mobile_number);
        location = (EditText)findViewById(R.id.location);
        submit = (AppCompatButton) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToFirebase();
            }
        });
        layout = (LinearLayout)findViewById(R.id.layout);
        imageView = (ImageView) findViewById(R.id.image1);
        selectedColor = fetchAccentColor();
        addPhoto = (ImageView) findViewById(R.id.add_photo);
        photoText = (TextView) findViewById(R.id.photo_text);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initiateMultiPicker();
            }
        });

        mSettings = this.getSharedPreferences(user.getUid(), Context.MODE_PRIVATE);
        String username = mSettings.getString("name","");
        String email = mSettings.getString("email","");
        String mobileNumber = mSettings.getString("mobile","");
        name.setText(username);
        input_email.setText(email);
        mobile_number.setText(mobileNumber);
        if (user != null) {
            String userEmail = user.getEmail();

            uid = user.getUid();
            if (input_email.getText().toString().isEmpty()) {
                input_email.setText(userEmail);
            }

        }
        if(! Utility.isNetworkAvailable(this)){
            Toast.makeText(this,
                    "Please check internet connection", Toast.LENGTH_SHORT).show();}

    }
    private void initiateMultiPicker() {
        Intent intent = new Intent(this, GalleryActivity.class);
        Params params = new Params();

        params.setPickerLimit(8);

        params.setToolbarColor(selectedColor);
        params.setActionButtonColor(selectedColor);
        params.setButtonTextColor(selectedColor);
        intent.putExtra(Constants.KEY_PARAMS, params);

        if ((selectedIdsBackup!=null)&&!selectedIdsBackup.isEmpty()) {

            intent.putExtra("selectedIdBack", selectedIdsBackup);
        }

        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
    }
    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }


        imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
        selectedIdsBackup = (ArrayList<Long>) intent.getSerializableExtra("selectedId");




        emptyViews();


        if (!imagesList.isEmpty()){
            for (int i =0 ;i<imagesList.size();i++){
                View v = layout.getChildAt(i+1);
                if (v instanceof ImageView){
                    Picasso.with(getApplicationContext())
                            .load(imagesList.get(i).uri)
                            .placeholder(R.drawable.image_processing)
                            .error(R.drawable.no_image).fit()
                            .centerInside()
                            .into((ImageView) v);

                    v.setVisibility(View.VISIBLE);
                }
            }

            String text= getString(R.string.photo_added,imagesList.size());
            photoText.setText(text);

        }else{
            photoText.setText(R.string.photo);

        }






    }

    public void emptyViews(){

        for (int i= 0;i<8;i++){
            View v = layout.getChildAt(i+1);
            if (v instanceof ImageView){

                imageView.setImageDrawable(null);
                v.setVisibility(View.GONE);

            }
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = input_email.getText().toString();
        String mobileN= mobile_number.getText().toString();
        String titleS = title.getText().toString();
        String descriptionS = description.getText().toString();
        String nameS = name.getText().toString();
        String priceS = price.getText().toString();
        String locationS = location.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (mobileN.isEmpty() || mobileN.length() <10) {
            mobile_number.setError("Please enter valid number");
            valid = false;
        } else {
            mobile_number.setError(null);
        }


        if(titleS.isEmpty()||titleS.length()<5) {

            title.setError("Please enter title not less than 5 letters");
            valid = false;
        } else {
            title.setError(null);

        }

        if(descriptionS.isEmpty()||descriptionS.length()<10) {

            description.setError("Please enter description not less than 10 letters");
            valid = false;
        } else {
            description.setError(null);

        }

        if(nameS.isEmpty()) {

            name.setError("Please enter your name");
            valid = false;
        } else {
            name.setError(null);

        }

        if(priceS.isEmpty()) {

            price.setError("Please enter price");
            valid = false;
        } else {
            price.setError(null);

        }

        if(locationS.isEmpty()) {

            location.setError(null);
            valid = false;
        } else {
            location.setError(null);

        }


        return valid;
    }

    public void submitToFirebase(){
        if(!validate()){

            Toast.makeText(getApplicationContext(),"Please check the inputs again",Toast.LENGTH_SHORT).show();
            return;
        }else if(! Utility.isNetworkAvailable(this)){

            Toast.makeText(this,
                    "Please check internet connection and try again", Toast.LENGTH_SHORT).show();
            return;
        }


        mSettings.edit().putString("name",name.getText().toString()).apply();
        mSettings.edit().putString("email",input_email.getText().toString()).apply();
        mSettings.edit().putString("mobile",mobile_number.getText().toString()).apply();
        // Generate a reference to a new location and add some data using push()
        DatabaseReference pushedPostRef = databaseReference.push();

// Get the unique ID generated by a push()
        final String adId = pushedPostRef.getKey();

        if (imagesList==null || imagesList.isEmpty()) {
            imagesList = new ArrayList<>();
            Uri uri = Uri.parse("android.resource://" + "com.baniya.pintooaggarwal.olxclone"
                    + "/" + "drawable/no_image");
            imagesList.add(new Image(0,uri,uri.toString(),false));
        }










        for (int i = 0; i < imagesList.size(); i++) {




            UUID imageId = UUID.randomUUID();
//             String imageId= pushedPostRef.getKey();
            final StorageReference storageReference2nd = storageReference.child("image" + imageId);

            storageReference2nd.putFile(imagesList.get(i).uri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                             @Override
                                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                 @SuppressWarnings("VisibleForTests")
                                                 String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                                                 databaseReference.child(uid).child(String.valueOf(adId)).child("images").push().setValue(downloadUrl);
                                                 Toast.makeText(SellYourItemActivity.this, "Done.", Toast.LENGTH_SHORT).show();




                                             }
                                         }
                    ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    //and displaying error message
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }








        AdUploadInfo adUploadInfo = new AdUploadInfo
                (title.getText().toString()
                ,price.getText().toString()
                ,description.getText().toString()
                ,name.getText().toString()
                ,input_email.getText().toString()
                ,mobile_number.getText().toString()
                        ,location.getText().toString()
                ,mspinner.getItemAtPosition(0).toString()
        );
        databaseReference.child(uid).child(String.valueOf(adId)).setValue(adUploadInfo);

        setResult(RESULT_OK);
        finish();

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}



