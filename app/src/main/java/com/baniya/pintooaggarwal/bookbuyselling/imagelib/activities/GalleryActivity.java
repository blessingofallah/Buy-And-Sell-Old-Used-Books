package com.baniya.pintooaggarwal.bookbuyselling.imagelib.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
//import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baniya.pintooaggarwal.bookbuyselling.Manifest;
import com.baniya.pintooaggarwal.bookbuyselling.R;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.adapters.GalleryImagesAdapter;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Image;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Params;
import com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Utils;
import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

//import static com.baniya.pintooaggarwal.bookbuyselling.imagelib.utils.Constants.REQUEST_CAMERA_PERMISSION;

/**
 * Created by Pintoo Aggarwal on 23-09-2017.
 */
public class GalleryActivity extends BaseActivity {

    RelativeLayout parentLayout;
    Toolbar toolbar;
    TextView toolbar_title;
    RecyclerView recycler_view;
    AlertDialog alertDialog;
    GalleryImagesAdapter imageAdapter;
    ArrayList<Image> imagesList = new ArrayList<>();
    ArrayList<Long> handlesForCamera=new ArrayList<>();
    private Params params;
    CameraPhoto cameraPhoto;
    private File fileen = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        cameraPhoto = new CameraPhoto(getApplicationContext());



    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForPermissions();
        //checkForCamerapermissions();
        init();

    }

//    private void checkForCamerapermissions() {
//        if(hasCameraPermission(this))
//
//        {
//            cameraIntent();
//        }
//        else {
//            requestCameraPermissions(this, Constants.REQUEST_CAMERA_PERMISSION);
//
//        }

