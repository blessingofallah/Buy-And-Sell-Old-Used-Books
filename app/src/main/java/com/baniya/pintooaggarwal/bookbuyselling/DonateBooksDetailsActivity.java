package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DonateBooksDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText mbookdetails;
    EditText mbookQuantities;
    EditText mname;
    EditText maddress;
    EditText memail;
    EditText mphone;
    private Spinner mspinner;
    private ArrayAdapter<CharSequence> adapter;

    Button msendbutton;
    String name=null;
    //Context context ;
    Toolbar toolbar;
   // Bundle bundle;
  //  String toolBar_title;

    //String mail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_books_details);

        toolbar = (Toolbar) findViewById(R.id.toolbarfordonation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mspinner=(Spinner)findViewById(R.id.ngo_category);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.ngonames, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mspinner.setAdapter(adapter);
        mspinner.setOnItemSelectedListener(this);


        mbookdetails = (EditText)findViewById(R.id.booksdetails);
        mbookQuantities = (EditText)findViewById(R.id.quantity);
        mname = (EditText)findViewById(R.id.Dname);
        maddress = (EditText)findViewById(R.id.Daddress);
        memail = (EditText)findViewById(R.id.Dinput_email);
        mphone = (EditText)findViewById(R.id.Dmobile_number);

        msendbutton = (Button) findViewById(R.id.Ddone);
        msendbutton.setOnClickListener(this);

//        Bundle bundle = getIntent().getExtras();
//        if(bundle != null) {
//            toolbar.setTitle(bundle.getString("ngonames"));
////            toolBar_title = (String) toolbar.getTitle();
//        }
    }

//    public DonateBooksDetailsActivity()
//    {
//
//    }
//
//    public DonateBooksDetailsActivity(String mail_id) {
//        this.mail_id = mail_id;
//    }

    public void sendemail()
    {
        String  emailaddress[] =new String[]{memail.getText().toString(),"mepintu.sing@gmail.com" ,"bookeistan@gmail.com", "pintoo40114902015@msi-ggsip.org"};
//        NGODetailsActivity ngoDetailsActivity = new NGODetailsActivity("hiii");
//        String name = ngoDetailsActivity.getMailid();
//            NGOListActivity ngoListActivity = new NGOListActivity();
//            String name = (String) ngoListActivity.getName();// getIntent().getExtras("ngonames");
            switch (name) {
                case "Sarvahitey":
                    emailaddress[3] = "sarvahitey@gmail.com";
                    break;
                case "YouthClub Foundation":
                    emailaddress[3] = "youthclubeastdelhi@gmail.com";

                    break;

                case "Edubright":
                    emailaddress[3] = "Dipanshuarora08@gmail.com";
                    break;
                case "Prastaar":

                    emailaddress[3] = "prastaar@gmail.com";
                    break;
                default:
                    break;

        }
      // String  emailaddress[]  ={memail.getText().toString(),"mepintu.sing@gmail.com","swabhig@gmail.com" ,"bookeistan@gmail.com"};
       // String emailaddress = "mepintu.sing@gmail.com";
        String subject =  " Donation of Books....";
        String message = " My Name is \n " + mname.getText().toString() + "My Address is \n " + maddress.getText().toString() +
                      " Phone no is \n " +mphone.getText().toString()+ " I want to donate my some books \n " + mbookdetails.getText().toString() + " , Quantities are "
                        + mbookQuantities.getText().toString() ;

        SendMail sm = new SendMail(this , emailaddress, subject , message);

        sm.execute();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View view) {
        sendemail();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        name = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}