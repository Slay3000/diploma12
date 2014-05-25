package ua.ifntung.parkulab.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{
EditText loginEdit,passwordEdit;
Button save,clear;
SharedPreferences settings;
public static final String APP_PREFERENCES = "logpass"; 
public static String TAG_LOGIN="login";
public static String TAG_PASSWORD="password";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		loginEdit=(EditText)findViewById(R.id.inputLogin);
		passwordEdit=(EditText)findViewById(R.id.inputPassword);
		save=(Button)findViewById(R.id.btnSaveS);
		clear=(Button)findViewById(R.id.btnClear);
		save.setOnClickListener(this);
		clear.setOnClickListener(this);
		settings=getSharedPreferences(APP_PREFERENCES, 0);
	if(settings.contains(TAG_LOGIN)) {
			loginEdit.setText(settings.getString(TAG_LOGIN, ""));
		}
		if(settings.contains(TAG_PASSWORD)) {
			passwordEdit.setText(settings.getString(TAG_PASSWORD, ""));
		}

	}
	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btnSaveS:
		String log=loginEdit.getText().toString();
		String pass=passwordEdit.getText().toString();
	SharedPreferences.Editor editor=settings.edit();
	
	editor.putString(TAG_LOGIN,log);
	editor.putString(TAG_PASSWORD, pass);
	editor.commit();
	InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	 inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	Toast.makeText(getApplicationContext(), "Збережено", Toast.LENGTH_SHORT).show();
		break;
	
		case R.id.btnClear:
			loginEdit.setText("");
			passwordEdit.setText("");
			settings.edit().remove(TAG_LOGIN).commit();
			settings.edit().remove(TAG_PASSWORD).commit();
			Toast.makeText(getApplicationContext(), "Очищено", Toast.LENGTH_SHORT).show();
			break;
	

	}

}}
