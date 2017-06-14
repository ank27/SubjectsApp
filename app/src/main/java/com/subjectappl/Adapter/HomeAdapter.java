package com.subjectappl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.subjectappl.Models.Subject;
import com.subjectappl.R;
import com.subjectappl.Utils.OnDeleteClickListner;
import com.subjectappl.Utils.Toaster;

import java.util.List;

import io.realm.Realm;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    List<Subject> subjectList;
    Activity activity;
    LayoutInflater mInflater;
    OnDeleteClickListner onDeleteClickListner;
    String TAG="HomeAdapter";

    public HomeAdapter(List<Subject> productList, Activity activity) {
        this.subjectList = productList;
        this.activity = activity;
        mInflater = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public void setOnItemClickListener(final OnDeleteClickListner listener){
        this.onDeleteClickListner = listener;
    }


    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.single_subject_view, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder holder, final int position) {
        final Subject subject=subjectList.get(position);
        holder.subject_title.setText(subject.subject_title);
        holder.subject_desc.setText(subject.subject_description);

        holder.subject_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"deleteSubject  position="+position +" &subject_id="+subject.id);
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(activity);
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this subject?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Subject subjectRealmResults= realm.where(Subject.class).equalTo("id",subject.id).findFirst();
                                subjectRealmResults.deleteFromRealm();
                                realm.commitTransaction();

                                Toaster.showToast("Subject deleted");
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    public void add(Subject subject){
        subjectList.add(subject);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public void delete(int position) {
        subjectList.remove(position);
        notifyItemRemoved(position);
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         TextView subject_desc;
         TextView subject_title;
         ImageView subject_image;
         ImageView subject_delete;

         public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            subject_title= (TextView) itemLayoutView.findViewById(R.id.subject_title);
            subject_desc= (TextView) itemLayoutView.findViewById(R.id.subject_desc);
            subject_image=(ImageView) itemLayoutView.findViewById(R.id.subject_image);
            subject_delete=(ImageView) itemLayoutView.findViewById(R.id.subject_delete);
//            subject_delete.setOnClickListener(this);
         }

//         @Override
//         public void onClick(View v) {
//             Log.d(TAG,"view click subjcet delete");
////             if (OnDeleteClickListner != null) {
//             Log.d(TAG, "deleteOnclick position=" + getAdapterPosition() + " & subject_id=" + subjectList.get(getAdapterPosition()).id);
//             onDeleteClickListner.deleteSubject(getAdapterPosition(), subjectList.get(getAdapterPosition()).id);
////             }
//         }
     }
}