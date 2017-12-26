package com.example.adeelturk.swipelefttoright;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("InlinedApi")
@SuppressWarnings("deprecation")
public class CameraActivity extends AppCompatActivity {


	private  final String TAG="impracticaljockers.CameraActivity";

	//private static String songUrl;
	//private static String songName;
	public static int cameraOrientation=90;

	private static File mFinalCollageFile;
	private static Camera mCamera=null;
	private CameraPreview mPreview;

	private Button switchCamera;
	private Context myContext;
	private RelativeLayout cameraPreview;
	private boolean cameraFront = false;

	private Button effectsButton;
	private Button collagesButton;
	private Button moreButton;

	private boolean switchButtonFlag;

	//private FrameLayout mAudioFrameLayout;
	private boolean recording ;

	private DisplayMetrics mDisplayMetrics;
	private int displayHeight;
	private int displayWidth;
	private Boolean mRecordingFlag;
	//private MediaPlayer mediaPlayer;
	private Boolean frontCameraFlag;

	private Camera.PictureCallback mPictureCallback;
	public static final String folderName="impracticalJockers";

	private RelativeLayout mCamerPreviewContainer;

	private Camera.Parameters params ;

	private int _xDelta;
	private int _yDelta;
	private static int WIDTH = 250;
	private static int HEIGHT = 250;

	private static int WIDTH_ON_TOUCH = 250;
	private static int HEIGHT_ON_TOUCH = 250;

	private Button mCaptureButton;
	private RelativeLayout mRootVRelativeLayout;





	public static final String TAG_SHARED_KEY="imagepath";




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		mDisplayMetrics=new DisplayMetrics();
		recording = false;
		switchButtonFlag=true;

		//mediaPlayer = new MediaPlayer();
		//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//mediaPlayer.setOnCompletionListener(mediaCompletionListener);

		setContentView(R.layout.activity_camera);

		mCaptureButton=(Button)findViewById(R.id.captureButton);

		mRootVRelativeLayout=(RelativeLayout)findViewById(R.id.root);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		mCamerPreviewContainer=(RelativeLayout)findViewById(R.id.cameraPreivewConatiner);
		createFolder();
		initialize();

		mPictureCallback=new Camera.PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

				//Toast.makeText(CameraActivity.this,"Pic"+imageCapturedCheck,Toast.LENGTH_SHORT).show();

				File pictureFile = getOutputMediaFile();
				if (pictureFile == null) {
					return;
				}
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					//savePreferences(pictureFile.getAbsolutePath());

					/*Intent i = new Intent(CameraActivity.this,PhotoEditingActivity.class);
					startActivity(i);*/

					try {


						ExifInterface ei = new ExifInterface(pictureFile.getAbsolutePath());
						int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
								ExifInterface.ORIENTATION_UNDEFINED);

