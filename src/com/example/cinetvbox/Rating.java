package com.example.cinetvbox;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class Rating extends Activity {

	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String url = "http://akrmhrjn.byethost32.com/cinetvbox_api/rating_api.php";
	
	
	//private static String url = "http://192.168.10.102/cinetvbox_api/rating_api.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	
	String mTitle;
	
	 //Edittext
    EditText review;
    RatingBar ratingBar_default;
    TextView showrate;
    Button btnsubmit;
    
    String ratevalue;
    String uid,mid;
   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (null != intent) {
			uid= intent.getStringExtra("uid");	
			mid= intent.getStringExtra("mid");	
		  }
        
        review = (EditText) findViewById(R.id.rating_edittxtreview);
        showrate = (TextView) findViewById(R.id.Rating_txtShowRate);
                
        ratingBar_default = (RatingBar) findViewById(R.id.ratingbar_default);

        //BUTTON RATING FIVE STARS...
        /*final Button btnRate = (Button)findViewById(R.id.Rating_btnRate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { 
            	ratingBar_default.setRating(5);
       	     showrate.setText("Rating: " + String.valueOf(ratingBar_default.getRating()));  
            }
        });
        */
        //RATING FROM RATING_BAR...
        ratingBar_default.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){ 
    	    @Override
    	    public void onRatingChanged(RatingBar ratingBar, float rating,
    	      boolean fromUser) {
    	     // TODO Auto-generated method stub 
    	    	showrate.setText("Rating: " + String.valueOf(rating)); 
    	    	ratevalue = String.valueOf(rating);
    	    }}); 
        
        btnsubmit = (Button) findViewById(R.id.btn_submit);
        
        btnsubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new rate().execute();
			}
		});
        
    }  
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            Intent parentIntent1 = new Intent(this,movie_detail.class);
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
    private class rate extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            pDialog = new ProgressDialog(Rating.this);
			pDialog.setMessage("Submitting your rating..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	
        	
            // Creating service handler class instance
            ServiceHandler jp = new ServiceHandler();
            
            String rv = review.getText().toString();
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", uid));
			params.add(new BasicNameValuePair("mid", mid));
			params.add(new BasicNameValuePair("rate", ratevalue));
			params.add(new BasicNameValuePair("review", rv));
	
            // Making a request to url and getting response
            String jsonStr = jp.makeServiceCall(url,ServiceHandler.POST, params);
			
			
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
            	try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    int success = jsonObj.getInt(TAG_SUCCESS);

    				if (success == 1) {
    					// successfully inserted
    					//Toast.makeText(Register.this, "Successfully registered!!", Toast.LENGTH_LONG).show();
    					Intent i = new Intent(Rating.this, NavigationDrawerActivity.class);
    					i.putExtra("uid", uid);
    					
    					startActivity(i);
    					
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
      
      }
    }
}
