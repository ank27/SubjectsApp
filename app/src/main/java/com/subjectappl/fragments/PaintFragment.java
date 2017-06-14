package com.subjectappl.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subjectappl.Adapter.HomeAdapter;
import com.subjectappl.Models.Subject;
import com.subjectappl.R;
import com.subjectappl.Utils.NetworkEvent;
import com.subjectappl.Utils.Toaster;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.realm.Realm;


public class PaintFragment extends Fragment {
    Activity activity;
    View rootView;
    String TAG = "PaintFragment";
    TextView save_drawing;
    View drawing_view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_paint, container, false);
        save_drawing=(TextView) rootView.findViewById(R.id.save_drawing);
        drawing_view = rootView.findViewById(R.id.drawing_view);
        save_drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = drawing_view.getDrawingCache();
                String path = Environment.getExternalStorageDirectory().toString();
                Log.d(TAG,"path="+path);
                File file = new File(path+"/image.png");
                FileOutputStream ostream;
                try {
                    file.createNewFile();
                    ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                    Toaster.showToast("Drawing saved!!!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toaster.showToast("Error occured, Try again later!!!");
                }
            }
        });
        return rootView;
    }
}
