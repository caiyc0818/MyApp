package com.bcinfo.tripaway.view;


import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FilterLayout extends LinearLayout {

	private String TAG = "FilterLayout";

	private Context mContext;

	private RelativeLayout infoRootLayout ;
	

	
	private CompoundButton currentDestinationView ;

	ImageLoader imageLoader = ImageLoader.getInstance();

	private FlowLayout destinationFlowLayout;

	private LinearLayout destinationLayout;

	private FlowLayout priceFlowLayout;

	private LinearLayout priceLayout;

	private FlowLayout daysFlowLayout;

	private LinearLayout daysLayout;
	
	private CompoundButton currentPriceView;
	
	private CompoundButton currentDaysView;

	private TextView text1;

	private TextView text2;

	private TextView text3;
	


	private boolean isText1MutiChoice=false;
	private boolean isText2MutiChoice=false;
	private boolean isText3MutiChoice=false;
	
	public FilterLayout(Context context) {
		super(context);
		mContext = context;
	}

	public FilterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		if (isInEditMode()) {
			return;
		}
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.filter, this);
		findView();
	}

	   private void findView(){
	    	destinationFlowLayout=(FlowLayout) findViewById(R.id.destination);
	    	destinationLayout=(LinearLayout) findViewById(R.id.destinationLayout);
	    	priceFlowLayout=(FlowLayout) findViewById(R.id.price);
	    	priceLayout=(LinearLayout) findViewById(R.id.priceLayout);
	    	daysFlowLayout=(FlowLayout)findViewById(R.id.days);
	    	daysLayout=(LinearLayout) findViewById(R.id.daysLayout);
	    	text1=(TextView) findViewById(R.id.text1);
	    	text2=(TextView) findViewById(R.id.text2);
	    	text3=(TextView) findViewById(R.id.text3);
	    }
	    public void addTextValue(List<String> toStrs,List<String> fromStrs1,List<String> fromStrs2,List<String> fromStrs3){
	    	if(fromStrs1.size()>0){
	    		for(int i=0;i<destinationFlowLayout.getChildCount();i++){
	    			CompoundButton	 v=(CompoundButton)destinationFlowLayout.getChildAt(i);
	    			if(v.isChecked()){
	    				toStrs.add(fromStrs1.get(i));
	    			}
	    		}
	    	}
	    	
	    	if(fromStrs2.size()>0){
	    		for(int i=0;i<priceFlowLayout.getChildCount();i++){
	    			CompoundButton	 v=(CompoundButton)priceFlowLayout.getChildAt(i);
	    			if(v.isChecked()){
	    				toStrs.add(fromStrs2.get(i));
	    			}
	    		}
	    	}
	    	
	    	if(fromStrs3.size()>0){
	    		for(int i=0;i<daysLayout.getChildCount();i++){
	    			CompoundButton	 v=(CompoundButton)daysLayout.getChildAt(i);
	    			if(v.isChecked()){
	    				toStrs.add(fromStrs3.get(i));
	    			}
	    		}
	    	}
	    }
	    
	   public void setText1(String str){
		   text1.setText(str);
		   
	   }
	   
	   public void setText2(String str){
		   text2.setText(str);
		   
	   }
	   
	   
	   public void setText3(String str){
		   text3.setText(str);
		   
	   }
	   
	    public void setDestinationValue(List<String> strs){
	    	addDestinationFlowView( strs, destinationFlowLayout);
	    }
	    
	    public void setDestinationLayoutGone(){
	    	destinationLayout.setVisibility(View.GONE);
	    }
	    public void setPriceValue(List<String> strs){
	    	addPriceFlowView( strs, priceFlowLayout);
	    }
	    
	    public void setPriceLayoutGone(){
	    	priceLayout.setVisibility(View.GONE);
	    }
	    
	    public void setDaysValue(List<String> strs){
	    	addDaysFlowView( strs, daysFlowLayout);
	    }
	    
	    public void setDaysLayoutGone(){
	    	daysLayout.setVisibility(View.GONE);
	    }
	    
	 private void addDestinationFlowView(final List<String> strs, FlowLayout flowLayout) {
			for (int i = 0; i < strs.size(); i++) {
				CompoundButton newView ;
				if(isText1MutiChoice){
					 newView = new CheckBox(((Activity) getContext()));
				}else
				 newView = new RadioButton(((Activity) getContext()));
				newView.setBackgroundResource(R.drawable.square);
				newView.setText(strs.get(i));
				newView.setButtonDrawable(android.R.color.transparent);
				newView.setTag(i);
				newView.setTypeface(TripawayApplication.normalTf);
				newView.setGravity(Gravity.CENTER);
				newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				newView.setTextColor(Color.BLACK);
				
				newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton mCompoundButton, boolean isChecked) {
						// TODO Auto-generated method stub
						int i=(Integer)(mCompoundButton.getTag());
						if(isChecked){
							mCompoundButton.setTextColor(Color.WHITE);
						
							if(currentDestinationView==null){
								
							}else {
								if(!isText1MutiChoice){
									
								currentDestinationView.setTextColor(Color.BLACK);
								currentDestinationView.setChecked(false);
								}else {
									
								}
								
							}
							currentDestinationView=mCompoundButton;
							if(onDestinationListener!=null){
								onDestinationListener.OnCheck(strs.get(i),i,true);
							}
						}else {
							mCompoundButton.setTextColor(Color.BLACK);
							if(isText1MutiChoice){
								if(onDestinationListener!=null){
									onDestinationListener.OnCheck(strs.get(i),i,false);
								}
							}
						}
					}
				});
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						DensityUtil.dip2px(((Activity) getContext()), 80),
						DensityUtil.dip2px(((Activity) getContext()), 29));
				params.rightMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				params.bottomMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				newView.setLayoutParams(params);
				flowLayout.addView(newView);
			}
		}
	 
	 private void addPriceFlowView(final List<String> strs, FlowLayout flowLayout) {
			for (int i = 0; i < strs.size(); i++) {
				CompoundButton newView ;
				if(isText2MutiChoice){
					 newView = new CheckBox(((Activity) getContext()));
				}else
					newView = new RadioButton(((Activity) getContext()));
				newView.setBackgroundResource(R.drawable.square);
				newView.setText(strs.get(i));
				newView.setButtonDrawable(android.R.color.transparent);
				newView.setTag(i);
				newView.setTypeface(TripawayApplication.normalTf);
				newView.setGravity(Gravity.CENTER);
				newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				newView.setTextColor(Color.BLACK);
				
				newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					

					@Override
					public void onCheckedChanged(CompoundButton mCompoundButton, boolean isChecked) {
						// TODO Auto-generated method stub
						int i=(Integer)(mCompoundButton.getTag());
						if(isChecked){
							mCompoundButton.setTextColor(Color.WHITE);
							
							if(currentPriceView==null){
								
							}else {
								if(!isText2MutiChoice){
									
								currentPriceView.setTextColor(Color.BLACK);
								currentPriceView.setChecked(false);}
							}
							currentPriceView=mCompoundButton;
							if(onPriceListener!=null){
								onPriceListener.OnCheck(strs.get(i),i,true);
							}
						}else {
							mCompoundButton.setTextColor(Color.BLACK);
							if(isText2MutiChoice){
								if(onDestinationListener!=null){
									onPriceListener.OnCheck(strs.get(i),i,false);
								}
							}
						} 
					}
				});
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						DensityUtil.dip2px(((Activity) getContext()), 80),
						DensityUtil.dip2px(((Activity) getContext()), 29));
				params.rightMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				params.bottomMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				newView.setLayoutParams(params);
				flowLayout.addView(newView);
			}
		}
	 
	 private void addDaysFlowView(final List<String> strs, FlowLayout flowLayout) {
			for (int i = 0; i < strs.size(); i++) {
				CompoundButton newView ;
				if(isText3MutiChoice){
					 newView = new CheckBox(((Activity) getContext()));
				}else
					newView = new RadioButton(((Activity) getContext()));
				newView.setBackgroundResource(R.drawable.square);
				newView.setText(strs.get(i));
				newView.setButtonDrawable(android.R.color.transparent);
				newView.setTag(i);
				newView.setTypeface(TripawayApplication.normalTf);
				newView.setGravity(Gravity.CENTER);
				newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				newView.setTextColor(Color.BLACK);
				
				newView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton mCompoundButton, boolean isChecked) {
						// TODO Auto-generated method stub
						int i=(Integer)(mCompoundButton.getTag());
						if(isChecked){
							mCompoundButton.setTextColor(Color.WHITE);
							
							if(currentDaysView==null){
								
							}else {
								if(!isText3MutiChoice){
									
								currentDaysView.setChecked(false);
								currentDaysView.setTextColor(Color.BLACK);
								}
							}
							currentDaysView=mCompoundButton;
							if(onDaysListener!=null){
								onDaysListener.OnCheck(strs.get(i),i,true);
							}
						}else {
							mCompoundButton.setTextColor(Color.BLACK);
							if(isText3MutiChoice){
								if(onDestinationListener!=null){
									onDaysListener.OnCheck(strs.get(i),i,false);
								}
							}
						} 
					}
				});
				FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
						DensityUtil.dip2px(((Activity) getContext()), 80),
						DensityUtil.dip2px(((Activity) getContext()), 29));
				params.rightMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				params.bottomMargin = DensityUtil.dip2px(((Activity) getContext()), 5);
				newView.setLayoutParams(params);
				flowLayout.addView(newView);
			}
		}
	 
	 
	 public interface OnDestinationListener{
		 public void OnCheck(String Value,int position,boolean state);
	 }
	 
	 private OnDestinationListener onDestinationListener;
	 public void setOnDestinationListener(OnDestinationListener onDestinationListener){
		 this.onDestinationListener=onDestinationListener;
	 }
	 
	 public interface OnDaysListener{
		 public void OnCheck(String Value,int position,boolean state);
	 }
	 
	 private OnDaysListener onDaysListener;
	 public void setOnDaysListener(OnDaysListener onDaysListener){
		 this.onDaysListener=onDaysListener;
	 }
	 
	 public interface OnPriceListener{
		 public void OnCheck(String Value,int position,boolean state);
	 }
	 
	 private OnPriceListener onPriceListener;
	 public void setonPriceListener(OnPriceListener onPriceListener){
		 this.onPriceListener=onPriceListener;
	 }
	 
	 public void setMutiChoice(boolean choice1,boolean choice2,boolean choice3){
		 this.isText1MutiChoice=choice1;
		 this.isText2MutiChoice=choice2;
		 this.isText3MutiChoice=choice3;
	 }
	 
	 public void resetState(){
		 if(currentDaysView!=null){
			currentDaysView.setChecked(false);
			currentDaysView.setTextColor(Color.BLACK);
			currentDaysView=null;
			}
		 if(currentPriceView!=null){
			currentPriceView.setChecked(false);
			currentPriceView.setTextColor(Color.BLACK);
			currentPriceView=null;
		 }
		 if(currentDestinationView!=null){
			currentDestinationView.setChecked(false);
			currentDestinationView.setTextColor(Color.BLACK);
			currentDestinationView=null;
		 }
		 if(isText3MutiChoice){
			int count= daysFlowLayout.getChildCount();
			for(int i=0;i<count;i++){
				CheckBox view=(CheckBox) daysFlowLayout.getChildAt(i);
				if(view.isChecked()){
					view.setChecked(false);
				}
			}
		 }
	 }
}
