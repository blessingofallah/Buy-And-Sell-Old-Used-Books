package com.baniya.pintooaggarwal.bookbuyselling;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class NGOListActivity extends AppCompatActivity {

//    TypedArray ngo_pics;
//
//    List<NgoItem> ngoItems;
    ListView mylistview;
    private Toolbar toolbar;
    //String name;
    AdView adView;

    String[] ngonames= {"Sarvahitey","YouthClub Foundation","Edubright","Prastaar"};
    int[] ngopics = {R.drawable.sarvahitey,
            R.drawable.youthclub,
            R.drawable.edubright,
            R.drawable.prastaar
    };

//    public String getName() {
//        return name;
//    }
//
//    public NGOListActivity()
//    {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngolist);
//        ngoItems =  new ArrayList<NgoItem>();

        //Admob purpose
//        adView=(AdView)findViewById(R.id.ad_ngoList);
//        MobileAds.initialize(this , "ca-app-pub-6864838753851425~9130646227");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

//
          toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         mylistview = (ListView) findViewById(R.id.ngo_list);
         ListViewAdapter listViewAdapter = new ListViewAdapter(NGOListActivity.this , ngonames , ngopics);
         mylistview.setAdapter(listViewAdapter);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              //  name = ngonames[i];

                Intent intent = new Intent(NGOListActivity.this, NGODetailsActivity.class);

                intent.putExtra("ngonames", ngonames[i]);
                intent.putExtra("ngopics" , ngopics[i]);
                startActivity(intent);
                //System.out.print(name);


            }
        });
//
//
//
//        ngo_pics = getResources().obtainTypedArray(R.array.ngo_logo);
//
//
//        for (int i = 0; i < 2; i++) {
//            NgoItem item = new NgoItem(ngo_pics.getResourceId(i, -1));
//            ngoItems.add(item);
//        }

       // mylistview = (ListView) findViewById(R.id.ngo_list);
//        ArrayAdapter<String> marrayadapter = new ArrayAdapter<String>(NGOListActivity.this ,
//                android.R.layout.simple_list_item_1,
//                getResources().getStringArray(R.array.ngo_logo));


//        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Intent intent = new Intent(NGOListActivity.this, NGODetailsActivity.class);
//                intent.putExtra("NGO NAMES ",mylistview.getItemAtPosition(i).toString());
//                startActivity(intent);
//
//            }
//        });
       // mylistview.setAdapter(marrayadapter);

//        ListViewAdapter adapter = new ListViewAdapter(this, ngoItems);
//        mylistview.setAdapter(adapter);
//
//        mylistview.setOnItemClickListener(this);

    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
////        Intent intent = new Intent(NGOListActivity.this, NGODetailsActivity.class);
////        intent.putExtra("NGO NAMES ",mylistview.getItemAtPosition(i).toString());
////        startActivity(intent);
//
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
