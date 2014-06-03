package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import ua.ifntung.parkulab.parser.JSONParser;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllCategoriesActivity extends ListActivity {

    private ProgressDialog pDialog;
    AlertDialog deleteDialog;
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> categoriesList;
    Button addNewCategory;
    private static String url_all_items = "get_all_categories.php";
    private static final String url_delete_item = "delete_category.php";
String pid,server;

    private static final String TAG_ID = "id";
    private static final String TAG_CATEGORY_NAME = "catname";
    private static final String TAG_SUCCESS="success";
    private static final String TAG_CATEGORIES="category";
    JSONArray categories = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_categories);
        server=getIntent().getStringExtra("server");
        addNewCategory=(Button)findViewById(R.id.addNewCat);
        categoriesList = new ArrayList<HashMap<String, String>>();

        new LoadAllCategories().execute();

       addNewCategory.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(),
					NewCategoryActivity.class);
			startActivity(i);
		}
	}) ;
        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	pid = ((TextView) findViewById(R.id.idC)).getText()
		                    .toString();
					new DeleteCategory().execute();
          
                
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            intent.putExtra("server", server);
            startActivity(intent);
        }

    }

    class LoadAllCategories extends AsyncTask<String, String, String> {

      
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllCategoriesActivity.this);
            pDialog.setMessage("Завантажуємо список всіх предметів...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(server+url_all_items, "GET", params);

            Log.d("Всі предмети: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    categories = json.getJSONArray(TAG_CATEGORIES);

                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject c = categories.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_CATEGORY_NAME);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_ID, id);
                        map.put(TAG_CATEGORY_NAME, name);

                        categoriesList.add(map);
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(),
                            NewItemActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            AllCategoriesActivity.this, categoriesList,
                            R.layout.list_category, new String[] { TAG_ID,
                            TAG_CATEGORY_NAME},
                            new int[] { R.id.idC, R.id.catname });
                    setListAdapter(adapter);
                }
            });

        }

    }
    class DeleteCategory extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllCategoriesActivity.this);
			pDialog.setMessage("Видаляємо предмет...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String[] args) {

			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", pid));

				JSONObject json = jsonParser.makeHttpRequest(server+url_delete_item,
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
