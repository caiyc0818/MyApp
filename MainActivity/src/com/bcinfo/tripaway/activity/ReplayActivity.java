package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.MeaasgeListAdapter;
import com.bcinfo.tripaway.adapter.MeaasgeListAdapter.ReSendMessageInterface;
import com.bcinfo.tripaway.adapter.ReplyListAdapter;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.camera.TestPicActivity;
import com.bcinfo.tripaway.db.MessageInfoDB;
import com.bcinfo.tripaway.getui.msglistview.MsgListView;
import com.bcinfo.tripaway.getui.msglistview.MsgListView.IXListViewListener;
import com.bcinfo.tripaway.getui.receiver.PushDemoReceiver;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.BitmapUtil;
import com.bcinfo.tripaway.utils.DateUtil;
import com.bcinfo.tripaway.utils.LogUtil;
import com.bcinfo.tripaway.utils.PreferenceUtil;
import com.bcinfo.tripaway.utils.StringUtils;
import com.bcinfo.tripaway.utils.ToastUtil;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog;
import com.bcinfo.tripaway.view.dialog.SelectPicDialog.OperationListener;
import com.bcinfo.tripaway.view.emoji.Emojicon;
import com.bcinfo.tripaway.view.emoji.EmojiconEditText;
import com.bcinfo.tripaway.view.emoji.SelectFaceHelper;
import com.bcinfo.tripaway.view.emoji.SelectFaceHelper.OnFaceOprateListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

public class ReplayActivity extends BaseActivity implements OnClickListener, ReSendMessageInterface, IXListViewListener
{
    /**
     * 返回按钮
     */
    private LinearLayout backLayout;
    
    /**
     * 标题
     */
    private TextView titleTxt;
    
    /**
     * 查看群组成员
     */
    private ImageView groupBtn;
    
    /**
     * 消息列表
     */
    private MsgListView msgListView;
    
    /**
     * 添加图片
     */
    private ImageButton addImageBtn;
    
    /**
     * 添加表情
     */
    private ImageView faceBtn;
    
    /**
     * 信息输入框
     */
    private EmojiconEditText messageEdt;
    
    /**
     * 发送按钮
     */
    private Button sendBtn;
    
    /**
     * 添加表情布局
     */
    private View faceView;
    
    /**
     * 添加图片布局
     */
    private RelativeLayout addPicImg;
    
    private SelectFaceHelper mFaceHelper;
    
    private ReplyListAdapter messageAdapter;
    
    private String queueId = "";
    
    private String title = "";
    
//    private boolean isFromQueue = false;
    
    private int pagenum = 1;
    
    private int pagesize = 20;
    
    private String path = "";
    
    private List<MessageInfo> messageList = new ArrayList<MessageInfo>();
    
    /**
     * 上传toaken
     */
    private String uploadToken;
    
//    private boolean isTeam = false;
    
    private String receiveId = "";
    ArrayList<String> listfile;
    ArrayList<String> lists;

	private String type;
	
	private LinearLayout inputBar;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.replay_activity);
        
            Log.e("ReplayActivity", "onCreate");
//            isFromQueue = getIntent().getBooleanExtra("fromQueue", false);
            queueId = getIntent().getStringExtra("queueId");
            if (queueId == null)
            {
                queueId = "";
            }
            type = getIntent().getStringExtra("type");
