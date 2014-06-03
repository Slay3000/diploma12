package ua.ifntung.parkulab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminOptionsActivity extends Activity{
Button allItems, newItem,categoryManage, userManage;
String server;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 server=getIntent().getStringExtra("server");
	setContentView(R.layout.admin_options);
	 allItems=(Button)findViewById(R.id.allItemsBtn);
	
	categoryManage=(Button)findViewById(R.id.categoryManageBtn);
	userManage=(Button)findViewById(R.id.userManageBtn);
	newItem=(Button)findViewById(R.id.addNewItemBtn);
	allItems.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),
						AllItemsActivity.class);
				i.putExtra("server", server);
				startActivity(i);		
		}
	});
	newItem.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),
					NewItemActivity.class);
			i.putExtra("server", server);
			startActivity(i);			
		}
	});
	userManage.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
					
		}
	});

	categoryManage.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),
					AllCategoriesActivity.class);
			i.putExtra("server", server);
			startActivity(i);			
		}
	});
	}

}
