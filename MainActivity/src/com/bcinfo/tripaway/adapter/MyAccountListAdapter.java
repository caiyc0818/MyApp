package com.bcinfo.tripaway.adapter;

import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.bean.Account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的账户-账户列表 adapter
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月30日 10:05:23
 */
public class MyAccountListAdapter extends BaseAdapter
{
    private List<Account> AccountList;
    private Context mContext;
    private LayoutInflater inflator;

    public MyAccountListAdapter(List<Account> AccountList, Context mContext)

    {
        super();
        this.AccountList = AccountList;
        this.mContext = mContext;
        inflator = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {

        return AccountList.size();
    }

    @Override
    public Object getItem(int position)
    {

        return AccountList.get(position);
    }

    @Override
    public long getItemId(int position)
    {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item_my_account_added, null);
            holder.briefIconTv = (ImageView) convertView.findViewById(R.id.my_account_added_iv);
            holder.briefNameTv = (TextView) convertView.findViewById(R.id.my_account_added_brief_name_tv);
            holder.briefDefaultTv = (ImageView) convertView.findViewById(R.id.my_account_added_brief_default_iv);
            holder.briefAccountNameTv = (TextView) convertView.findViewById(R.id.my_account_added_brief_accountName_tv);
            holder.addItemTv = (TextView) convertView.findViewById(R.id.my_account_add_item_tv);
            holder.addedBriefContainer = (LinearLayout) convertView.findViewById(R.id.layout_my_account_added_brief_container);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Account myaccount = AccountList.get(position);
        if (myaccount == null)
        {
            return convertView;
        }
        if (AccountList.size() - 1 > position)
        {
            if (myaccount.getIsdefault() != null && myaccount.getIsdefault().equals("1"))
            {
                holder.briefDefaultTv.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.briefDefaultTv.setVisibility(View.GONE);
            }
            holder.briefNameTv.setText(myaccount.getType());
            holder.briefAccountNameTv.setText(myaccount.getNo());
            holder.briefIconTv.setImageResource(R.drawable.alipay_binded_icon);
            holder.addedBriefContainer.setVisibility(View.VISIBLE); //
            holder.addItemTv.setVisibility(View.GONE);
        }
        else if (AccountList.size() - 1 == position)
        {
            holder.briefIconTv.setImageResource(R.drawable.my_account_add_mark);
            holder.addedBriefContainer.setVisibility(View.GONE);
            holder.addItemTv.setVisibility(View.VISIBLE);
            holder.briefDefaultTv.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder
    {
        ImageView briefIconTv;// 图标
        TextView briefNameTv;// 账户名称
        ImageView briefDefaultTv;// 默认 图标
        TextView briefAccountNameTv;// 服务者的支付账户
        TextView addItemTv;// 添加账号
        LinearLayout addedBriefContainer; // 默认显示账号信息
    }
}
