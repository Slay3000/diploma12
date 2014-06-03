package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.Collection;
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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;

public class ImageManageActivity extends Activity {
	AQuery aq=new AQuery(this);
	String id,server;
	Boolean clickable=false;
	  GridView gridView;
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();
	private static String url_get_img_by_id = "get_img.php";
	private static String url_delete_image = "delete_image.php";
	private static final String TAG_IMG = "img";
	private static final String TAG_IADDR = "photo_address";
	ProgressDialog pDialog;
	private List<String> images = new ArrayList<String>();
	private List<String> address = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   id=getIntent().getStringExtra("id");
	   server=getIntent().getStringExtra("server");
	 
	    setContentView(R.layout.image_manage);
	    
	    new GetImg().execute();
	

	  
	}

	public class ImageAdapter extends BaseAdapter 
	{
	    private Context context;

	    public ImageAdapter(Context c) 
	    {
	        context = c;
	    }

	    //---returns the number of images---
	    public int getCount() {
	        return address.size();
	    }

	    //---returns the ID of an item--- 
	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    //---returns an ImageView view---
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        ImageView imageView;
	        if (convertView == null) {
	            imageView = new ImageView(context);
	            imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(5, 5, 5, 5);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        aq.id(imageView).image(address.get(position));
	       
	        return imageView;
	    }
	}    
	class GetImg extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			  pDialog = new ProgressDialog(ImageManageActivity.this);
			 pDialog.setMessage("Завантажуємо зображення...");
			  pDialog.setIndeterminate(false); pDialog.setCancelable(false);
			  pDialog.show();
			
		}

		protected String doInBackground(String... args) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", id));
				JSONObject json = jsonParser.makeHttpRequest(server
						+ url_get_img_by_id, "POST", params);

				Log.d("Всі предмети: ", json.toString());

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					JSONArray img = json.getJSONArray(TAG_IMG);

					for (int i = 0; i < img.length(); i++) {
						JSONObject c = img.getJSONObject(i);

						JSONObject res = img.getJSONObject(i);
						images.add(res.getString(TAG_IADDR));

					}
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {

					if (images.isEmpty())
						address.add(server + "img/" + "no_img.jpg");
					else {
						clickable=true;
for (int i=0;i<images.size();i++)
					address.add(server+"img/"+images.get(i));
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							   gridView = (GridView) findViewById(R.id.gridview);
							    gridView.setAdapter(new ImageAdapter(ImageManageActivity.this));
						}
					});
					  gridView.setOnItemClickListener(new OnItemClickListener() 
					    {
					        public void onItemClick(AdapterView<?> parent, 
					        View v, int position, long id) 
					        {    
					        	if (clickable)
					         new DeleteImage().execute(images.get(position));
					        }
					    });  
				
			pDialog.dismiss();
		}	

	}
	 class DeleteImage extends AsyncTask<String, String, String> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(ImageManageActivity.this);
				pDialog.setMessage("Видаляємо зображення...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			protected String doInBackground(String[] args) {

				int success;
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("photo_address", args[0].toString()));

					JSONObject json = jsonParser.makeHttpRequest(server+url_delete_image,
							"POST", params);

					Log.d("Видаляємо предмет", json.toString());

					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						Intent i = getIntent();
						i.putExtra("server", server);
						finish();
						startActivity(i);
									}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(String file_url) {
				runOnUiThread( new Runnable() {
					public void run() {
						  Intent i = new Intent(ImageManageActivity.this, ImageManageActivity.class);
							i.putExtra("server", server);
							i.putExtra("id", id);
							startActivity(i);
							finish();
					}
				});
				pDialog.dismiss();

			}

		}
}
