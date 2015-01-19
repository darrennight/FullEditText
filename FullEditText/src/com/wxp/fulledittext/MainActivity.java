package com.wxp.fulledittext;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	FullText fullText;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fullText = (FullText) findViewById(R.id.id_edit_main_et);
		textView = (TextView) findViewById(R.id.id_edit_main_tv);
		String url = "http://1.astippp.sinaapp.com/all.php?rows=0";
		RequestQueue mQueue = Volley.newRequestQueue(this);
		/*
		 * JsonObjectRequest jsonObjectRequest = new
		 * JsonObjectRequest("http://1.astippp.sinaapp.com/all.php?rows=1",
		 * null, new Response.Listener<JSONObject>() {
		 * 
		 * @Override public void onResponse(JSONObject response) { Log.d("wxp",
		 * response.toString());
		 * fullText.getEditableText().append(response.toString()); Log.d("wxp",
		 * "fullText:"+fullText.getText().toString());
		 * 
		 * } }, new Response.ErrorListener() {
		 * 
		 * @Override public void onErrorResponse(VolleyError error) {
		 * Log.e("wxp", error.getMessage(), error); } });
		 */
		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("wxp", response);
						mHandler.obtainMessage(0, response.toString())
								.sendToTarget();

						//
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("wxp", error.getMessage(), error);
					}
				});
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						Log.d("wxp", response.toString());
						mHandler.obtainMessage(0, response.toString())
						.sendToTarget();
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("wxp", error.getMessage(), error);
					}
				});

		mQueue.add(jsonArrayRequest);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// mHandler.obtainMessage(0, (String)msg.obj).sendToTarget();
			textView.setText(msg.obj.toString());
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
