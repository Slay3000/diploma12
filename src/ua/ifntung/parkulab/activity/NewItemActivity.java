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

public class NewItemActivity extends Activity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName, inputLocation,inputDesc,inputINumber;

    private static String url_create_item = "http://slaytmc.esy.es/create_item.php";

    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        inputName = (EditText) findViewById(R.id.inputName);
        inputLocation = (EditText) findViewById(R.id.inputLocation);
        inputINumber = (EditText) findViewById(R.id.inputINumber);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
       

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
            

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("location", location));
            params.add(new BasicNameValuePair("inumber", inumber));
            params.add(new BasicNameValuePair("description", description));
           

            JSONObject json = jsonParser.makeHttpRequest(url_create_item, "POST", params);

            Log.d("Створюємо відповідь ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), AllItemsActivity.class);
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
