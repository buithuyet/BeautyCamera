package com.example.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class HomeScreen extends Activity {
	//variables
	ImageView camerabut;
	ImageView imgbut;
	String selectedImagePath;
	private static final int SELECT_PICTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		camerabut=(ImageView)findViewById(R.id.imageHome1);
		imgbut=(ImageView)findViewById(R.id.imageHome2);
		
		// set events for home buttons
		camerabut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent hintent=new Intent(HomeScreen.this,MainActivity.class);
				startActivity(hintent);
			}
		});
		
		imgbut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
			}
		});
		
		
	}
	
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                Intent intent = new Intent(HomeScreen.this,ImgView.class);
	                intent.putExtra("path", selectedImagePath);
	    			startActivity(intent); 
	            }
	        }
	    }

	    /**
	     * helper to retrieve the path of an image URI
	     */
	    public String getPath(Uri uri) {
	            // just some safety built in 
	            if( uri == null ) {
	                // TODO perform some logging or show user feedback
	                return null;
	            }
	            // try to retrieve the image from the media store first
	            // this will only work for images selected from gallery
	            String[] projection = { MediaStore.Images.Media.DATA };
	            Cursor cursor = managedQuery(uri, projection, null, null, null);
	            if( cursor != null ){
	                int column_index = cursor
	                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	                cursor.moveToFirst();
	                return cursor.getString(column_index);
	            }
	            // this is our fallback here
	            return uri.getPath();
	    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
