package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class MessageDetails extends Activity {

    Button removeButton;
    boolean layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle info = getIntent().getExtras();

        //info.putString("Key", "From phone");
        //start Transaction to insert fragment in screen:
        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        final MessageFragment mf = new MessageFragment();
        mf.setArguments(info);
        ft.add(R.id.frame_layout, mf );
        ft.commit();



        removeButton = (Button)findViewById(R.id.deleteBtn);
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(layout = findViewById(R.id.frame_layout) != null)
                    {
                        //start Transaction to insert fragment in screen:
//                        FragmentTransaction ft =  getFragmentManager().beginTransaction();
//                        ft.remove(mf);
//                        ft.commit();

                    }
                    else
                    {

                    }
                }
            });



    }


}
