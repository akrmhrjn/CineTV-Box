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
import android.widget.Toast;

public class Register extends Activity {

	// Progress Dialog
		public ProgressDialog pDialog;


		// url to login
		private static String url = "http://akrmhrjn.byethost32.com/cinetvbox_api/register_api.php";
		//private static String url = "http://192.168.10.102/cinetvbox_api/register_api.php";
		
		// JSON Node names
		private static final String TAG_SUCCESS = "success";
		

	 
		String mTitle;
		
		 //Edittext
	    EditText name;
	    EditText phone;
	    EditText password;
	    EditText confirmpassword;
	    EditText email;
	    Button register;
	    
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.register);	       	
	            // TODO Auto-generated constructor stub
	        mTitle="Register to CineTV Box";
	        getActionBar().setTitle(mTitle);
	        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        
	        register = (Button)findViewById(R.id.btn_register);
	        name=(EditText)findViewById(R.id.Register_Edittxtname);
	        password=(EditText)findViewById(R.id.Register_Edittxtpassword);      	
        	confirmpassword=(EditText)findViewById(R.id.Register_Edittxtconfirmpassword);
			email=(EditText)findViewById(R.id.Register_Edittxtemail);
			phone=(EditText)findViewById(R.id.Register_Edittxtphone);
	        
	        register.setOnClickListener(new OnClickListener(){
        	@Override
        		
        	public void onClick(View arg0){   	
            		String pass = password.getText().toString();
            		String confirmpass = confirmpassword.getText().toString();
      	            String p = phone.getText().toString();
    	            String e = email.getText().toString();
            		if(pass.equals("")||confirmpass.equals("")||p.equals("")||e.equals(""))
            		{
            			Toast.makeText(Register.this, "Please fill all the fields.", Toast.LENGTH_LONG).show();
            		}
            		else
            		{
            			if (pass.equals(confirmpass))
                		{
                			new registerUser().execute(); 
                		}
                		else
                		{
        					Toast.makeText(Register.this, "Password didnot match!!", Toast.LENGTH_LONG).show();
                		}           		      		        		       				

            		}
            		    	           	
        	
        	}
	       });
	            	            	
	
		}
		
		/**
	     * Async task class to get json by making HTTP call
	     * */
	    private class registerUser extends AsyncTask<Void, Void, Void> {
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	            pDialog = new ProgressDialog(Register.this);
				pDialog.setMessage("Registering your account..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	        	
	        	
	            // Creating service handler class instance
	            ServiceHandler jp = new ServiceHandler();
	            
	            String nm = name.getText().toString();
	            String ph = phone.getText().toString();
	            String pass = password.getText().toString();
	            String em = email.getText().toString();
				
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", nm));
				params.add(new BasicNameValuePair("phone", ph));
				params.add(new BasicNameValuePair("email", em ));
				params.add(new BasicNameValuePair("password", pass));
				
				
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
	    					Intent i = new Intent(Register.this, Login.class);
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
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	            Intent parentIntent1 = new Intent(this,Login.class);
	          	  startActivity(parentIntent1);
	          	  return true;
	               
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }

}
