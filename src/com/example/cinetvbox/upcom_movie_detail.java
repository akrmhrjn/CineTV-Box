package com.example.cinetvbox;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class upcom_movie_detail extends Activity {
	
	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String urlselect = "http://akrmhrjn.byethost32.com/cinetvbox_api/getsingleupcommovie_api.php";
	private static String urlinsert = "http://akrmhrjn.byethost32.com/cinetvbox_api/thumbsup_api.php";
	private static String urlthumb = "http://akrmhrjn.byethost32.com/cinetvbox_api/getsinglethumbrating_api.php";
	//private static String url = "http://192.168.10.101/cinetvbox_api/getsinglemovie_ap`i.php";
	
	//private static String urlselect = "http://192.168.10.102/cinetvbox_api/getsingleupcommovie_api.php";
	//private static String urlinsert = "http://192.168.10.102/cinetvbox_api/thumbsup_api.php";
	//private static String urlthumb = "http://192.168.10.102/cinetvbox_api/getsinglethumbrating_api.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MOVIE = "movies";
	private static final String TAG_MID = "mid";
	private static final String TAG_MNAME = "moviename";
	private static final String TAG_IMG = "image";
	private static final String TAG_TOTALTHUMB = "totalcount";
	private static final String TAG_DESC = "description";
	private static final String TAG_GEN = "regen";
	private static final String TAG_RELEASE = "release";
	private static final String TAG_LANG = "language";
	private static final String TAG_MSG = "message";

	
	  // contacts JSONArray
    JSONArray movies = null;
    
    ArrayList movielist;
	String uid,mid;
	
	String mname,movimg,thumb,gen,des,rel,lang,message,urate; 
	
	TextView title;
	TextView genre;
	TextView currate;
	TextView description,raterr,redate,langu,yourrate,totalthumb;
	double r;
	ImageView mimg,thumbup,thumbupfill;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.upcom_movie_detail);
	        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        
	        Intent intent = getIntent();
	        if (null != intent) {
				uid= intent.getStringExtra("uid");	
				mid= intent.getStringExtra("mid");	
			  }
	       
	        
	        title = (TextView) findViewById(R.id.movieName);
	        genre = (TextView) findViewById(R.id.genere_name);
	        currate = (TextView) findViewById(R.id.averageRating);
	        description = (TextView) findViewById(R.id.description_content);
	        mimg = (ImageView) findViewById(R.id.movie_image);
	        //rateitnow = (Button) findViewById(R.id.btn_review);
	        langu = (TextView) findViewById(R.id.txt_language);
	        redate = (TextView) findViewById(R.id.txt_releasedate);
	        raterr = (TextView) findViewById(R.id.txt_rater);
	        totalthumb = (TextView) findViewById(R.id.yourrate);
			thumbup = (ImageView) findViewById(R.id.imgthumb);
			thumbupfill = (ImageView) findViewById(R.id.imgthumbfill);
        
			 
	        new GetMovie().execute();
	        

	 }
	 
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	            Intent parentIntent1 = new Intent(this,NavigationDrawerActivity.class);
	            parentIntent1.putExtra("uid", uid);
				parentIntent1.putExtra("mid", mid);
	          	  startActivity(parentIntent1);
	          	  return true;
	               
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
		
		/**
	     * Async task class to get json by making HTTP call
	     * */
	    private class GetMovie extends AsyncTask<Void, Void, Void> {
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(upcom_movie_detail.this);
				pDialog.setMessage("Loading movie details..");
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
				params.add(new BasicNameValuePair("uid", uid));
				params.add(new BasicNameValuePair("mid", mid));
				
	             // Making a request to url and getting response
	             String jsonStr = jp.makeServiceCall(urlselect,ServiceHandler.POST,params);
	             String jsonStr3 = jp.makeServiceCall(urlthumb,ServiceHandler.POST,params);
	             
	            Log.d("Response: ", "> " + jsonStr);
	            
	            if (jsonStr != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	                
	                    // Getting JSON Array node
	                    movies = jsonObj.getJSONArray(TAG_MOVIE);
	 
	                    // looping through All Users
	                    for (int i = 0; i < movies.length(); i++) {
	                        JSONObject c = movies.getJSONObject(i);
	                         
	                        //mid = c.getString(TAG_MID);
	                        mname = c.getString(TAG_MNAME);
	                        movimg = c.getString(TAG_IMG);
	                        thumb = c.getString(TAG_TOTALTHUMB);
	                        gen = c.getString(TAG_GEN); 
	                        des = c.getString(TAG_DESC);
	                        rel = c.getString(TAG_RELEASE); 
	                        lang = c.getString(TAG_LANG); 
	       
	                       
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                Log.e("ServiceHandler", "Couldn't get any data from the url");
	            }
	            
	            if (jsonStr3 != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr3);
	                
	                    // Getting JSON Array node
	                    message = jsonObj.getString(TAG_MSG);
	                  
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
		     
                if(message.equals("1"))
                {
                	thumbup.setVisibility(View.INVISIBLE);
 					thumbupfill.setVisibility(View.VISIBLE);
                }
                totalthumb.setText(thumb+" wants to see this.");
		        title.setText(mname);
                genre.setText(gen);
                description.setText(des);
                langu.setText(lang);
                redate.setText(rel);
                
                
                if (mimg != null) {
        			new ImageDownloaderTask(mimg).execute("http://akrmhrjn.byethost32.com/cinetvbox_api/img/"+movimg);
        		}
		        thumbup.setOnClickListener(new OnClickListener() {
	 				
	 				@Override
	 				public void onClick(View v) {
	 					// TODO Auto-generated method stub
	 					thumbup.setVisibility(View.INVISIBLE);
	 					thumbupfill.setVisibility(View.VISIBLE);	
	 					
	 					new insertthumb().execute();
	 				}
	 			});
		        thumbupfill.setOnClickListener(new OnClickListener() {
	 				
	 				@Override
	 				public void onClick(View v) {
	 					// TODO Auto-generated method stub
	 					Toast.makeText(upcom_movie_detail.this, "You already gave thumbs up!!", Toast.LENGTH_SHORT).show();
	 				}
	 			});
				
			}
		 }
	    
	    /**
	     * Async task class to get json by making HTTP call
	     * */
	    private class insertthumb extends AsyncTask<Void, Void, Void> {
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(upcom_movie_detail.this);
				pDialog.setMessage("Submitting your thumbs up..");
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
				params.add(new BasicNameValuePair("uid", uid));
				params.add(new BasicNameValuePair("mid", mid));

	            // Making a request to url and getting response
	            String jsonStr = jp.makeServiceCall(urlinsert,ServiceHandler.POST, params);
	            String jsonStr2 = jp.makeServiceCall(urlselect,ServiceHandler.POST, params);
				
				
	            Log.d("Response: ", "> " + jsonStr);
	 
	            if (jsonStr != null) {
	            	try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	                     
	                    int success = jsonObj.getInt(TAG_SUCCESS);

	    				if (success == 1) {
	    					// successfully inserted
	    					//Toast.makeText(upcom_movie_detail.this, "You gave movie thumbs up!!", Toast.LENGTH_LONG).show();
	    					// closing this screen
	    					//finish();
	    				} else {
	    					// failed to create product
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
	                    movies = jsonObj.getJSONArray(TAG_MOVIE);
	 
	                    // looping through All Users
	                    for (int i = 0; i < movies.length(); i++) {
	                        JSONObject c = movies.getJSONObject(i);
	                         
	                        //mid = c.getString(TAG_MID);
	                        mname = c.getString(TAG_MNAME);
	                        movimg = c.getString(TAG_IMG);
	                        thumb = c.getString(TAG_TOTALTHUMB);
	                        gen = c.getString(TAG_GEN); 
	                        des = c.getString(TAG_DESC);
	                        rel = c.getString(TAG_RELEASE); 
	                        lang = c.getString(TAG_LANG); 
	       
	                       
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
	        
	        // Dismiss the progress dialog
	            pDialog.dismiss();
	            
	            if(message.equals("1"))
                {
                	thumbup.setVisibility(View.INVISIBLE);
 					thumbupfill.setVisibility(View.VISIBLE);
                }
                totalthumb.setText(thumb+" wants to see this.");
		        title.setText(mname);
                genre.setText(gen);
                description.setText(des);
                langu.setText(lang);
                redate.setText(rel);
                
                
                if (mimg != null) {
        			new ImageDownloaderTask(mimg).execute("http://akrmhrjn.byethost32.com/cinetvbox_api/img/"+movimg);
        		}
                thumbupfill.setOnClickListener(new OnClickListener() {
	 				
	 				@Override
	 				public void onClick(View v) {
	 					// TODO Auto-generated method stub
	 					Toast.makeText(upcom_movie_detail.this, "You already gave thumbs up!!", Toast.LENGTH_SHORT).show();
	 				}
	 			});
	      
	      }
	    }
		
}