package com.example.customcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


 

public class MainActivity extends Activity implements SurfaceHolder.Callback,SensorEventListener   {

	 	Camera camera;
	    SurfaceView surfaceView;
	    SurfaceHolder surfaceHolder;
	    boolean previewing = false;
	    static int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
	    private String selectedImagePath;
	    
	    
	    View viewControl2;
	    LayoutInflater controlInflater = null;
	    LayoutInflater controlInflater2 = null;
	    LayoutInflater controlInflater3 = null;
	    RelativeLayout.LayoutParams layoutParamsControl2;
	    RelativeLayout rLayout2;
	    boolean viewing=false;
	    
	    
	    
	    
	    ImageView imgview;
	    ImageView imgcapture;
	    ImageView effect_normal;
	    ImageView effect_sepia;
	    ImageView effect_mono;
	    ImageView effect_sunset;
	    ImageView imgmainview;
	    ImageView flipcam;
	    private static final int SELECT_PICTURE = 1;
	   // ImageView flash;

	    
	    PictureCallback rawCallback;
	    ShutterCallback shutterCallback;
	    PictureCallback jpegCallback;
	    
	   // Sensor variables
	    private SensorManager mSensorManager;
	    Sensor accelerometer;
	    Sensor magnetometer;
	    
	    
	    public static void setAutoOrientationEnabled(Context context, boolean enabled)
	    {
	          Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
	    }
	    
	   
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	     /*  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        if (prefs.getBoolean("fix_orientation_to_portrait", false)) {
	            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        } else {
	            //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	        	//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        	setAutoOrientationEnabled(this,true);
	        }
	        */
	        setContentView(R.layout.activity_main);
	        /*
	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        if (prefs.getBoolean("fix_orientation_to_portrait", false)) {
	            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        } else {
	            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	        	// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        }
	        */
	        
	       /* mOpenCvCameraView=(JavaCameraView)findViewById(R.id.cameraview);
	        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	        mOpenCvCameraView.setCvCameraViewListener(this);
	        mOpenCvCameraView.setMaxFrameSize(800, 480);
	        */
	        
	        //check default folder exist, if not make new directory
	   	 File defaultfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/BCamera");
	   		 if (!defaultfile.exists()){
	   			defaultfile.mkdirs();
	   		 }
	   		 
	   		 
	   		 
	   	 
	       
	 
	      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	 
	        getWindow().setFormat(PixelFormat.UNKNOWN);
	        surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
	        surfaceHolder = surfaceView.getHolder();
	        surfaceHolder.addCallback(this);
	        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        
	        
	        rawCallback = new PictureCallback() {
	            public void onPictureTaken(byte[] data, Camera camera) {
	                Log.d("Log", "onPictureTaken - raw");
	                
	                
	            }
	        };

	        /** Handles data for jpeg picture */
	        shutterCallback = new ShutterCallback() {
	            public void onShutter() {
	                Log.i("Log", "onShutter'd");
	            }
	        };
	        jpegCallback = new PictureCallback() {
	            public void onPictureTaken(byte[] data, Camera camera) {
	            	
	            
	                Log.d("Log", "onPictureTaken - jpeg");
	                
	            	Intent intent = new Intent(MainActivity.this,ImgView.class);
	                intent.putExtra("data", data);
	    			startActivity(intent); 
	                

	            }
	        };
	      
	    
	        
	       
	        
	        controlInflater = LayoutInflater.from(getBaseContext());
	        View viewControl = controlInflater.inflate(R.layout.custom, null);
	        controlInflater3=LayoutInflater.from(getBaseContext());
	        View viewBar=controlInflater3.inflate(R.layout.custombar, null);
	        
	        //capture an image
	        
