package com.baniya.pintooaggarwal.bookbuyselling.imagelib.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import com.baniya.pintooaggarwal.bookbuyselling.R;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.activities.GalleryActivity;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Image;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Params;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Utils;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.views.AutoImageView;

import java.util.ArrayList;

/**
 * Created by Pintoo Aggarwal on 23-09-2017.
 */
public class GalleryImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Image> list;
    Activity activity;
    int columnCount;
    public ArrayList<Long> selectedIDs;
    private int screenWidth;
    private View.OnClickListener onClickListener;
    Params params;

    public GalleryImagesAdapter(Activity activity, ArrayList<Image> list, int columnCount, Params params) {
        this.activity = activity;
        this.list = list;
        this.columnCount = columnCount;
        this.params = params;
        selectedIDs = new ArrayList<>();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position)._id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.image_item, parent, false);
        ImageHolder dataObjectHolder = new ImageHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ImageHolder holder = (ImageHolder) viewHolder;
        final Image entity = list.get(position);
        float height;
        if (entity.isPortraitImage)
            height = Float.valueOf(activity.getResources().getDimension(R.dimen.image_height_portrait));
        else
            height = Float.valueOf(activity.getResources().getDimension(R.dimen.image_height_landscape));
        if(holder.imageView != null) {
            Picasso.with(activity)
                    .load(entity.uri)
                    .placeholder(R.drawable.image_processing)
                    .error(R.drawable.no_image)
                    .resize(screenWidth / columnCount, (int)height)
                    .onlyScaleDown()
                    .centerInside()
                    .into(holder.imageView);
        }
        if(selectedIDs.contains(entity._id)) {
            if(params.getLightColor() != 0)
                holder.frameLayout.setForeground(new ColorDrawable(params.getLightColor()));
            holder.selectedImageView.setVisibility(View.VISIBLE);
        }
        else {
            holder.frameLayout.setForeground(null);
            holder.selectedImageView.setVisibility(View.GONE);
        }
        holder.setTag(R.id.image_id, entity._id);
        holder.parentLayout.setOnClickListener(onClickListener);
    }

    public void setSelectedItem(View parentView, long imageId){
        if(selectedIDs.contains(imageId)) {
            selectedIDs.remove(Long.valueOf(imageId));
            ((FrameLayout)parentView.findViewById(R.id.frameLayout)).setForeground(null);
            ((ImageView)parentView.findViewById(R.id.selectedImageView)).setVisibility(View.GONE);
        } else {
            if(selectedIDs.size() < params.getPickerLimit()) {
                selectedIDs.add(Long.valueOf(imageId));
                if(params.getLightColor() != 0)
                    ((FrameLayout) parentView.findViewById(R.id.frameLayout)).setForeground(new ColorDrawable(params.getLightColor()));
                ((ImageView) parentView.findViewById(R.id.selectedImageView)).setVisibility(View.VISIBLE);
            }
            else{
                if(activity instanceof GalleryActivity){
                    ((GalleryActivity) activity).showLimitAlert("You can select only " + params.getPickerLimit() + " images at a time.");
                }
            }
        }
        for (int i = 0; i < selectedIDs.size();i++)
        Log.d("selectedId","selected id  " + String.valueOf(selectedIDs.get(i)));
    }

    public void disableSelection(){
        selectedIDs.clear();


        notifyDataSetChanged();
    }
    public void enableSelection(ArrayList<Long> s){

if (s!=null){
        selectedIDs =s;
        notifyDataSetChanged();}

    }

    public void setItems(ArrayList<Image> imagesList) {
        this.list.clear();
        this.list.addAll(imagesList);
    }

    public ArrayList<Long> getSelectedIDs() {
        return selectedIDs;
    }

    public void setOnHolderClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parentLayout;
        public FrameLayout frameLayout;
        public AutoImageView imageView;
        public ImageView selectedImageView;

        public ImageHolder(View v) {
            super(v);
            imageView = (AutoImageView) v.findViewById(R.id.imageView);
            selectedImageView = (ImageView) v.findViewById(R.id.selectedImageView);
            parentLayout = (RelativeLayout) v.findViewById(R.id.parentLayout);
            frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout);
            if(params.getToolbarColor() != 0)
                Utils.setViewBackgroundColor(activity, selectedImageView, params.getToolbarColor());
        }

        public void setId(int position) {
            parentLayout.setId(position);
        }

        public void setTag(int resource_id, long id) {
            parentLayout.setTag(resource_id, id);
        }
    }

}
