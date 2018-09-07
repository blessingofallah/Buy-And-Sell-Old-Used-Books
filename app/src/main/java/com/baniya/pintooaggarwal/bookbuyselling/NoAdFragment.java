package com.baniya.pintooaggarwal.bookbuyselling;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by Pintoo Aggarwal on 30-08-2017.
 */

public class NoAdFragment extends android.support.v4.app.Fragment
{
    public NoAdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_ad, container, false);
    }

}
