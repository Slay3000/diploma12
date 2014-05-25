package ua.ifntung.parkulab.activity;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.activity.ShowItemActivity.GetImg;
import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

	Button btnViewProducts,btnNewProduct,btnShowImg,btnF,btnQR;
	
	//this is for checklogpass
	JSONParser jsonParser = new JSONParser();
	SharedPreferences settings;
	String login,password;
	private static String url_check_logpass = "http://slaytmc.esy.es/check_logpass.php";
	private static final String TAG_SUCCESS = "success";
	public static final String APP_PREFERENCES = "logpass"; 
	private static final String TAG_LOGIN = "login";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_PERMISSION = "permission";
	int success = 0;

	//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		btnViewProducts = (Button) findViewById(R.id.btnViewItems);
		btnNewProduct = (Button) findViewById(R.id.btnCreateItem);
		btnShowImg = (Button) findViewById(R.id.btnShowImage);
		btnF = (Button) findViewById(R.id.btnCategory);
		btnQR=(Button) findViewById(R.id.btnQR);
		

	
		btnViewProducts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						AllItemsActivity.class);
				startActivity(i);

			}
		});

		btnNewProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						NewItemActivity.class);
				startActivity(i);

			}
		});
		btnShowImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				
				startActivity(intent);

			}
		});
		btnF.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				settings=getSharedPreferences(APP_PREFERENCES, 0);	
				login=settings.getString(TAG_LOGIN, "");
				password=settings.getString(TAG_PASSWORD, "");
				success=0;
				new CheckLogPass().execute();
				}
		});
		btnQR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), GetItemByQR.class);
				
				startActivity(intent);

			}
		});
	}
	
	class CheckLogPass extends AsyncTask<String,String, String> {
		ProgressDialog	pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		pDialog = new ProgressDialog(MainScreenActivity.this);
			pDialog.setMessage("Отримуємо інформацію про предмет...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair(TAG_LOGIN,login));
						params.add(new BasicNameValuePair(TAG_PASSWORD, password));
						params.add(new BasicNameValuePair(TAG_PERMISSION,"admin"));
						JSONObject json = jsonParser.makeHttpRequest(
								url_check_logpass, "POST", params);

						Log.d("Результат: ", json.toString());

						
							try {
								success = json.getInt(TAG_SUCCESS);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						


			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread( new Runnable() {
				public void run() {
					if ((login=="") ||  (password=="")){
				 		Toast.makeText(getApplicationContext(), "no logpass", Toast.LENGTH_SHORT).show();
					}
					else {

		if (success==1){
				Intent intent = new Intent(getApplicationContext(), AllCategoriesActivity.class);
				
				startActivity(intent);
			}
		
			else {
			 		 		Toast.makeText(getApplicationContext(), "wrong logpass", Toast.LENGTH_SHORT).show();
					
			}

				}		
				
				
				
				}
			});
		
		}
	

	}
}
