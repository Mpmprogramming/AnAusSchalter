package com.example.anausschalter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	//Set Vars
	TextView disp; //set TextView
	String mHTTPReturn; //set HTTP-return
	Integer mLength;
	//CompoundButton mSwitch1;
	String mStationIP = "192.168.178.22";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        //Init Display
        disp=(TextView)findViewById(R.id.textView1);
        disp.setText("Please wait...");
        funcLightTest(true);         
        	        

    }
    
    
    
  //Power-Test
    public void funcLightTest(final Boolean mShowState) {
    	
    	new HttpHandler() {
            @Override
            public HttpUriRequest getHttpRequestMethod() {
            	//return new HttpGet("http://192.168.178.21/test.txt");
            	return new HttpGet("http://" + mStationIP + "/config/xmlapi/statelist.cgi");
                // return new HttpPost(url)
            }
            @Override
            public void onResponse(String result) {
                //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();                
                mHTTPReturn = result;                
                String[] mSplit = mHTTPReturn.split("ise_id=\'1266\' value=\'");  //ise_id="1266" value="
                mLength = mSplit.length;
                String[] mSplit2 = mSplit[1].split("\'");  //"
                if (mShowState == true) {
	                if (Boolean.parseBoolean(mSplit2[0]) == true){                	 
	                	disp.setText("Status: ON");	
	                }
	                else{
	                	disp.setText("Status: OFF");
	                }
                }                
            }
        }.execute();
        
    }
    
    
    //anschalten
    public void onClickAN (View sender){
    	funcLightTest(false);
    	if (disp.getText() == "Status: OFF"){
    		disp.setText("Please wait ...");	
    		new RequestTask().execute("http://" + mStationIP + "/config/xmlapi/statechange.cgi?ise_id=1266&new_value=true");
    		funcLightTest(true);
    	}
    	else{
    		Toast.makeText(getBaseContext(), "Light is already ON", Toast.LENGTH_LONG).show();
    	}  	
    }
    
    //ausschalten
    public void onClickAUS (View sender){
    	funcLightTest(false);
    	if (disp.getText() == "Status: ON"){
    		disp.setText("Please wait ...");	
    		new RequestTask().execute("http://" + mStationIP + "/config/xmlapi/statechange.cgi?ise_id=1266&new_value=false");
    		funcLightTest(true);
    	}
    	else{
    		Toast.makeText(getBaseContext(), "Light is already OFF", Toast.LENGTH_LONG).show();
    	} 
    }
    
    
    
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
