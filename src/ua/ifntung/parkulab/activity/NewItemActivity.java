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

import ua.ifntung.parkulab.activity.AllCategoriesActivity.LoadAllCategories;
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

public class NewItemActivity extends Activity {

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputName, inputLocation, inputDesc, inputINumber;
	Spinner spinnerCategory;
String server;
	private static String url_create_item = "create_item.php";
	private static String url_all_categories = "get_all_categories.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_CATEGORIES = "category";
	private static final String TAG_CATEGORY_NAME = "catname";
	JSONArray categories = null;
	public ArrayList<String> catList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		  server=getIntent().getStringExtra("server");
		inputName = (EditText) findViewById(R.id.inputName);
		spinnerCategory = (Spinner) findViewById(R.id.inputCategory);
		inputLocation = (EditText) findViewById(R.id.inputLocation);
		inputINumber = (EditText) findViewById(R.id.inputINumber);
		inputDesc = (EditText) findViewById(R.id.inputDesc);
		new LoadAllCategories().execute();
		catList.toString();
		catList.add("Виберіть категорію");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>

		(this, android.R.layout.simple_spinner_item, catList);

		spinnerCategory.setAdapter(dataAdapter);

		spinnerCategory.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);
				return false;
			}
		});

		Button btnCreateItem = (Button) findViewById(R.id.btnCreateItem);

		btnCreateItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				new CreateNewItem().execute();
			}
		});

	}

	class CreateNewItem extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NewItemActivity.this);
			pDialog.setMessage("Додаємо предмет...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String name = inputName.getText().toString();
			String location = inputLocation.getText().toString();
			String inumber = inputINumber.getText().toString();
			String description = inputDesc.getText().toString();
			String category = spinnerCategory.getSelectedItem().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			try {
				params.add(new BasicNameValuePair("name", URLEncoder.encode(
						name, "UTF-8")));
				params.add(new BasicNameValuePair("location", URLEncoder
						.encode(location, "UTF-8")));
				params.add(new BasicNameValuePair("inumber", URLEncoder.encode(
						inumber, "UTF-8")));
				params.add(new BasicNameValuePair("description", URLEncoder
						.encode(description, "UTF-8")));
				params.add(new BasicNameValuePair("catname", URLEncoder
						.encode(category, "UTF-8")));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JSONObject json = jsonParser.makeHttpRequest(server+url_create_item,
					"POST", params);

			Log.d("Створюємо відповідь ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					Intent i = new Intent(getApplicationContext(),
							AllItemsActivity.class);
					i.putExtra("server",server);
					startActivity(i);

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

	class LoadAllCategories extends
			AsyncTask<String, String, ArrayList<String>> {

		protected ArrayList<String> doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(server+url_all_categories,
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
