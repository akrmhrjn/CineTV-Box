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
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class movie_detail extends Activity {
	
	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String url = "http://akrmhrjn.byethost32.com/cinetvbox_api/getsinglemovie_api.php";
	private static String urlrating = "http://akrmhrjn.byethost32.com/cinetvbox_api/getsingleuserrating_api.php";

	//private static String url = "http://192.168.10.102/cinetvbox_api/getsinglemovie_api.php";
	//private static String urlrating = "http://192.168.10.102/cinetvbox_api/getsingleuserrating_api.php";
	
	// JSON Node names
	private static final String TAG_MOVIE = "movies";
	private static final String TAG_MID = "mid";
	private static final String TAG_MNAME = "moviename";
	private static final String TAG_IMG = "image";
	private static final String TAG_RATE = "avgrate";
	private static final String TAG_DESC = "description";
	private static final String TAG_GEN = "regen";
	private static final String TAG_RELEASE = "release";
	private static final String TAG_LANG = "language";
	private static final String TAG_MSG = "message";
	private static final String TAG_URATE = "yourrate";

	
	  // contacts JSONArray
    JSONArray movies = null;
    
    ArrayList movielist;
	String uid,mid;
	
	String mname,movimg,rate,gen,des,rel,lang,message,urate; 
	
	Button rateitnow;
	TextView title;
	TextView genre;
	TextView currate;
	TextView description,raterr,redate,langu,yourrate,yourratetitle;
	double r;
	ImageView mimg;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.movie_detail);
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
	        rateitnow = (Button) findViewById(R.id.btn_review);
	        langu = (TextView) findViewById(R.id.txt_language);
	        redate = (TextView) findViewById(R.id.txt_releasedate);
	        raterr = (TextView) findViewById(R.id.txt_rater);
	        yourrate = (TextView) findViewById(R.id.txt_yourrating);
	        yourratetitle = (TextView) findViewById(R.id.yourrate);
	        
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
	            
	            pDialog = new ProgressDialog(movie_detail.this);
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
	             String jsonStr = jp.makeServiceCall(url,ServiceHandler.POST,params);
	             String jsonStr2 = jp.makeServiceCall(urlrating,ServiceHandler.POST,params);
	            Log.d("Response: ", "> " + jsonStr + jsonStr2);
	            
	            if (jsonStr != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	                
	                    // Getting JSON Array node
	                    movies = jsonObj.getJSONArray(TAG_MOVIE);
	 
	                    // looping through All Users
	                    for (int i = 0; i < movies.length(); i++) {
	                        JSONObject c = movies.getJSONObject(i);
	                         
	                        mid = c.getString(TAG_MID);
	                        mname = c.getString(TAG_MNAME);
	                        movimg = c.getString(TAG_IMG);
	                        rate = c.getString(TAG_RATE);
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
	            if (jsonStr2 != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr2);
	                
	                    // Getting JSON Array node
	                    message = jsonObj.getString(TAG_MSG);
	                    urate = jsonObj.getString(TAG_URATE);
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
                	rateitnow.setVisibility(View.INVISIBLE);
                	yourrate.setVisibility(View.VISIBLE);
                	yourratetitle.setVisibility(View.VISIBLE);
                	yourrate.setText(urate);
                }
		        title.setText(mname);
                genre.setText(gen);
                currate.setText(rate);
                description.setText(des);
                langu.setText(lang);
                redate.setText(rel);
                r= Double.parseDouble(rate);
                if(r>=0&&r<0.5)
                	raterr.setText("Unbearable");
                else if(r>=0.5&&r<1)
                	raterr.setText("Give it a miss");
                else if(r>=1&&r<1.5)
                	raterr.setText("Watch it drunk");
                else if(r>=1.5&&r<2)
                	raterr.setText("Boring");
                else if(r>=2&&r<2.5)
                	raterr.setText("Watchable");
                else if(r>=2.5&&r<3)
                	raterr.setText("Average");
                else if(r>=3&&r<3.5)
                	raterr.setText("Above Average");
                else if(r>=3.5&&r<4)
                	raterr.setText("Good");
                else if(r>=4&&r<4.5)
                	raterr.setText("Amazing");
                else
                    raterr.setText("Must see");
                
                if (mimg != null) {
        			new ImageDownloaderTask(mimg).execute("http://192.168.10.102/cinetvbox_api/img/"+movimg);
        		}
		      
		        rateitnow.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(movie_detail.this,Rating.class);
						i.putExtra("uid", uid);
						i.putExtra("mid", mid);
						startActivity(i);
					}
				});
				
			}
		 }
		
}