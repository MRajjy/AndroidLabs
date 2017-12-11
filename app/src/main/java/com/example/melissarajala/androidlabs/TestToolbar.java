package com.example.melissarajala.androidlabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    FloatingActionButton faButton;
    String msg;
    EditText text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        msg = "nothing";
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //floating action button
        faButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        faButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.toolbar), "Snackbar visible",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    //create the toolbar by inflating it from the xml file
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    //respond to a menu item being selected
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();

        switch(id){
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                Snackbar.make(findViewById(R.id.toolbar), "You selected option 1",
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "Option 2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle("Do you want to go back?");
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent intent = new Intent(TestToolbar.this, StartActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //do nothing
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");

                AlertDialog.Builder builder2 = new AlertDialog.Builder(TestToolbar.this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();


                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder2.setView(inflater.inflate(R.layout.dialog_layout, null))

                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                text = (EditText) findViewById(R.id.newMsg); //why is the text object nul????????
                                msg = text.getText().toString();
                                Snackbar.make(findViewById(R.id.newMsg), msg,
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do nothing if cancelled
                            }
                        });
                builder2.create();
                builder2.show();
                break;
            case R.id.about:
                Toast t = Toast.makeText(TestToolbar.this, "Version 1.0, by Melissa Rajala", Toast.LENGTH_LONG);
                t.show();
                break;
        }
        return true;
    }
}
