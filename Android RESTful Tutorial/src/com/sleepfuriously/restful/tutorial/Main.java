//	main.java
//
//	This is a tutorial for RESTful stuff with an
//	Android.
//
//		http://www.techrepublic.com/blog/app-builder/calling-restful-services-from-your-android-app/1076
//
package com.sleepfuriously.restful.tutorial;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity 
					implements OnClickListener {

	private static final String tag = "Main";
	protected Button get_butt;
	
	//----------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        get_butt = (Button) findViewById(R.id.button1);
        get_butt.setOnClickListener(this);
    }

	//----------------
	public void onClick(View v) {
		Log.i(tag, "click!");
//		Button b = (Button)findViewById(R.id.button1);
		get_butt.setClickable(false);
		get_butt.setEnabled(false);
		new LongRunningGetIO().execute();
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private class LongRunningGetIO 
				extends AsyncTask <Void, Void, String> {

		/*********************
		 * Looks like it takes an HttpEntity and turns it into
		 * a string.
		 * 
		 * @param entity
		 * @return
		 * @throws IllegalStateException
		 * @throws IOException
		 */
		protected String getASCIIContentFromEntity(HttpEntity entity) 
				throws IllegalStateException, IOException {
			Log.i(tag, "entering getASCIIContentFromEntity()");
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		//--------------------
		@Override
		protected String doInBackground(Void... params) {
			Log.i(tag, "begin doInBackground()");
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://www.cheesejedi.com/rest_services/get_big_cheese.php?puzzle=1");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}
		protected void onPostExecute(String results) {
			Log.i(tag, "begin onPostExecute()");
			if (results!=null) {
				EditText et = (EditText)findViewById(R.id.editText1);
				et.setText(results);
			}
			get_butt.setClickable(true);
			get_butt.setEnabled(true);
//			Button b = (Button)findViewById(R.id.button1);
//			b.setClickable(true);
		}  
	}
}
