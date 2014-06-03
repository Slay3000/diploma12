package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreenActivity extends Activity {

	JSONParser jsonParser = new JSONParser();
	SharedPreferences settings;
	String login, password, server;
	private static String url_check_logpass = "check_logpass.php";
	private static final String TAG_SUCCESS = "success";
	public static final String APP_PREFERENCES = "logpass";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_SERVER = "server";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_PERMISSION = "permission";
	int success = 0;

	//
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		settings = getSharedPreferences(APP_PREFERENCES, 0);
		login = settings.getString(TAG_LOGIN, "");
		server = settings.getString(TAG_SERVER, "");
		password = settings.getString(TAG_PASSWORD, "");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {

		case R.id.getByQrMenu:
			i = new Intent(getApplicationContext(), GetItemByQRActivity.class);
			i.putExtra("server", server);
			startActivity(i);
			break;
		case R.id.adminMenu:
		
			login = settings.getString(TAG_LOGIN, "");
			server = settings.getString(TAG_SERVER, "");
			password = settings.getString(TAG_PASSWORD, "");
			success = 0;
			new CheckLogPass().execute();

			break;

		case R.id.settingsMenu:
			i = new Intent(getApplicationContext(), SettingsActivity.class);

			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class CheckLogPass extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainScreenActivity.this);
			pDialog.setMessage("Перевіряємо правильність логіну та паролю...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_LOGIN, login));
			params.add(new BasicNameValuePair(TAG_PASSWORD, password));
			params.add(new BasicNameValuePair(TAG_PERMISSION, "admin"));
			JSONObject json = jsonParser.makeHttpRequest(server
					+ url_check_logpass, "POST", params);

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
			runOnUiThread(new Runnable() {
				public void run() {
					if ((login == "") || (password == "")) {
						Toast.makeText(getApplicationContext(), "Поле логіну та паролю є порожнім",
								Toast.LENGTH_SHORT).show();
					} else {

						if (success == 1) {
							Intent intent = new Intent(getApplicationContext(),
									AdminOptionsActivity.class);
							intent.putExtra("server", server);
							startActivity(intent);
						}

						else {
							Toast.makeText(getApplicationContext(),
									"wrong logpass", Toast.LENGTH_SHORT).show();

						}

					}

				}
			});

		}

	}
}
