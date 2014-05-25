package ua.ifntung.parkulab.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditItemsActivity extends Activity {

	EditText txtName, txtLocation, txtDesc, txtINumber;
	Spinner txtCategory;
	Button btnSave, btnDelete, btnAddPhoto, btnCancel;

	String id;

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	private static final String url_item_detials = "http://slaytmc.esy.es/get_item_details.php";
	private static String url_all_categories = "http://slaytmc.esy.es/get_all_categories.php";
	private static final String url_update_item = "http://slaytmc.esy.es/update_item.php";

	

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_INUMBER = "inumber";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CATEGORIES = "category";
	private static final String TAG_CATEGORY_NAME = "catname";
	JSONArray categories = null;
	public ArrayList<String> catList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnAddPhoto = (Button) findViewById(R.id.btnAddPhoto);
		txtCategory = (Spinner) findViewById(R.id.inputCategoryE);

		Intent i = getIntent();

		id = i.getStringExtra(TAG_ID);
new LoadAllCategories().execute();

catList.add(TAG_CATEGORY_NAME);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>

		(this, android.R.layout.simple_spinner_item, catList);

		txtCategory.setAdapter(dataAdapter);

	txtCategory.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
				return false;
			}
		});
	new GetItemDetails().execute();
	

		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new SaveItemDetails().execute();
			}
		});



		btnAddPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditItemsActivity.this,
						UploadPhotoActivity.class);
				intent.putExtra(TAG_ID, id);
				startActivity(intent);

			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	class GetItemDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditItemsActivity.this);
			pDialog.setMessage("Завантажуємо деталі продукту...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String[] params) {

			runOnUiThread(new Runnable() {
				public void run() {
					int success;
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id", id));

						JSONObject json = jsonParser.makeHttpRequest(
								url_item_detials, "GET", params);

						Log.d("Деталі предмету ", json.toString());

						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							JSONArray itemObj = json.getJSONArray(TAG_ITEM);

							JSONObject item = itemObj.getJSONObject(0);

							txtName = (EditText) findViewById(R.id.inputNameE);
							txtLocation = (EditText) findViewById(R.id.inputLocationE);
							txtINumber = (EditText) findViewById(R.id.inputINumberE);
							txtDesc = (EditText) findViewById(R.id.inputDescE);
							
							txtName.setText(item.getString(TAG_NAME));
							txtLocation.setText(item.getString(TAG_LOCATION));
							txtINumber.setText(item.getString(TAG_INUMBER));
							txtDesc.setText(item.getString(TAG_DESCRIPTION));

							ArrayAdapter myAdap = (ArrayAdapter) txtCategory
									.getAdapter();

							int spinnerPosition = myAdap
									.getPosition(item.getString(TAG_CATEGORY_NAME));
							
							txtCategory.setSelection(spinnerPosition);

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
		}
	}

	class SaveItemDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditItemsActivity.this);
			pDialog.setMessage("Зберігаємо предмет ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String[] args) {

			String name = txtName.getText().toString();
			String location = txtLocation.getText().toString();
			String inumber = txtINumber.getText().toString();
			String description = txtDesc.getText().toString();
			String categoryName = txtCategory.getSelectedItem().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_ID, id));

			try {
				params.add(new BasicNameValuePair(TAG_NAME, URLEncoder.encode(
						name, "UTF-8")));
				params.add(new BasicNameValuePair(TAG_LOCATION, URLEncoder
						.encode(location, "UTF-8")));
				params.add(new BasicNameValuePair(TAG_INUMBER, URLEncoder
						.encode(inumber, "UTF-8")));
				params.add(new BasicNameValuePair(TAG_DESCRIPTION, URLEncoder
						.encode(description, "UTF-8")));
				params.add(new BasicNameValuePair(TAG_CATEGORY_NAME, URLEncoder
						.encode(categoryName, "UTF-8")));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			JSONObject json = jsonParser.makeHttpRequest(url_update_item,
					"POST", params);

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
					
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}

	

	class LoadAllCategories extends
			AsyncTask<String, String, ArrayList<String>> {

		protected ArrayList<String> doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(url_all_categories,
					"GET", params);

			Log.d("Всі предмети: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					categories = json.getJSONArray(TAG_CATEGORIES);

					for (int i = 0; i < categories.length(); i++) {
						JSONObject c = categories.getJSONObject(i);

						String name = c.getString(TAG_CATEGORY_NAME);
						catList.add(name);

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return catList;

		}

	}

}
