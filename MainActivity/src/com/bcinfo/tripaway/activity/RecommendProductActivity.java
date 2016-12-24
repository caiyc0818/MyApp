package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity;
import com.bcinfo.tripaway.activity.ClubMebHomepageActivity.OnLoadMoreListener;
import com.bcinfo.tripaway.activity.CarProductDetailActivity;

import com.bcinfo.tripaway.activity.HouseProductDetailActivity;
import com.bcinfo.tripaway.activity.ProductDetailNewActivity;
import com.bcinfo.tripaway.activity.ProductJoinMebActivity;
import com.bcinfo.tripaway.adapter.ProductAdapter;
import com.bcinfo.tripaway.adapter.ProductInfoAdapter;
import com.bcinfo.tripaway.bean.AvailableTime;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.Dest;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.DensityUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wefika.flowlayout.FlowLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendProductActivity extends BaseActivity {






	private ProductInfoAdapter productInfoAdapter;

	private List<ProductNewInfo> productList ;

	private ListView productDetailList;

	private LinearLayout layout_back_button;
	
	
	
	




	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.recommend_product_info);
		// initLocation();
		productList=getIntent().getParcelableArrayListExtra("Products");
		initView();

	}


	protected void initView() {
		 RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
		 titleLayout.setVisibility(View.VISIBLE);
	        titleLayout.setAlpha(1);
	        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));

		productDetailList = (ListView) findViewById(R.id.product_detail_listview);
		productDetailList.setFocusable(false);
		productInfoAdapter = new ProductInfoAdapter(this, productList);
		productDetailList.setAdapter(productInfoAdapter);
		productDetailList.setOnItemClickListener(mSimillarListListener);
		
		layout_back_button = (LinearLayout) findViewById(R.id.layout_back_button);
		layout_back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				activityAnimationClose();
			}
		});
		
	}

	

	




	
	
	  OnItemClickListener mSimillarListListener = new OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        {
	            // TODO Auto-generated method stub
	            Intent intent = null;
	            ProductNewInfo productNewInfo = productList.get(position);
	            if (productNewInfo.getProductType().equals("single"))
	            {
	                if (productNewInfo.getServices().equals("traffic"))
	                {
	                    intent = new Intent(RecommendProductActivity.this, CarProductDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	                else if (productNewInfo.getServices().equals("stay"))
	                {
	                    intent = new Intent(RecommendProductActivity.this, HouseProductDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	                else
	                {
	                    intent = new Intent(RecommendProductActivity.this, GrouponProductNewDetailActivity.class);
	                    intent.putExtra("productId", productNewInfo.getId());
	                }
	            }
	            else if (productNewInfo.getProductType().equals("base")
	                || productNewInfo.getProductType().equals("customization"))
	            {
	                intent = new Intent(RecommendProductActivity.this, GrouponProductNewDetailActivity.class);
	                intent.putExtra("productId", productNewInfo.getId());
	            }
	            else if (productNewInfo.getProductType().equals("team"))
	            {
	                intent = new Intent(RecommendProductActivity.this, GrouponProductNewDetailActivity.class);
	                intent.putExtra("productId", productNewInfo.getId());
	            }
	            startActivity(intent);
	            activityAnimationOpen();
	        }
	    };
	    
	    /**
	     * 页面启动动画
	     */
	    public void activityAnimationOpen()
	    {
	        this.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
	    }
}
