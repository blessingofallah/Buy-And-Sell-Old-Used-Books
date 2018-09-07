package com.baniya.pintooaggarwal.bookbuyselling;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.baniya.pintooaggarwal.bookbuyselling.RecyclerViewAdapter;
/**
 * Created by Pintoo Aggarwal on 30-08-2017.
 */
public class MyAdsFragment  extends android.support.v4.app.Fragment  /* implements SearchView.OnQueryTextListener*/ {
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
  //  RecyclerView.Adapter adapter ;
    RecyclerViewAdapter adapter;
    ValueEventListener valueEventListener;
    ValueEventListener valueEventListener2;


    // Creating List of ImageUploadInfo class.
    List<AdUploadInfo> list = new ArrayList<>();

    private FirebaseUser user;


    public MyAdsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_images, container, false);


        user = FirebaseAuth.getInstance().getCurrentUser();

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
        // Setting RecyclerView layout as LinearLayout.
//        final LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(linearLayoutManager);


        if (user!=null) {
            databaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());


            valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {


                    for (DataSnapshot child2 : snapshot.getChildren()) {

                        AdUploadInfo adUploadInfo = child2.getValue(AdUploadInfo.class);


                        for (DataSnapshot child3 : child2.child("images").getChildren()) {

                            adUploadInfo.setImageUrl(child3.getValue().toString());
                            adUploadInfo.setUserId(user.getUid());
                            adUploadInfo.setAdId(child2.getKey());
                            if (list.size()==2000000){
                                break;
                            }
                            list.add(adUploadInfo);

                            break;
                        }


                    }

                    adapter = new RecyclerViewAdapter(getActivity(), list);

                    recyclerView.setAdapter(adapter);

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {





                }
            };

            databaseReference.addListenerForSingleValueEvent(valueEventListener);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            int queryOffset=2000000;
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);


                int visibleItemCount        = mLayoutManager.getChildCount();
                int totalItemCount          = mLayoutManager.getItemCount();
              //  int firstVisibleItemPosition= linearLayoutManager.findFirstVisibleItemPosition();


                if (((visibleItemCount  /* + firstVisibleItemPosition*/) >= totalItemCount /* && firstVisibleItemPosition >= 0*/))/*&&(firstVisibleItemPosition+1 % 5 == 0))*/ {

                    queryOffset = queryOffset + 2000000;
                    final int alreadyLoaded =queryOffset-2000000;


                    valueEventListener2=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            int x=0;
                            for (DataSnapshot child2 : snapshot.getChildren()) {
                                x=x+1;
                                if (x<=alreadyLoaded){
                                    continue;
                                }
                                AdUploadInfo adUploadInfo = child2.getValue(AdUploadInfo.class);


                                for (DataSnapshot child3 : child2.child("images").getChildren()) {

                                    adUploadInfo.setImageUrl(child3.getValue().toString());
                                    adUploadInfo.setUserId(user.getUid());
                                    adUploadInfo.setAdId(child2.getKey());
                                    if (list.size()==queryOffset){
                                        break;
                                    }
                                    list.add(adUploadInfo);

                                    break;
                                }


                            }


                            recyclerView.getAdapter().notifyItemRangeInserted(list.size(),2000000);


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {




                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListener2);
                }



            }

        });
        return view;    }

    @Override
    public void onPause() {
        super.onPause();
        if (valueEventListener!=null){
            databaseReference.removeEventListener(valueEventListener);
        }
        if (valueEventListener2!=null){
            databaseReference.removeEventListener(valueEventListener2);
        }
    }

   // HomeActivity homeActivity = new HomeActivity(adapter , list);

//    @Override
//    public boolean onQueryTextSubmit(String s) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        MyAdsFragment myAdsFragment = new MyAdsFragment();
//
//
//        newText = newText.toLowerCase();
//        ArrayList<AdUploadInfo> newList = new ArrayList<>();
//        for(AdUploadInfo adUploadInfo : list)
//        {
//            String name = adUploadInfo.getAdTitle().toLowerCase();
//            if(name.contains(newText))
//            {
//                newList.add(adUploadInfo);
//            }
//        }
//        adapter.setFilter(newList);
//            return true;
// }



}


