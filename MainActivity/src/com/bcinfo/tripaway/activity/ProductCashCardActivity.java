package com.bcinfo.tripaway.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ProductCashCardAdapter;
import com.bcinfo.tripaway.bean.CashOrder;
import com.bcinfo.tripaway.view.MyListView;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProductCashCardActivity extends BaseActivity {
	private ListView prodactCash;
	private List<CashOrder> lists = new ArrayList<CashOrder>();
	private List<CashOrder> seclist = new ArrayList<CashOrder>();
	private ProductCashCardAdapter myAdapter;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.product_cashcardlist);
		setSecondTitle("我的现金券");
		lists = (List<CashOrder>) getIntent().getSerializableExtra("cashList");
		prodactCash = (ListView) findViewById(R.id.select_dialog_listview);
		myAdapter = new ProductCashCardAdapter(this, lists);
		prodactCash.setAdapter(myAdapter);
		prodactCash.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				seclist.add(lists.get(position));
				Intent it = new Intent(ProductCashCardActivity.this, ConfirmPayActivity3.class);
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("result", (Serializable) seclist);
				it.putExtras(bundle);  
				setResult(818, it);
				finish();
				activityAnimationClose();
				
				
				
			}
		});
	}
	
	
	
	
}
