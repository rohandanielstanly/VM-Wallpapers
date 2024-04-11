package com.vm.wallpapers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;


public class FavoritesFragmentActivity extends Fragment {
	
	private String url = "";
	private double n = 0;
	
	private ArrayList<HashMap<String, Object>> listmap1 = new ArrayList<>();
	
	private ConstraintLayout base;
	private SwipeRefreshLayout swiperefreshlayout1;
	private RecyclerView recyclerview1;
	
	private SharedPreferences favoritelistData;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.favorites_fragment, _container, false);
		initialize(_savedInstanceState, _view);
		com.google.firebase.FirebaseApp.initializeApp(getContext());
		initializeLogic();
		return _view;
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		Fade fade = new Fade(); 
		View decor = getActivity().getWindow().getDecorView(); 
		fade.excludeTarget(android.R.id.statusBarBackground, true);
		fade.excludeTarget(android.R.id.navigationBarBackground, true);
		getActivity().getWindow().setEnterTransition(fade); 
		getActivity().getWindow().setExitTransition(fade);
		base = _view.findViewById(R.id.base);
		swiperefreshlayout1 = _view.findViewById(R.id.swiperefreshlayout1);
		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		favoritelistData = getContext().getSharedPreferences("favoritelistData", Activity.MODE_PRIVATE);
		
		swiperefreshlayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
				swiperefreshlayout1.setRefreshing(false);
			}
		});
	}
	
	private void initializeLogic() {
		_onCreate_();
	}
	
	public void _onCreate_() {
		
		recyclerview1.setNestedScrollingEnabled(false);
		
		
		if(getActivity().getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT){ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2)); 
			
		} else{ 
				
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 4)); 
			
		}
		
		recyclerview1.setHasFixedSize(true);
		if (favoritelistData.getString("data", "").equals("[]") || favoritelistData.getString("data", "").equals("")) {
			
		}
		else {
			listmap1 = new Gson().fromJson(favoritelistData.getString("data", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			recyclerview1.setAdapter(new Recyclerview1Adapter(listmap1));
		}
	}
	
	
	public void _more() {
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
			}
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					Intent intent = new Intent(getContext().getApplicationContext(), WallyViewerActivity.class);
					intent.putExtra("key", _data.get((int)_position).get("key").toString());
					ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, ViewCompat.getTransitionName(image)); 
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
