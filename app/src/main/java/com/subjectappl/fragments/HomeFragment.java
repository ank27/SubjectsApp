package com.subjectappl.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.subjectappl.Adapter.HomeAdapter;
import com.subjectappl.Models.Subject;
import com.subjectappl.R;
import com.subjectappl.Utils.Toaster;
import com.subjectappl.Utils.OnDeleteClickListner;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class HomeFragment extends Fragment {
    Activity activity;
    View rootView;
    RecyclerView subject_container;
    RelativeLayout no_subject_layout,subject_layout;
    String TAG = "HomeFragment";
    HomeAdapter subjectAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        subject_container =(RecyclerView) rootView.findViewById(R.id.subject_container);
        no_subject_layout=(RelativeLayout) rootView.findViewById(R.id.no_subject_layout);
        subject_layout = (RelativeLayout) rootView.findViewById(R.id.subject_layout );

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        subject_container.setLayoutManager(layoutManager);
        processData();

//        subjectAdapter.setOnItemClickListener(new OnDeleteClickListner() {
//            @Override
//            public void deleteSubject(final int position, final int subject_id) {
//                Log.d(TAG,"deleteSubject position="+position +" &subject_id="+subject_id);
//                final AlertDialog.Builder builder;
//                builder = new AlertDialog.Builder(activity);
//                builder.setTitle("Delete entry")
//                        .setMessage("Are you sure you want to delete this subject?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Realm realm = Realm.getDefaultInstance();
//                                realm.beginTransaction();
//                                Subject subjectRealmResults= realm.where(Subject.class).equalTo("id",subject_id).findFirst();
//                                subjectRealmResults.deleteFromRealm();
//                                realm.commitTransaction();
//                                Toaster.showToast("Subject deleted");
//
//                                subjectAdapter.delete(position);
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void processData() {
        Realm realm = Realm.getDefaultInstance();
        String user_id = "";
        List<Subject> payloads=new ArrayList<>();
        payloads = realm.where(Subject.class).findAll();

        if (payloads.size()>0) {
            no_subject_layout.setVisibility(View.GONE);
            subject_layout.setVisibility(View.VISIBLE);
            subjectAdapter = new HomeAdapter(payloads, activity);
            subject_container.setAdapter(subjectAdapter);
        }else {
            no_subject_layout.setVisibility(View.VISIBLE);
            subject_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        processData();
    }
}
