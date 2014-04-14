package ua.ifntung.parkulab.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.ifntung.parkulab.galleryWigdet.GalleryViewPager;
import ua.ifntung.parkulab.galleryWigdet.UrlPagerAdapter;
import android.app.Activity;
import android.os.Bundle;

public class ShowItemActivity extends Activity{


	   private GalleryViewPager mViewPager;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_item);
		String[] urls = {
                "http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
                "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
                "http://slaytmc.esy.es/img/1.jpg",
                "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
                "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
                "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
        };
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
   
        
        mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
	}

	
}
