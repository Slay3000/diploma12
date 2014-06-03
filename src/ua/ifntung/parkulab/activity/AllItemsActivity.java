package ua.ifntung.parkulab.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.parser.JSONParser;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class AllItemsActivity extends ListActivity {

    private ProgressDialog pDialog;
    ListAdapter adapter;
    HashMap<String, String> map = new HashMap<String, String>();;
    JSONParser jParser = new JSONParser();
String id;
Bitmap pic;
File file;
    ArrayList<HashMap<String, String>> itemsList=new ArrayList<HashMap<String,String>>();

    private static String url_all_items = "get_all_items.php";
    private static final String url_delete_item = "delete_item.php";
    private static String url_add_qr = "add_item_qr.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    public static final int POP_ED = 101; 
    public static final int POP_QR = 102;
    public static final int POP_PH = 103; 
    public static final int POP_IMG = 104; 
     public static final int POP_DEL = 105; 
String server;
    JSONArray items = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_items);
  	  server=getIntent().getStringExtra("server");
        	
             itemsList = new ArrayList<HashMap<String, String>>();

        new LoadAllItems().execute();

        ListView lv = getListView();
        registerForContextMenu(lv); 
EditText inputSearch =(EditText)findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ((SimpleAdapter) AllItemsActivity.this.adapter).getFilter().filter(cs);   
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                          
            }
        });
    		
    
     
    
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long idd) {
               id = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();
view.showContextMenu();
                     
            
           
            	
            }
        });

    }



	@Override
	public boolean onContextItemSelected(MenuItem item) {
		 
		switch (item.getItemId()) 
		{
		 case POP_ED:
			    Intent in = new Intent(getApplicationContext(), EditItemsActivity.class);
                in.putExtra(TAG_ID, id);
in.putExtra("server", server);
                startActivityForResult(in, 100);
			    break;
		   case POP_QR:
			new Coder().execute();
			    break;
		    case POP_PH:
		    	Intent intent = new Intent(AllItemsActivity.this,
						UploadPhotoActivity.class);
		    	intent.putExtra("server", server);
				intent.putExtra(TAG_ID, id);
				startActivity(intent);
			    break;
		    case POP_IMG:
		    	 Intent i = new Intent(getApplicationContext(), ImageManageActivity.class);
	                i.putExtra(TAG_ID, id);
	i.putExtra("server", server);
	                startActivity(i);
		    break; 
		    case POP_DEL:
			  new DeleteItem().execute();
			    break;
		    default:
			    return super.onContextItemSelected(item);
		}
	
		return true;
		
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, POP_ED, Menu.NONE, getString(R.string.edit_item_menu));
		menu.add(Menu.NONE, POP_QR, Menu.NONE, getString(R.string.generate_qr_menu));
		menu.add(Menu.NONE, POP_PH, Menu.NONE, getString(R.string.add_photo_menu));
		menu.add(Menu.NONE, POP_IMG, Menu.NONE, getString(R.string.show_images));
		menu.add(Menu.NONE, POP_DEL, Menu.NONE, getString(R.string.delete_item_menu));
	
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

    class LoadAllItems extends AsyncTask<String, String, String> {

      
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllItemsActivity.this);
            pDialog.setMessage("Завантажуємо список всіх предметів...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(server+url_all_items, "GET", params);

            Log.d("Всі предмети: ", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    items = json.getJSONArray(TAG_ITEMS);

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
map=new HashMap<String, String>();
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);

                        itemsList.add(map);
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(),
                            NewItemActivity.class);
                    i.putExtra("server", server);
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
            Collections.sort(itemsList,new Comparator<HashMap<String,String>>(){
                public int compare(HashMap<String,String> mapping1,HashMap<String,String> mapping2){
                    return mapping1.get("name").compareTo(mapping2.get("name"));
                }
            });
            
            runOnUiThread(new Runnable() {
                public void run() {
                     adapter = new SimpleAdapter(
                            AllItemsActivity.this, itemsList,
                            R.layout.list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.id, R.id.name });
                    setListAdapter(adapter);
                }
            });

        }

    }

    class DeleteItem extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllItemsActivity.this);
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

				JSONObject json = jParser.makeHttpRequest(server+url_delete_item,
						"POST", params);

				Log.d("Видаляємо предмет", json.toString());

				success = json.getInt(TAG_SUCCESS);
				
				
								
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					  Intent i = new Intent(AllItemsActivity.this, AllItemsActivity.class);
						i.putExtra("server", server);
						startActivity(i);
						finish();
				}
			});
			pDialog.dismiss();

		}

	}
    class Coder extends AsyncTask<String, String, String> {
    	
    	
    	
		 private ProgressDialog dialog;
		 private static final int BLACK = 0xFF000000;
		 private static final int WHITE = 0xFFFFFFFF;
		 private static final int ID = 34646456;
		 DateFormat dateFormat = new SimpleDateFormat("HHmmss_ddMMyy");
			Calendar cal;
		 
		 
		 @Override
		 protected void onPreExecute() {
		  dialog = new ProgressDialog(AllItemsActivity.this);
		  dialog.setTitle("Генерується");
		  dialog.setMessage("Зачекайте...");
		  dialog.setCancelable(false);
		  dialog.show();
		  super.onPreExecute();
		 }
		 
		 @Override
		 protected String doInBackground(String... params) {
		
				List<NameValuePair> param = new ArrayList<NameValuePair>();
		    	param.add(new BasicNameValuePair(TAG_ID,id));
		    	JSONObject json = jParser.makeHttpRequest(server+url_add_qr,
						"POST", param);
		    	Log.d("Створюємо відповідь ", json.toString());
			 FileOutputStream out;
		  try {
		
			  
		   BitMatrix matrix = new QRCodeWriter().encode("tmc"+id,
		     com.google.zxing.BarcodeFormat.QR_CODE, 250, 250);
		 pic =matrixToBitmap(matrix);
		 
	
		  } catch (WriterException e) {
		   e.printStackTrace();
		  }

		  String root = Environment.getExternalStorageDirectory().toString();
		  File myDir = new File(root + "/saved_images");    
		  myDir.mkdirs();
		    cal=Calendar.getInstance();
			String imgNameForUpload = dateFormat.format(cal.getTime()).toString();
		  String fname = imgNameForUpload+".jpg";
		   file = new File (myDir, fname);
		   
		  if (file.exists ()) file.delete (); 
		  try {
		      out = new FileOutputStream(file);
		         pic.compress(Bitmap.CompressFormat.JPEG, 90, out);
		     
		         out.flush();
		         out.close();

		  } catch (Exception e) {
		         e.printStackTrace();
		  }
		  
		  return null;
		 }
		 
		 @Override
		 protected void onPostExecute(String g) {
		  try {
			  Toast.makeText(getApplicationContext(), "Згенерований QR-код знаходиться: "+file, Toast.LENGTH_LONG).show();
		   dialog.dismiss();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  
		 }
		 
		 
		 private Bitmap matrixToBitmap(BitMatrix matrix) {
		  int width = matrix.getWidth();
		  int height = matrix.getHeight();
		  Bitmap image = Bitmap.createBitmap(width, height,
		    Bitmap.Config.ARGB_8888);
		  for (int x = 0; x < width; x++) {
		   for (int y = 0; y < height; y++) {
		    image.setPixel(x, y, matrix.get(x, y) ? BLACK : WHITE);
		   }
		  }
		  return image;
		 }
	 
		}


 
}

