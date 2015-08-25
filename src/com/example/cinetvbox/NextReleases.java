package com.example.cinetvbox;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NextReleases extends Fragment {
	
	// Progress Dialog
		public ProgressDialog pDialog;


		// url to login
		private static String url = "http://akrmhrjn.byethost32.com/cinetvbox_api/getupcomtypemovie_api.php";
		private static String urlcat = "http://akrmhrjn.byethost32.com/cinetvbox_api/getupcomcategorymovie_api.php";
		
		//private static String url = "http://192.168.10.102/cinetvbox_api/getupcomtypemovie_api.php";
		//private static String urlcat = "http://192.168.10.102/cinetvbox_api/getupcomcategorymovie_api.php";
		// JSON Node names
		private static final String TAG_MOVIE = "movies";
		private static final String TAG_MID = "mid";
		private static final String TAG_MNAME = "moviename";
		private static final String TAG_IMG = "image";
		private static final String TAG_TOTALTHUMB = "totalcount";

		
		  // contacts JSONArray
	    JSONArray movies = null;
	    
	    ArrayList movielist;
		String uid,mid;
		
		BaseAdapter adapter;
		
		GridView gridView;
		TextView pagename;
		
		Spinner category;
		private String[] list = {"All","Bollywood","Hollywood","Kollywood","Action","Comedy" };
		String cat;
	    
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			uid = getArguments().getString("uid");
			//cat = getArguments().getString("category");
			
			pagename= (TextView)getView().findViewById(R.id.txtpagename);
			pagename.setText("NEXT RELEASES");
				
			


			
			gridView=(GridView)getView().findViewById(R.id.gridViewCustom);
			movielist = new ArrayList();
			
			
			category =(Spinner)getView().findViewById(R.id.spinner);
	        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
			  adapter_state.setDropDownViewResource(R.layout.spinner_item);
			  category.setAdapter(adapter_state);
			  
			  category.setOnItemSelectedListener(new OnItemSelectedListener() {
				 public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
					  category.setSelection(position);
					  cat = (String) category.getSelectedItem();
					  movielist.clear();
					  new GetMovie().execute();
					
					 }
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
	          
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBunle ){
			View view = inflater.inflate(R.layout.upcoming, container, false);
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
				pDialog.setCancelable(true);
				pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	
	        	
	            // Creating service handler class instance
	            ServiceHandler jp = new ServiceHandler();	
	            
	         // Building Parameters
	 			List<NameValuePair> params = new ArrayList<NameValuePair>();
	 			params.add(new BasicNameValuePair("type", "Next Releases"));
	 			params.add(new BasicNameValuePair("category",cat));
	 			
	             // Making a request to url and getting response
	             String jsonStr;
	             
	             if(cat.equals("All"))
				  {
	            	 jsonStr= jp.makeServiceCall(url,ServiceHandler.POST,params);
				  }
				  else
				  {
					  jsonStr= jp.makeServiceCall(urlcat,ServiceHandler.POST,params);
				  }
	 
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
	                        String thumb = c.getString(TAG_TOTALTHUMB);   
	                        if(thumb=="")
	                        	thumb="0";
	                        MovieClass movieData = new MovieClass();
	                        movieData.setMid(mid);
	                		movieData.setHeadline(cname);
	                		movieData.setUrl("http://akrmhrjn.byethost32.com/cinetvbox_api/img/"+movimg);
	                		movieData.setThumb(thumb);
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
		        
		
		        adapter = new CustomGridAdapter(getActivity(), movielist);
				gridView.setAdapter(adapter);
				
				gridView.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						Object o = gridView.getItemAtPosition(position);
						MovieClass movieData = (MovieClass) o;
			
						Intent i = new Intent(getActivity(),upcom_movie_detail.class);
						i.putExtra("uid", uid);
						i.putExtra("mid", movieData.getMid());
						startActivity(i);
						
					}
				});		
			}
	    }
	    
}
