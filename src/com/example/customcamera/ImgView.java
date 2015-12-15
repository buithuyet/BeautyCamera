package com.example.customcamera;

import java.io.ByteArrayOutputStream;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.impl.conn.SingleClientConnManager;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.EM;
import org.opencv.objdetect.CascadeClassifier;

import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMixBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;


public class ImgView extends Activity implements OnTouchListener,OnSeekBarChangeListener{
	
	ImageView imgview;
	Button savebut;
	Button cancelbut;
	Button backfilter;
	Bitmap bitmap;
	Bitmap bitmapresult;
	Mat mat;
	MatOfRect faceDetections;
	boolean viewing=false;
	ProgressBar bar;
	String filename;
	LinearLayout lnlayout;
	ImageView effectChoose[];
	ImageView beautyChoose[];
	ImageView borderMenu[];
	ImageView borderChoose[];
	AlertDialog.Builder alertDialog;
	SeekBar seekbar;
	
	TextView beauty;
	TextView effects;
	TextView frames;
	int[] imgBorderMenu={R.drawable.christmas,R.drawable.newyear};
	int[] imgBorder = {R.drawable.backicon,R.drawable.original, R.drawable.chris1,R.drawable.chris2,R.drawable.chris3,
			R.drawable.chris4,R.drawable.chris5,R.drawable.chris6,R.drawable.chris7,R.drawable.chris8,R.drawable.chris9,R.drawable.chris10};
	float opacity;
	int imageID;
	
	public void selectedFilter(ImageView imageView[], int index){
		for(int i=1;i<imageView.length;i++){
			if(i==index)
				imageView[i].getDrawable().setColorFilter(0x33ff69b4,PorterDuff.Mode.SRC_ATOP);
			else
				imageView[i].getDrawable().clearColorFilter();
		}
		
	}
	
	// seekbar
	
