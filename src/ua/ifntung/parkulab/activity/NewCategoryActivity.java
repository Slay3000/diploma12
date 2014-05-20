package ua.ifntung.parkulab.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class NewCategoryActivity extends Activity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputCategoryName;

    private static String url_create_category = "http://slaytmc.esy.es/create_category.php";

    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        inputCategoryName = (EditText) findViewById(R.id.categoryName);
        
       

        Button btnCreateCategory = (Button) findViewById(R.id.btnSaveCategory);
        btnCreateCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new CreateNewCategory().execute();
            }
        });
    }

    class CreateNewCategory extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewCategoryActivity.this);
            pDialog.setMessage("Додаємо предмет...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String catName = inputCategoryName.getText().toString();
           
            

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("catname", catName));
           
            JSONObject json = jsonParser.makeHttpRequest(url_create_category, "POST", params);

            Log.d("Створюємо відповідь ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), AllCategoriesActivity.class);
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

}
