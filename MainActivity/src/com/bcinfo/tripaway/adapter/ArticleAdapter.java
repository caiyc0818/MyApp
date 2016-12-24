package com.bcinfo.tripaway.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ContentActivity;
import com.bcinfo.tripaway.bean.Article;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArticleAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Article> artList;
	
	public ArticleAdapter(){
		
	}
	
	public ArticleAdapter(Context mContext,List<Article> artList){
		this.mContext = mContext;
		this.artList = artList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return artList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return artList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Article article = artList.get(position);
		LayoutInflater inflater = null;
		ViewHolder holder = null;
		if(convertView == null){
			inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.my_article_list_item, null);
			holder = new ViewHolder();
			holder.imageView =(ImageView) convertView.findViewById(R.id.article_photo);
			holder.mArticleName =(TextView) convertView.findViewById(R.id.article_name);
			holder.mArticleIntro = (TextView) convertView.findViewById(R.id.article_introduce);
			convertView.setTag(holder);
		}else{
			holder= (ViewHolder) convertView.getTag();
		}
		holder.imageView.setImageResource(R.drawable.ic_launcher);
//		if(article.getImageList() != null&&article.getImageList().size()>0){
//			String url = article.getImageList().get(0).getUrl();
//			if(!StringUtils.isEmpty(url)){
//				ImageLoader.getInstance().displayImage(Urls.imgHost+url, holder.imageView, AppConfig.options(R.drawable.ic_launcher));
//			}
//		}
		if(article.getCover() != null){
			if(!StringUtils.isEmpty(article.getCover())){
				ImageLoader.getInstance().displayImage(Urls.imgHost+article.getCover(), holder.imageView, AppConfig.options(R.drawable.ic_launcher));
			}
		}
		holder.imageView.setTag(article.getArticleId());
//		holder.imageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String id = (String) v.getTag();
//				String path = Urls.content_host +id;
//				Intent it = new Intent(mContext,ContentActivity.class);
//				it.putExtra("path", path);
//				mContext.startActivity(it);
//			}
//		});
		
		holder.mArticleName.setText(article.getTitle());
		holder.mArticleIntro.setText(article.getAbstracts());
		return convertView;
	}
	
	
	class ViewHolder{
		
		private ImageView imageView;
		
		private TextView mArticleName;
		
		private TextView mArticleIntro;
	}

}
