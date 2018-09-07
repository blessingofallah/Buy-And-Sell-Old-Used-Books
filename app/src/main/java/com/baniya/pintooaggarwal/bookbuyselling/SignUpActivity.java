package com.baniya.pintooaggarwal.bookbuyselling;


        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.ads.AdRequest;
        import com.google.android.gms.ads.AdView;
        import com.google.android.gms.ads.MobileAds;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView loginScreen;
    private FirebaseAuth auth;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_signup);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);


        // Get references
        auth = FirebaseAuth.getInstance();
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginScreen = (TextView) findViewById(R.id.link_login);

        // Set signup button click behavior
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        //   set Already member TextView click behavior
        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity

                finish();
            }
        });
    }

    private void signup() {

        // validate user input first
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false); // disable signup button

        // create progress dialog with default spinner style
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        // Handler used to simulate authentication logic execution time
        // progress dialog will appear for 3 seconds
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {


                            // check network status
                            if(! Utility.isNetworkAvailable(SignUpActivity.this)){

                                Toast.makeText(SignUpActivity.this,
                                        "Please check internet connection", Toast.LENGTH_SHORT).show();



                            };
                            // check if user already registered
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpActivity.this,
                                        "User with this email already exist.", Toast.LENGTH_SHORT).show();}


                            signupButton.setEnabled(true);
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();


                            onSignupSuccess();
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);

    }

    private void onSignupSuccess()
    {
        Toast.makeText(getBaseContext(), "Signup Succeeded", Toast.LENGTH_SHORT).show();
        signupButton.setEnabled(true);
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

        if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("Password too short, enter minimum 6 characters!");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


}

