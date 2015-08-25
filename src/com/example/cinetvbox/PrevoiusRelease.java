package com.example.cinetvbox;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


public class PrevoiusRelease extends Fragment {
	
	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String url = "http://192.168.1.108/cinetvbox_api/getmovie_api.php";
	
	// JSON Node names
	private static final String TAG_MOVIE = "movies";
	private static final String TAG_MID = "mid";
	private static final String TAG_MNAME = "moviename";
	private static final String TAG_IMG = "image";
	private static final String TAG_RATE = "avgrate";

	
	  // contacts JSONArray
    JSONArray movies = null;
    
    ArrayList movielist;
	String uid,mid;
	
	GridView gridView;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		uid = getArguments().getString("uid");
		
		gridView=(GridView)getView().findViewById(R.id.gridViewCustom);
		movielist = new ArrayList();
		
		new GetMovie().execute();
		
		   
           // Create the Custom Adapter Object
          /* gridViewCustomeAdapter = new GridViewCustomAdapter(getActivity());
           // Set the Adapter to GridView
           gridView.setAdapter(gridViewCustomeAdapter);
             
           // Handling touch/click Event on GridView Item
             gridView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                  String selectedItem;
                  if(position%2==0)
                      selectedItem="Facebook";
                  else
                      selectedItem="Twitter";
               Toast.makeText(getActivity().getApplicationContext(),"Selected Item: "+selectedItem, Toast.LENGTH_SHORT).show();
               
              }
             });*/
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBunle ){
		View view = inflater.inflate(R.layout.homegrid, container, false);
		return view;
		
	}
	
	/**
     * Async task class to get json by making HTTP call
     * */
    private class GetMovie extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please Wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	
        	
            // Creating service handler class instance
            ServiceHandler jp = new ServiceHandler();			
 			
             // Making a request to url and getting response
             String jsonStr = jp.makeServiceCall(url,ServiceHandler.POST);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                
                    // Getting JSON Array node
                    movies = jsonObj.getJSONArray(TAG_MOVIE);
 
                    // looping through All Users
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);
                         
                        mid = c.getString(TAG_MID);
                        String cname = c.getString(TAG_MNAME);
                        String movimg = c.getString(TAG_IMG);
                        String rate = c.getString(TAG_RATE);         
                        MovieClass movieData = new MovieClass();
                        movieData.setMid(mid);
                		movieData.setHeadline(cname);
                		movieData.setUrl("http://192.168.1.108/cinetvbox_api/img/"+movimg);
                		movieData.setRate(rate);
                		movielist.add(movieData);
                       
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 			
            return null;
        }

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	
	@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //noticelists.clearChoices();
        
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /**
         * Updating parsed JSON data into ListView
         * */
        
		gridView.setAdapter(new CustomGridAdapter(getActivity(), movielist));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = gridView.getItemAtPosition(position);
				MovieClass movieData = (MovieClass) o;
				/*Toast.makeText(getActivity(), "Selected :" + " " + movieData.getMid(),
						Toast.LENGTH_LONG).show();*/
				
				Intent i = new Intent(getActivity(),movie_detail.class);
				i.putExtra("uid", uid);
				i.putExtra("mid", movieData.getMid());
				startActivity(i);
				
			}
		});
		
		
	}
		
    }

}