						switch (orientation) {
							case ExifInterface.ORIENTATION_ROTATE_90:
								pictureFile=rotateImage(pictureFile, 90);
                           /* Toast.makeText(getActivity(),"90",Toast.LENGTH_SHORT).show();*/
								break;
							case ExifInterface.ORIENTATION_ROTATE_180:
								pictureFile=rotateImage(pictureFile, 180);
                           /* Toast.makeText(getActivity(),"180",Toast.LENGTH_SHORT).show();*/
								break;
							case ExifInterface.ORIENTATION_ROTATE_270:
								pictureFile=rotateImage(pictureFile, 270);
                           /* Toast.makeText(getActivity(),"270",Toast.LENGTH_SHORT).show();*/
								break;
							case ExifInterface.ORIENTATION_NORMAL:
                            /*Toast.makeText(getActivity(),"default",Toast.LENGTH_SHORT).show();*/
							default:
								break;

						}

					}catch (Exception e){

						e.printStackTrace();
					}






					Toast.makeText(CameraActivity.this,"Pic captured",Toast.LENGTH_SHORT).show();

					camera.startPreview();

				}  catch (Exception e) {

					Toast.makeText(CameraActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
				}


			}
		};


		// Setup Gesture Detectors

		setCamera();



	}





	private void savePreferences(String value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(CameraActivity.TAG_SHARED_KEY, value);

		editor.commit();

	}









	public void setCamera(){

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:

				//Toast.makeText(CameraActivity.this,"0",Toast.LENGTH_SHORT).show();
				degrees = 0;
				CameraActivity.cameraOrientation=90;
				//int rotate = (info.orientation - degrees + 360) % 360;

				params.setRotation(270);
				mCamera.setParameters(params);


				this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
				displayHeight=mDisplayMetrics.heightPixels/4;
				displayWidth=mDisplayMetrics.widthPixels;

				LayoutParams mLayoutParams1=cameraPreview.getLayoutParams();
				mLayoutParams1.width=mDisplayMetrics.widthPixels;
				mLayoutParams1.height=mDisplayMetrics.heightPixels;
				//mVideoView.setLayoutParams(mLayoutParams);
				mPreview.setLayoutParams(mLayoutParams1);
				cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

				break; // Natural orientation
			case Surface.ROTATION_90:
				degrees = 90;

				//Toast.makeText(CameraActivity.this,"90",Toast.LENGTH_SHORT).show();

				params.setRotation(0);
				mCamera.setParameters(params);

				CameraActivity.cameraOrientation=0;
				this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
				displayHeight=mDisplayMetrics.heightPixels/4;
				displayWidth=mDisplayMetrics.widthPixels;

				LayoutParams mLayoutParams90=cameraPreview.getLayoutParams();
				mLayoutParams90.width=mDisplayMetrics.widthPixels;
				mLayoutParams90.height=mDisplayMetrics.heightPixels;
				//mVideoView.setLayoutParams(mLayoutParams);
				mPreview.setLayoutParams(mLayoutParams90);
				cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

				break; // Landscape left
			case Surface.ROTATION_180:
				degrees = 180;
				//Toast.makeText(CameraActivity.this,"180",Toast.LENGTH_SHORT).show();
				CameraActivity.cameraOrientation=270;

				params.setRotation(90);
				mCamera.setParameters(params);

				this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
				displayHeight=mDisplayMetrics.heightPixels/4;
				displayWidth=mDisplayMetrics.widthPixels;


				LayoutParams mLayoutParams180=cameraPreview.getLayoutParams();
				mLayoutParams180.width=mDisplayMetrics.widthPixels;
				mLayoutParams180.height=mDisplayMetrics.heightPixels;
				//mVideoView.setLayoutParams(mLayoutParams);
				mPreview.setLayoutParams(mLayoutParams180);
				cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));



				break;// Upside down
			case Surface.ROTATION_270:
				degrees = 270;

				//Toast.makeText(CameraActivity.this,"270",Toast.LENGTH_SHORT).show();

				params.setRotation(180);
				mCamera.setParameters(params);


				CameraActivity.cameraOrientation=180;

				this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
				displayHeight=mDisplayMetrics.heightPixels/4;
				displayWidth=mDisplayMetrics.widthPixels;


				LayoutParams mLayoutParams11=cameraPreview.getLayoutParams();
				mLayoutParams11.width=mDisplayMetrics.widthPixels;
				mLayoutParams11.height=mDisplayMetrics.heightPixels;
				//mVideoView.setLayoutParams(mLayoutParams);
				mPreview.setLayoutParams(mLayoutParams11);
				cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;// Landscape right
		}





	}


	
	public void createFolder(){
		
		
		File folder = new File(Environment.getExternalStorageDirectory() + folderName);
		boolean success = true;
		if (!folder.exists()) {
		    success = folder.mkdir();
		}
		if (success) {
		    // Do something on success
		} else {
		    // Do something else on failure 
		}
	}
	
	
	public void initialize() {
		
		cameraPreview = (RelativeLayout) findViewById(R.id.camera_preview);

		this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		displayHeight=mDisplayMetrics.heightPixels/3;
		displayWidth=mDisplayMetrics.widthPixels;
		

		 int a = Camera.getNumberOfCameras();
		 System.out.println(a);
		 
		 if(hasCamera(this)){
			 mCamera= Camera.open(findFrontCamera());
			 
		 }else{
			 
			 Toast.makeText(this, "Sorry u dont Have any camera", Toast.LENGTH_SHORT).show();
			 switchButtonFlag=false;
		 }
		 /*if(hasCamera(this)){
			 mCamera=Camera.open(findFrontCamera());
			 
			 }*/
		
		mPreview = new CameraPreview(myContext, mCamera);
		LayoutParams mLayoutParams=cameraPreview.getLayoutParams();
		 mLayoutParams.width=mDisplayMetrics.widthPixels;
		 mLayoutParams.height=mDisplayMetrics.heightPixels;
		 //mVideoView.setLayoutParams(mLayoutParams);
		 mPreview.setLayoutParams(mLayoutParams);
		cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//mPreview.setLayoutParams(mLayoutParams);
		cameraPreview.addView(mPreview);



		mCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			try {


				mCamera.takePicture(null, null, mPictureCallback);



				/*mFinalCollageFile = getOutputMediaFile();

				View v1 = mCamerPreviewContainer;




				v1.setDrawingCacheEnabled(true);

				v1.buildDrawingCache(true);
				Bitmap bm = Bitmap.createBitmap(v1.getDrawingCache());
				v1.setDrawingCacheEnabled(false);



				try {



					FileOutputStream out = new FileOutputStream(mFinalCollageFile);
					bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();


					// Toast.makeText(CollageViewer.this, "saved", Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
					e.printStackTrace();
				}*/



			}catch(Exception e){

				Toast.makeText(CameraActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

			}
			}
		});
		
		//effectsButton.setVisibility(View.VISIBLE);
		switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
		switchCamera.setOnClickListener(switchCameraListener);

		params = mCamera.getParameters();
		params.setZoom(0);
		//mCPreviewCall

		//mCamera.setPreviewCallback(new PreviewCall());
	}



	
	