	public void enableSeekbar(boolean state){
		if(state==true)
			seekbar.setEnabled(true);
		else seekbar.setEnabled(false);
		
		
	}
	// end seekbar
	//---------------------default list of filters----------------------------------------------------
	public void setDefaultFilters(){
		enableSeekbar(false);
		effects.setTextColor(Color.parseColor("#FF00FF"));
		 // list of filters
		lnlayout.removeAllViews();
		int[] imgs = { R.drawable.original, R.drawable.eff1945, R.drawable.eff1975, R.drawable.eff2015 };
		effectChoose=new ImageView[4];
		Bitmap bm;
	    for (int i = 0; i < 4; i++) {
	    	effectChoose[i] = new ImageView(this);
	      
	    	effectChoose[i].setPadding(2, 2, 2, 2);
	        
	       // set bitmap for imageviews
	        bm = BitmapFactory.decodeResource(getResources(), imgs[i]);
	        bm=ImageHelper.getRoundedCornerBitmap(bm, 20);
	        effectChoose[i].setImageBitmap(bm);
	        //imageView[i].setBackgroundResource(imgs[i]);
	       
	        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
	        layoutParams.setMargins(3, 0, 3, 0);
	        effectChoose[i].setLayoutParams(layoutParams);
	        
	        effectChoose[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Log.d("onclick  ", "ok");
					if(v==effectChoose[0])
					{
						selectedFilter(effectChoose,0);
						imgview.setImageBitmap(bitmap);
						bitmapresult=bitmap;
					}
		            
					if(v==effectChoose[1]){
						selectedFilter(effectChoose,1);
						new ProgressTask2().execute();
					}
					if(v==effectChoose[2]){
						selectedFilter(effectChoose,2);
						new ProgressTask3().execute();
					}
					if(v==effectChoose[3]){
						selectedFilter(effectChoose,3);
						new ProgressTask4().execute();	
					}
				}
			});
			
	       
	        lnlayout.addView(effectChoose[i]);
	    }
	   // --end list of filters--
	   
		
	}
	
	// -------------------------end default list---------------------
	
	// beauty filters-----------------------------------
	public void setBeautyFilters(){
		enableSeekbar(false);
		lnlayout.removeAllViews();
		int[] imgs = {R.drawable.original, R.drawable.skinicon};
		beautyChoose=new ImageView[2];
		Bitmap bm;
		 for (int i = 0; i < 2; i++) {
			 beautyChoose[i] = new ImageView(ImgView.this);
			 beautyChoose[i].setPadding(2, 2, 2, 2);
		        
		       // set bitmap for imageviews
		        bm = BitmapFactory.decodeResource(getResources(), imgs[i]);
		        bm=ImageHelper.getRoundedCornerBitmap(bm, 20);
		        beautyChoose[i].setImageBitmap(bm);
		        //imageView[i].setBackgroundResource(imgs[i]);
		       
		        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
		        layoutParams.setMargins(3, 0, 3, 0);
		        beautyChoose[i].setLayoutParams(layoutParams);
		        
		        beautyChoose[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(v==beautyChoose[0])
						{
							selectedFilter(beautyChoose,0);
							imgview.setImageBitmap(bitmap);
							bitmapresult=bitmap;
						}
			            
						if(v==beautyChoose[1]){
							selectedFilter(beautyChoose,1);
							new ProgressTask().execute();
						}
						
					}
				});
				
		        lnlayout.addView(beautyChoose[i]);
		    }
		
	}
	
	//end beauty filters--------------------------------
	
	// frames-----------------------------------
	
	public void setBorderMenu(){
		enableSeekbar(false);
		lnlayout.removeAllViews();
		borderMenu=new ImageView[2];
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		 for (int i = 0; i < 2; i++) {
			 borderMenu[i] = new ImageView(ImgView.this);
			 borderMenu[i].setPadding(2, 2, 2, 2);
		        
		       // set bitmap for imageviews
		        bm = BitmapFactory.decodeResource(getResources(), imgBorderMenu[i],options);
		        bm=ImageHelper.getRoundedCornerBitmap(bm, 20);
		        borderMenu[i].setImageBitmap(bm);
		        //imageView[i].setBackgroundResource(imgs[i]);
		       
		        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
		        layoutParams.setMargins(3, 0, 3, 0);
		        borderMenu[i].setLayoutParams(layoutParams);
		        
		        borderMenu[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						addBorderMenu(v);
						
					}
				});
				
		        lnlayout.addView(borderMenu[i]);
		        
		    }
	}
	//add bordermenu
	public void addBorderMenu(View v){
		if(v==borderMenu[0])
		{	
			selectedFilter(borderMenu,0);
			setBorders();
			
		}
		
		if(v==borderMenu[1]){
			selectedFilter(borderMenu,1);
		}
		
	}
	
	//end add bordermenu
	public void setBorders(){
			lnlayout.removeAllViews();
			
			borderChoose=new ImageView[12];
			Bitmap bm;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			 for (int i = 0; i < 12; i++) {
				 borderChoose[i] = new ImageView(ImgView.this);
				 borderChoose[i].setPadding(2, 2, 2, 2);
			        
			       // set bitmap for imageviews
			        bm = BitmapFactory.decodeResource(getResources(), imgBorder[i],options);
			        bm=ImageHelper.getRoundedCornerBitmap(bm, 20);
			        borderChoose[i].setImageBitmap(bm);
			        //imageView[i].setBackgroundResource(imgs[i]);
			       
			        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
			        layoutParams.setMargins(3, 0, 3, 0);
			        borderChoose[i].setLayoutParams(layoutParams);
			        
			        borderChoose[i].setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							enableSeekbar(true);
							addBorder(v);
							
						}
					});
					
			        lnlayout.addView(borderChoose[i]);
			        
			    }
			
		}
		
		//end frames--------------------------------
		
	
	// add boder---------------
		public void addBorder(View v){
			
			if(v==borderChoose[0])
			{
				selectedFilter(borderChoose,0);
				setBorderMenu();
			}
			if(v==borderChoose[1])
			{
				selectedFilter(borderChoose,1);
				imgview.setImageBitmap(bitmap);
				bitmapresult=bitmap;
			}
			for(int i=2;i<12;i++){
				if(v==borderChoose[i]){
					selectedFilter(borderChoose,i);
					new ProgressBorder().execute(imgBorder[i]);
				}
			}
			
		}
		
	//end add border-----------	
		private class ProgressBorder extends AsyncTask <Integer,Void,Void>{
		    @Override
		    protected void onPreExecute(){
		        bar.setVisibility(View.VISIBLE);
		        setnotClickable();
		       
		    }
		   

		    @Override
		    protected void onPostExecute(Void result) {
		          bar.setVisibility(View.GONE);
		          runOnUiThread(new Runnable() {
			          @Override
					public void run() {  
			        	  			imgview.setImageBitmap(bitmapresult);
								   }
							});
		          
		          setClickable();
		    }


			@Override
			protected Void doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				imageID=params[0];
				overlayBorder();
				return null;
			}
		}
		
		public void overlayBorder (){
			GPUImageAlphaBlendFilter border=new GPUImageAlphaBlendFilter(opacity);
			Bitmap bm = BitmapFactory.decodeResource(getResources(),imageID);
			border.setBitmap(bm);
			
			GPUImage mGPUImage = new GPUImage(this);
			mGPUImage.setFilter(border);
			mGPUImage.setImage(bitmap);
			bitmapresult=mGPUImage.getBitmapWithFilterApplied();
			
			bm.recycle();
		}
		
	//restart text clicked effect
	public void resetText(){
		beauty.setTextColor(Color.parseColor("#FFFFFF"));
		effects.setTextColor(Color.parseColor("#FFFFFF"));
		frames.setTextColor(Color.parseColor("#FFFFFF"));
		
	}
	//end 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgedit);
		imgview=(ImageView)findViewById(R.id.imgedit);
		savebut=(Button)findViewById(R.id.saveedit);
		
		bar = (ProgressBar) this.findViewById(R.id.progressBaredit);
		seekbar=(SeekBar)findViewById(R.id.seekBar1);
		cancelbut=(Button)findViewById(R.id.canceledit);
		lnlayout = (LinearLayout) findViewById(R.id.linearScrolledit);
		beauty=(TextView)findViewById(R.id.textBeauty);
		effects=(TextView)findViewById(R.id.textEffect);
		frames=(TextView)findViewById(R.id.textFrame);
		
		opacity=1.0f;
		
		//----set events for texts-------------
		
		beauty.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetText();
				((TextView) v).setTextColor(Color.parseColor("#FF00FF"));
				setBeautyFilters();
				
			}
		});
		
		effects.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetText();
				((TextView) v).setTextColor(Color.parseColor("#FF00FF"));
				setDefaultFilters();
			}
		});
		frames.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetText();
				((TextView) v).setTextColor(Color.parseColor("#FF00FF"));
				setBorderMenu();
			}
		});
		
		seekbar.setOnSeekBarChangeListener(this);
		//--------end set events------------------------
		
		
		
		setDefaultFilters();
		
		if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
	    }
		
		//check incoming intent, whether from Camera or from Edit
		if (getIntent().getExtras().containsKey("data")){
			Intent intent=getIntent();
			byte[] data;
			data=intent.getByteArrayExtra("data");
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			
				float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1};
				Matrix matrix = new Matrix();
				Matrix matrixMirrorY = new Matrix();
				matrixMirrorY.setValues(mirrorY);

				matrix.postConcat(matrixMirrorY);
				if (MainActivity.currentCameraId==Camera.CameraInfo.CAMERA_FACING_FRONT)
					{
					matrix.postRotate(90);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					}
			
		}
		
		if (getIntent().getExtras().containsKey("path")){
		
			Bundle extras = getIntent().getExtras();
			String imgpath;
			imgpath=extras.getString("path");
			File imgFile = new  File(imgpath);
			//scale down loaded image if it is too big
			if(imgFile.exists()){
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				//options.inSampleSize=2;
				bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
				
				int maxHeight = 1280;
				int maxWidth = 1280;    
				float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));
	
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
	
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
		}
	
		mat =new Mat ( bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC3);
		Bitmap myBitmap32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Utils.bitmapToMat(myBitmap32, mat);
		myBitmap32.recycle();
		
		
		//set imageview
		imgview.setImageBitmap(bitmap);
		//face detettion
		faceDetections=detectFace(mat);
		
		bitmapresult=bitmap;
		
		//set events
		cancelbut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mat.release();
				bitmap.recycle();
				bitmapresult.recycle();
				faceDetections.release();
				finish();
			}
		});
		
		
		/*
		imgview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					
						imgview.setImageBitmap(bitmap);
						
		        } else if (MotionEvent.ACTION_UP == event.getAction()) {
		        		imgview.setImageBitmap(bitmapresult);
		        }

				return true;
			}
			
		});
		*/
		
		imgview.setOnTouchListener(this);
		
		savebut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//new ProgressTask0().execute();
				ShareMenu();
				alertDialog.show();
				
				
				
			}
			
		});
		
	}
	public void ShareMenu(){
		// list of options
		alertDialog = new AlertDialog.Builder(ImgView.this);
		String names[] ={"Save","Share"};
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.optionshare, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Next");
        ListView lv = (ListView) convertView.findViewById(R.id.listViewforoption);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
               if(position==0){
            	   new ProgressTask0().execute();
            	   alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
            		    public void onClick(DialogInterface dialog, int which) {
            		        //Either of the following two lines should work.
            		        //dialog.cancel();
            		        dialog.dismiss();
            		    } 
            		});
               }
               if(position==1){
            	   ShareOption();
            	   
               }
            	   
            }
        });
        //--end list of option--
	}
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}
	
	public void ShareOption(){
		
		Uri uri = getImageUri(ImgView.this, bitmapresult);
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		//shareIntent.setType("text/plain");
		//ImgView.shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
		shareIntent.setType("image/jpeg");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Launcher");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(shareIntent, "Share Deal"));
	}
	
	public void skinEffect(){
		Mat dst=mat.clone();
		Mat nmat=mat.clone();
		Mat hsvthreshold=new Mat();
	
		 Mat blur=new Mat();
	      
	     Mat dst2=new Mat();
	     
	     Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_BGRA2RGB);
	     //blur=mat.clone();  
	     Imgproc.bilateralFilter(dst2, blur,25, 50, 50);
	     
	     Imgproc.cvtColor(blur, blur, Imgproc.COLOR_RGB2BGRA);
		 
		 double[] threshold = null;
		 		
			        for (Rect rect : faceDetections.toArray()) {
			        
			       //Extract Region of Interest
			        double cor1x=rect.x-0.1*rect.width;	
			        double cor1y=rect.y-0.2*rect.height;
			        if((int)cor1x<0)
			        	cor1x=0;
			        if((int)cor1y<0)
			        	cor1y=0;
			        
			        double cor2x=rect.x+1.1*rect.width;	
			        double cor2y=rect.y+1.2*rect.height;
			        if((int)cor2x>mat.width())
			        	cor2x=mat.width();
			        if((int)cor2y>mat.height())
			        	cor2y=mat.height();
			        
			        Point p1=new Point(cor1x,cor1y);
		    		Point p2=new Point(cor2x,cor2y);
		    		Rect regrect= new Rect(p1, p2);
		    		Mat reg=new Mat(mat,regrect);
		    		Log.d("Log abcd ", Integer.toString(reg.cols()));
		    		Imgproc.cvtColor(reg, hsvthreshold, Imgproc.COLOR_BGRA2BGR);
					Imgproc.cvtColor(hsvthreshold, hsvthreshold, Imgproc.COLOR_BGR2HSV);
			       
			        //threshold by model
			        threshold=getSkinModel(nmat,rect);
			        
			        if(threshold[5]<50){
			        	
			            //Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_BGRA2GRAY);
			   	       // Imgproc.equalizeHist(dst2, dst2);
			   	        //Imgproc.cvtColor(dst2, dst2, Imgproc.COLOR_GRAY2RGB);
			        	dst.convertTo(dst, CvType.CV_32FC3);
			        	Core.pow(dst, 1.07, dst);
			        	Core.convertScaleAbs(dst, dst, 1, 0);
			        	Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_BGRA2RGB);
				        
					    Imgproc.bilateralFilter(dst2, blur,25, 50, 50);
					    Imgproc.cvtColor(blur, blur, Imgproc.COLOR_RGB2BGRA);
			        	
			        	Mat reglow=new Mat(dst,regrect);
			        	threshold=getSkinModel(dst,rect);
			    		Imgproc.cvtColor(reglow, hsvthreshold, Imgproc.COLOR_BGRA2BGR);
						Imgproc.cvtColor(hsvthreshold, hsvthreshold, Imgproc.COLOR_BGR2HSV);
						
						
						
			        }
			        //Log.d("Log", Double.toString(threshold[0]));
			        Mat mask=new Mat();
			        
			        Scalar lower= new Scalar(threshold[3], threshold[4], threshold[5]);
			        Scalar higher= new Scalar(threshold[0], threshold[1], threshold[2]);
			        
			        Core.inRange(hsvthreshold, lower, higher, mask);
			       // dst.copyTo(result, mask);
			        Mat kernel= Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10,10));
			        Imgproc.erode(mask, mask, kernel);
			        Imgproc.dilate(mask, mask, kernel);
			        
			        Mat globalmmask=new Mat(mat.rows(), mat.cols(), CvType.CV_8U, new Scalar(0));
			        mask.copyTo(globalmmask.submat(regrect));
			        
			        blur.copyTo(dst, globalmmask);
				       
			        Mat sharp=new Mat();
			        Imgproc.GaussianBlur(dst, sharp, new Size(0,0), 3);
			      
			        Core.addWeighted(dst, 1.3, sharp, -0.3, 5, dst);
			        
			        }
		/*	        
		Mat matx=new Mat();
		Mat maty=new Mat();
		
		matx.create(dst.size(), CvType.CV_32FC1);
		maty.create(dst.size(), CvType.CV_32FC1);
		int i,j;
		
		for(j = 0; j < dst.rows(); j++ ){
		   for( i= 5; i < dst.cols()-5; i++ ){
		     double[] value =matx.get(j, i);
		     
		     if(i<dst.cols()/2-5){
		     value[0]=i-5;
		     matx.put(j, i, value);
		     maty.put(j, i, new Double(j));
		     continue;
		     }
		     if(i>dst.cols()/2+5){
		    	 value[0]=i+5;
			     matx.put(j, i, value);
			     maty.put(j, i, new Double(j));
			     continue;
			     }
		    	 
			   matx.put(j, i, new Double(i));
			   maty.put(j, i, new Double(j));
			   
		     
		     //double r =Math.sqrt((i-dst.cols()/2)*(i-dst.cols()/2) + (j-dst.rows()/2)*(j-dst.rows()/2))  ;
	        // double rnx =Math.pow(r,2.5)/(dst.cols()/2);
	        // double rny =Math.pow(r,2.5)/(dst.rows()/2);
	        // double value1=rnx*(i-dst.cols()/2)/r;
	         //double value2=rny*(j-dst.rows()/2)/r;
			   
			   
			  // double x = i-dst.cols()/2;
             //  double y = j-dst.rows()/2;  
	         //double r =Math.sqrt((i-0.5)*(i-0.5) + (j-0.5)*(j-0.5))  ;
              // double r =Math.sqrt(x*x+y*y)  ;
	         //double rnx =Math.pow(r,2.5)/(dst.cols()/2);
	        // double rny =Math.pow(r,2.5)/(dst.rows()/2);
	        // double value1=rnx*(i-dst.cols()/2)/r;
	        // double value2=rnx*(j-dst.rows()/2)/r;
	        // matx.put(j, i, value1);
		    // maty.put(j, i,value2 );
		    }
		   }
		//Imgproc.remap(dst, dst2, matx, maty,Imgproc.INTER_LINEAR);
		Imgproc.remap(dst, dst2, matx, maty,Imgproc.INTER_LINEAR,Imgproc.BORDER_DEFAULT,new Scalar(0, 0, 0));
		*/
			        
			
		//dst=lipBoost(dst);
		//dst=redBoost(dst);
		//dst=lipBoost(dst);
	    //double[] rgb={132,50,64};
	   // double[] rgb_template={254,40,162};
		//dst=ChangeTone(dst, rgb, rgb_template);
	   
		Bitmap resultBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(),Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(dst, resultBitmap);
		
		//imgview.setImageBitmap(resultBitmap);
		bitmapresult=resultBitmap;
		
		dst.release();
		hsvthreshold.release();	
		logHeap();
		
	}
	
	public void sexylipEffect(){
		Mat dst=mat.clone();
		Mat nmat=mat.clone();
		Mat hsvthreshold=new Mat();
	
		 Mat blur=new Mat();
	      
	     Mat dst2=new Mat();
	     Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_BGRA2RGB);
	        
	     Imgproc.bilateralFilter(dst2, blur,25, 50, 50);
	     Imgproc.cvtColor(blur, blur, Imgproc.COLOR_RGB2BGRA);
		 
		 double[] threshold = null;
		 		
			        for (Rect rect : faceDetections.toArray()) {
			        
			       //Extract Region of Interest
			        double cor1x=rect.x-0.1*rect.width;	
			        double cor1y=rect.y-0.2*rect.height;
			        if((int)cor1x<0)
			        	cor1x=0;
			        if((int)cor1y<0)
			        	cor1y=0;
			        
			        double cor2x=rect.x+1.1*rect.width;	
			        double cor2y=rect.y+1.2*rect.height;
			        if((int)cor2x>mat.width())
			        	cor2x=mat.width();
			        if((int)cor2y>mat.height())
			        	cor2y=mat.height();
			        
			        Point p1=new Point(cor1x,cor1y);
		    		Point p2=new Point(cor2x,cor2y);
		    		Rect regrect= new Rect(p1, p2);
		    		Mat reg=new Mat(mat,regrect);
		    		Log.d("Log abcd ", Integer.toString(reg.cols()));
		    		Imgproc.cvtColor(reg, hsvthreshold, Imgproc.COLOR_BGRA2BGR);
					Imgproc.cvtColor(hsvthreshold, hsvthreshold, Imgproc.COLOR_BGR2HSV);
			       
			        //threshold by model
			        threshold=getSkinModel(nmat,rect);
			        
			        if(threshold[5]<50){
			        	
			        	dst.convertTo(dst, CvType.CV_32FC3);
			        	Core.pow(dst, 1.07, dst);
			        	Core.convertScaleAbs(dst, dst, 1, 0);
			        	Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_BGRA2RGB);
				        
					    Imgproc.bilateralFilter(dst2, blur,25, 50, 50);
					    Imgproc.cvtColor(blur, blur, Imgproc.COLOR_RGB2BGRA);
			        	
			        	Mat reglow=new Mat(dst,regrect);
			        	threshold=getSkinModel(dst,rect);
			    		Imgproc.cvtColor(reglow, hsvthreshold, Imgproc.COLOR_BGRA2BGR);
						Imgproc.cvtColor(hsvthreshold, hsvthreshold, Imgproc.COLOR_BGR2HSV);
						
						
						
			        }
			        //Log.d("Log", Double.toString(threshold[0]));
			        Mat mask=new Mat();
			        
			        Scalar lower= new Scalar(threshold[3], threshold[4], threshold[5]);
			        Scalar higher= new Scalar(threshold[0], threshold[1], threshold[2]);
			        
			        Core.inRange(hsvthreshold, lower, higher, mask);
			       // dst.copyTo(result, mask);
			        Mat kernel= Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10,10));
			        Imgproc.erode(mask, mask, kernel);
			        Imgproc.dilate(mask, mask, kernel);
			        
			        Mat globalmmask=new Mat(mat.rows(), mat.cols(), CvType.CV_8U, new Scalar(0));
			        mask.copyTo(globalmmask.submat(regrect));
			        
			        blur.copyTo(dst, globalmmask);
				       
			        Mat sharp=new Mat();
			        Imgproc.GaussianBlur(dst, sharp, new Size(0,0), 3);
			      
			        Core.addWeighted(dst, 1.3, sharp, -0.3, 5, dst);
			        
			        }
		
			        
			
	    dst=lipBoost(dst);
		dst=redBoost(dst);
		
		Bitmap resultBitmap = Bitmap.createBitmap(dst.cols(), dst.rows(),Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(dst, resultBitmap);
		//imgview.setImageBitmap(resultBitmap);
		bitmapresult=resultBitmap;
		
		dst.release();
		hsvthreshold.release();	
		
		
	}
	
	public MatOfRect detectFace(Mat matsource){
		CascadeClassifier cascadeClassifier = null;
		Mat src=new Mat();
		matsource.convertTo(src, CvType.CV_8U);
		
		 try {
	            // Copy the resource into a temp file so OpenCV can load it
			 InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
		
	            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
	            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
	           
	          
	            FileOutputStream os = new FileOutputStream(mCascadeFile);
	           
	            byte[] buffer = new byte[4096];
	           
	           
	            int bytesRead;
	           
	            
	            while ((bytesRead = is.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	         
	            is.close();
	            os.close();
	          
	            // Load the cascade classifier
	            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
	        } catch (Exception e) {
	            Log.e("OpenCVActivity", "Error loading cascade", e);
	        }
	        
		
		
		MatOfRect faceDetections = new MatOfRect();
		cascadeClassifier.detectMultiScale(src, faceDetections, 1.2, 3,0,new Size(100,100), new Size(1000,1000));
		src.release();
		return faceDetections;
		
	}
	
	
	//get skin model
	public double[] getSkinModel(Mat mat,Rect facerect){
		 Mat face  = new Mat(mat, facerect);
		 
		 // Extract sample face with 6x6=36% of face box
		 Point p1=new Point(facerect.x+0.1*facerect.width, facerect.y+0.1*facerect.height);
		 Point p2=new Point(facerect.x+0.9*facerect.width, facerect.y+0.9*facerect.height);
		 Rect samplerect=new Rect(p1, p2);
		 Mat sample=new Mat(mat,samplerect);
		
		//Thresholding sample to get skinsample
		 Mat grey=new Mat();
		 Mat samplemask=new Mat();
		 Imgproc.cvtColor(sample,grey ,Imgproc.COLOR_BGR2GRAY);
		 //Imgproc.equalizeHist(grey, grey);
		 //Imgproc.GaussianBlur(grey,grey,new Size(3, 3),0);
		 Imgproc.threshold(grey, samplemask, 5,255, Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
		 //samplemask.convertTo(samplemask, CvType.CV_8SC1);
		 //Mat kernel= Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));
		    //Imgproc.erode(samplemask, samplemask, kernel);
		   // Imgproc.dilate(samplemask, samplemask, kernel);
		 Mat skinsample=new Mat();
		 sample.copyTo(skinsample, samplemask);
		 
		 //Color transform skinsample to HSV
		 Mat hsv=new Mat();
		 Imgproc.cvtColor(skinsample, hsv,Imgproc.COLOR_BGRA2BGR);
		 Imgproc.cvtColor(hsv, hsv,Imgproc.COLOR_BGR2HSV);
		 
		 //split to 3 channels
		 List<Mat> channels= new ArrayList<Mat>();
		 Mat ch_h, ch_s, ch_v;
		 Core.split(hsv, channels);
		 ch_h = channels.get(0);
		 ch_s = channels.get(1);
		 ch_v = channels.get(2);
		 
		
		 //compute mean value of each channel M_u
		
		 int countnonzero=Core.countNonZero(ch_h);
		 int TotalNumberOfPixels = hsv.rows() * hsv.cols();
		 int numberofzero=TotalNumberOfPixels-countnonzero;
		 int countnonzero2=Core.countNonZero(ch_s);
		 int numberofzero2=TotalNumberOfPixels-countnonzero2;
		 int countnonzero3=Core.countNonZero(ch_v);
		 int numberofzero3=TotalNumberOfPixels-countnonzero3;
		 
		 Scalar h=Core.sumElems(ch_h);
		 Scalar s=Core.sumElems(ch_s);
		 Scalar v=Core.sumElems(ch_v);

		
		double meanh=h.val[0]/countnonzero;
		double means=s.val[0]/countnonzero2;
		double meanv=v.val[0]/countnonzero3;
		Log.d("Log", Double.toString(meanh));
		Log.d("Log", Double.toString(means));
		Log.d("Log", Double.toString(meanv));
		
		//Compute variance of Gaussian
		
		ch_h.convertTo(ch_h, CvType.CV_32F);
		ch_s.convertTo(ch_s, CvType.CV_32F);
		ch_v.convertTo(ch_v, CvType.CV_32F);
		Mat muh=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F,new Scalar(meanh));
		Mat mus=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F,new Scalar(means));
		Mat muv=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F,new Scalar(meanv));
		
		//Imgproc.cvtColor(mu, mu, Imgproc.COLOR_RGB2HSV);
		Mat squareh=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F);
		Mat squares=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F);
		Mat squarev=new Mat(ch_h.rows(), ch_h.cols(), CvType.CV_32F);
		Core.absdiff(ch_h, muh, squareh);
		Core.absdiff(ch_s, mus, squares);
		Core.absdiff(ch_v, muv, squarev);
		
		Core.pow(squareh, 2, squareh);
		Core.pow(squares, 2, squares);
		Core.pow(squarev, 2, squarev);
	//	squareh.mul(squareh);
	//	squares.mul(squares);
	//	squarev.mul(squarev);
		Scalar s1=Core.sumElems(squareh);
		Scalar s2=Core.sumElems(squares);
		Scalar s3=Core.sumElems(squarev);
		
		double h_var=(s1.val[0]-numberofzero*meanh*meanh)/countnonzero;
		double s_var=(s2.val[0]-numberofzero2*means*means)/countnonzero2;
		double v_var=(s3.val[0]-numberofzero3*meanv*meanv)/countnonzero3;
		//Log.d("Log", Double.toString(h_var));
		double h_variance=Math.sqrt(h_var);
		double s_variance=Math.sqrt(s_var);
		double v_variance=Math.sqrt(v_var);
		Log.d("Log", Double.toString(h_variance));
		Log.d("Log", Double.toString(s_variance));
		Log.d("Log", Double.toString(v_variance));
		
		// set upper bounds and lower bounds
		double h_upper=meanh+2*h_variance;
		double s_upper=means+2*s_variance;
		double v_upper=meanv+2*v_variance;
		
		double h_lower=meanh-3*h_variance;
		double s_lower=means-3*s_variance;
		double v_lower=meanv-3*v_variance;
		
		double[] threshold={h_upper,s_upper,v_upper,h_lower,s_lower,v_lower};
		
		face.release();
		sample.release();
		grey.release();
		skinsample.release();
		hsv.release();
		ch_h.release();
		ch_s.release();
		ch_v.release();
		muh.release();
		mus.release();
		muv.release();
		squareh.release();
		squares.release();
		squarev.release();
		channels.clear();
		
		return threshold;
	}
	
	public Mat redBoost(Mat src){
		/* Mat alpha=new Mat();
		 Core.inRange(src, new Scalar(25,80,80,80), new Scalar(34,255,255,255),alpha);    
		  Core.bitwise_not(alpha,alpha);

		  //split source  
		  List<Mat> bgr= new ArrayList<Mat>();  
		  Core.split(src,bgr);   

		  //Merge to final image including alpha   
		  List<Mat> tmp= new ArrayList<Mat>();  
		  tmp.add(bgr.get(0));
		  tmp.add(bgr.get(1));
		  tmp.add(bgr.get(2));
		  tmp.add(alpha);
		    
		  Core.merge(tmp,src);
		
		*/
		
		Imgproc.cvtColor(src, src,Imgproc.COLOR_BGRA2BGR);
		Imgproc.cvtColor(src, src,Imgproc.COLOR_BGR2HSV);
		 //split to 3 channels
		 List<Mat> channels= new ArrayList<Mat>();
		 Mat ch_b, ch_g, ch_r;
		 Core.split(src, channels);
		 ch_b = channels.get(0);
		 ch_g = channels.get(1);
		 ch_r = channels.get(2);
		//Core.addWeighted(ch_r, 1, ch_r, 0, 0, ch_r);
		 Core.addWeighted(ch_g, 1, ch_g, 0, -20, ch_g);
		 Core.addWeighted(ch_b, 1, ch_b, 0,-15 , ch_b);
		 
		 
		 
		 List<Mat> newchannels=new ArrayList<Mat>();
		    newchannels.add(ch_b);
		    newchannels.add(ch_g);
		    newchannels.add(ch_r);
		    Mat dst=new Mat();
		    Core.merge(newchannels, dst);
		    Imgproc.cvtColor(dst, dst,Imgproc.COLOR_HSV2BGR);
		 
			
		    Imgproc.cvtColor(dst, dst,Imgproc.COLOR_BGR2BGRA); 
		    
		return dst;
	}
	public Mat lipBoost(Mat src){
		Imgproc.cvtColor(src, src,Imgproc.COLOR_BGRA2BGR);
		
		 //split to 3 channels
		 List<Mat> channels= new ArrayList<Mat>();
		 Mat ch_b, ch_g, ch_r;
		 Core.split(src, channels);
		 ch_b = channels.get(0);
		 ch_g = channels.get(1);
		 ch_r = channels.get(2);
		// Core.addWeighted(ch_r, 0.65, ch_r, 0, 90, ch_r);
		// Core.addWeighted(ch_g, 0.86, ch_g, 0, 35, ch_g);
		// Core.addWeighted(ch_b, 0.47, ch_b, 0, 133, ch_b);
		 
		Core.addWeighted(ch_r, 0.76, ch_r, 0, 60, ch_r);
		// Core.addWeighted(ch_g, 0.86, ch_g, 0, 35, ch_g);
	    // Core.addWeighted(ch_b, 0.47, ch_b, 0, 133, ch_b);
		 
		 List<Mat> newchannels=new ArrayList<Mat>();
		    newchannels.add(ch_b);
		    newchannels.add(ch_g);
		    newchannels.add(ch_r);
		    Mat dst=new Mat();
		    Core.merge(newchannels, dst);
		    
		    Imgproc.cvtColor(dst, dst,Imgproc.COLOR_BGR2BGRA); 
		return dst;
	}
	
	//function
		public Mat ChangeTone(Mat mat,double[] rgb,double[] rgb_template){
			// Compute Color Conversion Cofficient
			double grey=(rgb[0]+rgb[1]+rgb[2])/3;
			double grey_temp=(rgb_template[0]+rgb_template[1]+rgb_template[2])/3;
			double R_coff,G_coff,B_coff;
			R_coff=(rgb_template[0]/grey_temp)/(rgb[0]/grey);
			G_coff=(rgb_template[1]/grey_temp)/(rgb[1]/grey);
			B_coff=(rgb_template[2]/grey_temp)/(rgb[2]/grey);
			
			// TODO Auto-generated method stub
			Mat rgb_mat=new Mat();
			Mat result=new Mat();
		    Imgproc.cvtColor(mat, rgb_mat, Imgproc.COLOR_BGRA2RGB);
		    
		    Mat ch_r, ch_g, ch_b;
		 // "channels" is a vector of 3 Mat arrays:
		    List<Mat> channels= new ArrayList<Mat>();
		 // split img:
		    Core.split(rgb_mat, channels);
		 // get the channels (dont forget they follow BGR order in OpenCV)
		    ch_r = channels.get(0);
		    ch_g = channels.get(1);
		    ch_b = channels.get(2);
		    
		    Core.addWeighted(ch_r, R_coff, ch_r,0, 0, ch_r);
		    Core.addWeighted(ch_g, G_coff, ch_g,0, 0, ch_g);
		    Core.addWeighted(ch_b, B_coff, ch_b,0, 0, ch_b);
		   
		    List<Mat> newchannels=new ArrayList<Mat>();
		    newchannels.add(ch_r);
		    newchannels.add(ch_g);
		    newchannels.add(ch_b);
		    Mat dst=new Mat();
		    Core.merge(newchannels, dst);
		   // dst.convertTo(dst, -1,1.5,0);
		    
		    
		    Imgproc.cvtColor(dst, result,Imgproc.COLOR_BGR2RGBA);
		    return result;
			
		}
	
	public static void logHeap() {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Log.d("tag", "debug. =================================");
        Log.d("tag", "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        Log.d("tag", "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
    }
	
	public void overLay4(){
		GPUImageLookupFilter overFilter= new GPUImageLookupFilter();
		overFilter.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.lookupwildness));
		
		GPUImage mGPUImage = new GPUImage(this);
		mGPUImage.setFilter(overFilter);
		
		
		mGPUImage.setImage(bitmap);
		
		bitmapresult=mGPUImage.getBitmapWithFilterApplied();
		
		
	}
	public void overLay2(){
		GPUImageNashvilleFilter overFilter= new GPUImageNashvilleFilter(this);
		GPUImage mGPUImage = new GPUImage(this);
		mGPUImage.setFilter(overFilter);
		mGPUImage.setImage(bitmap);
		
		bitmapresult=mGPUImage.getBitmapWithFilterApplied();
	
		
	}
	public void overLay3(){
		GPUImageLordKelvinFilter overFilter= new GPUImageLordKelvinFilter(this);
		
		GPUImage mGPUImage = new GPUImage(this);
		mGPUImage.setFilter(overFilter);
		
		
		mGPUImage.setImage(bitmap);
		
		bitmapresult=mGPUImage.getBitmapWithFilterApplied();
		
		
		
	}
	
	private class ProgressTask extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	        bar.setVisibility(View.VISIBLE);
	        setnotClickable();
	       
	    }
	   

	    @Override
	    protected void onPostExecute(Void result) {
	          bar.setVisibility(View.GONE);
	          runOnUiThread(new Runnable() {
		          @Override
				public void run() {  
		        	  			imgview.setImageBitmap(bitmapresult);
							   }
						});
	         
	          setClickable();
	          
	    }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			 skinEffect();
			    	
			    	 
			return null;
		}
	}
	
	
	private class ProgressTask2 extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	        bar.setVisibility(View.VISIBLE);
	        setnotClickable();
	       
	    }
	   

	    @Override
	    protected void onPostExecute(Void result) {
	          bar.setVisibility(View.GONE);
	          runOnUiThread(new Runnable() {
		          @Override
				public void run() {  
		        	  			imgview.setImageBitmap(bitmapresult);
							   }
						});
	          
	          setClickable();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			    	 //sexylipEffect();
			overLay2();
			return null;
		}
	}
	private class ProgressTask3 extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	        bar.setVisibility(View.VISIBLE);
	        setnotClickable();
	       
	    }
	   

	    @Override
	    protected void onPostExecute(Void result) {
	          bar.setVisibility(View.GONE);
	          runOnUiThread(new Runnable() {
		          @Override
				public void run() {  
		        	  			imgview.setImageBitmap(bitmapresult);
							   }
						});
	          
	          setClickable();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			    	 //sexylipEffect();
			overLay3();
			return null;
		}
	}
	private class ProgressTask4 extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	        bar.setVisibility(View.VISIBLE);
	        setnotClickable();
	       
	    }
	   

	    @Override
	    protected void onPostExecute(Void result) {
	          bar.setVisibility(View.GONE);
	          runOnUiThread(new Runnable() {
		          @Override
				public void run() {  
		        	  			imgview.setImageBitmap(bitmapresult);
							   }
						});
	          
	          setClickable();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			    	 //sexylipEffect();
			overLay4();
			return null;
		}
	}
	private class ProgressTask0 extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	        bar.setVisibility(View.VISIBLE);
	        setnotClickable();
	       
	    }
	   

	    @Override
	    protected void onPostExecute(Void result) {
	          bar.setVisibility(View.GONE);
	          setClickable();
	          Toast.makeText(getBaseContext(),"Save successfully",Toast.LENGTH_SHORT).show();
	          //finish();
	    }

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			Bitmap saveBitmap=((BitmapDrawable)imgview.getDrawable()).getBitmap();
			FileOutputStream out = null;
			String filename=String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/BCamera/BuiCamera_%d.jpg",System.currentTimeMillis());
			File file= new File(filename);
			try {
			    out = new FileOutputStream(filename);
			    saveBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out); // bmp is your Bitmap instance
			   
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			    try {
			        if (out != null) {
			            out.close();
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			new SingleMediaScanner(ImgView.this, file);
			return null;
		}
	}
	
	public void setnotClickable(){
		savebut.setClickable(false);
		imgview.setClickable(false);
		cancelbut.setClickable(false);
	}
	public void setClickable(){
		savebut.setClickable(true);
		imgview.setClickable(true);
		cancelbut.setClickable(true);
		
	}
	
	
	// Make zoom in,out--------------------------------------------------------------------
	private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
	 @Override
	    public boolean onTouch(View v, MotionEvent event) 
	    {
	        ImageView view = (ImageView) v;
	        view.setScaleType(ImageView.ScaleType.MATRIX);
	        float scale;

	        dumpEvent(event);
	        // Handle touch events here...

	        switch (event.getAction() & MotionEvent.ACTION_MASK) 
	        {
	            case MotionEvent.ACTION_DOWN:   // first finger down only
	                                                savedMatrix.set(matrix);
	                                                start.set(event.getX(), event.getY());
	                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
	                                                mode = DRAG;
	                                                imgview.setImageBitmap(bitmap);
	                                                break;

	            case MotionEvent.ACTION_UP: imgview.setImageBitmap(bitmapresult);// first finger lifted

	            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

	                                                mode = NONE;
	                                                Log.d(TAG, "mode=NONE");
	                                                break;

	            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

	                                                oldDist = spacing(event);
	                                                Log.d(TAG, "oldDist=" + oldDist);
	                                                if (oldDist > 5f) {
	                                                    savedMatrix.set(matrix);
	                                                    midPoint(mid, event);
	                                                    mode = ZOOM;
	                                                    Log.d(TAG, "mode=ZOOM");
	                                                }
	                                                break;

	            case MotionEvent.ACTION_MOVE:

	                                                if (mode == DRAG) 
	                                                { 
	                                                    matrix.set(savedMatrix);
	                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
	                                                } 
	                                                else if (mode == ZOOM) 
	                                                { 
	                                                    // pinch zooming
	                                                    float newDist = spacing(event);
	                                                    Log.d(TAG, "newDist=" + newDist);
	                                                    if (newDist > 5f) 
	                                                    {
	                                                        matrix.set(savedMatrix);
	                                                        scale = newDist / oldDist; // setting the scaling of the
	                                                                                    // matrix...if scale > 1 means
	                                                                                    // zoom in...if scale < 1 means
	                                                                                    // zoom out
	                                                        matrix.postScale(scale, scale, mid.x, mid.y);
	                                                    }
	                                                }
	                                                break;
	        }

	        view.setImageMatrix(matrix); // display the transformation on screen

	        return true; // indicate event was handled
	    }

	    /*
	     * --------------------------------------------------------------------------
	     * Method: spacing Parameters: MotionEvent Returns: float Description:
	     * checks the spacing between the two fingers on touch
	     * ----------------------------------------------------
	     */

	    private float spacing(MotionEvent event) 
	    {
	        float x = event.getX(0) - event.getX(1);
	        float y = event.getY(0) - event.getY(1);
	        return FloatMath.sqrt(x * x + y * y);
	    }

	    /*
	     * --------------------------------------------------------------------------
	     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
	     * Description: calculates the midpoint between the two fingers
	     * ------------------------------------------------------------
	     */

	    private void midPoint(PointF point, MotionEvent event) 
	    {
	        float x = event.getX(0) + event.getX(1);
	        float y = event.getY(0) + event.getY(1);
	        point.set(x / 2, y / 2);
	    }

	    /** Show an event in the LogCat view, for debugging */
	    private void dumpEvent(MotionEvent event) 
	    {
	        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
	        StringBuilder sb = new StringBuilder();
	        int action = event.getAction();
	        int actionCode = action & MotionEvent.ACTION_MASK;
	        sb.append("event ACTION_").append(names[actionCode]);

	        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
	        {
	            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
	            sb.append(")");
	        }

	        sb.append("[");
	        for (int i = 0; i < event.getPointerCount(); i++) 
	        {
	            sb.append("#").append(i);
	            sb.append("(pid ").append(event.getPointerId(i));
	            sb.append(")=").append((int) event.getX(i));
	            sb.append(",").append((int) event.getY(i));
	            if (i + 1 < event.getPointerCount())
	                sb.append(";");
	        }

	        sb.append("]");
	        Log.d("Touch Events ---------", sb.toString());
	    }
	//end of zoom in,out----------------------------------------------------------------------


		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			opacity=(float)progress/100;
			
			
		}


		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			//Toast.makeText(ImgView.this,"seek bar progress:"+imageID, Toast.LENGTH_SHORT).show();
			overlayBorder();
			imgview.setImageBitmap(bitmapresult);
		}
}	
	



