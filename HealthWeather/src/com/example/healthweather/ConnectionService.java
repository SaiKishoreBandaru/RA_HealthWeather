package com.example.healthweather;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;








//import com.example.challenge1.R;
import com.google.gson.Gson;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConnectionService extends Activity{
	FileWriter fw ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connectionservice);
		

       Spinner spinner = (Spinner)findViewById(R.id.spinner);
     
		TextView myTV = (TextView) findViewById(R.id.tview);
		Double d = 0.0;
		
		 String root = Environment.getExternalStorageDirectory().toString();
		  File myDir = new File(root + "/Data"); File fn = new File(myDir,"flu.txt"); String line = "";
		  
		  if (!fn.exists()) { myTV.setText("no file"); }
		  
		  try { BufferedReader br = new BufferedReader(new FileReader(fn));
		  
		  while ((line = br.readLine()) != null) { d = d + 1.0; }
		  
		  } catch (FileNotFoundException e) { // TODO Auto-generated catch
		   e.printStackTrace(); } 
		  catch (IOException e) { // TODO
		   e.printStackTrace(); }
		  myTV.setText(d.toString() + " flu cases in USA");
		 

		
	}

}
