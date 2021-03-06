package com.bcinfo.tripaway.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.activity.ChatActivity;
import com.bcinfo.tripaway.activity.MainActivity;
import com.bcinfo.tripaway.activity.MessageActivity;
import com.bcinfo.tripaway.adapter.QueuesListAdapter;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.QueuesInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.db.QueuesListDB;
import com.bcinfo.tripaway.getui.receiver.PushDemoReceiver;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppInfo;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.refreshandload.XListView;
import com.bcinfo.tripaway.view.refreshandload.XListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author hanweipeng
 * @date 2015-7-8 下午3:48:05
 */
public class MessageFragment extends BaseFragment implements OnClickListener, OnItemClickListener, IXListViewListener
{
    protected static final String TAG = "MessageFragment";
    
    
    private RelativeLayout notice;
    /*
     * 消息列表
     */
    private XListView mMessageListView;
    
    /*
     * 消息列表适配器
     */
    private QueuesListAdapter mMessageListAdapter;
    
    public static final int NEW_MESSAGE = 0x000;// 有新消息
    
    private View mNetErrorView;
    
    private int pageNum = 1;
    
    private String pageSize = "10";
    
    private List<QueuesInfo> queuesInfoList = new ArrayList<QueuesInfo>();
    
    /**
     * 下拉刷新
     */
    private boolean isPullRefresh = false;
    
    /**
     * 上拉加载更多
     */
    private boolean isLoadmore = false;


	private RelativeLayout singleChat;


