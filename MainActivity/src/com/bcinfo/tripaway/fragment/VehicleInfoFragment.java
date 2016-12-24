package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bcinfo.imageviewer.activity.ImageViewerActivity;
import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity.OnLoadMoreListener;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.ImageInfo;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wefika.flowlayout.FlowLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class  VehicleInfoFragment extends Fragment  
{  
    private TextView but = null; 
    private CarExt carInfo;
	private TextView nameTextView;
	private TextView seatNumTextView;
	private TextView descTextView;
	private TextView touristNumTextView;
	private TextView luggageBigNumTextView;
	private TextView luggageSmallNumTextView;
	private FlowLayout vehicleFlowLayout;
	private TextView picNumTextView;
	private ImageView carPic;
	private ImageView vehicleIcon;
	private TextView vehicleTypeTextView;
	private ImageView noImageView;
	private LinearLayout vehicle_layout;
    
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.vehicle_info_fragment,null);//注意不要指定父视图  
        //but = (TextView) view.findViewById(R.id.vipNum);  
        findView ( view);
        setValue();
        return view;  
    }  
    
    public VehicleInfoFragment(CarExt carInfo){
    	 this.carInfo=carInfo;
    }
    
    @Override  
    public void onActivityCreated(Bundle savedInstanceState)  
    {  
        super.onActivityCreated(savedInstanceState);  
//        but.setOnClickListener(new OnClickListener()  
//        {  
//            @Override  
//            public void onClick(View v)  
//            {  
//                Toast.makeText(VehicleInfoFragment.this.getActivity(),"hahah", 0).show();  
//            }  
//        }); 
        
    }  
    
    private void findView (View view ){
    	noImageView = (ImageView) view.findViewById(R.id.noimage);
    	nameTextView = (TextView) view.findViewById(R.id.vehicle_name);
    	descTextView = (TextView) view.findViewById(R.id.desc);
    	seatNumTextView = (TextView) view.findViewById(R.id.seatNum);
    	vehicleTypeTextView = (TextView) view.findViewById(R.id.vehicleType);
    	picNumTextView = (TextView) view.findViewById(R.id.pic_num);
    	luggageBigNumTextView = (TextView) view.findViewById(R.id.luggageBigNum);
    	luggageSmallNumTextView = (TextView) view.findViewById(R.id.luggageSmallNum);
    	carPic = (ImageView) view.findViewById(R.id.car_pic);
    	vehicleIcon= (ImageView) view.findViewById(R.id.vehicleIcon);
    	vehicle_layout = (LinearLayout) view.findViewById(R.id.vehicle_layout);
    	
    	vehicleFlowLayout = (FlowLayout) view.findViewById(R.id.vehicleEquipment);
    	
    	
    	
    }
    private void setValue(){
    	if(carInfo==null) {
    		noImageView.setVisibility(View.VISIBLE);
    		vehicle_layout.setVisibility(View.GONE);
    		return;}
    	noImageView.setVisibility(View.GONE);
    	vehicle_layout.setVisibility(View.VISIBLE);
    	nameTextView.setText(carInfo.getCarName());
    	seatNumTextView.setText(carInfo.getSeatNum());
    	if(!StringUtils.isEmpty(carInfo.getDesc()))
    	descTextView.setText(carInfo.getDesc());
    	else {
    		descTextView.setVisibility(View.GONE);
    	}
    	picNumTextView.setText(carInfo.getCarPics().size()+"P");
    	luggageBigNumTextView.setText(carInfo.getCapacityBig());
    	luggageSmallNumTextView.setText(carInfo.getCapacitySmall());
    	setCarType();
    	addFlowView(carInfo.getFacilities(), vehicleFlowLayout);
    	int h=((View)vehicleFlowLayout.getParent().getParent()).getHeight();
    	List<ImageInfo> imageInfoList=carInfo.getCarPics();
    	if(imageInfoList.size()>0){
    		carPic.setImageResource(R.drawable.ic_launcher);
    		if(!StringUtils.isEmpty(imageInfoList.get(0).getUrl())){
    			
    			ImageLoader.getInstance().displayImage(Urls.imgHost + imageInfoList.get(0).getUrl(),
    					carPic,
    					AppConfig.options(R.drawable.ic_launcher)
    					);
    		}
		 
            
            carPic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					   Intent intentForView = new Intent( getActivity(), ImageViewerActivity.class);
			            intentForView.putExtra("image_index", 0);
			            intentForView.putExtra("STATE_POSITION", 0);
			            intentForView.putParcelableArrayListExtra("images", (ArrayList<ImageInfo>)VehicleInfoFragment.this.carInfo.getCarPics());
			            getActivity().startActivity(intentForView);
				}
			});
    	 }
    	((ClubMebHomepageActivity) getActivity()).resetViewPagerHeight(0);
    }
     
	private void addFlowView(List<String> strs, FlowLayout flowLayout) {
		FlowLayout.LayoutParams 	 params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT,
				FlowLayout.LayoutParams.WRAP_CONTENT);
		params.rightMargin = DensityUtil.dip2px(getActivity(), 5);
		params.bottomMargin = DensityUtil.dip2px(getActivity(), 5);
		List<View> views=new ArrayList<View>();
		for (int i = 0; i < strs.size(); i++) {
			TextView newView = new TextView(getActivity());
			newView.setBackgroundResource(R.drawable.shape_person_info);
			newView.setText(strs.get(i));
			newView.setTag(i);
			newView.setGravity(Gravity.CENTER);
			newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			newView.setTextColor(getResources().getColor(R.color.gray_text));
			views.add(newView);
		
//			newView.setLayoutParams(params);
			flowLayout.addView(newView,params);
		}
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) flowLayout.getLayoutParams(); //取控件textView当前的布局参数  
		
		linearParams.height =DensityUtil.dip2px(getActivity(), (strs.size()/5+1)*45) ;// 控件的高强制设成20  
		  
		linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;// 控件的宽强制设成30   
		  
		flowLayout.setLayoutParams(linearParams);
	}
	
	private void setCarType(){
		String carType=carInfo.getCarType();
		if(!StringUtils.isEmpty(carType)){
			if(carType.equals("suv")){
				vehicleTypeTextView.setText("suv");
				vehicleIcon.setImageResource(R.drawable.suv);
			}
			else 
				if(carType.equals("mpv")){
					vehicleTypeTextView.setText("mpv");
					vehicleIcon.setImageResource(R.drawable.mpv);
				}
				else
					if(carType.equals("轿车")){
						vehicleTypeTextView.setText("轿车");
						vehicleIcon.setImageResource(R.drawable.car);
					}
					else 	if(carType.equals("客车")){
						vehicleTypeTextView.setText("客车");
						vehicleIcon.setImageResource(R.drawable.bus);
					}
					else 	if(carType.equals("中大型轿车")){
						vehicleTypeTextView.setText("中大型轿车");
						vehicleIcon.setImageResource(R.drawable.big_car);
					}
			
		}
		
	}
}  
