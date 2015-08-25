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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class Login extends Activity {
	
	
	// Progress Dialog
	public ProgressDialog pDialog;


	// url to login
	private static String url = "http://akrmhrjn.byethost32.com/cinetvbox_api/login_api.php";
	
	//private static String url = "http://192.168.10.102/cinetvbox_api/login_api.php";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ID = "uid";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_NAME = "name";
	
	// contacts JSONArray
    JSONArray login = null;
    String uid,uname;
    
    //
    EditText email;
	EditText password;
	Button register;
	Button btnlogin;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);	       	
            
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        
        email=(EditText)findViewById(R.id.login_EdittxtUsername);
		password=(EditText)findViewById(R.id.login_Edittxtpassword);
		register = (Button)findViewById(R.id.btn_register);
		
        btnlogin = (Button)findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new OnClickListener(){
        	@Override
        		
        	public void onClick(View arg0){
        		String pass = password.getText().toString();
	            String e = email.getText().toString();
        		if(pass.equals("")||e.equals(""))
        		{
        			Toast.makeText(Login.this, "Invalid Login!!", Toast.LENGTH_LONG).show();
        		}
        		else{       				
        			new GetLogin().execute();
        		}  				
        		
        	}
       });
        
        register.setOnClickListener(new OnClickListener(){
        	@Override
        		
        	public void onClick(View arg0){
        		       				
        		Intent i = new Intent(Login.this, Register.class);
				startActivity(i);
        	}
       });
            	            	

	}
	
	/**
	  * Async task class to get json by making HTTP call
	     * */
	    private class GetLogin extends AsyncTask<Void, Void, Void> {
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(Login.this);
				pDialog.setMessage("Logging In..");
				//pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	
	        	
	            // Creating service handler class instance
	            ServiceHandler jp = new ServiceHandler();
	 
	            
	            String em = email.getText().toString();
				String pass = password.getText().toString();
				
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", em));
				params.add(new BasicNameValuePair("password", pass));
				
	            // Making a request to url and getting response
	            String jsonStr = jp.makeServiceCall(url,ServiceHandler.POST, params);
	 
	            Log.d("Response: ", "> " + jsonStr);
	 
	            
	           if (jsonStr != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	                    
	                    
	                    int success = jsonObj.getInt(TAG_SUCCESS);
	                               
	                    // Getting JSON Array node
	                    login = jsonObj.getJSONArray(TAG_LOGIN);
	
	                    // looping through All Users
	                    for (int i = 0; i < login.length(); i++) {
	           
	                    	JSONObject c = login.getJSONObject(i);
	                    	uid = c.getString(TAG_ID);
	                    	uname = c.getString(TAG_NAME);
	                    }
	                    
	                    Log.d("aaloo:",uid);
	    				if (success == 1) {
	    					// successfully created product
	    					Intent i = new Intent(Login.this, NavigationDrawerActivity.class);
	    					i.putExtra("uid", uid);
	    					i.putExtra("uname", uname);
	    					startActivity(i);
	    					 //Toast.makeText(getActivity().getBaseContext(), "Success!!", Toast.LENGTH_LONG).show(); 
	    					
	    					// closing this screen
	    					finish();
	    				} 
	    				else if (success == 0)
	    					Toast.makeText(Login.this, "Invalid Login!!", Toast.LENGTH_LONG).show();
	    				else {
	    					// failed to create product
	    					
	    					
	    				}
	    					
	    			} catch (JSONException e) {
	    				e.printStackTrace();
	    			}
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
	
