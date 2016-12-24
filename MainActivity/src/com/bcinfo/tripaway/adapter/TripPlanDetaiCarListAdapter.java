package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.CarExt;
import com.bcinfo.tripaway.bean.CarImageCategory;
import com.bcinfo.tripaway.bean.CarServCategory;
import com.bcinfo.tripaway.bean.Facility;
import com.bcinfo.tripaway.bean.ProductServiceResource;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.JsonUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 行程规划详情汽车列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年11月29日 上午10:04:40
 */
public class TripPlanDetaiCarListAdapter extends BaseAdapter
{
    private static final String TAG = "TripPlanDetaiCarListAdapter";
    
    private Context mContext;
    
    private ArrayList<ProductServiceResource> mItemList;
    
    public TripPlanDetaiCarListAdapter(Context context, ArrayList<ProductServiceResource> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mItemList = list;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mItemList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mItemList.get(position);
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
        ProductServiceResource resource = mItemList.get(position);
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.trip_plan_detail_car_listitem, null);
            holder = new ViewHolder();
            holder.car_name = (TextView)convertView.findViewById(R.id.car_name);
            holder.car_photo = (ImageView)convertView.findViewById(R.id.car_photo);
            holder.car_description = (TextView)convertView.findViewById(R.id.car_description);
            holder.car_price = (TextView)convertView.findViewById(R.id.car_price);
            holder.car_brand = (TextView)convertView.findViewById(R.id.car_brand);
            holder.car_number = (TextView)convertView.findViewById(R.id.car_number);
            holder.car_kilometer = (TextView)convertView.findViewById(R.id.car_kilometer);
            holder.car_buyTime = (TextView)convertView.findViewById(R.id.car_buyTime);
            holder.car_seatNum = (TextView)convertView.findViewById(R.id.car_seatNum);
            holder.car_type = (TextView)convertView.findViewById(R.id.car_type);
            holder.car_room = (TextView)convertView.findViewById(R.id.car_room);
            holder.car_equipment_gv = (GridView)convertView.findViewById(R.id.car_equipment_gv);
            holder.car_fee_gv = (GridView)convertView.findViewById(R.id.car_fee_gv);
            holder.car_detail_listview = (ListView)convertView.findViewById(R.id.car_detail_listview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.car_name.setText(resource.getServName());
        if (!StringUtils.isEmpty(resource.getTitleImage()))
        {
            ImageLoader.getInstance().displayImage(Urls.imgHost + resource.getTitleImage(), holder.car_photo);
        }
        
        holder.car_description.setText(resource.getServDesc());
        holder.car_price.setText(resource.getFee());
        CarExt carExt = JsonUtil.getCarInfo(resource.getResourceExt());
        if (carExt != null)
        {
            holder.car_brand.setText(carExt.getCarName());
            holder.car_number.setText(carExt.getLicense());
            holder.car_kilometer.setText(carExt.getDistance());
            holder.car_buyTime.setText(carExt.getShoppingTime());
            holder.car_seatNum.setText(carExt.getSeatNum());
            holder.car_type.setText(carExt.getCarType());
            holder.car_room.setText(carExt.getCapacity());
            List<CarServCategory> carServers = carExt.getCarServers();
            for (int i = 0; i < carServers.size(); i++)
            {
                CarServCategory carServer = carServers.get(i);
                if (carServer != null)
                {
                    List<Facility> facilityList = carServer.getFacilities();
                    if (i == 0)
                    {
                        initEquipmentGridView(holder.car_equipment_gv, facilityList);
                    }
                    else
                    {
                        initFeeGridView(holder.car_fee_gv, facilityList);
                    }
                }
            }
            List<CarImageCategory> carImages = carExt.getCarImages();
            initCarPictureListView(holder.car_detail_listview, carImages);
        }
        return convertView;
    }
    
    private void initEquipmentGridView(GridView gridview, List<Facility> facilityList)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < facilityList.size(); i++)
        {
            Facility facility = facilityList.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("isContain", facility.getStatus());
            map.put("name", facility.getFacilityName());
            mItemList.add(map);
        }
        // CarInfoGridViewAdapter adapter = new CarInfoGridViewAdapter(mContext, mItemList);
        // gridview.setAdapter(adapter);
    }
    
    private void initFeeGridView(GridView gridview, List<Facility> facilityList)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < facilityList.size(); i++)
        {
            Facility facility = facilityList.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("isContain", facility.getStatus());
            map.put("name", facility.getFacilityName());
            mItemList.add(map);
        }
        // CarInfoGridViewAdapter adapter = new CarInfoGridViewAdapter(mContext, mItemList);
        // gridview.setAdapter(adapter);
    }
    
    private void initCarPictureListView(ListView listview, List<CarImageCategory> carImages)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        // for (int i = 0; i < carImages.size(); i++)
        // {
        // CarServCategory carImage = carImages.get(i);
        // mItemList.addAll(carImage.getImgList());
        // }
        // ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(mContext, mItemList);
        // listview.setAdapter(adapter);
    }
    
    public class ViewHolder
    {
        TextView car_name;
        
        ImageView car_photo;
        
        TextView car_description;
        
        TextView car_price;
        
        TextView car_brand;
        
        TextView car_number;
        
        TextView car_kilometer;
        
        TextView car_buyTime;
        
        TextView car_seatNum;
        
        TextView car_type;
        
        TextView car_room;
        
        private GridView car_equipment_gv;
        
        private GridView car_fee_gv;
        
        private ListView car_detail_listview;
    }
}
