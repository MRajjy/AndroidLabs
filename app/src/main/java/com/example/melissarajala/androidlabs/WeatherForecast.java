package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.System.in;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    ProgressBar progress;
    TextView minGUI, maxGUI, currentGUI;
    ImageView picGUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);

        ForecastQuery fq = new ForecastQuery();
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        //temperature variables
        String min, max, current, icon;
        Bitmap pic;
        float i = 0;
        protected String doInBackground(String ...args) {
            InputStream stream;
                try {
                    URL url = new URL(args[(int) i]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    stream = conn.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(stream, "UTF-8");
                    //while loop until end of XML document is reached
                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        //if the XML tag is a START_TAG
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            //check that name of tag is temperature and get the attributes: value, min, and max
                            if (parser.getName().equals("temperature")) {
                                current = parser.getAttributeValue(null, "value");
                                publishProgress(25, 50, 75);
                                System.out.println("The current temp is: " + current);
                                min = parser.getAttributeValue(null, "min");
                                publishProgress(25, 50, 75);
                                System.out.println("The min temp is: " + min);
                                max = parser.getAttributeValue(null, "max");
                                publishProgress(25, 50, 75);
                                System.out.println("The max temp is: " + max);
                                //check that name of tag is weather and get the attribute: icon
                            } else if (parser.getName().equals("weather")) {
                                icon = parser.getAttributeValue(null, "icon");

                                if (fileExistance(icon)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(icon);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    pic = BitmapFactory.decodeStream(fis);

                                } else {
                                    //download the icon image from open weather map using the icon attribute from above
                                    HttpURLConnection connection = null;
                                    URL urlImage = new URL("http://openweathermap.org/img/w/" + icon + ".png");
                                    try {
                                        connection = (HttpURLConnection) urlImage.openConnection();
                                        connection.connect();
                                        int responseCode = connection.getResponseCode();
                                        if (responseCode == 200) {
                                            System.out.println("Downloading...");
                                            pic = BitmapFactory.decodeStream(connection.getInputStream());
                                            publishProgress(100);
                                        } else
                                            return null;
                                    } catch (Exception e) {
                                        return null;
                                    } finally {
                                        if (connection != null) {
                                            connection.disconnect();
                                        }
                                    }
                                }
                                //save the bitmap object to local storage
                                FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                Log.i(ACTIVITY_NAME, "Image name = " + icon + ".png");
                                pic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

//                                if (fileExistance(icon)) {
//                                    FileInputStream fis = null;
//                                    try {
//                                        fis = openFileInput(icon);
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//                                    pic = BitmapFactory.decodeStream(fis);
//
//                                }
                            }
                        }
                        //go to the next tag in the XML document
                        parser.next();
                    }
                }
                catch (XmlPullParserException e){
                    System.out.println("XMLPullParserException");
                }
                catch (MalformedURLException mue) {
                    System.out.println("malformed URL");
                }
                catch (IOException e) {
                    System.out.println("IOException");
                }
            //string returned for doInBackground
            return "Finished doInBackground";
        }

        //called from GUI
        public void onProgressUpdate(Integer ...value ) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        //update the GUI components with the temperatures and pic
        public void onPostExecute(String s )
        {
            minGUI = (TextView) findViewById(R.id.minTemp);
            maxGUI = (TextView) findViewById(R.id.maxTemp);
            currentGUI = (TextView) findViewById(R.id.currentTemp);
            picGUI = (ImageView) findViewById(R.id.imageView3);

            currentGUI.setText("The current temperature is: " + current);
            minGUI.setText("The minimum temperature is: " + min);
            maxGUI.setText("The maximum temperature is: " + max);
            picGUI.setImageBitmap(pic);

            progress.setVisibility(View.INVISIBLE);
        }

    }
    //method used for checking if image is already in local storage
    public boolean fileExistance(String fname){
        Log.i(ACTIVITY_NAME, "Image found locally.");
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }



}