	        imgcapture=(ImageView)viewControl.findViewById(R.id.Button01);
	        imgcapture.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					captureImage();
					
					
					
				}
			});
	        
	        //open Gallery
	        imgmainview=(ImageView)viewControl.findViewById(R.id.galleryimage);
	        imgmainview.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				}
			});
	        
	        
	        
	        //append main bar 
	        RelativeLayout.LayoutParams layoutParamsControl = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
	                                                            LayoutParams.WRAP_CONTENT);
	        layoutParamsControl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
	        
	        RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.linearid);
	        rLayout.addView(viewControl, layoutParamsControl);
	        
	        //append top bar
	        RelativeLayout.LayoutParams lay_para= new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
	        lay_para.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
	        RelativeLayout rLayout3 = (RelativeLayout)findViewById(R.id.linearid);
	        rLayout3.addView(viewBar, lay_para);
	        
	        //set events to top bar
	        flipcam=(ImageView)viewBar.findViewById(R.id.flipcam);
	        //flash=(ImageView)viewBar.findViewById(R.id.flash);
	        
	        flipcam.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (previewing) {
					    camera.stopPreview();
					    previewing = false;
					}
					//NB: if you don't release the current camera before switching, you app will crash
					camera.release();

					//swap the id of the camera to be used
					
					if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
					    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
					    camera = Camera.open(currentCameraId);
						camera.setDisplayOrientation(90);
						Camera.Parameters params = camera.getParameters();
						
						//params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
						params.setPictureSize(1280,720);
						List<Camera.Size> sizes = params.getSupportedPreviewSizes();
						Camera.Size size=sizes.get(0);
						Log.d("Log: camera size",Integer.toString(size.height));
						Log.d("Log: camera size",Integer.toString(size.width));
						
						
						camera.setParameters(params);
						
						  
						
						
						try {

						    camera.setPreviewDisplay(surfaceHolder);
						    
						} catch (IOException e) {
						    e.printStackTrace();
						}
						camera.startPreview();
						previewing=true;
					}
					else {
					    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
					    
					    camera = Camera.open(currentCameraId);
					    camera.setDisplayOrientation(90);
					    
					    Camera.Parameters params = camera.getParameters();
						params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
						params.setRotation(90);
						params.setPictureSize(1280,720);
						//params.setExposureCompensation(params.getMaxExposureCompensation());

						//if(params.isAutoExposureLockSupported()) {
						// params.setAutoExposureLock(false);
						//}
						camera.setParameters(params);
					    try {

						    camera.setPreviewDisplay(surfaceHolder);
						    
						} catch (IOException e) {
						    e.printStackTrace();
						}
						camera.startPreview();
						previewing=true;
					    
					}
					;
					
					
				
					
					
					
				}
				
			});
	        
	       // flash.setOnClickListener(new View.OnClickListener() {
				
			//	@Override
			//	public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
			//	}
			//});
	        
	        
	        
	        //append sub bar
	        controlInflater2=LayoutInflater.from(getBaseContext());
	        viewControl2 = controlInflater2.inflate(R.layout.filterlist, null);
	        layoutParamsControl2 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
	        layoutParamsControl2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
	        float scale = getResources().getDisplayMetrics().density;
	        int bottom_pixel = (int) ( 60* scale + 0.5f);
			layoutParamsControl2.setMargins(0, 0, 0,bottom_pixel);
			rLayout2 = (RelativeLayout)findViewById(R.id.linearid);
			
			//set events
	        imgview=(ImageView)findViewById(R.id.imgview);
	        imgview.setOnClickListener(new  View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(viewing){
						rLayout2.removeView(viewControl2);
						viewing=false;
						
					}
					
					else{	
					rLayout2.addView(viewControl2, layoutParamsControl2);
					viewing=true;
					}
				}
			});
	        
	        
		        effect_normal=(ImageView) viewControl2.findViewById(R.id.normal_eff);
		        effect_sepia=(ImageView)viewControl2.findViewById(R.id.sepia_eff);
		        effect_mono=(ImageView)viewControl2.findViewById(R.id.mono_eff);
		        effect_sunset=(ImageView)viewControl2.findViewById(R.id.sunset_eff);
		        
		       effect_normal.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Camera.Parameters params = camera.getParameters();
					params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
					params.setColorEffect(Camera.Parameters.EFFECT_NONE);
					camera.setParameters(params);
				}
			});
		        effect_sepia.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Camera.Parameters params = camera.getParameters();						
						params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
						camera.setParameters(params);
						
					}
				});
		        effect_mono.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Camera.Parameters params = camera.getParameters();
						params.setColorEffect(Camera.Parameters.EFFECT_MONO);
						camera.setParameters(params);
						
					}
				});
		        effect_sunset.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Camera.Parameters params = camera.getParameters();
						params.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
						camera.setParameters(params);
						
						}
						
					
				});
		        
				
		        // intialize sensor variables
		      mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		      accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		      magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		      
	        
	    }
	    	
	    
	  
	    
	    
	    private void captureImage() {
	        // TODO Auto-generated method stub
	    	
	        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);
	        camera.takePicture(null, null, jpegCallback);
	        
	      //  String filename=String.valueOf(file_name);
		//	Log.d("Log","this is "+filename);
			
	    }

	    
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                Intent intent = new Intent(MainActivity.this,ImgView.class);
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

	    
	    /*
		@Override
		protected void onResume() {
			super.onResume();
	        if (!OpenCVLoader.initDebug()) {
	        //mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
	          //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
	        } else {
	            
	            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
	        }
		}


		

		
		





		@Override
		public void onCameraViewStarted(int width, int height) {
			// TODO Auto-generated method stub
			
		}





		@Override
		public void onCameraViewStopped() {
			// TODO Auto-generated method stub
			
		}





		@Override
		public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
			// TODO Auto-generated method stub
			return inputFrame.rgba();
			
		    
		}
	    
	    */
	    
	   
	    

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			currentCameraId=Camera.CameraInfo.CAMERA_FACING_BACK;   
			mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
			
			
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
			mSensorManager.unregisterListener(this);
		}


		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			
		}



		@Override
	    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			
	        if(previewing){
	            camera.stopPreview();
	            
	            previewing = false;
	        }
	       
	        if (camera != null){
	        	//setAutoOrientationEnabled(this,true);
	            try {
	            	Camera.Parameters params = camera.getParameters();
	            	params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	            	params.setPictureSize(1280,720);
	            	params.setRotation(90);
	            	//params.setJpegQuality(100);
	            	
	            	Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	            	int rotation = display.getRotation();
	            	/*if(rotation==0){
	            	camera. setDisplayOrientation(90);
	            	params.setRotation(90);
	            	camera.setParameters(params);
	            	}
	            	if(rotation==1)
	            	{
	            	camera. setDisplayOrientation(0);
	            	params.setRotation(0);
	            	camera.setParameters(params);
	            	}
	            	if(rotation==3)
	            	{
	            	camera. setDisplayOrientation(180);
	            	params.setRotation(180);
	            	camera.setParameters(params);
	            	}
	            	
	            	//params.setRotation(90);
	            	 
	            	 */
	            	 Log.d("Log camera rotate", Integer.toString(rotation));
	            	
	            	//params.setExposureCompensation(params.getMaxExposureCompensation());

	            	//if(params.isAutoExposureLockSupported()) {
	            	 //params.setAutoExposureLock(false);
	            	//}
	            	//params.setPreviewSize(width, height);
	            	camera.setParameters(params);
	            	camera. setDisplayOrientation(90);
					camera.setPreviewDisplay(holder);
					
					camera.startPreview();
	                previewing = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	               
	        }
	    }
	 
		
		
	    @Override
	    public void surfaceCreated(SurfaceHolder holder) {
	        camera = Camera.open();
	      camera. setDisplayOrientation(90);
	       
	      
	    }
	 
	    @Override
	    public void surfaceDestroyed(SurfaceHolder holder) {
	       camera.stopPreview();
	       camera.release();
	        camera = null;
	        previewing = false;
	    }


		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		float[] mGravity;
		float[] mGeomagnetic=new float[3];
		Float azimut;
		@Override
		public void onSensorChanged(SensorEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			      mGravity = arg0.values.clone();
				//Log.d("Log", "sensor: "+Float.toString(mGravity[1]));
			    if (arg0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			      mGeomagnetic = arg0.values.clone();
			  //  Log.d("Log", "sensor: "+Float.toString(mGeomagnetic[0]));
			    if (mGravity != null&& mGeomagnetic != null) {
			      float R[] = new float[9];
			      float I[] = new float[9];
			      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			      Log.d("Log", "sensor cc: "+success);
			      if (success) {
			        float orientation[] = new float[3];
			        SensorManager.getOrientation(R, orientation);
			        azimut = orientation[0]; // orientation contains: azimut, pitch and roll
			        //Toast.makeText(getBaseContext(),"Angle: "+Float.toString(azimut),Toast.LENGTH_SHORT).show();
			        
			      }
			    }
			
		}
	    
	  
	    	
}
