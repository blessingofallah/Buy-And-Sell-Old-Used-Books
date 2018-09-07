package com.baniya.pintooaggarwal.bookbuyselling;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdView;

public class NGODetailsActivity extends AppCompatActivity {

//    EditText mbookdetails;
//    EditText mbookQuantities;
//    EditText mname;
//    EditText maddress;
//    EditText memail;
//    EditText mphone;
//
//    Button msendbutton;
     Toolbar toolbar;
//     String toolBar_title;
     private TextView ngo_details;
     AdView adView;

    // String mailid;

//    public String getMailid() {
//        return mailid;
//    }
//
//    public void setMailid(String mailid) {
//        this.mailid = mailid;
//    }

//    public String getToolBar_title() {
//        return toolBar_title;
//    }
//
//    public void setToolBar_title(String toolBar_title) {
//        this.toolBar_title = toolBar_title;
//    }
//
    public NGODetailsActivity(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngodetails);


        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_Home);
//        MobileAds.initialize(this , "ca-app-pub-3940256099942544~3347511713");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

         toolbar = (Toolbar) findViewById(R.id.toolbarforlist);
         ngo_details = (TextView)findViewById(R.id.Ngo_details_text);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
          //  ngo_details.setText("hellllllllllloooooo");

         Bundle bundle = getIntent().getExtras();
         if(bundle != null)
         {
             toolbar.setTitle(bundle.getString("ngonames"));
             String toolBar_title = (String) toolbar.getTitle();


             switch (toolBar_title)
             {
                 case "Sarvahitey" :
                     ngo_details.setText("In sarvahitey,\n" +
                             "Through Vidya Vistar help the kids there to have a bigger dream for their future. As we visited the slum, met the kids, met the parents, we discovered an opportunity to serve the not-so-lucky fellows there to remove the disparity between them and our luckier brothers.\n" +
                             "\n" +
                             "Since then, teaching kids residing in the entire slum with much enthusiasm complement us by perseverant attendance and motivated attitude towards learning.\n" +
                             "\n" +
                             "The volunteers motivate the kids and their parents on Saturdays through events, extra-curricular activities, alternative methods of education and oration, while the young adults are given remedial lessons and have doubt clearing classes. On Sundays, however, the kids are taught, in distinct batches, their syllabus and curriculum, in the mornings and Maths, English and Hindi basics in the evening. \n" +
                             "\n" +
                             "The best part of this project is, however, the compliments and praises and blessings that we receive from the parents when we go there. The pats on our backs and the ‘thank you’s make up for the exhaustion of the previous week .\n");

                     break;
                 case "YouthClub Foundation" :
                     ngo_details.setText("Youth club foundation is the organisation of youth.\n" +
                             "We are teaching those students, who are helpless, orphan and needy.\n" +
                             "In these way there are many students got knowledge and new exprience.\n" +
                             "We organised many activities like destrbuting food among poor people festival season, sanitary campangning in our area, delivers books among poor students, organise blood donation camp, and running educatinal academy.\n" +
                             "If you provide us books this is a great help for us.\n" +
                             "We shall be highly obliged, if you do this nobel deed.\n" +
                             "Each one teach one.\n" +
                             "\n" +
                             "Thank you \n" +
                             "Team\n" +
                             "YOUTH CLUB FOUNDATION (R)");

                     break;
                 case "Edubright" :
                     ngo_details.setText("We request you to donate your old books (which is not in your use now) to us as we provide better hands to them rather than using them to make lifafas,or to sell in kabari shops etc .Your small contribution gives sources to get education to the needy onces. \n" +
                             "As we all knows books are our closest friend we can find solutions of our many problems \n" +
                             "Through the medium of books,\n" +
                             "But many poor childrens who can not afford to buy the books,also miss the presence of friend (books) in thier lives. \n" +
                             "If you donate your old books you can also contribute in our economic growth as if more and more childrens get education then they will be became better human resource who can contribute in economic welfare of the country.\n" +
                             "So please its my humble request to all donate your old books which can give better future to a children.\n" +
                             "Thank You\n");
                     break;
                 case "Prastaar" :
                     ngo_details.setText("Prastaar is committed to positively impacting the communities it serves by providing assistance to underprivileged children, children in hospitals, government schools, various other grass-root organizations and charities.\n" +
                             "We are entirely depended the donation of books, games & puzzles, and other material by thousands of our donors.\n" +
                             "You can donate new or old books related to children to us in good condition. You can consider donating following books to us :-\n" +
                             "Children’s Books – Non-Fiction: Preschool; Elementary; Middle School; High School.\n" +
                             "Chidren’s Books- Fiction: Preschool; Elementary; Middle School; High School\n" +
                             "Books- Computer\n" +
                             "Books- Crafts & Hobby\n" +
                             "Books- Family & Relationships\n" +
                             "Children Books- Fiction: Beyond High School\n" +
                             "Books- Health & Fitness\n" +
                             "Books- History & Politics\n" +
                             "Books- Performing Arts\n" +
                             "Books- Pets/Animals\n" +
                             "Books- Reference");
                     break;
                 default: ngo_details.setText("kuch nhi hua bc");
             }

            // DonateBooksDetailsActivity donateBooksDetailsActivity = new DonateBooksDetailsActivity(toolBar_title);
             //donateBooksDetailsActivity.sendemail();
         }






