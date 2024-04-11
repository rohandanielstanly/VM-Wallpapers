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
import java.util.ArrayList;
import java.util.HashMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import java.util.Timer;
import java.util.TimerTask;
import com.bumptech.glide.Glide;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.DialogFragment;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import androidx.core.app.ActivityOptionsCompat;
import com.bumptech.glide.request.target.Target;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.load.DataSource;
import androidx.core.view.ViewCompat;
import androidx.annotation.Nullable;
import android.transition.Fade;


public class CollectionsActivity extends AppCompatActivity {

	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private double n = 0;
	private String url = "";

	private ArrayList<HashMap<String, Object>> listmap1 = new ArrayList<>();

	private ConstraintLayout base;
	private SwipeRefreshLayout swiperefreshlayout1;
	private RecyclerView recyclerview1;

	private DatabaseReference wallpapersdata = _firebase.getReference("wallpapersdata");
	private ChildEventListener _wallpapersdata_child_listener;
	private TimerTask timer;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.collections);
		initialize(_savedInstanceState);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initializeLogic();
	}

	private void initialize(Bundle _savedInstanceState) {
		Fade fade = new Fade();
		View decor = CollectionsActivity.this.getWindow().getDecorView();
		fade.excludeTarget(android.R.id.statusBarBackground, true);
		fade.excludeTarget(android.R.id.navigationBarBackground, true);
		CollectionsActivity.this.getWindow().setEnterTransition(fade);
		CollectionsActivity.this.getWindow().setExitTransition(fade);
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
		swiperefreshlayout1 = findViewById(R.id.swiperefreshlayout1);
		recyclerview1 = findViewById(R.id.recyclerview1);

		swiperefreshlayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
				timer = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								swiperefreshlayout1.setRefreshing(false);
							}
						});
					}
				};
				_timer.schedule(timer, (int)(2000));
			}
		});

		_wallpapersdata_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("collections")) {
					if (_childValue.get("collections").toString().equals(getIntent().getStringExtra("collections"))) {
						setTitle(getIntent().getStringExtra("collections"));
						listmap1.add((int)0, _childValue);
						recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
					}
				}
			}

			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				if (_childValue.containsKey("collections")) {
					if (_childValue.get("collections").toString().equals(getIntent().getStringExtra("collections"))) {
						n = 0;
						for(int _repeat17 = 0; _repeat17 < (int)(listmap1.size()); _repeat17++) {
							if (listmap1.get((int)n).get("key").toString().equals(_childKey)) {
								listmap1.remove((int)(n));
								listmap1.add((int)0, _childValue);
							}
							n++;
						}
						recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
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
				if (_childValue.containsKey("collections")) {
					if (_childValue.get("collections").toString().equals(getIntent().getStringExtra("collections"))) {
						n = 0;
						for(int _repeat16 = 0; _repeat16 < (int)(listmap1.size()); _repeat16++) {
							if (listmap1.get((int)n).get("key").toString().equals(_childKey)) {
								listmap1.remove((int)(n));
							}
							n++;
						}
						recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
					}
				}
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
		overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
		finish();
	}
	public void _onCreate_() {
		_designs();

		recyclerview1.setNestedScrollingEnabled(false);


		if(CollectionsActivity.this.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){

			recyclerview1.setLayoutManager(new GridLayoutManager(CollectionsActivity.this, 2));

		} else{

			recyclerview1.setLayoutManager(new GridLayoutManager(CollectionsActivity.this, 4));

		}

		recyclerview1.setHasFixedSize(true);
	}


	public void _designs() {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(0xFFFFFFFF);
		_toolbar.setTitleTextColor(Color.BLACK);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

		final Drawable upArrow = getResources().getDrawable(R.drawable.outline_arrow_back);
		upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);

		for(int i = 0; i < _toolbar.getChildCount(); i++) {
				View view = _toolbar.getChildAt(i);
			    if(view instanceof TextView) {

					TextView textView = (TextView) view;
					//textView.startAnimation(animate);

				    Typeface myCustomFont=Typeface.createFromAsset(getAssets(),"fonts/manrope_bold.otf");
				    textView.setTypeface(myCustomFont);
				    }

		}
	}


	public void _more() {
	}
	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if(CollectionsActivity.this.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){

			recyclerview1.setLayoutManager(new GridLayoutManager(CollectionsActivity.this, 2));

		} else{

			recyclerview1.setLayoutManager(new GridLayoutManager(CollectionsActivity.this, 4));

		}

	}

	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {

		ArrayList<HashMap<String, Object>> _data;

		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _inflater.inflate(R.layout.wallpapers_cus, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}

		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;

			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final androidx.constraintlayout.widget.ConstraintLayout base = _view.findViewById(R.id.base);
			final ImageView image = _view.findViewById(R.id.image);
			final LinearLayout progress_container = _view.findViewById(R.id.progress_container);
			final ProgressBar progressbar1 = _view.findViewById(R.id.progressbar1);

			if (_data.get((int)_position).containsKey("wallpaper_url")) {
				url = _data.get((int)_position).get("wallpaper_url").toString();
			}
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);

			_view.startAnimation(animation);

			Glide.with(CollectionsActivity.this)
			.load(url)
			.transition(withCrossFade())
			.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
			.listener(new RequestListener<Drawable>() {
				@Override
				public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

						image.setVisibility(View.VISIBLE);
						progress_container.setVisibility(View.GONE);
						return false;
					 }
				@Override
				public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
					progress_container.setVisibility(View.GONE);
					image.setVisibility(View.VISIBLE);
					return false;

				}
			}).into(image);
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					Intent intent = new Intent(CollectionsActivity.this, WallyViewerActivity.class);
					intent.putExtra("key", _data.get((int)_position).get("key").toString());
					ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CollectionsActivity.this, image, ViewCompat.getTransitionName(image));
					startActivity(intent, options.toBundle());
				}
			});
		}

		@Override
		public int getItemCount() {
			return _data.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
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
