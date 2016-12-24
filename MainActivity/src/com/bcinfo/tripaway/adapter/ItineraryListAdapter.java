package com.bcinfo.tripaway.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.TripDaysPlan;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 行程规划列表适配器
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年4月29日 下午4:01:08
 */
public class ItineraryListAdapter extends BaseAdapter
{
    protected static final String TAG = "ItineraryListAdapter";
    
    private Context mContext;
    
    private ArrayList<String> mItemList;
    
    public ItineraryListAdapter(Context context, ArrayList<String> list)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.itinerary_list_item, null);
            holder.day_index = (TextView)convertView.findViewById(R.id.day_index);
            holder.trip_plan_detail_listview = (ListView)convertView.findViewById(R.id.trip_plan_detail_listview);
            holder.layout_car_info = (LinearLayout)convertView.findViewById(R.id.layout_car_info);
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
            holder.layout_air_info = (LinearLayout)convertView.findViewById(R.id.layout_air_info);
            holder.airline_name = (TextView)convertView.findViewById(R.id.airline_name);
            holder.flight_description = (TextView)convertView.findViewById(R.id.flight_description);
            holder.start_city_cn = (TextView)convertView.findViewById(R.id.start_city_cn);
            holder.start_city_en = (TextView)convertView.findViewById(R.id.start_city_en);
            holder.end_city_cn = (TextView)convertView.findViewById(R.id.end_city_cn);
            holder.end_city_en = (TextView)convertView.findViewById(R.id.end_city_en);
            holder.flight_total_time = (TextView)convertView.findViewById(R.id.flight_total_time);
            holder.plane_name = (TextView)convertView.findViewById(R.id.plane_name);
            holder.airline_logo = (ImageView)convertView.findViewById(R.id.airline_logo);
            holder.price_type = (TextView)convertView.findViewById(R.id.price_type);
            holder.flight_price = (TextView)convertView.findViewById(R.id.flight_price);
            holder.start_airport = (TextView)convertView.findViewById(R.id.start_airport);
            holder.end_airport = (TextView)convertView.findViewById(R.id.end_airport);
            holder.start_time = (TextView)convertView.findViewById(R.id.start_time);
            holder.flight_days = (TextView)convertView.findViewById(R.id.flight_days);
            holder.end_time = (TextView)convertView.findViewById(R.id.end_time);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.day_index.setText("第" + (position + 1) + "天");
        initPlanDetailList(holder.trip_plan_detail_listview);
        initCarInfo(holder);
        initFlightInfo(holder);
        return convertView;
    }
    
    public class ViewHolder
    {
        public TextView day_index;
        
        public ListView trip_plan_detail_listview;
        
        public LinearLayout layout_car_info;
        
        public TextView car_name;
        
        public ImageView car_photo;
        
        public TextView car_description;
        
        public TextView car_price;
        
        public TextView car_brand;
        
        public TextView car_number;
        
        public TextView car_kilometer;
        
        public TextView car_buyTime;
        
        public TextView car_seatNum;
        
        public TextView car_type;
        
        public TextView car_room;
        
        public GridView car_equipment_gv;
        
        public GridView car_fee_gv;
        
        public ListView car_detail_listview;
        
        public LinearLayout layout_air_info;
        
        public TextView airline_name;
        
        public TextView flight_description;
        
        public TextView start_city_cn;
        
        public TextView start_city_en;
        
        public TextView end_city_cn;
        
        public TextView end_city_en;
        
        public TextView flight_total_time;
        
        public TextView plane_name;
        
        public ImageView airline_logo;
        
        public TextView price_type;
        
        public TextView flight_price;
        
        public TextView start_airport;
        
        public TextView end_airport;
        
        public TextView start_time;
        
        public TextView flight_days;
        
        public TextView end_time;
    }
    
    /*****************************************************************************/
    private void initPlanDetailList(ListView listView)
    {
        ArrayList<TripDaysPlan> mTripPlanArrList = new ArrayList<TripDaysPlan>();
        for (int i = 0; i < 2; i++)
        {
            TripDaysPlan info = new TripDaysPlan();
            info.setIndex((i + 1) + "");
            info.setTitle("曼谷大皇宫 The grand palace");
            ArrayList<String> labels = new ArrayList<String>();
            labels.add("单人");
            labels.add("穷游");
            labels.add("泰国");
            labels.add("寺庙");
            info.setLabels(labels);
            info.setPhotoUrl("http://youimg1.c-ctrip.com/target/tg/014/137/627/1395f2c54ba94598b6842c0edb6a44a7.jpg");
            info.setDescription("大皇宫也叫大王宫，泰王室最大的宫殿建筑群，汇集了泰国装饰，建筑，雕刻、绘画灯民族特色的精华。它位于湄南河东岸，始建于拉玛一世时期的1782年，曼谷王朝从拉玛一世到拉玛八世，均居于大皇宫内。1946年拉玛八世在宫中被刺之后，拉玛九世便搬至大皇宫东面新建的集拉达宫居住。现在，大皇宫除了用于举行加冕典礼、宫廷庆祝等仪式和活动外，平时对外开放，成为泰国著名的游览场所。");
            info.setPrice("￥220");
            info.setAddress("嘛哈路 Tha Chang（N9）码头99号");
            info.setTime("每天8:30-16:30，午间不休息");
            HashMap<String, String> map = new HashMap<String, String>();
            HashMap<String, String> map1 = new HashMap<String, String>();
            String pictureUrl = "http://file25.mafengwo.net/M00/29/98/wKgB4lNNH3-AM2gDADQzCaie-BE54.jpeg";
            String pictureDes = "参观时要求穿着整齐，禁止无袖T、背心、露脐装、透视装、任何短裤、破洞乞丐裤、紧身裤、裙裤、迷你裙，不能穿拖鞋";
            String pictureUrl1 = "http://file25.mafengwo.net/M00/1F/68/wKgB4lNNF7OAHFICADCho5db3ho76.jpeg";
            map.put("pictureUrl", pictureUrl);
            map.put("pictureDes", pictureDes);
            map1.put("pictureUrl", pictureUrl1);
            map1.put("pictureDes", pictureDes);
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            list.add(map);
            list.add(map1);
            info.setIntroduceList(list);
            mTripPlanArrList.add(info);
        }
        // TripPlanDetailNewListAdapter mTripPlanAdapter = new TripPlanDetailNewListAdapter(mContext, mTripPlanArrList);
        // listView.setAdapter(mTripPlanAdapter);
    }
    
    private void initCarInfo(ViewHolder holder)
    {
        String url = "http://img0.imgtn.bdimg.com/it/u=2050223010,3127628968&fm=21&gp=0.jpg";
        ImageLoader.getInstance().displayImage(url, holder.car_photo);
        String des =
            "自1900年12月22日戴姆勒汽车公司向其客户献上了世界上第一辆以梅赛德斯（Mercedes）为品牌的轿车开始，奔驰汽车就成为汽车工业的楷模。其品牌标志已成为世界上最著名的汽车品牌标志之一，100多年来，奔驰品牌一直是汽车技术创新的先驱者";
        holder.car_description.setText(des);
        initEquipmentGridView(holder.car_equipment_gv);
        initFeeGridView(holder.car_fee_gv);
        initCarDetailListView(holder.car_detail_listview);
    }
    
    private void initEquipmentGridView(GridView gv)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        // for (int i = 0; i < 6; i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("isContain", "true");
            map.put("name", "车载WIFI");
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("isContain", "true");
            map1.put("name", "儿童座椅");
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("isContain", "true");
            map2.put("name", "车载冰箱");
            HashMap<String, String> map3 = new HashMap<String, String>();
            map3.put("isContain", "false");
            map3.put("name", "乘客保险");
            mItemList.add(map);
            mItemList.add(map1);
            mItemList.add(map2);
            mItemList.add(map3);
        }
        // CarInfoGridViewAdapter adapter = new CarInfoGridViewAdapter(mContext, mItemList);
        // gv.setAdapter(adapter);
    }
    
    private void initFeeGridView(GridView gv)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        // for (int i = 0; i < 6; i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("isContain", "true");
            map.put("name", "汽油费");
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("isContain", "true");
            map1.put("name", "过路过桥费");
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("isContain", "false");
            map2.put("name", "公共交通费");
            HashMap<String, String> map3 = new HashMap<String, String>();
            map3.put("isContain", "false");
            map3.put("name", "停车费");
            mItemList.add(map);
            mItemList.add(map1);
            mItemList.add(map2);
            mItemList.add(map3);
        }
        // CarInfoGridViewAdapter adapter = new CarInfoGridViewAdapter(mContext, mItemList);
        // gv.setAdapter(adapter);
    }
    
    private void initCarDetailListView(ListView lv)
    {
        ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        String pictureUrl = "http://car0.autoimg.cn/upload/2014/7/17/u_201407171935493884435.jpg";
        String pictureDes = "车内很整洁希望能给您的旅途带来一份好心情";
        String pictureUrl1 = "http://car0.autoimg.cn/upload/2013/11/26/u_201311261856165934972.jpg";
        map.put("pictureUrl", pictureUrl);
        map.put("pictureDes", pictureDes);
        map1.put("pictureUrl", pictureUrl1);
        map1.put("pictureDes", pictureDes);
        mItemList.add(map);
        mItemList.add(map1);
        // ScenicDetailListAdapter adapter = new ScenicDetailListAdapter(mContext, mItemList);
        // lv.setAdapter(adapter);
    }
    
    private void initFlightInfo(ViewHolder holder)
    {
        String airlineLogoUrl =
            "http://221.7.213.132/res_android/images/21/25491/32660_295b36509-cab9-4019-93ec-6b5cc0c3bd54.png";
        ImageLoader.getInstance().displayImage(airlineLogoUrl, holder.airline_logo);
    }
}
