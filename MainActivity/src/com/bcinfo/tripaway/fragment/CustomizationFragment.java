package com.bcinfo.tripaway.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BlogDetailActivity;
import com.bcinfo.tripaway.activity.MyOrderDetailActivity;
import com.bcinfo.tripaway.activity.PersonalCustomizationStepEnd;
import com.bcinfo.tripaway.activity.PersonalCustomizationStepOne;
import com.bcinfo.tripaway.activity.RecommendProductActivity;
import com.bcinfo.tripaway.adapter.CustomizationListAdapter;
import com.bcinfo.tripaway.bean.Customization;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.TraceLogs;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomizationFragment extends Fragment {
	private TextView from = null;
	private TextView to = null;
	private TextView days = null;
	private TextView startDate = null;
	private TextView audltNum = null;
	private TextView childrenNum = null;
	private TextView arrange = null;
	private TextView budget = null;
	private ImageView isSucess = null;
	private ImageView del = null;
	private ListView submitList = null;

	private Customization customization;
	
	private OnDelLessoner onDelLessoner;
	private Button customizationButton;

	public CustomizationFragment(Customization customization,OnDelLessoner onDelLessoner) {
		this.customization = customization;
		this.onDelLessoner=onDelLessoner;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.personal_customization_item, null);// 注意不要指定父视图
		from = (TextView) view.findViewById(R.id.from);
		to = (TextView) view.findViewById(R.id.to);
		days = (TextView) view.findViewById(R.id.days);
		startDate = (TextView) view.findViewById(R.id.startDate);
		audltNum = (TextView) view.findViewById(R.id.audltNum);
		childrenNum = (TextView) view.findViewById(R.id.childrenNum);
		arrange = (TextView) view.findViewById(R.id.arrange);
		budget = (TextView) view.findViewById(R.id.budget);
		isSucess = (ImageView) view.findViewById(R.id.isSucess);
		del = (ImageView) view.findViewById(R.id.del);
		submitList = (ListView) view.findViewById(R.id.submit_detail_list);
		customizationButton= (Button) view.findViewById(R.id.customizationButton);

		from.setText(customization.getFrom());
		to.setText(customization.getTo());
		if(customization.getDays().equals("14"))
			days.setText(">13天");
		else
		days.setText(customization.getDays() + "天");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = format.parse(customization.getStartDate());
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日出发");
			startDate.setText(format1.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(customization.getAdultNum().equals("14"))
			audltNum.setText("13人以上");
		else
		audltNum.setText(customization.getAdultNum() + "成人");
		
		if(customization.getChildrenNum().equals("14"))
			childrenNum.setText("13人以上");
		else
		childrenNum.setText(customization.getChildrenNum() + "儿童");
		arrange.setText(customization.getArrange());
		budget.setText(customization.getBudget()+"元");
		isSucess = (ImageView) view.findViewById(R.id.isSucess);
		final String status = customization.getStatus();
		if (status.equals("wait"))
			isSucess.setVisibility(View.INVISIBLE);
		else if (status.equals("success")) {
			isSucess.setImageResource(R.drawable.submit_sucess);
		} else {
			isSucess.setImageResource(R.drawable.submit_fail);
		}

		CustomizationListAdapter adapter = new CustomizationListAdapter(
				getActivity(),
				(ArrayList<TraceLogs>) customization.getTraceLogs());
		submitList.setAdapter(adapter);
		del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					new AlertDialog.Builder(getActivity()).setTitle("确认删除吗？").setPositiveButton("确认",new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delCustomization(customization.getDesireId());
						dialog.dismiss();
					}
				}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				
			}
		});
		if (status.equals("failure")){
			customizationButton.setVisibility(View.VISIBLE);
			customizationButton.setText("重新定制");
		}else if (status.equals("success")){
			List<ProductNewInfo> recommendProducts=customization.getRecommendProducts();
			if(recommendProducts.size()>0){
				customizationButton.setVisibility(View.VISIBLE);
				customizationButton.setText("小T为我定制的线路");
			}
			
		}
		customizationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (status.equals("failure")){
					Intent intent = new Intent(
							getActivity(), PersonalCustomizationStepOne.class);
					startActivity(intent);
				}else {
					Intent intent = new Intent(
							getActivity(), RecommendProductActivity.class);
					intent.putParcelableArrayListExtra("Products",(ArrayList<ProductNewInfo>)customization.getRecommendProducts() );
					startActivity(intent);
				}
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	
	private void delCustomization( String desireId
    		
)
    {
        HttpUtil.delete(Urls.del_customization+desireId,new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if ("00000".equals(response.optString("code"))) {
					ToastUtil.showToast(getActivity(), "删除成功");
					onDelLessoner.OnDel(CustomizationFragment.this);
		            
				}else {
					ToastUtil.showToast(getActivity(), "删除失败");
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LogUtil.i("CustomizationFragment", "CustomizationFragment", responseString);
			}
		});
        
    }
	
public interface OnDelLessoner{
	public void OnDel(CustomizationFragment fragment);
}
}
