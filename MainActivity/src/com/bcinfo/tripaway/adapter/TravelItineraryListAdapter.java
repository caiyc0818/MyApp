package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.List;

 


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.BaseActivity;
import com.bcinfo.tripaway.activity.DescriptionActivity;
 
import com.bcinfo.tripaway.activity.TripPlanDetailActivity;
import com.bcinfo.tripaway.adapter.UserCommentListAdapter.ViewHolder;
import com.bcinfo.tripaway.bean.Comment;
import com.bcinfo.tripaway.bean.PlanThings;
import com.bcinfo.tripaway.bean.ProductInfo;
import com.bcinfo.tripaway.bean.RoutePlan;
import com.bcinfo.tripaway.utils.loadIMG.BitmapManager;
import com.bcinfo.tripaway.view.image.RoundImageView;

/**
 * 我的行程单列表适配器
 * @function
 * @author     JiangCS  
 * @version   1.0, 2015年1月13日 下午5:44:31
 */
public class TravelItineraryListAdapter extends BaseAdapter
{
    private android.app.Activity mActivity;
    private ArrayList<ProductInfo> productInfoList;
    private BitmapManager bitmapManager;

    public TravelItineraryListAdapter(android.app.Activity mActivity, ArrayList<ProductInfo> list)
    {
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
        this.productInfoList = list;
        bitmapManager = new BitmapManager(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher));
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return productInfoList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return productInfoList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ProductInfo info=productInfoList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.travel_itinerary_listitem, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.productImgPager = (ViewPager) convertView.findViewById(R.id.product_img_pager);
        holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
        holder.productName = (TextView) convertView.findViewById(R.id.product_name);
        holder.productImgIndicator = (TextView) convertView.findViewById(R.id.product_img_indicator);
        holder.productAddress = (TextView) convertView.findViewById(R.id.product_address);
        holder.productAuthorPhoto = (RoundImageView) convertView.findViewById(R.id.product_author_photo);
        holder.productAuthorName = (TextView) convertView.findViewById(R.id.product_author_name);
        holder.productAuthorContact = (TextView) convertView.findViewById(R.id.product_author_contact);
        holder.layoutProductDescription = (LinearLayout) convertView.findViewById(R.id.layout_product_description);
        holder.productDescription = (TextView) convertView.findViewById(R.id.product_description);
        holder.descriptionDetailButton = (TextView) convertView.findViewById(R.id.product_description_detail_button);
        holder.layoutPlanDetail = (LinearLayout) convertView.findViewById(R.id.layout_plan_detail);
        holder.planListview = (ExpandableListView) convertView.findViewById(R.id.plan_listview);
        holder.planDetailButton = (TextView) convertView.findViewById(R.id.trip_plan_detail_button);
        initProductImagePage(holder.productImgPager, holder.productImgIndicator, productInfoList.get(position)
                .getLogoUrls());
        holder.productPrice.setText(productInfoList.get(position).getPrice());
        holder.productName.setText(productInfoList.get(position).getName());
        holder.productAddress.setText(productInfoList.get(position).getAddress());
        bitmapManager.loadBitmap(productInfoList.get(position).getAuthorPhotoUrl(), holder.productAuthorPhoto);
        holder.productAuthorName.setText(productInfoList.get(position).getAuthorName());
        holder.productAuthorContact.setText(productInfoList.get(position).getAuthorContact());
//        holder.productDescription.setText(.getDescription());
        holder.descriptionDetailButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mActivity, DescriptionActivity.class);
                intent.putExtra("title", "产品描述");
                intent.putExtra("description", productInfoList.get(position).getDescription());
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                
            }
        });
        initPlanListView(holder.mPlanGroups, holder.mPlanChilds, holder.planListview);
        holder.planDetailButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent  intent = new Intent(mActivity, TripPlanDetailActivity.class);
                mActivity. startActivity(intent);
                mActivity.overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
            }
        });
        return convertView;
    }

    /**
     * 初始化产品图片集
     */
    private void initProductImagePage(final ViewPager viewPager, final TextView indicator, ArrayList<String> urls)
    {
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(((BaseActivity) mActivity).getSupportFragmentManager(), urls);
        CharSequence text = mActivity.getString(R.string.viewpager_indicator, 1, urls.size());
        indicator.setText(text);
        viewPager.setAdapter(mAdapter);
        // 更新下标
        viewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageSelected(int arg0)
            {
                CharSequence text = mActivity.getString(R.string.viewpager_indicator, arg0 + 1, viewPager.getAdapter()
                        .getCount());
                indicator.setText(text);
            }
        });
    }

    /**
     * 初始化行程规划列表
     */
    private void initPlanListView(List<RoutePlan> mPlanGroups, List<List<PlanThings>> mPlanChilds,
            ExpandableListView planListview)
    {
        mPlanGroups.clear();
        mPlanChilds.clear();
        for (int i = 0; i < 2; i++)
        {
            RoutePlan group = new RoutePlan();
            group.setDateIndex("D1");
            group.setContent("第一天：洛杉矶");
            mPlanGroups.add(group);
            List<PlanThings> child = new ArrayList<PlanThings>();
            PlanThings planThings = new PlanThings();
            planThings.setIndex("1");
            planThings.setContent("洛杉矶环球影城");
            child.add(planThings);
            child.add(planThings);
            mPlanChilds.add(child);
        }
        PlanExpandableListAdapter mPlanListAdapter = new PlanExpandableListAdapter(mActivity, mPlanGroups, mPlanChilds);
        planListview.setAdapter(mPlanListAdapter);
        planListview.setOnGroupClickListener(new OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                // TODO Auto-generated method stub
                return true;
            }
        });
        planListview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getPlanListViewHeight(
                mPlanGroups.size(), mPlanChilds)));
        for (int i = 0; i < planListview.getCount(); i++)
        {
            planListview.expandGroup(i);
        }
    }

    /**
     * 获取行程规划列表整体高度
     * @return
     */
    private int getPlanListViewHeight(int groupSize, List<List<PlanThings>> mPlanChilds)
    {
        int childNumber = 0;
        int groupItemHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.plan_group_item_height) + 1;
        int childItemHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.plan_child_item_height) + 1;
        int groupHeigt = groupItemHeight * groupSize;
        for (int i = 0; i < mPlanChilds.size(); i++)
        {
            List<PlanThings> chils = mPlanChilds.get(i);
            childNumber += chils.size();
        }
        int childHeight = childItemHeight * childNumber;
        return groupHeigt + childHeight;
    }
    public class ViewHolder
    {
        /**
         * 产品照片ViewPager
         */
        public ViewPager productImgPager;
        /**
         * 产品价格
         */
        public TextView productPrice;
        /**
         * 产品名称
         */
        public TextView productName;
        /**
         * 照片序号
         */
        public TextView productImgIndicator;
        /**
         * 发布者头像
         */
        public RoundImageView productAuthorPhoto;
        /**
         * 发布者名字
         */
        public TextView productAuthorName;
        /**
         * 产品地点
         */
        public TextView productAddress;
        /**
         * 产品发布者联系方式
         */
        public TextView productAuthorContact;
        /**
         * 产品描述布局
         */
        public LinearLayout layoutProductDescription;
        /**
         * 产品描述
         */
        public TextView productDescription;
        /**
         * 产品描述详情按钮
         */
        public TextView descriptionDetailButton;
        /**
         * 行程规划布局
         */
        public LinearLayout layoutPlanDetail;
        /**
         * 行程规划列表
         */
        public ExpandableListView planListview;
        /**
         * 行程规划列表详情按钮
         */
        public TextView planDetailButton;
        /*
         * 行程规划每天内容数据
         */
        public List<List<PlanThings>> mPlanChilds = new ArrayList<List<PlanThings>>();
        /*
         *  行程规划日期数据
         */
        public List<RoutePlan> mPlanGroups = new ArrayList<RoutePlan>();
        /*
         *  行程规划列表适配器
         */
        public PlanExpandableListAdapter mPlanListAdapter;
    }
}
