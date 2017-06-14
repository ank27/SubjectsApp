package com.subjectappl.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.subjectappl.Adapter.HomeAdapter;
import com.subjectappl.Models.Subject;
import com.subjectappl.R;
import com.subjectappl.Utils.MarshMallowPermission;
import com.subjectappl.Utils.Toaster;

import java.io.IOException;

import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

public class CreateSubjectFragment extends Fragment implements View.OnClickListener {
    Activity activity;
    View rootView;
    String TAG = "CreateFragment";
    EditText title;
    EditText description;
    Button add_image_btn,btn_save;
    ImageView subject_image;
    Realm realm;
    private static final int GET_FROM_GALLERY_ACTIVITY_REQUEST_CODE = 101;
    Uri uri_image,uri_image_new=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_create_subject, container, false);
        title=(EditText) rootView.findViewById(R.id.title);
        description=(EditText) rootView.findViewById(R.id.description);
        add_image_btn=(Button) rootView.findViewById(R.id.add_image_btn);
        subject_image=(ImageView) rootView.findViewById(R.id.subject_image);
        btn_save=(Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        final MarshMallowPermission marshMallowPermission=new MarshMallowPermission(getActivity());
        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(marshMallowPermission.checkPermissionForCamera() && marshMallowPermission.checkPermissionForExternalStorage()) {
                    uploadFromGallery();
                }else {
                    marshMallowPermission.checkPermissions();
                }
            }
        });
        return rootView;
    }

    private void uploadFromGallery() {
        uri_image=null;
        uri_image_new=null;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_FROM_GALLERY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FROM_GALLERY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri_image = data.getData();
            Log.d(TAG,"image_ok "+ uri_image+"");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri_image);
                Log.d(TAG,"SubjcetImage ="+uri_image);
                subject_image.setImageBitmap(bitmap);
                subject_image.setScaleType(ImageView.ScaleType.FIT_XY);
                subject_image.setAlpha((float) 0.87);
            } catch (IOException e) {
                Log.d(TAG,"Image resolve error gallery");
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                if (title.getText().toString().equalsIgnoreCase("") && description.getText().toString().equalsIgnoreCase("")){
                    Toaster.showToast("Please fill all fields");
                }else{
                    String subject_title=title.getText().toString();
                    String subject_description=description.getText().toString();

                    realm=Realm.getDefaultInstance();
                    int count = realm.where(Subject.class).findAll().size();
                    Log.d(TAG,"count="+count);
                    Subject subject=new Subject(count,subject_title,subject_description,"");
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(subject);
                    realm.commitTransaction();
                    realm.close();
                    Toaster.showToast("Subject saved!!!");

                    title.getText().clear();
                    description.getText().clear();
                }
                break;
        }
    }
}
