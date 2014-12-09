package com.example.healthweather;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConnectionService extends Activity{
	FileWriter fw ;
	static String ss="";
	static int low=0,high=0,medium=0;
	
	static int low1=0,high1=0,medium1=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connectionservice);


		final Spinner spinner = (Spinner)findViewById(R.id.spinner);

		TextView myTV = (TextView) findViewById(R.id.tview);
		Double d = 0.0;

		new SendFile().execute("");

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/Data"); final File fn = new File(myDir,"reuslt1.txt"); String line = "";

		if (!fn.exists()) { myTV.setText("no file"); 
		}

		try { BufferedReader br = new BufferedReader(new FileReader(fn));

		while ((line = br.readLine()) != null) { d = d + 1.0; 

		}

		} catch (FileNotFoundException e) { // TODO Auto-generated catch
			e.printStackTrace(); } 
		catch (IOException e) { // TODO
			e.printStackTrace(); }
		myTV.setText(d.toString() + " flu cases in USA");


		Button b2;

		b2=(Button)findViewById(R.id.button5);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConnectionService.this,SecondActivity.class);

				startActivity(intent);

			}
		});

		Button b4;

		b4=(Button)findViewById(R.id.button7);
		b4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConnectionService.this,Report.class);

				startActivity(intent);

			}
		});
		Button b3=(Button)findViewById(R.id.button6);

		b3.setOnClickListener(new View.OnClickListener() {




			@Override
			public void onClick(View v) {

				try { BufferedReader br = new BufferedReader(new FileReader(fn));String line1="";
				ss="Tweets "+"\n";
				while ((line1 = br.readLine()) != null) {

					String tmp=spinner.getSelectedItem().toString();
					if(line1.contains(spinner.getSelectedItem().toString()))
					{
						ss=ss+line1+"\n";
						if(line1.contains("high"))
						{
							high=high+1;
						}
						if(line1.contains("low"))
						{
							low=low+1;
						}
						if(line1.contains("medium"))
						{
							medium=medium+1;
						}
					}

					if(line1.contains("high"))
					{
						high1=high1+1;
					}
					if(line1.contains("low"))
					{
						low1=low1+1;
					}
					if(line1.contains("medium"))
					{
						medium1=medium1+1;
					}


					// ss=ss+tmp+"\n";
				}


				TextView t1=(TextView) findViewById(R.id.xview); 
				t1.setText(ss+"\n"+high+low+medium);


				//Print Graphs

				WebView webview = (WebView) findViewById(R.id.webView1);
				String data="['High'," + high + "],['Medium',"
						+ medium + "],['Low',"
						+ low  + "]";
				String data1="['High'," + high1 + "],['Medium',"
						+ medium1 + "],['Low',"
						+ low1  + "]";

				String content = "<html>"
						+ "  <head>"
						+ "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
						+ "    <script type=\"text/javascript\">"
						+ "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
						+ "      google.setOnLoadCallback(drawChart);"
						+ "      function drawChart() {"
						+ "        var data = google.visualization.arrayToDataTable(["

						+ "          ['Intensity', 'Percentage'],"
						+ data
						
						 + "        ]);"
						 + "        var data1 = google.visualization.arrayToDataTable(["

						+ "          ['Intensity', 'Percentage'],"
						+ data1
						
						 + "        ]);"
						 + "        var options = {"

						+ "          title: 'Overall Sentiment Analysis',"
						+ "hAxis: {title: 'Intensity', titleTextStyle: {color: 'red'}}"

						+ "        };"
						+ "    var options1 = {"

						+ "          title: 'Statewise Sentiment Analysis',"
						+ "hAxis: {title: 'Intensity', titleTextStyle: {color: 'red'}}"

						+ "        };"
						+ "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
						+ "var chart1 = new google.visualization.PieChart(document.getElementById('piechart'));"
						+ "        chart.draw(data1, options);"
						+ "chart1.draw(data,options1);"
						+ "      }"
						+ "    </script>"
						+ "  </head>"
						+ "  <body>"
						+" <p style='font-size:xx-large;'>Statistics</p> "
						+ "    <div id=\"chart_div\" style=\"width: 1200px; height: 700px;\"></div>"
						+ "    <div id=\"piechart\" style=\"width: 1200px; height: 700px;\"></div>"
						+ "  </body>" + "</html>";

				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webview.getSettings().setLoadWithOverviewMode(true);
				webview.getSettings().setUseWideViewPort(true);
				webview.getSettings().setBuiltInZoomControls(true);
				webview.requestFocusFromTouch();

				webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				//webview.loadUrl("file:///android_asset/Code.html");
				webview.loadDataWithBaseURL("file:///android_asset/", content,
						"text/html", "utf-8", null);



				} catch (FileNotFoundException e) { // TODO Auto-generated catch
					e.printStackTrace(); } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				// TODO Auto-generated method stub

			}




		});



	}

}

class SendFile extends AsyncTask<String, Void, String> {

	private Exception exception;

	@Override
	protected String doInBackground(String... urls) {
		try {

			JSch ssh = new JSch();
			JSch.setConfig("StrictHostKeyChecking", "no");
			Session session;
			try {
				session = ssh.getSession("group1", "134.193.136.114", 22);

				session.setPassword("group1");
				session.connect();
				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;

				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath() + "/Data");

				sftp.get("/home/group1/result.txt", directory + "/result1.txt");

				// / sftp.put(directory+"/GPS.txt", "/home/cloudera/");

			} catch (JSchException e) {
				// TODO Auto-generated catch block
				Log.i(null, e.toString());
				e.printStackTrace();

			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		} catch (Exception e) {
			this.exception = e;
			return null;
		}
		return null;
	}

	protected void onPostExecute() {
		// TODO: check this.exception
		// TODO: do something with the feed
	}

}