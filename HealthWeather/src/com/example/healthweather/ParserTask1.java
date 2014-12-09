 package com.example.healthweather; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/** A class to parse the Google Places in JSON format */
public class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

	Context context;ProgressDialog progress;//progress dialog
	public ParserTask1(Context context) {
		this.context =context ;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = new ProgressDialog(context);
		progress.setMessage("Loading...");
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.show();
	}
	// Parsing the data in non-ui thread    	
	@Override
	protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

		JSONObject jObject;	
		List<List<HashMap<String, String>>> routes = null;			           

		try{
			jObject = new JSONObject(jsonData[0]);
			DirectionsJSONParser parser = new DirectionsJSONParser();


			// Starts parsing data
			routes = parser.parse(jObject);    
		}catch(Exception e){
			e.printStackTrace();
		}
		return routes;
	}

	// Executes in UI thread, after the parsing process
	@Override
	protected void onPostExecute(List<List<HashMap<String, String>>> result) {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;
		@SuppressWarnings("unused")
		MarkerOptions markerOptions = new MarkerOptions();

		// Traversing through all the routes
		for(int i=0;i<result.size();i++){
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();

			// Fetching i-th route
			List<HashMap<String, String>> path = result.get(i);

			// Fetching all the points in i-th route
			for(int j=0;j<path.size();j++){
				HashMap<String,String> point = path.get(j);					

				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);	

				points.add(position);						
			}

			// Adding all the points in the route to LineOptions
			lineOptions.addAll(points);
			lineOptions.width(5);
			lineOptions.color(Color.BLUE);	

		}

		// Drawing polyline in the Google Map for the i-th route
		MapNewActivity.mGoogleMap.addPolyline(lineOptions);	
		//dismissing of progress dialog
		if (progress != null)
			if (progress.isShowing()) {
				progress.dismiss();
			}

	}			
}  