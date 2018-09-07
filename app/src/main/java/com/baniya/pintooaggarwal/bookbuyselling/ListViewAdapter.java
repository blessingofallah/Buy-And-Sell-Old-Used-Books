package com.baniya.pintooaggarwal.bookbuyselling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Image;

import java.nio.InvalidMarkException;
import java.util.List;

/**
 * Created by Pintoo Aggarwal on 23-09-2017.
 */

public class ListViewAdapter extends ArrayAdapter <String>{

    Context context;
    String [] ngo_names ;

    public ListViewAdapter(@NonNull Context context, int resource, String[] ngo_names) {
        super(context, resource);
        this.ngo_names = ngo_names;
    }


    public String[] getNgo_names() {
        return ngo_names;
    }

    public void setNgo_names(String[] ngo_names) {
        this.ngo_names = ngo_names;
    }

    public int[] getNgo_pics() {
        return ngo_pics;
    }

    public void setNgo_pics(int[] ngo_pics) {
        this.ngo_pics = ngo_pics;
    }

    int []  ngo_pics ;

    public ListViewAdapter(Context context , String[] names , int[] pics ) {
        super(context ,R.layout.ngo_list_item);
        this.context = context;
        this.ngo_names = names;
        this.ngo_pics = pics;
    }

    @Override
    public int getCount() {
        return ngo_names.length;
    }

//    @Override
//    public Object getItem(int position) {
//        return ngoItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return ngoItems.indexOf(getItem(position));
//    }

    private class ViewHolder
    {
        ImageView imageView;
        TextView textView;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if(view == null) {
            LayoutInflater minflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = minflater.inflate(R.layout.ngo_list_item, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.ngo_list_icon);
            viewHolder.textView = (TextView) view.findViewById(R.id.ngo_name);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)view.getTag();
        }
            viewHolder.imageView.setImageResource(ngo_pics[position]);
            viewHolder.textView.setText(ngo_names[position]);
        return view;
    }

}
