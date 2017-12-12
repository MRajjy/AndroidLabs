package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Melissa Rajala on 2017-12-10.
 */

public class MessageFragment extends Fragment {
    Activity parent;
    long id = 0;
    String msg = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle passedInfo = getArguments();

        if(passedInfo != null) {
            id = passedInfo.getLong("ID");
            msg = passedInfo.getString("Message");

        }
        Log.i("Passed key", ""+id);


        parent = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_message_fragment, null);
        TextView idField = (TextView) v.findViewById(R.id.idText);
        TextView msgField = (TextView) v.findViewById(R.id.msgText);

        idField.setText(Long.toBinaryString(id));
        msgField.setText(msg);


        Button b = (Button)v.findViewById(R.id.deleteBtn);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                parent.getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
//            }
//        });
        return v;
    }



}
