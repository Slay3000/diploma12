package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.galleryWigdet.GalleryViewPager;
import ua.ifntung.parkulab.galleryWigdet.UrlPagerAdapter;
import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

public class ShowItemActivity extends Activity {
	String qrcode;
	TextView name, location, category, inumber, description;
	private static String url_get_item = "http://slaytmc.esy.es/get_item_by_qr.php";
	private static String url_get_img = "http://slaytmc.esy.es/get_img.php";
	ProgressDialog pDialog;
	String id;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEM = "item";
	private static final String TAG_IMG = "img";
	private static final String TAG_IADDR = "photo_address";
	private static final String TAG_QR = "qrcode";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CATEGORY = "catname";
	private static final String TAG_INUMBER = "inumber";
	private List<String> images = new ArrayList<String>();
	JSONParser jsonParser = new JSONParser();
	JSONArray item = null;
	private GalleryViewPager mViewPager;

	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_item);
		name = (TextView) findViewById(R.id.showName);
		qrcode=getIntent().getExtras().getString("qrcode");
		
new GetItemInfo().execute();



	}

	class GetImg extends AsyncTask<String, String, String> {
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
       /*     pDialog = new ProgressDialog(ShowItemActivity.this);
            pDialog.setMessage("Завантажуємо зображення...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }
		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("id",id));
				JSONObject json = jsonParser.makeHttpRequest(url_get_img,
						"POST", params);

				Log.d("Всі предмети: ", json.toString());

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					JSONArray img = json.getJSONArray(TAG_IMG);
				
					  for (int i = 0; i < img.length(); i++) {
	                        JSONObject c = img.getJSONObject(i);

					JSONObject res = img.getJSONObject(i);
			images.add(getString(R.string.img_server)+res.getString(TAG_IADDR));
					

					  }} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
	//		pDialog.dismiss();
		 runOnUiThread(new Runnable() {
            public void run() {
            	
        		if (images.isEmpty())
        		images.add(getString(R.string.img_server)+"no_img.jpg");
        		
            	UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(ShowItemActivity.this, images);

        		mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        		mViewPager.setOffscreenPageLimit(3);
        		mViewPager.setAdapter(pagerAdapter);

            }
	 }); 
			
		}
		

	}

	class GetItemInfo extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ShowItemActivity.this);
			pDialog.setMessage("Отримуємо інформацію про предмет...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			runOnUiThread(new Runnable() {
				public void run() {

					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("qrcode", qrcode));
						JSONObject json = jsonParser.makeHttpRequest(
								url_get_item, "POST", params);

						Log.d("Всі предмети: ", json.toString());

						int success = json.getInt(TAG_SUCCESS);

						if (success == 1) {
							JSONArray itemObj = json.getJSONArray(TAG_ITEM);

							JSONObject res = itemObj.getJSONObject(0);

							name = (TextView) findViewById(R.id.showName);
							location = (TextView) findViewById(R.id.showLocation);
							category = (TextView) findViewById(R.id.showCategory);
							inumber = (TextView) findViewById(R.id.showINumber);
							description = (TextView) findViewById(R.id.showDesc);
							id = res.getString(TAG_ID);
							name.setText(res.getString(TAG_NAME)+id);
							location.setText(res.getString(TAG_LOCATION));
							category.setText(res.getString(TAG_CATEGORY));
							inumber.setText(res.getString(TAG_INUMBER));
							description.setText(res.getString(TAG_DESCRIPTION));
						} else {
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			new GetImg().execute();
		}

	}
}
