package com.example.customcamera;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity{

	ImageView imageView;
	Gallery g;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.galleryview);
	
	final List<String> adapter=ReadSDCard();
	
	
	g = (Gallery) findViewById(R.id.gallery1);
	
	imageView = (ImageView) findViewById(R.id.imgView_gall);
	
	Bitmap mbitmap = BitmapFactory.decodeFile(adapter.get(adapter.size()-1));
	imageView.setImageBitmap(mbitmap);
	
	
	g.setAdapter(new ImageAdapter(this, adapter));
	g.setSelection(adapter.size()-1);
	g.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent,
	      View v, int position, long id) {
	    	
	    	
	    	//Toast.makeText(getBaseContext(),"pic" + (position + 1) + " selected",
			//		Toast.LENGTH_SHORT).show();
			// display the images selected
			
			Bitmap myBitmap = BitmapFactory.decodeFile(adapter.get(position));
			imageView.setImageBitmap(myBitmap);
	    }
	});
	}

	private List<String> ReadSDCard()
	{
	 List<String> tFileList = new ArrayList<String>();

	
	 //It have to be matched with the directory in SDCard
	
	 File f = new File(Environment.getExternalStorageDirectory() + "/BCamera");

	 File[] files=f.listFiles();

	 for(int i=0; i<files.length; i++)
	 {
	  File file = files[i];
	  /*It's assumed that all file in the path are in supported type*/
	  tFileList.add(file.getPath());
	 }
	
		  
		 return tFileList;
	}

	public class ImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private List<String> FileList;

	public ImageAdapter(Context c, List<String> fList) {
	    mContext = c;
	    FileList = fList;
	    TypedArray a = obtainStyledAttributes(R.styleable.GalleryActivity);
	    mGalleryItemBackground = a.getResourceId( R.styleable.GalleryActivity_android_galleryItemBackground,0);
	    a.recycle();
	}

	public int getCount() {
	    return FileList.size();
	}

	public Object getItem(int position) {
	    return position;
	} 

	public long getItemId(int position) {
	    return position;
	}

	public View getView(int position, View convertView,
	  ViewGroup parent) {
	    ImageView i = new ImageView(mContext);

	    Bitmap bm = BitmapFactory.decodeFile(
	      FileList.get(position).toString());
	    i.setImageBitmap(bm);
	    final float scale = getResources().getDisplayMetrics().density;
	    int pixels = (int) ( 103* scale + 0.5f);
	    i.setLayoutParams(new Gallery.LayoutParams(pixels, pixels));
	    i.setScaleType(ImageView.ScaleType.FIT_XY);
	    i.setBackgroundResource(mGalleryItemBackground);

	    return i;
	}
	}

	public TypedArray obtainStyledAttributes(int theme) {
	// TODO Auto-generated method stub
	return null;
	}
}
	
	
	

