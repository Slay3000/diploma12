package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.ifntung.parkulab.activity.ShowItemActivity.GetImg;
import ua.ifntung.parkulab.camera.CameraPreview;
import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GetItemByQRActivity extends Activity {
	private static String url_all_qr = "get_all_qr.php";
	ProgressDialog pDialog;	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_CODES = "codes";
	private static final String TAG_QR = "qrcode";
	JSONParser jsonParser = new JSONParser();
	JSONArray codes = null;
String res,server;
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	TextView tw;
	TextView scanText;
	Button scanButton;
ArrayList<String> blist=new ArrayList<String>();
	ImageScanner scanner;
	AlertDialog.Builder dlgAlert;
	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		new GetAvaliableQRCodes().execute();
		super.onCreate(savedInstanceState);
		 server=getIntent().getStringExtra("server");
		setContentView(R.layout.get_item_by_qr);


    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		// Instance barcode scanner
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(GetItemByQRActivity.this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);
	
		

		scanText = (TextView) findViewById(R.id.scanText);

		scanButton = (Button) findViewById(R.id.ScanButton);
	
		 dlgAlert  = new AlertDialog.Builder(this);

		 
   
        dlgAlert.setTitle("Помилка");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create();
	
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (barcodeScanned) {
					barcodeScanned = false;
					scanText.setText("Скануємо...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
		
	}

	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					res=sym.getData().toString();
				if (blist.contains(sym.getData().toString())){
					Intent in = new Intent(getApplicationContext(), ShowItemActivity.class);
	                in.putExtra("qrcode", sym.getData().toString());
	                in.putExtra("server", server);
	              startActivity(in);
					 }
				else
				{	
				
				dlgAlert.setMessage("Невірний код");
					dlgAlert.show();}
				
					barcodeScanned = true;
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	class GetAvaliableQRCodes extends AsyncTask<String,String, String> {
	      @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(GetItemByQRActivity.this);
	            pDialog.setMessage("Отримуємо усі наявні QR-коди...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(server+url_all_qr, "GET",
					params);

			Log.d("Всі предмети: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					codes = json.getJSONArray(TAG_CODES);

					   for (int i = 0; i < codes.length(); i++) {
	                        JSONObject c = codes.getJSONObject(i);

	                blist.add(c.getString(TAG_QR));
	                      	                    }

		
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

}