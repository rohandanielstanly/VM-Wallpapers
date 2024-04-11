package com.vm.wallpapers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;


public class WallpapersFragmentActivity extends Fragment {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private String url = "";
	private double n = 0;
	private String key = "";
	
	private ArrayList<HashMap<String, Object>> listmap1 = new ArrayList<>();
	
	private ConstraintLayout base;
	private SwipeRefreshLayout swiperefreshlayout1;
	private RecyclerView recyclerview1;
	
	private DatabaseReference wallpapersdata = _firebase.getReference("wallpapersdata");
	private ChildEventListener _wallpapersdata_child_listener;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.wallpapers_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		com.google.firebase.FirebaseApp.initializeApp(getContext());
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		//Animations has been set when user will click any items it will show a super cool Animation :)
		Fade fade = new Fade(); 
		View decor = getActivity().getWindow().getDecorView(); 
		fade.excludeTarget(android.R.id.statusBarBackground, true);
		fade.excludeTarget(android.R.id.navigationBarBackground, true);
		getActivity().getWindow().setEnterTransition(fade); 
		getActivity().getWindow().setExitTransition(fade);
		base = _view.findViewById(R.id.base);
		swiperefreshlayout1 = _view.findViewById(R.id.swiperefreshlayout1);
		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		
		swiperefreshlayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
				swiperefreshlayout1.setRefreshing(false);
			}
		});
		
		_wallpapersdata_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				listmap1.add((int)0, _childValue);
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				n = 0;
				for(int _repeat11 = 0; _repeat11 < (int)(listmap1.size()); _repeat11++) {
					if (listmap1.get((int)n).get("key").toString().equals(_childKey)) {
						listmap1.remove((int)(n));
						listmap1.add((int)n, _childValue);
					}
					n++;
				}
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				n = 0;
				for(int _repeat11 = 0; _repeat11 < (int)(listmap1.size()); _repeat11++) {
					if (listmap1.get((int)n).get("key").toString().equals(_childKey)) {
						listmap1.remove((int)(n));
					}
					n++;
				}
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
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
	
	public void _onCreate_() {
		
		recyclerview1.setNestedScrollingEnabled(false);
		
		//knowing if device is rotated or not if rotated then the recyclerview will be converted into four items or it will show two items in a row
		if(getActivity().getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2)); 
			
		} else{ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 4)); 
			
		}
		
		recyclerview1.setHasFixedSize(true);
	}
	
	
	public void _more() {
		//it's also gets device is rotated or not. but it is needed to work when user in the current fragment
	}
	@Override 
	public void onConfigurationChanged(android.content.res.Configuration newConfig) { 
		super.onConfigurationChanged(newConfig);
		
		if(getActivity().getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2)); 
			
		} else{ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 4)); 
			
		}
		
	}
	
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			//creating an animation and using it in recyclerview viewgroups to animate items
			Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), android.R.anim.fade_in);
						
			_view.startAnimation(animation);
			
			if (getActivity() != null) {
				Glide.with(getContext().getApplicationContext())
				.load(url)
				.transition(withCrossFade())
				.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
				.listener(new RequestListener<Drawable>() { 
					@Override 
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { 
							//if load failed then image will be visible and progress_container will be gone
							image.setVisibility(View.VISIBLE); 
							progress_container.setVisibility(View.GONE); 
							return false;
						 } 
					@Override 
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						//if loaded then image will be visible progress_container will be gone
						progress_container.setVisibility(View.GONE);
						image.setVisibility(View.VISIBLE);
						return false; 
						
					}
				}).into(image);
			}
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					//it will start the animation to open an wallpaper
					Intent intent = new Intent(getContext().getApplicationContext(), WallyViewerActivity.class); 
					ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, ViewCompat.getTransitionName(image)); 
					intent.putExtra("key", _data.get((int)_position).get("key").toString());
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
}
