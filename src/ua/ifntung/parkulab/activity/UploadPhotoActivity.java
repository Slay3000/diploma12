package ua.ifntung.parkulab.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import ua.ifntung.parkulab.parser.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadPhotoActivity extends Activity implements OnClickListener {

	private TextView messageText;
	private Button uploadButton, btnselectpic;
	private ImageView imageview;
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
	private String id, imgNameForUpload;
	private String upLoadServerUri = "http://slaytmc.esy.es/upload_to_server.php";
	JSONParser jsonParser = new JSONParser();
	private static String url_add_photo = "http://slaytmc.esy.es/add_photo.php";

	private static final String TAG_SUCCESS = "success";
	private String imagepath = null;
	DateFormat dateFormat = new SimpleDateFormat("HHmmss_ddMMyy");
	Calendar cal = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_img);

		uploadButton = (Button) findViewById(R.id.btnUpload);
		messageText = (TextView) findViewById(R.id.messageText);
		btnselectpic = (Button) findViewById(R.id.button_selectpic);
		imageview = (ImageView) findViewById(R.id.imageView_pic);

		btnselectpic.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		Intent i = getIntent();
		id = i.getStringExtra("id");

		

	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == btnselectpic) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Complete action using"), 1);
		} else if (arg0 == uploadButton) {

			imgNameForUpload = dateFormat.format(cal.getTime()).toString();
			new AddNewPhoto().execute();
			dialog = ProgressDialog.show(UploadPhotoActivity.this, "",
					"Uploading file...", true);
			messageText.setText("Починаємо завантаження.....");

			new Thread(new Runnable() {
				public void run() {

					uploadFile(imagepath);

				}
			}).start();

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK) {
			// Bitmap photo = (Bitmap) data.getData().getPath();

			Uri selectedImageUri = data.getData();
			imagepath = getPath(selectedImageUri);
			Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
			imageview.setImageBitmap(bitmap);
			messageText.setText("Uploading file path:" + imagepath);

		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public int uploadFile(String sourceFileUri) {

		final String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Файлу не знайдено :" + imagepath);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Файл не існує :" + imagepath);
				}
			});

			return 0;

		} else {
			try {

				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true); 
				conn.setUseCaches(false); 
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {
							String msg = "Завершено завантаження файлу";
							
							messageText.setText(msg);
							Toast.makeText(UploadPhotoActivity.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}


				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(UploadPhotoActivity.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(UploadPhotoActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}

	class AddNewPhoto extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("itemid", id));
			params.add(new BasicNameValuePair("photoaddr", imgNameForUpload));
			params.add(new BasicNameValuePair("filepath", imagepath));

			JSONObject json = jsonParser.makeHttpRequest(url_add_photo, "POST",
					params);

			Log.d("Створюємо відповідь ", json.toString());

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}

}