//    }

    private void init(){
        Utils.initToolBar(this, toolbar, true);
        toolbar_title.setText(R.string.select_images);
        if(this.getIntent() != null){
            if(this.getIntent().hasExtra(Constants.KEY_PARAMS)) {
                Object object = this.getIntent().getSerializableExtra(Constants.KEY_PARAMS);
                if(object instanceof Params)
                    params = (Params) object;
                else{
                    Utils.showLongSnack(parentLayout, "Provided serializable data is not an instance of Params object.");
                    setEmptyResult();
                }
            }
        }
        handleInputParams();
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL));
    }

    private void handleInputParams() {
        if(params.getPickerLimit() == 0){
            Utils.showLongSnack(parentLayout, "Please mention the picker limit as a parameter.");
            setEmptyResult();
        }
        Utils.setViewBackgroundColor(this, toolbar, params.getToolbarColor());
        if(params.getCaptureLimit() == 0){
            params.setCaptureLimit(params.getPickerLimit());
        }
    }

    private void checkForPermissions(){
        if(hasStoragePermission(this))
            getImagesFromStorage();
        else {
            requestStoragePermissions(this, Constants.REQUEST_STORAGE_PERMS);
           // requestCameraPermissions(this, Constants.REQUEST_CAMERA_PERMISSION);
            }
//        if (hasCameraPermission(this))
//            cameraIntent();
//        else
//        {
//            requestCameraPermissions(this, Constants.REQUEST_CAMERA_PERMISSION);
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_STORAGE_PERMS:
                if (validateGrantedPermissions(grantResults)) {
                    getImagesFromStorage();
                } else {
                    Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
                    setEmptyResult();
                }
                break;
            case Constants.MY_REQUEST_CAMERA:

                cameraIntent();
//                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
//                cameraIntent();
//                } else
//                    Toast.makeText(this, "Permission not granted.", Toast.LENGTH_LONG).show();
                break;

            case Constants.MY_REQUEST_WRITE_CAMERA:
                checkPermissionCA();
                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(alertDialog != null){
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
        if (imageAdapter!=null) {
            handlesForCamera = imageAdapter.getSelectedIDs();
        }
        imagesList=null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(imageAdapter != null){
            recycler_view.setHasFixedSize(true);
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recycler_view.getLayoutManager();
            manager.setSpanCount(getColumnCount());
            recycler_view.setLayoutManager(manager);
            recycler_view.requestLayout();
        }
    }

    @Override
    public void onBackPressed() {
        handleBackPress();
    }

    private void handleBackPress(){

            setEmptyResult();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackPress();
            return true;
        }
        else if(item.getItemId() == R.id.action_done){
            if(imageAdapter != null)
                prepareResult();
            else
                setEmptyResult();
            return true;
        }
        else if(item.getItemId() == R.id.action_camera){

            checkpermissionCW();

//            if (checkSelfPermission(android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
//            {
//                cameraIntent();
//            }
//            else
//            {
//                String[] permissionRequest = {android.Manifest.permission.CAMERA};
//                requestPermissions(permissionRequest , Constants.REQUEST_CAMERA_PERMISSION );
//            }




          //  cameraIntent();
//            if(hasCameraPermission(this))
//
//            {
//                cameraIntent();
//            }
//            else {
//                requestCameraPermissions(this, Constants.REQUEST_CAMERA_PERMISSION);
//
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkpermissionCW() {

        int permissionCheck = ContextCompat.checkSelfPermission(GalleryActivity.this , android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    GalleryActivity.this , new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},Constants.MY_REQUEST_WRITE_CAMERA
            );
        }
        else
        {
            checkPermissionCA();
        }
    }

    private void checkPermissionCA() {
        int permissioncheck = ContextCompat.checkSelfPermission(GalleryActivity.this, android.Manifest.permission.CAMERA);
        if (permissioncheck!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    GalleryActivity.this , new String[] { android.Manifest.permission.CAMERA},Constants.MY_REQUEST_CAMERA
            );
        }

        else
        {
            cameraIntent();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    private void prepareResult(){
        ArrayList<Long> selectedIDs = imageAdapter.getSelectedIDs();
        ArrayList<Image> selectedImages = new ArrayList<>(selectedIDs.size());
        for(Image image : imagesList){
            if(selectedIDs.contains(image._id))
                selectedImages.add(image);
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST, selectedImages);

        intent.putExtra("selectedId",selectedIDs);
        setIntentResult(intent);
    }

    private void getImagesFromStorage(){
        new ApiSimulator(this).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private void populateView(ArrayList<Image> images){
        if(imagesList == null)
            imagesList = new ArrayList<>();
        imagesList.addAll(images);
        ArrayList<Image> dupImageSet = new ArrayList<>();
        dupImageSet.addAll(imagesList);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recycler_view.setLayoutManager(mLayoutManager);
        imageAdapter = new GalleryImagesAdapter(this, dupImageSet, getColumnCount(), params);


        recycler_view.setAdapter(imageAdapter);
        imageAdapter.setOnHolderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long imageId = (long) view.getTag(R.id.image_id);
                imageAdapter.setSelectedItem(view, imageId);
                setCountOnToolbar();
            }
        });
    }




    private int getColumnCount() {
        if(params.getColumnCount() != 0)
            return params.getColumnCount();
        else if(params.getThumbnailWidthInDp() != 0) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float screenWidthInDp = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (screenWidthInDp / params.getThumbnailWidthInDp());
        }
        else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float screenWidthInDp = displayMetrics.widthPixels / displayMetrics.density;
            float thumbnailDpWidth = getResources().getDimension(R.dimen.thumbnail_width) / displayMetrics.density;
            return (int) (screenWidthInDp / thumbnailDpWidth);
        }
    }

    private void setCountOnToolbar(){
        if(imageAdapter.getSelectedIDs().size() > 0)
            toolbar_title.setText(""+imageAdapter.getSelectedIDs().size()+ " "+ getString(R.string.selected));
        else
            toolbar_title.setText(R.string.select_images);
    }

    private void setEmptyResult(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void setIntentResult(Intent intent){
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showLimitAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Alert")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class ApiSimulator extends AsyncTask<Void, Void, ArrayList<Image>> {
        Activity context;
        String error="";

        public ApiSimulator(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Loading..");
        }

        @Override
        protected ArrayList<Image> doInBackground(@NonNull Void... voids) {
            ArrayList<Image> images = new ArrayList<>();
            Cursor imageCursor = null;
            try {
                final String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH};
                final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                while (imageCursor.moveToNext()) {
                    long _id = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    int height = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                    int width = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                    String imagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(_id));
                    Image image = new Image(_id, uri, imagePath,(height > width)? true: false);
                    images.add(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = e.toString();
            } finally {
                if(imageCursor != null && !imageCursor.isClosed()) {
                    imageCursor.close();
                }
            }
            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<Image> images) {
            super.onPostExecute(images);
            dismissProgressDialog();

            if (isFinishing()) {
                return;
            }
            if(error.length() == 0)
                populateView(images);





            else
                Utils.showLongSnack(parentLayout, error);

                Intent intent = getIntent();
                ArrayList<Long> handles = (ArrayList<Long>) intent.getSerializableExtra("selectedIdBack");
                imageAdapter.enableSelection(handles);
                setCountOnToolbar();
            if (handlesForCamera!=null&&!handlesForCamera.isEmpty()) {
                imageAdapter.enableSelection(handlesForCamera);
                setCountOnToolbar();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case Constants.CAMERA_INTENT:
                      cameraPhoto.getPhotoPath();

                break;
        }
    }

    public void cameraIntent(){

//        fileen = getFile();
//        if(fileen!=null)
//        {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            try
//            {
//                Uri photoUri = Uri.fromFile(fileen);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(intent , Constants.CAMERA_INTENT);
//
//            }
//            catch (ActivityNotFoundException e)
//            {
//
//            }
//        }
//        else
//        {
//            Toast.makeText(this, "Please Check your sd Card status", Toast.LENGTH_SHORT).show();
//        }
//
//




      //  if (checkSelfPermission(Manifest))

  //   Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        Intent intent1 = new Intent();
//        intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//        if (intent1.resolveActivity(getPackageManager()) != null) {
//
//
//        startActivity(intent1);

    //      startActivityForResult(takePictureIntent,Constants.CAMERA_INTENT);

  //  }

        try
        {
            startActivityForResult(cameraPhoto.takePhotoIntent(), Constants.CAMERA_INTENT);
            cameraPhoto.addToGallery();
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Something Wrong While taking Photos", Toast.LENGTH_SHORT).show();
        }

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//        String imageName = getPictureName();
//
//        File imagefile = new File(pictureDirectory , imageName);
//
//      //  Uri pictureUri = Uri.fromFile(imagefile);
//
//        Uri pictureUri = FileProvider.getUriForFile(GalleryActivity.this, BuildConfig.APPLICATION_ID + ".provider",imagefile );
//
//
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , pictureUri);
//
//        startActivityForResult(cameraIntent , Constants.CAMERA_INTENT);



    }

//    private File getFile() {
//
//        File fileDir = new File(Environment.getExternalStorageDirectory() +
//                "/Android/data/" +
//                getApplicationContext().getPackageName()
//                +  "/Files");
//
//        if(!fileDir.exists())
//        {
//            if(!fileDir.mkdirs())
//            {
//                return  null;
//            }
//        }
//
//        File mediaFile = new File(fileDir.getPath() + File.separator + "temp.jpg");
//        return mediaFile;
//    }

//    private String getPictureName() {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String Timestamp = sdf.format(new Date());
//        return "TakenPhoto" + Timestamp + ".jpg";
//
//
//    }


}
