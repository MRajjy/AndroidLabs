package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private final static String ACTIVITY_NAME = "ChatWindow";
    Button send;
    EditText text;
    ListView chat;
    FrameLayout frame;
    boolean layout;
    ArrayList<String> msgs = new ArrayList<>();
    ChatDatabaseHelper cdh;
    Cursor results;


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

        //for lab 7 - fragments
        public long getItemId(int position){
            //move cursor to position and get ID of the object in that position
            results.moveToPosition(position);
            return  results.getLong(results.getColumnIndex("_id"));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        send = (Button) findViewById(R.id.button3);
        text = (EditText) findViewById(R.id.editText2);
        chat = (ListView) findViewById(R.id.chatView);
        frame = (FrameLayout) findViewById(R.id.frame_layout);

        //check if frame layout was loaded
        layout = findViewById(R.id.frame_layout) != null;
        System.out.println("Is the layout not null? " + layout);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chat.setAdapter (messageAdapter);


        //working with the database
        cdh = new ChatDatabaseHelper(this);
        //create a local variable for the SQLite database
        final SQLiteDatabase db = cdh.getWritableDatabase();
        //query for results from db
        results = db.query(false, ChatDatabaseHelper.TABLE_NAME, new String[] {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null , null, null, null, null);

        //resets the iteration of results
        results.moveToFirst();

        //add messages from db to msgs arraylist
        //How many rows in the results:
        int numResults = results.getCount();

        int messageIndex = results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

        for (int i= 0; i < numResults; i++){
            msgs.add(results.getString(messageIndex));
            results.moveToNext();
        }

        //resets the iteration of results
        results.moveToFirst();
        //log the message retrieved and column count
        while(!results.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + results.getString(results.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "Cursor's column count = " + results.getColumnCount());
            results.moveToNext();
        }

        //print out the name of each column in the table
        for(int i=0; i<results.getColumnCount();i++){
            System.out.println(results.getColumnName(i));
        }

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Create new row data
                ContentValues newData = new ContentValues();
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, text.getText().toString());

                //Then insert
                db.insert(ChatDatabaseHelper.TABLE_NAME, "" , newData);
//                System.out.println(msgs);

                msgs.add(text.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                text.setText("");

            }
        });

        //for lab 7 - listener for list view in chat window
        chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListItemClick", "You clicked an item");
                Bundle b = new Bundle();
                b.putLong("ID", id);
                b.putString("Message", results.getString(results.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
                //show message details
                //first determine if it is a phone or tablet
                if (layout = findViewById(R.id.frame_layout) != null){ //on a tablet
                    b.putBoolean("Tablet", true);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    MessageFragment mf = new MessageFragment(ChatWindow.this);
                    mf.setArguments(b); //pass the id to the fragment
                    //need to get message too
                    ft.replace(R.id.frame_layout, mf )
                            .addToBackStack("")
                            .commit();

                } else { //on a phone
                    //start the MessageDetails class
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(b);
                    startActivityForResult(intent, 10);
                }
            }
        });

    }

    //lab 7
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        if (responseCode == 10) {
            int messagePassed = data.getIntExtra("ID", 0);
            msgs.remove(messagePassed -1);
        }
    }

    //for lab 7 - delete item from msgs array
    public void delete(int index){
        msgs.remove(index-1);
    }

    public void onDestroy(){
        super.onDestroy();
        //close database
        if (cdh != null) {
            cdh.close();
        }
    }

}
