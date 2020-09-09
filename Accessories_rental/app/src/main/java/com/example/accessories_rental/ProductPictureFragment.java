package com.example.accessories_rental;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.functions.Consumer;

public class ProductPictureFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG";
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQ_CODE = 102;
    private static final int GALLERY_REQ_CODE_FOR_SECOND = 103;
    private static final int GALLERY_REQ_CODE_FOR_FIRST = 104;
    private ProductPictureFragment.ProductPictureFragmentListerner listener;
    public interface ProductPictureFragmentListerner{
        void setImageViewFront(ImageView frontimge);
        void setImageViewBack(ImageView backimage);
    }
    ImageButton camera;
    StorageReference storageReference;
    FirebaseAuth fauth;
    ImageView front_side, back_side;
    String currentPhotoPath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.product_picture, container, false);
        camera = view.findViewById(R.id.imageButton);
        front_side = view.findViewById(R.id.image_captured1);
        back_side = view.findViewById(R.id.image_captured2);
        listener.setImageViewFront(front_side);
        listener.setImageViewBack(back_side);
        storageReference= FirebaseStorage.getInstance().getReference();
        fauth=FirebaseAuth.getInstance();
        front_side.setOnClickListener(this);
        back_side.setOnClickListener(this);
        camera.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ProductPictureFragment.ProductPictureFragmentListerner){
            listener=(ProductPictureFragment.ProductPictureFragmentListerner) context;
        }
        else{
            throw  new RuntimeException(context.toString()+"must implement ProductPictureFragmentListerner");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_captured1:
                Intent frontpick=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(frontpick,GALLERY_REQ_CODE_FOR_FIRST);
                break;
            case R.id.image_captured2:
                Intent backpick=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(backpick,GALLERY_REQ_CODE_FOR_SECOND);
                break;
            case R.id.imageButton:
                cameraPermission();
                break;
        }

    }

    private void cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
            } else {
                dispatchTakePictureIntent();
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this.getActivity(), "Camera  Permission is required to use camera", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.getActivity().sendBroadcast(mediaScanIntent);
            }
        }
        if(requestCode == GALLERY_REQ_CODE_FOR_FIRST){
            if(resultCode == Activity.RESULT_OK){
                Uri uri=data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_"+getFileExt(uri);
                front_side.setTag(imageFileName);
                Picasso.get().load(uri).resize(160,160).into(front_side);
                uploadImagetoFirebase(imageFileName,uri);
         }
        }
        if(requestCode == GALLERY_REQ_CODE_FOR_SECOND){
            if(resultCode == Activity.RESULT_OK){
                Uri uri=data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_"+getFileExt(uri);
                back_side.setTag(imageFileName);
                Picasso.get().load(uri).resize(160,160).into(back_side);
                uploadImagetoFirebase(imageFileName,uri);
            }
        }
    }

    private void uploadImagetoFirebase(String name, Uri uri) {
            final StorageReference image=storageReference.child(name);
            image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                            }
                        });
                  Log.d("TAG","Image is uploaded");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Upload Failed"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
    }

    private String getFileExt(Uri uri) {
        ContentResolver c=getActivity().getContentResolver();
        MimeTypeMap  mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(uri));
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Accessories Rental");
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            currentPhotoPath = image.getAbsolutePath();
            return image;

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
            }
        }

    }

    String getDCIMCamera() {
        try {
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
            };

            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (path.contains("/DCIM/")) {
                        File file = new File(path);
                        path = file.getParent();
                        cursor.close();
                        return path;
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
        }
        return "";

    }

}