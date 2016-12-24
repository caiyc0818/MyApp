package com.bcinfo.tripaway.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.TripArticle;

/**
 * 微游记列表item适配器
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年1月5日 11:30:33
 * 
 */
public class ArticleListAdapter extends BaseAdapter implements OnCheckedChangeListener
{
    private List<TripArticle> articleList;
    
    private Context context;
    
    private LayoutInflater inflator;
    
    public LayoutInflater getInflator()
    {
        return inflator;
    }
    
    public void setInflator(LayoutInflater inflator)
    {
        this.inflator = inflator;
    }
    
    public List<TripArticle> getArticleList()
    {
        return articleList;
    }
    
    public void setArticleList(List<TripArticle> articleList)
    {
        this.articleList = articleList;
    }
    
    public ArticleListAdapter()
    {
    }
    
    /**
     * 定义存储boolean变量的map集合
     */
    private Map<Integer, Boolean> isLikeList;
    
    public ArticleListAdapter(List<TripArticle> articleList, Context context)
    {
        this.articleList = articleList;
        this.context = context;
        init();
    }
    
    /**
     * 初始化方法 定义所有元素的值为false
     */
    public void init()
    {
        System.out.println("长度为:" + articleList.size());
        isLikeList = new HashMap<Integer, Boolean>(articleList.size());
        for (int i = 0; i < articleList.size(); i++)
        {
            isLikeList.put(i, false);
        }
    }
    
    @Override
    public int getCount()
    {
        return articleList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO 自动生成的方法存根
        return articleList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO 自动生成的方法存根
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder articleHolder = null;
        // 获得指定position处的article
        TripArticle article = articleList.get(position);
        if (convertView == null)
        {
            articleHolder = new ViewHolder();
            inflator = LayoutInflater.from(context);
            convertView = inflator.inflate(R.layout.trip_microblog_items, parent, false);
            articleHolder.backgroundImgView = (ImageView)convertView.findViewById(R.id.img_item_trip_blog);
            articleHolder.isLikeCheckBox = (CheckBox)convertView.findViewById(R.id.isLike_trip_blog);
            // 设置position标签tag
            articleHolder.isLikeCheckBox.setTag(position);
            // 定义checked监听事件
            articleHolder.isLikeCheckBox.setOnCheckedChangeListener(this);
            articleHolder.backgroundImgView = (ImageView)convertView.findViewById(R.id.img_item_trip_blog);
            articleHolder.authorImageView = (ImageView)convertView.findViewById(R.id.master_icon_trip_blog);
            articleHolder.authorTextView = (TextView)convertView.findViewById(R.id.name_trip_bloger);
            articleHolder.timeTextView = (TextView)convertView.findViewById(R.id.time_trip_blog);
            articleHolder.tripDescribeTextView = (TextView)convertView.findViewById(R.id.describe_trip_blog);
            convertView.setTag(articleHolder);
        }
        else
        {
            articleHolder = (ViewHolder)convertView.getTag();
            articleHolder.isLikeCheckBox.setTag(position);
        }
        // ImageLoader.getInstance().displayImage(article.getAuthorIcon(), articleHolder.authorImageView);
        /*
         * switch (position % 3) { case 0: // 设置指定position处微游记item的背景图片 背景图片颜色依次是 红，绿，白 三种
         * articleHolder.backgroundImgView.setBackgroundResource(R.color.red);
         * 
         * break; case 1: articleHolder.backgroundImgView.setBackgroundResource(R .color.dark_green);
         * ImageLoader.getInstance().displayImage( "http://pic23.nipic.com/20120824/8218020_193654254129_2.jpg",
         * articleHolder.authorImageView); break; case 2:
         * 
         * articleHolder.backgroundImgView.setBackgroundResource(R.color.price_color );
         * ImageLoader.getInstance().displayImage(
         * "http://img4.chinaface.com/original/212dlz514BUdSEyhs4mJrrPXe1a4E.jpg" , articleHolder.authorImageView);
         * break;
         * 
         * }
         */
        // 设置微游记作者姓名
        // articleHolder.authorTextView.setText(article.getAuthor());
        articleHolder.timeTextView.setText(article.getPublishTime());
        articleHolder.tripDescribeTextView.setText(article.getDescription());
        // ImageLoader.getInstance().displayImage(article.getPhotosUrl().get(0), articleHolder.backgroundImgView);
        System.out.println("数值为:" + isLikeList.get(position));
        // articleHolder.isLikeCheckBox.setChecked(isLikeList.get(position));
        // articleHolder.isLikeCheckBox.setChecked(article.isLike());
        return convertView;
    }
    
    /**
     * 内部类 ViewHolder
     */
    class ViewHolder
    {
        // checkbox 是否喜欢
        private CheckBox isLikeCheckBox;
        
        // 微游记作者头标
        private ImageView authorImageView;
        
        // 微游记作者姓名
        private TextView authorTextView;
        
        // 发布时间
        private TextView timeTextView;
        
        // 微游记描述
        private TextView tripDescribeTextView;
        
        // 微游记背景图片
        private ImageView backgroundImgView;
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        CheckBox cb = (CheckBox)buttonView;
        int positionId = (Integer)cb.getTag();
        if (isChecked)
        {
            isLikeList.put(positionId, true);
        }
        else
        {
            isLikeList.put(positionId, false);
        }
        cb.setChecked(isLikeList.get(positionId));
    }
}
