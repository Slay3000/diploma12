package ua.ifntung.parkulab.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity {

	Button btnViewProducts,btnNewProduct,btnShowImg,btnF,btnQR;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		btnViewProducts = (Button) findViewById(R.id.btnViewItems);
		btnNewProduct = (Button) findViewById(R.id.btnCreateItem);
		btnShowImg = (Button) findViewById(R.id.btnShowImage);
		btnF = (Button) findViewById(R.id.btnCategory);
		btnQR=(Button) findViewById(R.id.btnQR);


		btnViewProducts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						AllItemsActivity.class);
				startActivity(i);

			}
		});

		btnNewProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						NewItemActivity.class);
				startActivity(i);

			}
		});
		btnShowImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
				
				startActivity(intent);

			}
		});
		btnF.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
	/*			Intent intent = new Intent(getApplicationContext(), .class);
				
				startActivity(intent);*/

			}
		});
		btnQR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), GetItemByQR.class);
				
				startActivity(intent);

			}
		});
	}
}
