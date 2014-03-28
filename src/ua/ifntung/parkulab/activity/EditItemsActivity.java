package ua.ifntung.parkulab.activity;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemsActivity extends Activity {

	EditText txtName, txtLocation, txtDesc, txtINumber;
	Button btnSave, btnDelete,btnAddPhoto, btnCancel;

	String id;

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	private static final String url_item_detials = "http://slaytmc.esy.es/get_item_details.php";

	private static final String url_update_item = "http://slaytmc.esy.es/update_item.php";

	private static final String url_delete_item = "http://slaytmc.esy.es/delete_item.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEM = "item";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_INUMBER = "inumber";
	private static final String TAG_DESCRIPTION = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnAddPhoto=(Button)findViewById(R.id.btnAddPhoto);
		Intent i = getIntent();

		id = i.getStringExtra(TAG_ID);

		new GetItemDetails().execute();

		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new SaveItemDetails().execute();
			}
		});

		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new DeleteItem().execute();
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
							txtLocation
									.setText(item.getString(TAG_LOCATION));
							txtINumber.setText(item.getString(TAG_INUMBER));
							txtDesc.setText(item.getString(TAG_DESCRIPTION));

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

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_ID, id));
			params.add(new BasicNameValuePair(TAG_NAME, name));
			params.add(new BasicNameValuePair(TAG_LOCATION, location));
			params.add(new BasicNameValuePair(TAG_INUMBER, inumber));
			params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

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

	class DeleteItem extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditItemsActivity.this);
			pDialog.setMessage("Видаляємо предмет...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String[] args) {

			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", id));

				JSONObject json = jsonParser.makeHttpRequest(url_delete_item,
						"POST", params);

				Log.d("Видаляємо предмет", json.toString());

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
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

}