//        mbookdetails = (EditText)findViewById(R.id.booksdetails);
//        mbookQuantities = (EditText)findViewById(R.id.quantity);
//        mname = (EditText)findViewById(R.id.Dname);
//        maddress = (EditText)findViewById(R.id.Daddress);
//        memail = (EditText)findViewById(R.id.Dinput_email);
//        mphone = (EditText)findViewById(R.id.Dmobile_number);
//
//
//        msendbutton = (Button) findViewById(R.id.Ddone);
//        msendbutton.setOnClickListener(this);

//        msendbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String  emailaddress [] ={memail.getText().toString(),"mepintu.sing@gmail.com","swabhig@gmail.com" ,"bookeistan@gmail.com"};
//                String subject =  " Donation of Books....";
//
//                String message = " My Name is " + mname.getText().toString() + " My Address is " + maddress.getText().toString() +
//                        " Phone no is  " +mphone.getText().toString()+ " I want to donate " + mbookdetails.getText().toString() + " .Quantities are "
//                        + mbookQuantities.getText().toString() ;
//
//
//                Intent i=new Intent(Intent.ACTION_SEND);
//                i.putExtra(Intent.EXTRA_EMAIL,emailaddress);
//                i.putExtra(Intent.EXTRA_SUBJECT,subject);
//
//                i.setType("plan/text");
//
//                i.putExtra(Intent.EXTRA_TEXT,message);
//
//                startActivity(i);
//                Toast.makeText(NGODetailsActivity.this,"send Successfuly",Toast.LENGTH_LONG).show();
//            }
//        });




    }
//    private void sendemail()
//    {
//        //String  emailaddress[]  ={memail.getText().toString(),"mepintu.sing@gmail.com","swabhig@gmail.com" ,"bookeistan@gmail.com"};
//        String emailaddress = "mepintu.sing@gmail.com";
//        String subject =  " Donation of Books....";
//        String message = " My Name is " + mname.getText().toString() + " My Address is " + maddress.getText().toString() +
//                      " Phone no is  " +mphone.getText().toString()+ " I want to donate " + mbookdetails.getText().toString() + " .Quantities are "
//                        + mbookQuantities.getText().toString() ;
//
//        SendMail sm = new SendMail(this , emailaddress, subject , message);
//
//        sm.execute();
//
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        sendemail();
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void submit_details(View view)
    {
        Intent intent = new Intent(NGODetailsActivity.this , DonateBooksDetailsActivity.class);
       // intent.putExtra("ngonames",)
        startActivity(intent);
    }

}
