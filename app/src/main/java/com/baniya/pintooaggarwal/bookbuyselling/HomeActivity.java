package com.baniya.pintooaggarwal.bookbuyselling;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
//import android.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.design.widget.NavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.AppCompatButton;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Image;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.baniya.pintooaggarwal.bookbuyselling.MyAdsFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // TODO connect the app to firebase
    private GoogleApiClient mGoogleApiClient;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle toggle;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private boolean mIsResumed = false;
    private int selectedId= 0;
    public static final int REQUEST_CODE_AD = 0;
    private ProgressBar mProgressBar;
    ValueEventListener valueEventListener;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView welcome;
    LinearLayout internetCheckLayout;
    private AppCompatButton reload;
    private ImageView profileimage;

    AdView adView;
   // private TextView toolbar_title;

  //  private LoginButton mloginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_Home);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);



       // toolbar_title = (TextView)findViewById(R.id.toolbar_title);
      //  toolbar_title.setText("Profile");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        welcome = (TextView) nvDrawer.getHeaderView(0).findViewById(R.id.welcome);
        internetCheckLayout = (LinearLayout) findViewById(R.id.internet_check);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        reload = (AppCompatButton) findViewById(R.id.reload);
        profileimage = (ImageView) nvDrawer.getHeaderView(0).findViewById(R.id.profileView);
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               user = firebaseAuth.getCurrentUser();
                if (user != null) {
                  Glide.with(getApplicationContext())
                            .load(user.getPhotoUrl())
                            .into(profileimage);
                   // profileimage.setImageURI(user.getPhotoUrl());
                    welcome.setText("Welcome-" + " " + user.getEmail());

                    if (!Utility.isNetworkAvailable(HomeActivity.this)) {

                        mProgressBar.setVisibility(View.INVISIBLE);
                        internetCheckLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(HomeActivity.this,
                                "Please check internet connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = new DisplayImagesFragment();

                    if (mIsResumed) {
                        fm.beginTransaction()
                                .replace(R.id.flContent, fragment)
                                .commit();

                    }
                }
//                else
//                {
//                    Intent intent = new Intent(HomeActivity.this,StartingActivity.class);
//                   // intent.putExtra("logout",true);
//                    startActivity(intent);
//                    finish();
//                }
            }

        };
        firebaseAuth.addAuthStateListener(mAuthListener);
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        FirebaseDatabase.getInstance().getReference(Constants.USER_KEY).child(user.getEmail().replace(".", ","))
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        if (dataSnapshot.getValue() != null) {
//                                                            ProfileInfo users = dataSnapshot.getValue(ProfileInfo.class);
//                                                            Glide.with(HomeActivity.this)
//                                                                    .load(users.getImageUrl())
//                                                                    .into(profileimage);
//
//                                                        }
//                                                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
               // });
        // connect to google api
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mGoogleApiClient.connect();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItem menuIem = nvDrawer.getMenu().getItem(selectedId);
                selectDrawerItem(menuIem);
            }
        });
        //setup drawer view
        setupDrawerContent(nvDrawer);
        toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

//    private void profileMethod() {
//
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,0);
//
//    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuIem)
    {
        switch(menuIem.getItemId())
        {
            case R.id.nav_home_fragment:
                selectedId=0;
                toolbar.setTitle("Home");
                if (getSupportFragmentManager().findFragmentById(R.id.flContent)!=null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.flContent)).commit();
                }
                if(! Utility.isNetworkAvailable(this)){

                    mProgressBar.setVisibility(View.INVISIBLE);
                    internetCheckLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this,
                            "Please check internet connection", Toast.LENGTH_SHORT).show();
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                internetCheckLayout.setVisibility(View.INVISIBLE);

                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = new DisplayImagesFragment();




                FragmentTransaction f1=fm.beginTransaction()
                        .replace(R.id.flContent, fragment);
                if (selectedId==0) {

                    f1.commit();
                }
                break;
            case R.id.nav_my_ads_fragment:
                selectedId=1;
                toolbar.setTitle("My ADs");
                if (getSupportFragmentManager().findFragmentById(R.id.flContent)!=null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.flContent)).commit();
                }
                if(! Utility.isNetworkAvailable(this)) {

                    mProgressBar.setVisibility(View.INVISIBLE);
                    internetCheckLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this,
                            "Please check internet connection", Toast.LENGTH_SHORT).show();
                    break;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                internetCheckLayout.setVisibility(View.INVISIBLE);

                if (user!=null) {
                    valueEventListener=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if ((dataSnapshot.hasChild(user.getUid())) && (mIsResumed)) {
                                Fragment fragment2 = new MyAdsFragment();

                                FragmentManager fm2 = getSupportFragmentManager();




                                FragmentTransaction f3=fm2.beginTransaction()
                                        .replace(R.id.flContent, fragment2);
                                if (selectedId==1){
                                    f3.commit();
                                }


                            } else if (mIsResumed) {
                                mProgressBar.setVisibility(View.GONE);
                                Fragment fragment3 = new NoAdFragment();

                                FragmentManager fm3 = getSupportFragmentManager();

                                fm3.beginTransaction()
                                        .replace(R.id.flContent, fragment3)
                                        .commit();
                            }


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    databaseReference.addValueEventListener(valueEventListener);

                }

                break;

            case R.id.donate_fragment :

                startActivity(new Intent(HomeActivity.this, NGOListActivity.class));
                break;

            case R.id.profile_fragment:

                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;

            case R.id.chat_fragment:

                startActivity(new Intent(HomeActivity.this, UserListingActivity.class));
                break;
            case R.id.signout:

                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(HomeActivity.this,StartingActivity.class));
                finish();




                break;
            default:
        }
        // Highlight the selected item has been done by NavigationView
        menuIem.setChecked(true);
        // Set action bar title
        setTitle(menuIem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
        }else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mIsResumed = true;
        if(! Utility.isNetworkAvailable(this))
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
//            MenuItem menuIem = nvDrawer.getMenu().getItem(selectedposition);
//            selectDrawerItem(menuIem);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mIsResumed = false;
        if (valueEventListener!=null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            MenuItem menuIem = nvDrawer.getMenu().getItem(selectedId);
//            selectDrawerItem(menuIem);
            menuIem.setChecked(true);
            setTitle(menuIem.getTitle());
            return;
        }

        MenuItem menuIem = nvDrawer.getMenu().getItem(0);
        selectDrawerItem(menuIem);
        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
    }

    public void ImageButton(View view)
    {
        Intent i = new Intent(new Intent(HomeActivity.this ,SellYourItemActivity.class));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //  getMenuInflater().inflate(R.menu.menu_item,menu);
       //   MenuItem menuItem = menu.findItem(R.id.action_search);
//          SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//          searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

//        MyAdsFragment myAdsFragment = new MyAdsFragment();
//        newText = newText.toLowerCase();
//        ArrayList<AdUploadInfo> newList = new ArrayList<>();
//        for(AdUploadInfo adUploadInfo : myAdsFragment.list)
//        {
//            String name = adUploadInfo.getAdTitle().toLowerCase();
//            if(name.contains(newText))
//            {
//                newList.add(adUploadInfo);
//            }
//        }
//       // RecyclerViewAdapter myadapter = new RecyclerViewAdapter();
//        myAdsFragment.adapter.setFilter(newList);
        return true;

    }
}