	private RelativeLayout groupChat;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        mNetErrorView = view.findViewById(R.id.net_status_bar_top);
        mMessageListView = (XListView)view.findViewById(R.id.message_listview);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.message_fragment_header, null);
        mMessageListView.addHeaderView(header);
        notice=(RelativeLayout)view.findViewById(R.id.notice_layout);
        singleChat=(RelativeLayout)view.findViewById(R.id.single_chat_layout);
        groupChat=(RelativeLayout)view.findViewById(R.id.group_chat_layout);
        mNetErrorView.setOnClickListener(this);
        notice.setOnClickListener(this);
        singleChat.setOnClickListener(this);
        groupChat.setOnClickListener(this);
        initListView();
        getMessageQueues();
        PushDemoReceiver.ehList.add(mEventHandler);// 监听推送的消息
        registerBoradcastReceiver();
        return view;
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	statisticsTitle="消息";
    }
    /**
     * 初始化消息列表
     * 
     * @param userList
     */
    private void initListView()
    {
        // mMessageDataList = mRecentDB.getRecentList(); // 获得最近的消息list
        // 查询缓存数据 加载缓存数据
        List<QueuesInfo> queuesInfos =
            QueuesListDB.getInstance().queryAllQueuesInfoByUserId(PreferenceUtil.getUserId());
        if (queuesInfos != null)
        {
            queuesInfoList.addAll(queuesInfos);
        }
        mMessageListView.setPullLoadEnable(false);
        mMessageListView.setPullRefreshEnable(true);
        mMessageListView.setXListViewListener(this);
        mMessageListAdapter = new QueuesListAdapter(getActivity(), queuesInfoList);
        mMessageListView.setAdapter(mMessageListAdapter);
        // mMessageListView.setOnItemClickListener(this);
    }
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        PushDemoReceiver.ehList.remove(mEventHandler);// 注销推送的消息
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
    
    private void getMessageQueues()
    {
        RequestParams params = new RequestParams();
        params.put("pagenum", pageNum);// 当前页码
        params.put("pagesize", pageSize);// 页码数
        HttpUtil.get(Urls.messsage_queues_url, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(getActivity(), "error=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(getActivity(), "error=" + throwable.getMessage());
                isPullRefresh = false;
                isLoadmore = false;
                if (isLoadmore)
                {
                    mMessageListView.stopLoadMore();
                }
                if (isPullRefresh)
                {
                    mMessageListView.stopRefresh();
                }
                if (pageNum != 1)
                {
                    pageNum--;
                }
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i(TAG, "查询消息列表接口", response.toString());
                String code = response.optString("code");
                if (!isLoadmore && !isPullRefresh)
                {
                    queuesInfoList.clear();
                }
                if (isLoadmore)
                {
                    // 上拉返回的结束加载更多布局
                    mMessageListView.stopLoadMore();
                }
                if (code.equals("00000"))
                {
                    if (isPullRefresh)
                    {
                        // 下拉刷新返回的
                        mMessageListView.successRefresh();
                        queuesInfoList.clear();
                    }
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                        JSONArray queuesArray = dataJson.optJSONArray("queues");
                        List<QueuesInfo> queuesInfos = new ArrayList<QueuesInfo>();
                        String deleteStr = PreferenceUtil.getHaveDeleteStr();
                        LogUtil.i(TAG, "已经删除了的列表id", deleteStr);
                        if (queuesArray != null && queuesArray.length() > 0)
                        {
                            for (int i = 0; i < queuesArray.length(); i++)
                            {
                                JSONObject queuesJson = queuesArray.optJSONObject(i);
//                                if (deleteStr.contains(queuesJson.optString("queueId"))
//                                    && queuesJson.optString("unread").equals("0"))
//                                {
//                                    System.out.println("本地已经删除的列表，本地不在处理");
//                                    continue;
//                                }
//                                else
//                                {
//                                    deleteStr = deleteStr.replace(queuesJson.optString("queueId") + ",", "");
//                                    PreferenceUtil.setString("queueId", deleteStr);
//                                }
                                QueuesInfo queuesInfo = new QueuesInfo();
                                queuesInfo.setQueueId(queuesJson.optString("queueId"));
                                queuesInfo.setQueueLogo(queuesJson.optString("queueLogo"));
                                queuesInfo.setQueueTitle(queuesJson.optString("queueTitle"));
                                queuesInfo.setType(queuesJson.optString("type"));
                                queuesInfo.setUnread(queuesJson.optString("unread"));
                                JSONArray avatarsArray = queuesJson.optJSONArray("avatars");
                                if (avatarsArray != null && avatarsArray.length() > 0)
                                {
                                    List<String> avatarsList = new ArrayList<String>();
                                    for (int j = 0; j < avatarsArray.length(); j++)
                                    {
                                        avatarsList.add(avatarsArray.optString(j));
                                        queuesInfo.setAvatarsList(avatarsList);
                                    }
                                }
                                JSONObject messageJson = queuesJson.optJSONObject("message");
                                if (messageJson != null && messageJson.length() > 0)
                                {
                                    MessageInfo messageInfo = new MessageInfo();
                                    messageInfo.setId(messageJson.optString("id"));
                                    
                                    if (deleteStr.contains(messageInfo.getId()))
                                        {
                                            System.out.println("本地已经删除的列表，本地不在处理");
                                            continue;
                                        }
                                    messageInfo.setContent(messageJson.optString("content"));
                                    messageInfo.setCreatetime(messageJson.optString("createtime"));
                                    messageInfo.setImage(messageJson.optString("image"));
                                    messageInfo.setPattern(messageJson.optString("pattern"));
                                    messageInfo.setTitle(messageJson.optString("title"));
                                    JSONObject senderJson = messageJson.optJSONObject("sender");
                                    if (senderJson != null && senderJson.length() > 0)
                                    {
                                        UserInfo senderInfo = new UserInfo();
                                        senderInfo.setGender(senderJson.optString("sex"));
                                        senderInfo.setAddress(senderJson.optString("address"));
                                        senderInfo.setStatus(senderJson.optString("status"));
                                        senderInfo.setEmail(senderJson.optString("email"));
                                        senderInfo.setNickname(senderJson.optString("nickName"));
                                        senderInfo.setUserId(senderJson.optString("userId"));
                                        senderInfo.setRole(senderJson.optString("role"));
                                        senderInfo.setPermission(senderJson.optString("permission"));
                                        senderInfo.setAvatar(senderJson.optString("avatar"));
                                        senderInfo.setIntroduction(senderJson.optString("introduction"));
                                        senderInfo.setMobile(senderJson.optString("mobile"));
                                        senderInfo.setUsersIdentity(senderJson.optString("usersIdentity"));
                                        JSONArray tagsJsonArray = senderJson.optJSONArray("tags");
                                        if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                                        {
                                            ArrayList<String> tags = new ArrayList<String>();
                                            for (int j = 0; j < tagsJsonArray.length(); j++)
                                            {
                                                tags.add(tagsJsonArray.optString(j));
                                            }
                                            senderInfo.setTags(tags);
                                        }
                                        messageInfo.setSender(senderInfo);
                                    }
                                    queuesInfo.setMessageInfo(messageInfo);
                                }
                                queuesInfos.add(queuesInfo);
                                QueuesListDB.getInstance().insertData(queuesInfo);
                            }
                        }
                        if (queuesInfos.size() < 10)
                        {
                            mMessageListView.setPullLoadEnable(false);
                        }
                        else
                        {
                            pageNum++;
                            mMessageListView.setPullLoadEnable(true);
                        }
                        queuesInfoList.addAll(queuesInfos);
                        mMessageListAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
//                    ToastUtil.showToast(getActivity(), "errorCode=" + response.optString("code"));
                    if (isPullRefresh)
                    {
                        // 下拉刷新接口返回的数据不正确
                        mMessageListView.stopRefresh();
                    }
                    if (pageNum != 1)
                    {
                        pageNum--;
                    }
                }
                // 上拉 下拉的初始状态置为false;
                isLoadmore = false;
                isPullRefresh = false;
            }
        });
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        // RecentItem item = (RecentItem)mMessageListAdapter.getItem(position);
        // User u = new User(item.getUserId(), "", item.getName(), item.getHeadImg(), 0);
        // // mMsgDB.clearNewCount(item.getUserId());
        // Intent toChatIntent = new Intent(getActivity(), ChatActivity.class);
        // toChatIntent.putExtra("user", u);
        // startActivity(toChatIntent);
        // getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.net_status_bar_top:
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                break;
            case R.id.notice_layout://通知
            	   intent = new Intent(getActivity(),ChatActivity.class);
            	   intent.putExtra("isNotice",true);
                  startActivity(intent);
                  animationOpen();
            	break;
            case R.id.single_chat_layout://私信
            	 intent = new Intent(getActivity(),MessageActivity.class);
          	   intent.putExtra("type","private");
                startActivity(intent);
                animationOpen();
            	break;
            case R.id.group_chat_layout://群聊
            	 intent = new Intent(getActivity(),MessageActivity.class);
            	   intent.putExtra("type","team");
                  startActivity(intent);
                  animationOpen();
            	break;
            default:
                break;
        }
    }
    /**
     * 页面启动动画
     */
    protected void animationOpen()
    {
        getActivity().overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
    }

    
    /**
     * 接收消息通知Handler
     */
    // private Handler mHandler = new Handler()
    // {
    // public void handleMessage(Message msg)
    // {
    // switch (msg.what)
    // {
    // case NEW_MESSAGE:
    // pageNum = 1;
    // getMessageQueues();
    // // String message = (String)msg.obj;
    // // String userId = JsonUtil.getFromUserId(message);
    // // String nick = JsonUtil.getFromUserNick(message);
    // // String content = JsonUtil.getMsgContent(message);
    // // int headId = 0;
    // // try
    // // {
    // // headId = Integer.parseInt(JsonUtil.getFromUserHead(message));
    // // }
    // // catch (Exception e)
    // // {
    // // LogUtil.e(TAG, "handleMessage", "head is not integer  " + e);
    // // }
    // break;
    // default:
    // break;
    // }
    // }
    // };
    
    /**
     * 事件监听EventHandler
     */
    private PushDemoReceiver.EventHandler mEventHandler = new PushDemoReceiver.EventHandler()
    {
        @Override
        public void onMessage(String message)
        {
            // TODO Auto-generated method stub
            // Message handlerMsg = mHandler.obtainMessage(NEW_MESSAGE);
            // handlerMsg.obj = message;
            // mHandler.sendMessage(handlerMsg);
            pageNum = 1;
            getMessageQueues();
        }
        
        @Override
        public void onNotify(String title, String content)
        {
            // TODO Auto-generated method stub
        }
        
        @Override
        public void onNetChange(boolean isNetConnected)
        {
            // TODO Auto-generated method stub
            if (!isNetConnected)
            {
                mNetErrorView.setVisibility(View.VISIBLE);
            }
            else
            {
                mNetErrorView.setVisibility(View.GONE);
            }
        }
    };
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        isPullRefresh = true;
        pageNum = 1;
        getMessageQueues();
    }
    
    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
        isLoadmore = true;
        getMessageQueues();
    }
    
    private void registerBoradcastReceiver()
    {
        IntentFilter filter = new IntentFilter();
        // 监听网络状态
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("com.bcinfo.clearLogin");
        filter.addAction("com.bcinfo.refreshUnread");
        filter.addAction("com.bcinfo.haveLogin");
        getActivity().registerReceiver(mBroadcastReceiver, filter);
    }
    
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
            {
                ConnectivityManager connectivityManager =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null)
                {
                    NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                    for (int i = 0; i < networkInfos.length; i++)
                    {
                        State state = networkInfos[i].getState();
                        if (NetworkInfo.State.CONNECTED == state)
                        {
                            mNetErrorView.setVisibility(View.GONE);
                            return;
                        }
                        mNetErrorView.setVisibility(View.VISIBLE);
                    }
                }
            }
            if (action.equals("com.bcinfo.clearLogin"))
            {
                queuesInfoList.clear();
                mMessageListAdapter.notifyDataSetChanged();
                
            }
            else if (action.equals("com.bcinfo.refreshUnread"))
            {
                refreshUnRead();
            }
            else if (action.equals("com.bcinfo.haveLogin"))
            {
            	refreshUnRead();
            }
        }
    };
    
    public void refreshUnRead()
    {
        // TODO Auto-generated method stub
        pageNum = 1;
        getMessageQueues();
    }
    
}
