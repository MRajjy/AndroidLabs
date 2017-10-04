package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    Button send;
    EditText text;
    ListView chat;
    ArrayList<String> msgs = new ArrayList<>();

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter (Context ctx){
            super(ctx, 0);
        }

        //returns number of lines in chat listview
        public int getCount(){
            return msgs.size();
        }

        public String getItem(int position){
            return msgs.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            //below you can use any type of statement (case) based on the position in the array.
            //also you can do if (convertView == null) then inflate else result = convertView

            if(position%2 == 0){
                //result must be a View --> since TextView (chat_row_incoming/outgoing) is a sub-set of View this is ok.
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText( getItem(position));
            return result;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        send = (Button) findViewById(R.id.button3);
        text = (EditText) findViewById(R.id.editText2);
        chat = (ListView) findViewById(R.id.chatView);
        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chat.setAdapter (messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                msgs.add(text.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                text.setText("");
            }
        });



    }

}