/////////////// Camera related//////////////////

	
	private int findFrontCamera() {
		int cameraId = 0;
		frontCameraFlag=false;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				frontCameraFlag=true;
				break;
			}
		}
		return cameraId;
	}
	
	private int findFrontFacingCamera() {
		int cameraId = -1;
		frontCameraFlag=false;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				frontCameraFlag=true;
				break;
			}
		}
		return cameraId;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				frontCameraFlag=false;
				break;
			}
		}
		return cameraId;
	}

	@Override
	public void onResume() {
		super.onResume();
		

	}

	
	private int count=0;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//--

		super.onBackPressed();
		releaseCamera();


		
		
	}



	

	@Override
	protected void onPause() {
		super.onPause();


		releaseCamera();
	}



	public void chooseCamera() {
		// if the camera preview is the front
		if (cameraFront) {
			int cameraId = findBackFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera,cameraOrientation);
			}
		} else {
			int cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera,cameraOrientation);
			}
		}
	}

	
	
	private boolean hasCamera(Context context) {
		// check if the device has camera
		int a = Camera.getNumberOfCameras();
		//if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
		if(a>0){
			return true;
		} else {
			return false;
		}
	}
	
	private void releaseCamera() {
		// stop and release camera
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
	/////////////// Camera related//////////////////
	
	
	///////////////////////Listners//////////////////////////
	OnClickListener swicthLayoutListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(switchButtonFlag){

				switchButtonFlag=false;
				mCamera.stopPreview();
				cameraPreview.setVisibility(View.INVISIBLE);
				
				
			}else{
				

				switchButtonFlag=true;
				cameraPreview.setVisibility(View.VISIBLE);
				mCamera.startPreview();
				
			}
		}
	};
	

	
	OnClickListener switchCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// get the number of cameras
			if (!recording) {
				int camerasNumber = Camera.getNumberOfCameras();
				if (camerasNumber > 1) {
					// release the old camera instance
					// switch camera, from the front and the back and vice versa

					releaseCamera();
					chooseCamera();
				} else {
					Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}
	};
	
	
	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {


		}
	};


	public File  rotateImage(File f, float angle) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap source = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);


		Bitmap finalBm= Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

		File editedFile=getOutputMediaFile();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(editedFile);
			finalBm.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
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

		f.delete();

		return editedFile;
	}



	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				folderName);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		setCamera();

	}







}