//            isTeam = getIntent().getBooleanExtra("isTeam", false);
//            if (receiveId == null)
//            {
//                receiveId = "";
//            }
        initView();
        
      
    }
    
    protected void initView()
    {
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
        titleLayout.setAlpha(1);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
        backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
        titleTxt = (TextView)findViewById(R.id.second_title_text);
		if (type.equals("complaint")) {
			titleTxt.setText("投诉详情");
		} else
			titleTxt.setText("咨询详情");
        groupBtn = (ImageView)findViewById(R.id.user_info_button);
        msgListView = (MsgListView)findViewById(R.id.msg_listView);
        msgListView.setPullLoadEnable(false);
        msgListView.setXListViewListener(this);
        addImageBtn = (ImageButton)findViewById(R.id.image_btn);
        faceBtn = (ImageView)findViewById(R.id.face_btn);
        messageEdt = (EmojiconEditText)findViewById(R.id.msg_et);
        sendBtn = (Button)findViewById(R.id.send_btn);
        faceView = findViewById(R.id.add_tool);
        addPicImg = (RelativeLayout)findViewById(R.id.add_pic);
//        if (isFromQueue)
//        {
//            messageList = MessageInfoDB.getInstance().queryMessageListByQueueId(queueId);
//        }
        messageAdapter = new ReplyListAdapter(this, messageList);
        msgListView.setAdapter(messageAdapter);
        msgListView.setSelection(messageList.size() - 1);
        backLayout.setOnClickListener(this);
        groupBtn.setOnClickListener(this);
        addImageBtn.setOnClickListener(this);
        faceBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        addPicImg.setOnClickListener(this);
        groupBtn.setVisibility(View.GONE);
        inputBar=(LinearLayout)this.findViewById(R.id.inputBar);
//        if (isFromQueue)
//        {
//            if (isTeam)
//            {
//                groupBtn.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                groupBtn.setVisibility(View.GONE);
//            }
//            getReplyDetail();
//        }
//        else
//        {
//            groupBtn.setVisibility(View.GONE);
//        }
        getReplyDetail();   
        messageEdt.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (faceView.isShown())
                {
                    faceView.setVisibility(View.GONE);
                }
                if (addPicImg.isShown())
                {
                    addPicImg.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                activityAnimationClose();
                break;
            case R.id.user_info_button:
                Intent intent = new Intent(this, GrouponMemberActivity.class);
                intent.putExtra("queueId", queueId);
                intent.putExtra("title", title);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_new, R.anim.activity_out);
                break;
            case R.id.image_btn:
                if (faceView.isShown())
                {
                    faceView.setVisibility(View.GONE);
                }
                if (addPicImg.isShown())
                {
                    addPicImg.setVisibility(View.GONE);
                }
                else
                {
                    addPicImg.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.face_btn:
                if (addPicImg.isShown())
                {
                    addPicImg.setVisibility(View.GONE);
                }
                if (null == mFaceHelper)
                {
                    mFaceHelper = new SelectFaceHelper(ReplayActivity.this, faceView);
                    mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
                }
                if (faceView.isShown())
                {
                    faceView.setVisibility(View.GONE);
                }
                else
                {
                    faceView.setVisibility(View.VISIBLE);
                    hideInputManager(ReplayActivity.this);
                }
                break;
            case R.id.send_btn:
                if (StringUtils.isEmpty(messageEdt.getText().toString()))
                {
                    ToastUtil.showToast(ReplayActivity.this, "不能发送空消息");
                    return;
                }
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.getSender().setUserId(PreferenceUtil.getUserId());
                messageInfo.setContent(messageEdt.getText().toString());
                messageInfo.getSender().setAvatar(PreferenceUtil.getString("avatar"));
                messageInfo.setCreatetime(DateUtil.getCurrentTimeStr());
                messageInfo.setPattern("chat");
                messageList.add(messageInfo);
                messageAdapter.notifyDataSetChanged();
                sendMessage(messageInfo);
                messageEdt.setText("");
                hideInputManager(ReplayActivity.this);
                msgListView.setSelection(messageList.size() - 1);
                break;
            case R.id.add_pic:
                new SelectPicDialog(ReplayActivity.this, mOperationListener).show();
                break;
            default:
                break;
        }
    }
    

    

    
    
    OperationListener mOperationListener = new OperationListener()
    {
        @Override
        public void operationPhoto(int witch)
        {
            // TODO Auto-generated method stub
            Intent intent = null;
            switch (witch)
            {
//                case 1:
//                    Intent intent1 =
//                        new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent1, 0);
//                    break;
                    
                case 1:
                    Intent intent1 =
                        new Intent(ReplayActivity.this, TestPicActivity.class);
                    startActivityForResult(intent1, 0);
                    break;
                    
                case 2:
                    String haveSD = Environment.getExternalStorageState();
                    if (!haveSD.equals(Environment.MEDIA_MOUNTED))
                    {
                        Toast.makeText(ReplayActivity.this, "存储卡不可用", Toast.LENGTH_LONG).show();
                        return;
                    }
                    File dir = new File(Environment.getExternalStorageDirectory() + "/" + "tripaway");
                    if (!dir.exists())
                    {
                        dir.mkdirs();
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                    Date date = new Date();
                    String str = format.format(date);
                    path = Environment.getExternalStorageDirectory() + "/" + "tripaway" + "/" + str + "photo.jpg";
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File imgFile = new File(dir, str + "photo.jpg");
                    Uri uri = Uri.fromFile(imgFile);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, 1);
                    break;
                default:
            }
        }
    };
    
    private void input(Emojicon emojicon)
    {
        if (messageEdt == null || emojicon == null)
        {
            return;
        }
        
        int start = messageEdt.getSelectionStart();
        int end = messageEdt.getSelectionEnd();
        if (start < 0)
        {
            messageEdt.append(emojicon.getEmoji());
        }
        else
        {
            messageEdt.getText().replace(Math.min(start, end),
                Math.max(start, end),
                emojicon.getEmoji(),
                0,
                emojicon.getEmoji().length());
        }
    }
    
    OnFaceOprateListener mOnFaceOprateListener = new OnFaceOprateListener()
    {
        @Override
        public void onFaceSelected(Emojicon emojicon)
        {
            Log.i("mOnFaceOprateListener", "onFaceSelected");
            input(emojicon);
        }
        
        @Override
        public void onFaceDeleted()
        {
            Log.i("mOnFaceOprateListener", "onFaceDeleted");
            backspace(messageEdt);
        }
        
    };
    
    private void getReplyDetail()
    {
        RequestParams params = new RequestParams();
        params.put("feedBackId", queueId);
        params.put("pagenum", pagenum);// 当前页码
        params.put("pagesize", pagesize);// 页码数
        HttpUtil.get(Urls.get_consultation_reply, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                msgListView.stopRefresh();
//                ToastUtil.showToast(ReplayActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                msgListView.stopRefresh();
//                ToastUtil.showToast(ReplayActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("ReplayActivity", "查询消息详情接口", response.toString());
                String code = response.optString("code");
                if (code.equals("00000"))
                {
                    if (pagenum == 1)
                    {
                        messageList.clear();
                    }
                    msgListView.stopRefresh();
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                        // JSONObject productJson = dataJson.optJSONObject("product");
                        JSONArray messageArray = dataJson.optJSONArray("feedBackReply");
                        List<MessageInfo> tempList = getMessageInfos(messageArray);
                        messageList.addAll(tempList);
                        Collections.sort(messageList);
                        messageAdapter.notifyDataSetChanged();
                        if (pagenum == 1)
                        {
                            msgListView.setSelection(messageList.size() - 1);
                        }
                        else
                        {
                            msgListView.setSelection(0);
                        }
                        pagenum++;
                        Intent intent = new Intent("com.bcinfo.refreshUnread");
                        sendBroadcast(intent);
                    }
                }
                else
                {
                    ToastUtil.showToast(ReplayActivity.this, response.optString("msg"));
                }
            }
            
        });
    }
    
    private void sendMessage(final MessageInfo newMessage)
    {
        try
        {
            
            JSONObject messageJson = new JSONObject();
           
            messageJson.put("content", StringUtils.strConvertUnicode(newMessage.getContent()));
            messageJson.put("feedBackId",queueId);
         
            StringEntity stringEntity = new StringEntity(messageJson.toString(), HTTP.UTF_8);
            HttpUtil.post(Urls.send_consultation_reply, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    ToastUtil.showToast(ReplayActivity.this, "throwable=" + throwable.getMessage());
                    // for (int i = 0; i < messageList.size(); i++)
                    // {
                    // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                    // {
                    // messageList.get(i).setSendStatus("1");
                    // break;
                    // }
                    // }
                    newMessage.setSendStatus("1");
                    msgListView.setSelection(messageList.size() - 1);
                    messageAdapter.notifyDataSetChanged();
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    ToastUtil.showToast(ReplayActivity.this, "throwable=" + throwable.getMessage());
                    // for (int i = 0; i < messageList.size(); i++)
                    // {
                    // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                    // {
                    // messageList.get(i).setSendStatus("1");
                    // break;
                    // }
                    // }
                    newMessage.setSendStatus("1");
                    msgListView.setSelection(messageList.size() - 1);
                    messageAdapter.notifyDataSetChanged();
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    LogUtil.i("ReplayActivity", "发送消息接口=", response.toString());
                    if (response.optString("code").equals("00000"))
                    {
                        newMessage.setId(response.optString("data"));
                        //MessageInfoDB.getInstance().insertData(newMessage);
                        messageAdapter.notifyDataSetChanged();
                        msgListView.setSelection(messageList.size() - 1);
                    }
                    else
                    {
                        //ToastUtil.showToast(ReplayActivity.this, "errorCode=" + response.optString("code"));
                    	System.out.println("errorCode=" + response.optString("code"));
                    }
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
   
    
  
    
    private byte[] getNewPicture(String path)
    {
        int degree = BitmapUtil.getBitmapDegree(path);
        Bitmap oldBitmap = ImageLoader.getInstance().loadImageSync("file:///" + path);
        Bitmap newBitmap = BitmapUtil.rotaingImageView(degree, oldBitmap);
        ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
        newBitmap.compress(CompressFormat.PNG, 100, output);// 把bitmap100%高质量压缩 到 output对象里
        
        newBitmap.recycle();// 自由选择是否进行回收
        
        byte[] result = output.toByteArray();// 转换成功了
        return result;
    }
    
    @Override
    public void reSend(int position)
    {
        // TODO Auto-generated method stub
        MessageInfo messageInfo = messageList.get(position);
                sendMessage(messageInfo);
          
    }
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        if (getApplication() != null)
        {
            getReplyDetail();
        }
        else
        {
            Log.e("ReplayActivity", "聊天页面被回收");
        }
    }
    
    @Override
    public void onLoadMore()
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
     //   PushDemoReceiver.ehList.remove(mEventHandler);
    }
    
  
    
    private List<MessageInfo> getMessageInfos(JSONArray messageArray)
    {
        List<MessageInfo> tempList = new ArrayList<MessageInfo>();
        if (messageArray != null && messageArray.length() > 0)
        {
            for (int i = 0; i < messageArray.length(); i++)
            {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setQueueId(queueId);
                JSONObject messageJson = messageArray.optJSONObject(i);
                messageInfo.setCreatetime(messageJson.optString("createTime"));
                messageInfo.setContent(messageJson.optString("content"));
                messageInfo.setId(messageJson.optString("replyId"));
                UserInfo userInfo=new UserInfo();
                userInfo.setUserId(messageJson.optString("replierId"));
                if (userInfo.getUserId()!=null&&!userInfo.getUserId().equals(PreferenceUtil.getUserId())){
                	inputBar.setVisibility(View .GONE);
                }
                userInfo.setNickname(messageJson.optString("replier"));
                messageInfo.setSender(userInfo);
              
             
                tempList.add(messageInfo);
            }
        }
        return tempList;
    }

}
