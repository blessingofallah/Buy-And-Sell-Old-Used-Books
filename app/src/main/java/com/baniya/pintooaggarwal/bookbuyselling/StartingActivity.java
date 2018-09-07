package com.baniya.pintooaggarwal.bookbuyselling;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;
        import com.google.android.gms.ads.AdRequest;
        import com.google.android.gms.ads.AdView;
        import com.google.android.gms.ads.MobileAds;
        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthCredential;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FacebookAuthProvider;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.GoogleAuthProvider;

public class StartingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private EditText emailText;
    private EditText passwordText;
    private TextView signup;
    private TextView resetText;
    private Button loginButton;
    private SignInButton mGoogleBtn;
    private ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 9051;
    // private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "Starting_Activity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    // facebook signin
    private CallbackManager mcallbackManager;
    private LoginButton mloginButton;

    AdView adView;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_login);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences getprefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart =  getprefs.getBoolean("firstStart",true);
                if(isFirstStart)
                {
                    startActivity(new Intent(StartingActivity.this , IntroOfApp.class));
                    SharedPreferences.Editor e = getprefs.edit();
                    e.putBoolean("firstStart",false);
                    e.apply();
                }
//                try {
//                    sleep(2000);
//                    Intent intent = new Intent(getApplicationContext(), StartingActivity.class);
//                    startActivity(intent);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        });
        thread.start();


        //facebook signin
//        if(getIntent().hasExtra("logout"))
//        {
//            LoginManager.getInstance().logOut();
//        }

        FacebookSdk.sdkInitialize(getApplicationContext());
       mcallbackManager = CallbackManager.Factory.create();
        mloginButton = (LoginButton) findViewById(R.id.facebooklogin);
        mloginButton.setReadPermissions("email","public_profile");
        mloginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                //goHomeActivity();
            }

            @Override
            public void onCancel() {

                Log.d("TAG" ,"facebook:cancel");

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
//        mloginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StartingActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });






        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser =mAuth.getCurrentUser();
                if(currentUser!=null)
                {
                    Intent intent = new Intent(StartingActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



        progressDialog = new ProgressDialog(this);
        emailText = (EditText)findViewById(R.id.input_email);
        passwordText =(EditText)findViewById(R.id.input_password);
        loginButton = (Button)findViewById(R.id.btn_login);
        signup = (TextView)findViewById(R.id.link_signup);
        resetText = (TextView)findViewById(R.id.reset_password);
        //google button
        mGoogleBtn = (SignInButton)findViewById(R.id.GoogleButton);
        mGoogleBtn.setSize(SignInButton.SIZE_WIDE);
        mGoogleBtn.setColorScheme(SignInButton.COLOR_DARK);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final GoogleApiClient mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("processing...");
                progressDialog.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , SignUpActivity.class);
                startActivity(intent);
            }
        });
        resetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartingActivity.this, ResetPasswordActivity.class));
            }
        });

    }

//    private void goHomeActivity() {
//        Intent intent = new Intent(StartingActivity.this,HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
       // Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                           // progressDialog.dismiss();
                            //Intent intent = new Intent(StartingActivity.this , HomeActivity.class);
                            //startActivity(intent);
                          //  startActivity(HomeActivity.getIntent(StartingActivity.this));
                           // FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(StartingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);

                        }
                        // ...
                        }
                });

    }

    public void login()
    {
        // validate user input first
        if(!validate())
        {
            onLoginFailed();
            return ;
        }

        loginButton.setEnabled(false);// disable Login button
        // create progress dialog with default spinner style
        final ProgressDialog progressDialog = new ProgressDialog(StartingActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        //authenticate user
        mAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(StartingActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(!task.isSuccessful())
                        {
                            // there was an error

                            // check network status
                            if(! Utility.isNetworkAvailable(StartingActivity.this))
                            {
                                Toast.makeText(StartingActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(StartingActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            loginButton.setEnabled(true);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            startActivity(new Intent(StartingActivity.this,HomeActivity.class));
                            finish();
                        }

                    }
                });
    }
    public void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }
    public boolean validate()
    {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 7 || password.length() > 10) {
            passwordText.setError("Password should be atleast 8 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode ,resultCode ,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
        else
        {
            progressDialog.dismiss();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            progressDialog.dismiss();
            startActivity(new Intent(StartingActivity.this,HomeActivity.class));

            finish();
        }

        else if(! Utility.isNetworkAvailable(StartingActivity.this))
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.dismiss();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            // FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(StartingActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }
}
