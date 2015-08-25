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
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;


public class Home extends Fragment {
	
	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String urlnow = "http://akrmhrjn.byethost32.com/cinetvbox_api/gettypemovie_api.php";
	private static String urlupcom = "http://akrmhrjn.byethost32.com/cinetvbox_api/getupcommovie_api.php";
	
	//private static String urlnow = "http://192.168.10.102/cinetvbox_api/gettypemovie_api.php";
	//private static String urlupcom = "http://192.168.10.102/cinetvbox_api/getupcommovie_api.php";
	
	// JSON Node names
	private static final String TAG_MOVIE = "movies";
	private static final String TAG_MID = "mid";
	private static final String TAG_MNAME = "moviename";
	private static final String TAG_IMG = "image";
	private static final String TAG_RATE = "avgrate";
	private static final String TAG_TOTALTHUMB = "totalcount";

	
	  // contacts JSONArray
    JSONArray movies = null;
    JSONArray movies2 = null;
    
    ArrayList movielist,movielist2;
	String uid,mid;
	
	GridView gridView1;
	GridView gridView2;
	Button now;
	Button next;
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		uid = getArguments().getString("uid");
		
		gridView1=(GridView)getView().findViewById(R.id.gridViewNowShowing);
		gridView2=(GridView)getView().findViewById(R.id.gridViewNext);
		gridView2=(GridView)getView().findViewById(R.id.gridViewNext);
		movielist = new ArrayList();
		movielist2 = new ArrayList();
		now =(Button)getView().findViewById(R.id.btn_more);
		next =(Button)getView().findViewById(R.id.btn_more2);
				   
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBunle ){
		View view = inflater.inflate(R.layout.homegrid, container, false);
		new GetMovie().execute();
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
 			
         // Building Parameters
 			List<NameValuePair> nparams = new ArrayList<NameValuePair>();
 			nparams.add(new BasicNameValuePair("type", "Now Showing"));
 			
 		// Building Parameters
 			List<NameValuePair> params = new ArrayList<NameValuePair>();
 			params.add(new BasicNameValuePair("type", "Next Releases"));
 			
 			
             // Making a request to url and getting response
             String jsonStr = jp.makeServiceCall(urlnow,ServiceHandler.POST,nparams);
             String jsonStr2 = jp.makeServiceCall(urlupcom,ServiceHandler.POST,params);
 
            Log.d("Response: ", "> " + jsonStr + jsonStr2);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                
                    // Getting JSON Array node
                    movies = jsonObj.getJSONArray(TAG_MOVIE);
 
                    // looping through All Users
                    for (int i = 0; i < 2; i++) {
                        JSONObject c = movies.getJSONObject(i);
                         
                        mid = c.getString(TAG_MID);
                        String cname = c.getString(TAG_MNAME);
                        String movimg = c.getString(TAG_IMG);
                        String rate = c.getString(TAG_RATE);         
                        MovieClass movieData = new MovieClass();
                        movieData.setMid(mid);
                		movieData.setHeadline(cname);
                		movieData.setUrl("http://akrmhrjn.byethost32.com/cinetvbox_api/img/"+movimg);
                		movieData.setRate(rate);
                		movielist.add(movieData);
                       
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            if (jsonStr2 != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr2);
                
                    // Getting JSON Array node
                    movies2 = jsonObj.getJSONArray(TAG_MOVIE);
 
                    // looping through All Users
                    for (int i = 0; i < 2; i++) {
                        JSONObject c = movies2.getJSONObject(i);
                         
                        mid = c.getString(TAG_MID);
                        String cname = c.getString(TAG_MNAME);
                        String movimg = c.getString(TAG_IMG);
                        String thumb = c.getString(TAG_TOTALTHUMB);         
                        MovieClass movieData = new MovieClass();
                        movieData.setMid(mid);
                		movieData.setHeadline(cname);
                		movieData.setUrl("http://akrmhrjn.byethost32.com/cinetvbox_api/img/"+movimg);
                		movieData.setThumb(thumb);
                		movielist2.add(movieData);
                       
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
        
		gridView1.setAdapter(new NowCustomGridAdapter(getActivity(), movielist));
		
		gridView1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = gridView1.getItemAtPosition(position);
				MovieClass movieData = (MovieClass) o;
	
				Intent i = new Intent(getActivity(),movie_detail.class);
				i.putExtra("uid", uid);
				i.putExtra("mid", movieData.getMid());
				startActivity(i);
				
			}
		});	
		gridView2.setAdapter(new CustomGridAdapter(getActivity(), movielist2));
		
		gridView2.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = gridView2.getItemAtPosition(position);
				MovieClass movieData = (MovieClass) o;
	
				Intent i = new Intent(getActivity(),upcom_movie_detail.class);
				i.putExtra("uid", uid);
				i.putExtra("mid", movieData.getMid());
				startActivity(i);
				
			}
		});
		
		now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
		    	bundle.putString("uid", uid);
				Fragment fragment = null;
				fragment = new NowShowing();
				fragment.setArguments(bundle);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.navigationdraweractivity_framelayout_contentframe, fragment).commit();
				}
				
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
		    	bundle.putString("uid", uid);
				Fragment fragment = null;
				fragment = new NextReleases();
				fragment.setArguments(bundle);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.navigationdraweractivity_framelayout_contentframe, fragment).commit();
				}
			}
		});
		
		
	}
		
    }

}
