package com.example.healthweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Report extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		WebView webview = (WebView) findViewById(R.id.webView2);
		String data="['High'," + ConnectionService.high + "],['Medium',"
				+ ConnectionService.medium + "],['Low',"
				+ ConnectionService.low  + "]";
		String data1="['High'," + ConnectionService.high1 + "],['Medium',"
				+ ConnectionService.medium1 + ",['Low',"
				+ ConnectionService.low1  + "]";
		
		
		String content = "<html>"
				+ "  <head>"
				+ "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
				+ "    <script type=\"text/javascript\">"
				+ "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				+ "      google.setOnLoadCallback(drawChart);"
				+ "      function drawChart() {"
				+ "        var data = google.visualization.arrayToDataTable(["

				+ "          ['Sentiment', 'Percentage'],"
				+ data
				/*
				 * + "          ['06AM',  5,      2,	0]," +
				 * "          ['09AM',  4,	5,	10]," +
				 * "          ['12PM',  6,	7,	5]," +
				 * "          ['03PM',  3,	10,	5]," +
				 * "          ['06PM',  10,	8,	4]," +
				 * "          ['09PM',  5,	8,	10]"
				 */+ "        ]);"
				+ "        var options = {"

				+ "          title: 'Sentiment Analysis',"
				+ "hAxis: {title: 'Sentiment', titleTextStyle: {color: 'red'}}"

				+ "        };"
				+ "    var options1 = {"

				+ "          title: 'Sentiment Analysis',"
				+ "hAxis: {title: 'Sentiment', titleTextStyle: {color: 'red'}}"

				+ "        };"
				+ "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
				+ "var chart1 = new google.visualization.PieChart(document.getElementById('piechart'));"
				+ "        chart.draw(data, options);"
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
		webview.loadDataWithBaseURL("file:///android_asset/", content,"text/html", "utf-8", null);
		//webview.loadData( content,"text/html",  null);

		
	}

}
