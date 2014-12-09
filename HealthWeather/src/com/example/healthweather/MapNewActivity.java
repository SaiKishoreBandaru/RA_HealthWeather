package com.example.healthweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;

public class MapNewActivity extends FragmentActivity implements LocationListener{
	
	public static GoogleMap mGoogleMap;	
	public static Location location; 
	//declare a array list to store all the latitude and longitude of places 
	//which is used when getting directions
	public static ArrayList<MarkerOptions> markerOptionArrayList =new ArrayList<MarkerOptions>();
	public static HashMap<String, ArrayList<MarkerOptions>> latLngmap =new HashMap<String, ArrayList<MarkerOptions>>();
	public static ArrayList<LatLng> latLongArrayList =new ArrayList<LatLng>();
	public static ArrayList<String> titleArraylist =new ArrayList<String>();
	public static ArrayList<String> vicinityArrayList =new ArrayList<String>();
	public static ArrayList<Double> distanceArrayList =new ArrayList<Double>();
	public static ArrayList<String> placeArrayList =new ArrayList<String>();
		
	static LatLng latLng;
	private Marker marker;
	static Hashtable<String, LatLng> markers;
	DecimalFormat df;
	public 
	String[] mPlaceType=null;
	String[] mPlaceTypeName=null;
	int thrKm=0,oneKm=0,fiveKm=0;

	static double mLatitude=0;
	static double mLongitude=0;
	LayoutInflater li;

		//location manget to get locations
	LocationManager locationManager ;
	//arraylist for adding two locations for directions
	ArrayList<LatLng> directionArrayList =new ArrayList<LatLng>();
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.maps_w);
		
		mPlaceType = getResources().getStringArray(R.array.place_type);

		 li = (LayoutInflater) MapNewActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		 
		// Getting Google Play availability status
				int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


				if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

					int requestCode = 10;
					Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
					dialog.show();

				}
				else
				{
					// Google Play Services are available

					// Getting reference to the SupportMapFragment
					SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

					// Getting Google Map
					mGoogleMap = fragment.getMap();

					// Enabling MyLocation in Google Map
//					mGoogleMap.setMyLocationEnabled(true);

					// Getting LocationManager object from System Service LOCATION_SERVICE
					locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

					// Creating a criteria object to retrieve provider
					Criteria criteria = new Criteria();

					// Getting the name of the best provider
					String provider = locationManager.getBestProvider(criteria, true);
					System.out.println("the best provider is........"+provider);


					// Getting Current Location From best N/w Provider
					locationManager.requestLocationUpdates(provider, 0, 0, this);
					location = locationManager.getLastKnownLocation(provider);

					if(location!=null){
						mLatitude = location.getLatitude();
						mLongitude = location.getLongitude();	
						//String = location.
					}
					
						latLng =new LatLng(mLatitude, mLongitude);

					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo((float) 14.4));

					callMethod();

				}
	}

	private void callMethod() {
		// TODO Auto-generated method stub
		int selectedPosition=0;
		if(getIntent().getExtras()!=null)
		{
			selectedPosition = getIntent().getIntExtra("PLACE_POSIT", 0);
		}
		String type = mPlaceType[selectedPosition];
		type="church";

		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		sb.append("location="+latLng.latitude+","+latLng.longitude);
		sb.append("&radius="+5000);
		sb.append("&types="+type);
		sb.append("&sensor=true");
		sb.append("&key="+"AIzaSyCUK8daJe98EzKJdvkJ4mqUebZLqetBT1o");
		
		
		if(Utils.haveNetworkConnection(MapNewActivity.this))
		{
			// Creating a new non-ui thread task to download Google place json data 
			PlacesTask placesTask = new PlacesTask(MapNewActivity.this);		        			        
			// Invokes the "doInBackground()" method of the class PlaceTask
			placesTask.execute(sb.toString());
		}
		else
			Utils.noNetworkAlert(MapNewActivity.this, "No internet connection");
	}
	public class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;Context context;
		ProgressDialog progress;//progress dialog

		PlacesTask(Context context)
		{
			this.context =context;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				System.out.println("Background started");
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){
			Log.i("PARSEEEE:::", "::::"+result);
			if(Utils.haveNetworkConnection(context))
			{
				ParserTaskNew parserTask = new ParserTaskNew(context);
				// Start parsing the Google places in JSON format
				// Invokes the "doInBackground()" method of the class ParseTask
				parserTask.execute(result);
			}
			else
				Utils.noNetworkAlert(context, "No internet connection.");

		}
		/** A method to download json data from url */
		private String downloadUrl(String strUrl) throws IOException{
			String data = "";
			InputStream  iStream = null;
			HttpURLConnection urlConnection = null;
			try{
				URL url = new URL(strUrl);                
				// Creating an http connection to communicate with url 
				urlConnection = (HttpURLConnection) url.openConnection();                

				// Connecting to url 
				urlConnection.connect();                

				// Reading data from url 
				iStream = urlConnection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

				StringBuffer sb  = new StringBuffer();

				String line = "";
				while( ( line = br.readLine())  != null){
					sb.append(line);
				}

				data = sb.toString();

				br.close();

			}catch(Exception e){
				Log.d("Exception while downloading url", e.toString());
			}finally{
				iStream.close();
				urlConnection.disconnect();
			}

			return data;
		}         

	}
	public class ParserTaskNew  extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject; Context context;ProgressDialog progress;//progress dialog
		MarkerOptions markerOptions ;
		int thrKmCount=0;

		ParserTaskNew(Context context)
		{
			this.context =context ;
		}

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsonData) {
			List<HashMap<String, String>> places = null;			
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try{
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);

			}catch(Exception e){
				Log.d("Exception",e.toString());
			}
			return places;	
			}
		
		// Executed after the complete execution of doInBackground() method
			@Override
			protected void onPostExecute(List<HashMap<String,String>> list){	
				for(int i=0;i<list.size();i++){

					// Creating a marker
					 markerOptions = new MarkerOptions();

					// Getting a place from the places list
					HashMap<String, String> hmPlace = list.get(i);

					// Getting latitude of the place
					double lat = Double.parseDouble(hmPlace.get("lat"));	            

					// Getting longitude of the place
					double lng = Double.parseDouble(hmPlace.get("lng"));


					// Getting name
					String name = hmPlace.get("place_name");

					// Getting vicinity
					String vicinity = hmPlace.get("vicinity");
					String string[] = vicinity.split(",");

					LatLng latLng = new LatLng(lat, lng);

					latLongArrayList.add(latLng);
					titleArraylist.add(""+name);
					vicinityArrayList.add(""+vicinity);
					if(string.length>1)
						placeArrayList.add(""+string[string.length-2]);
					else if(string.length<=1)
						placeArrayList.add(""+string[string.length-1]);


					// Setting the position for the marker
					markerOptions.position(latLng).title(name);

					// Setting the title for the marker. 
					//This will be displayed on taping the marker
					//markerOptions.title(name + " : " + vicinity);	            
					mGoogleMap.addMarker(markerOptions);
					
					System.out.println("Addiomg marker postionst");
				}
				
			
				mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo((float) 14.4));
				MarkerOptions Options = new MarkerOptions();
				Options.position(latLng);
				Options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_blue));
				mGoogleMap.addMarker(Options);
			

			}
	}
	
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

		mLatitude = arg0.getLatitude();
		mLongitude = arg0.getLongitude();	
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


}
