package com.vm.wallpapers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class WallyViewerActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private boolean favoriteIsChecked = false;
	private String FNAME = "";
	private String url = "";
	private String key = "";
	private String title = "";
	private String license = "";
	private  int dmColor;
	private  Bitmap bitmap;
	private double pos = 0;
	private double n = 0;
	private double n1 = 0;
	private HashMap<String, Object> map1 = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> favoriteList = new ArrayList<>();
	
	private ConstraintLayout base;
	private ProgressBar progressbar1;
	private ImageView imageview1;
	private LinearLayout bottom_container;
	private LinearLayout toolbar_container;
	private BottomNavigationView bottomnavigation1;
	private Toolbar toolbar;
	
	private ObjectAnimator fadeIn = new ObjectAnimator();
	private ObjectAnimator fadeOut = new ObjectAnimator();
	private DatabaseReference wallpapersdata = _firebase.getReference("wallpapersdata");
	private ChildEventListener _wallpapersdata_child_listener;
	private AlertDialog.Builder dialog1;
	private SharedPreferences favoritelistData;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.wally_viewer);
		initialize(_savedInstanceState);
		com.google.firebase.FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		//described before
		Fade fade = new Fade(); 
		View decor = getWindow().getDecorView(); 
//		fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
		fade.excludeTarget(android.R.id.statusBarBackground, true);
		fade.excludeTarget(android.R.id.navigationBarBackground, true);
		getWindow().setEnterTransition(fade); 
		getWindow().setExitTransition(fade);
		base = findViewById(R.id.base);
		progressbar1 = findViewById(R.id.progressbar1);
		imageview1 = findViewById(R.id.imageview1);
		bottom_container = findViewById(R.id.bottom_container);
		toolbar_container = findViewById(R.id.toolbar_container);
		bottomnavigation1 = findViewById(R.id.bottomnavigation1);
		toolbar = findViewById(R.id.toolbar);
		dialog1 = new AlertDialog.Builder(this);
		favoritelistData = getSharedPreferences("favoritelistData", Activity.MODE_PRIVATE);
		
		//no listener code
		
		_wallpapersdata_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (key.equals(_childKey)) {
					if (_childValue.containsKey("title")) {
						title = _childValue.get("title").toString();
						setTitle(title);
					}
					if (_childValue.containsKey("license")) {
						license = _childValue.get("license").toString();
					}
					if (_childValue.containsKey("wallpaper_url")) {
						url = _childValue.get("wallpaper_url").toString();
						Glide.with(getApplicationContext())
						.load(url)
						.transition(withCrossFade())
						//.skipMemoryCache(true)
						.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
						.listener(new RequestListener<Drawable>() { 
							@Override 
							public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { 
									
									imageview1.setVisibility(View.GONE); 
									progressbar1.setVisibility(View.VISIBLE); 
									return false;
								 } 
							@Override 
							public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
								progressbar1.setVisibility(View.GONE);
								if(resource != null) {
									imageview1.setVisibility(View.VISIBLE);
								}
								bitmap = ((BitmapDrawable)resource).getBitmap();
								base.setBackgroundColor(getDominantColor(bitmap));
								dmColor = getDominantColor(bitmap);
								return false; 
								
							}
						}).into(imageview1);
					}
				}
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (key.equals(_childKey)) {
					if (_childValue.containsKey("title")) {
						title = _childValue.get("title").toString();
						setTitle(title);
					}
					if (_childValue.containsKey("license")) {
						license = _childValue.get("license").toString();
					}
					if (_childValue.containsKey("wallpaper_url")) {
						url = _childValue.get("wallpaper_url").toString();
						Glide.with(getApplicationContext())
						.load(url)
						.transition(withCrossFade())
						.skipMemoryCache(true) 
						.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
						.listener(new RequestListener<Drawable>() { 
							@Override 
							public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { 
									
									imageview1.setVisibility(View.GONE); 
									progressbar1.setVisibility(View.VISIBLE); 
									return false;
								 } 
							@Override 
							public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
								progressbar1.setVisibility(View.GONE);
								if(resource != null) {
									imageview1.setVisibility(View.VISIBLE);
								}
								Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
								base.setBackgroundColor(getDominantColor(bitmap));
								return false; 
								
							}
						}).into(imageview1);
					}
				}
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		wallpapersdata.addChildEventListener(_wallpapersdata_child_listener);
	}
	
	private void initializeLogic() {
		_onCreate_();
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finishAfterTransition();
	}
	public void _onCreate_() {
		key = getIntent().getStringExtra("key");
		_designs();
		if (!favoritelistData.getString("data", "").equals("")) {
			favoriteList = new Gson().fromJson(favoritelistData.getString("data", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			n1 = 0;
			for(int _repeat26 = 0; _repeat26 < (int)(favoriteList.size()); _repeat26++) {
				if (favoriteList.get((int)n1).get("key").toString().equals(key)) {
					favoriteIsChecked = true;
				}
				n1++;
			}
		}
	}
	
	
	public void _designs() {
		
		toolbar.setElevation((float)0);
		
		bottomnavigation1.setElevation((float)0);
		
		//setting our manual toolbar layout as a actionbar and setting color and other things
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setTitleTextColor(Color.WHITE);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back); 
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		//
		//adding a shadow to the bottom_container
		bottom_container.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bottom));
		toolbar_container.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_top));
		bottomnavigation1.getMenu().add(0, 0, 0, "Info").setIcon(R.drawable.outline_info);
		bottomnavigation1.getMenu().add(0, 1, 0, "Download").setIcon(R.drawable.outline_get_app);
		bottomnavigation1.getMenu().add(0, 2, 0, "Apply").setIcon(R.drawable.outline_system_update);
		//
		//making the bottomnavigation1 menu not selected for first time
		int size = bottomnavigation1.getMenu().size()-1;
		    for (int i = 0; i < size; i++) {
			        bottomnavigation1.getMenu().getItem(i).setChecked(false);
			    }
		//
		//setting a custom font for the toolbar
		
		for(int i = 0; i < toolbar.getChildCount(); i++) { 
				View view = toolbar.getChildAt(i);
			    if(view instanceof TextView) {
				    
					TextView textView = (TextView) view;
					
				    Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/manrope_bold.otf");
				    textView.setTypeface(myCustomFont); 
				    }
			
		}
		//
	}
	
	
	public void _more() {
	}
	public static void setWindowFlag(Activity activity, final int bits, boolean on) { 
			Window win = activity.getWindow(); 
			WindowManager.LayoutParams winParams = win.getAttributes(); 
			if (on) { 
					winParams.flags |= bits; 
				} else { 
						winParams.flags &= ~bits;
						 } 
					 win.setAttributes(winParams); 
	}
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_favorites, menu);
		
		
		final MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
		
		final Drawable favoriteIcon1 = getResources().getDrawable(R.drawable.outline_favorite_border);
		final Drawable favoriteIcon2 = getResources().getDrawable(R.drawable.outline_favorite);
		//getting if item is in favorite list or not and setting a icon accordingly
		if(favoriteIsChecked){
				favoriteItem.setIcon(favoriteIcon2);
		}else{
				favoriteItem.setIcon(favoriteIcon1);
		}
		
		return true; 
	}
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
		int id = item.getItemId();
		
		if (id == R.id.action_favorite) { 
			//performing on favoriteIsChecked
			if(favoriteIsChecked){
				Drawable favoriteIcon = getResources().getDrawable(R.drawable.outline_favorite_border); 
				item.setIcon(favoriteIcon);
				favoriteIsChecked = false;
				_addToFavorite(false);
			}else{
				Drawable favoriteIcon = getResources().getDrawable(R.drawable.outline_favorite); 
				item.setIcon(favoriteIcon);
				favoriteIsChecked = true;
				_addToFavorite(true);
			}
			return true; 
		}
		
		return super.onOptionsItemSelected(item);
	}
	//getting the most visible color
	public static int getDominantColor(Bitmap bitmap) {
		    if (bitmap == null) {
			        return Color.TRANSPARENT;
			    }
		    int width = bitmap.getWidth();
		    int height = bitmap.getHeight();
		    int size = width * height;
		    int pixels[] = new int[size];
		    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		    int color;
		    int r = 0;
		    int g = 0;
		    int b = 0;
		    int a;
		    int count = 0;
		    for (int i = 0; i < pixels.length; i++) {
			        color = pixels[i];
			        a = Color.alpha(color);
			        if (a > 0) {
				            r += Color.red(color);
				            g += Color.green(color);
				            b += Color.blue(color);
				            count++;
				        }
			    }
		    r /= count;
		    g /= count;
		    b /= count;
		    r = (r << 16) & 0x00FF0000;
		    g = (g << 8) & 0x0000FF00;
		    b = b & 0x000000FF;
		    color = 0xFF000000 | r | g | b;
		    return color;
		
	}
	
	
	public void _showMaterialSnackbar(final String _title, final String _action, final boolean _t, final View _v) {
		Snackbar snackbar = Snackbar.make(_v, _title, Snackbar.LENGTH_LONG);
		TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/manrope_medium.otf");
		tv.setTypeface(font); //setting fonts for snackbar 
		if(_t){
			snackbar.setAction(_action, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					//Toast.makeText(MainActivity.this, "The item has been restored", Toast.LENGTH_SHORT).show();
					
				}
			});
		}
		snackbar.setDuration(3000);
		
		snackbar.show();
	}
	
	
	public void _textview(final TextView _t, final String _s) {
		FNAME = "fonts/".concat(_s.concat(".ttf"));
		try {
			_t.setTypeface(Typeface.createFromAsset(getAssets(), FNAME), Typeface.NORMAL);
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error!");
		}
	}
	
	
	public void _download_wallpaper(final String _link) {
		if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/Downloads/Vmwallapapers/"))) {
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/Downloads/Vmwallapapers/"));
		}
		DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri downloadUri = Uri.parse(_link);
		DownloadManager.Request request = new DownloadManager.Request(downloadUri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		
		request.setDestinationInExternalPublicDir("/Downloads/Vmwallapapers", title.concat("_vm.jpg"));
		mgr.enqueue(request);
		
	}
	
	
	public void _addToFavorite(final boolean _t) {
		if (_t) {
			map1 = new HashMap<>();
			map1.put("key", key);
			map1.put("wallpaper_url", url);
			favoriteList.add(map1);
			favoritelistData.edit().putString("data", new Gson().toJson(favoriteList)).commit();
			_showMaterialSnackbar("Added to favorite", "", false, imageview1);
		}
		else {
			if (!favoritelistData.getString("data", "").equals("")) {
				favoriteList = new Gson().fromJson(favoritelistData.getString("data", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				n1 = 0;
				for(int _repeat27 = 0; _repeat27 < (int)(favoriteList.size()); _repeat27++) {
					if (favoriteList.get((int)n1).get("key").toString().equals(key)) {
						favoriteList.remove((int)(n1));
						favoritelistData.edit().putString("data", new Gson().toJson(favoriteList)).commit();
						_showMaterialSnackbar("Removed from favorite", "", false, imageview1);
					}
					n1++;
				}
			}
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
