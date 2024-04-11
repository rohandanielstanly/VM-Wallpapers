package com.vm.wallpapers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import com.google.android.material.navigation.NavigationBarView;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.animation.AnimationUtils;


public class MainActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private String FNAME = "";
	private  TextView textView;
	private  Animation animate;
	
	private ConstraintLayout base;
	private FrameLayout frame;
	private BottomNavigationView bottomnavigation1;
	
	private Intent intent = new Intent();
	
	private OnCompleteListener fcm_onCompleteListener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		getSupportFragmentManager().beginTransaction().replace(R.id.frame, new WallpapersFragmentActivity()).commit();
		
		animate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		base = findViewById(R.id.base);
		frame = findViewById(R.id.frame);
		bottomnavigation1 = findViewById(R.id.bottomnavigation1);
		
		//no listener code
		
		fcm_onCompleteListener = new OnCompleteListener<InstanceIdResult>() {
			@Override
			public void onComplete(Task<InstanceIdResult> task) {
				final boolean _success = task.isSuccessful();
				final String _token = task.getResult().getToken();
				final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
				
			}
		};
	}
	
	private void initializeLogic() {
		_onCreate_();
	}
	
	public void _onCreate_() {
		_designs();
		//Please dont forget to watch custom variables
		
		
	}
	
	
	public void _designs() {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
		//setting toolbar text color and background color
		_toolbar.setTitleTextColor(Color.BLACK);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		//
		
		//setting toolbar title and title font and title animation
		setTitle("Wallpapers");
		
		for(int i = 0; i < _toolbar.getChildCount(); i++) { 
				View view = _toolbar.getChildAt(i);
			    if(view instanceof TextView) {
				    
					textView = (TextView) view;
					textView.startAnimation(animate);
				
				    Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/manrope_bold.otf");
				    textView.setTypeface(myCustomFont); 
				    }
			
		}
		//
		bottomnavigation1.getMenu().add(0, 0, 0, "Wallpapers").setIcon(R.drawable.outline_wallpaper);
		
		bottomnavigation1.getMenu().add(0, 1, 0, "Collections").setIcon(R.drawable.outline_source);
		
		bottomnavigation1.getMenu().add(0, 2, 0, "Favorites").setIcon(R.drawable.outline_book);
		//bottomnavigation1 menu item 0 is checked
		bottomnavigation1.getMenu().getItem(0).setChecked(true);
	}
	
	
	public void _more() {
		
	}
	
	
	public void _textview(final TextView _t, final String _s) {
		FNAME = "fonts/".concat(_s.concat(".otf"));
		try {
			_t.setTypeface(Typeface.createFromAsset(getAssets(), FNAME), Typeface.NORMAL);
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error!");
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
