package com.bcinfo.tripaway.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.AppConfig;
import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.TripawayApplication;
import com.bcinfo.tripaway.adapter.MeaasgeListAdapter;
import com.bcinfo.tripaway.adapter.MeaasgeListAdapter.ReSendMessageInterface;
import com.bcinfo.tripaway.bean.MessageInfo;
import com.bcinfo.tripaway.bean.OrgRole;
import com.bcinfo.tripaway.bean.ProductNewInfo;
import com.bcinfo.tripaway.bean.UserInfo;
import com.bcinfo.tripaway.camera.TestPicActivity;
import com.bcinfo.tripaway.db.MessageInfoDB;
import com.bcinfo.tripaway.getui.msglistview.MsgListView;
import com.bcinfo.tripaway.getui.msglistview.MsgListView.IXListViewListener;
import com.bcinfo.tripaway.getui.receiver.PushDemoReceiver;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.utils.AppManager;
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

public class ChatActivity extends BaseActivity implements OnClickListener, ReSendMessageInterface, IXListViewListener
{
	
	private ProductNewInfo productInfo;
	private ImageView proPic;
	private TextView proTitle;
	private TextView prosTitle;
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
    private RelativeLayout get;
    private SelectFaceHelper mFaceHelper;
    
    private MeaasgeListAdapter messageAdapter;
    
    private String queueId = "";
    
    private String title = "";
    
    private boolean isFromQueue = false;
    
    private int pagenum = 1;
    
    private int pagesize = 20;
    
    private String path = "";
    
    private List<MessageInfo> messageList = new ArrayList<MessageInfo>();
    
    /**
     * 上传toaken
     */
    private String uploadToken;
    private ProductNewInfo prod;
    private boolean isTeam = false;
    
    private String receiveId = "";
    ArrayList<String> listfile;
    ArrayList<String> lists;
    
    private boolean isNotice=false;
	private LinearLayout inputBar;
    @Override
    protected void onNewIntent(Intent intent)
    {
    	
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        pagenum = 1;
        isFromQueue = intent.getBooleanExtra("fromQueue", false);
        queueId = intent.getStringExtra("queueId");
        if (queueId == null)
        {
            queueId = "";
        }
        
        //ToastUtil.showToast(ChatActivity.this, intent.getExtras()+"");
        
        //ToastUtil.showToast(ChatActivity.this, intent.getBundleExtra("ok")+"");
        title = intent.getStringExtra("title");
        isTeam = intent.getBooleanExtra("isTeam", false);
        receiveId = intent.getStringExtra("receiveId");
        if (receiveId == null)
        {
            receiveId = "";
        }
        initView();
    }
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.chat_activity);
        initBroadcastReceiver();
        if (arg0 != null)
        {
            Log.e("onCreate", "arg0 != null");
            path = arg0.getString("path");
            messageList = (List<MessageInfo>)arg0.get("messageList");
            isFromQueue = arg0.getBoolean("fromQueue", false);
            title = arg0.getString("title");
         
            queueId = arg0.getString("queueId");
            isTeam = arg0.getBoolean("isTeam", false);
            receiveId = arg0.getString("receiveId");
        }
        else
        {
            Log.e("ChatActivity", "onCreate");
            isFromQueue = getIntent().getBooleanExtra("fromQueue", false);
            queueId = getIntent().getStringExtra("queueId");
            if (queueId == null)
            {
                queueId = "";
            }
            title = getIntent().getStringExtra("chatTitle");
            if(title==null)
            {
            	title = getIntent().getStringExtra("title");
            	
            }
            LogUtil.i("聊天界面的标题", title, title);
            isTeam = getIntent().getBooleanExtra("isTeam", false);
            isNotice = getIntent().getBooleanExtra("isNotice", false);
            receiveId = getIntent().getStringExtra("receiveId");
            if (receiveId == null)
            {
                receiveId = "";
            }
        }

    	prod=(ProductNewInfo) getIntent().getExtras().get("ok");
        initView();
        PushDemoReceiver.ehList.add(mEventHandler);

      
    }
    
    protected void initView()
    {
    	get=(RelativeLayout) findViewById(R.id.get);
    	proPic=(ImageView) findViewById(R.id.proPic);
    	proTitle=(TextView) findViewById(R.id.proTitle);
    	prosTitle=(TextView) findViewById(R.id.prosTitle);
    	if(prod!=null)
    	{
    		get.setVisibility(View.VISIBLE);
    	
    		ImageLoader.getInstance().displayImage(Urls.imgHost + prod.getTitleImg(),
    				proPic, AppConfig.options(R.drawable.user_defult_photo));
    		proTitle.setText(prod.getTitle());
    		prosTitle.setText(prod.getUser().getNickname());
    		
    	}else{
    		get.setVisibility(View.GONE);
    	}
    
        RelativeLayout titleLayout = (RelativeLayout)findViewById(R.id.second_title);
        titleLayout.setAlpha(1);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.title_bg));
        backLayout = (LinearLayout)findViewById(R.id.layout_back_button);
        titleTxt = (TextView)findViewById(R.id.second_title_text);
        
        titleTxt.setText(title);
        if(isTeam==true)
        {
            titleTxt.setText("群聊");
        }
        inputBar=(LinearLayout) findViewById(R.id.inputBar);
        if(isNotice){
        	titleTxt.setText("通知");
        	inputBar.setVisibility(View.GONE);
        	getNotice();
        }
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
        if (isFromQueue)
        {
            messageList = MessageInfoDB.getInstance().queryMessageListByQueueId(queueId);
        }
        messageAdapter = new MeaasgeListAdapter(this, messageList);
        msgListView.setAdapter(messageAdapter);
        msgListView.setSelection(messageList.size() - 1);
        backLayout.setOnClickListener(this);
        groupBtn.setOnClickListener(this);
        addImageBtn.setOnClickListener(this);
        faceBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        addPicImg.setOnClickListener(this);
        get.setOnClickListener(this);
        if (isFromQueue)
        {
            if (isTeam)
            {
                groupBtn.setVisibility(View.VISIBLE);
            }
            else
            {
                groupBtn.setVisibility(View.GONE);
            }
            getMessageQueueDetail();
        }
        else
        {
            groupBtn.setVisibility(View.GONE);
        }
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
            	Intent intent2 = new Intent("com.bcinfo.refreshUnread");
                sendBroadcast(intent2);
                finish();
                activityAnimationClose();
                break;
            case R.id.user_info_button:
                Intent intent = new Intent(this, GrouponMemberActivity.class);
                intent.putExtra("queueId", queueId);
                intent.putExtra("title", "群成员");
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
            case R.id.get:
            	Intent it = new Intent(ChatActivity.this, GrouponProductNewDetailActivity.class);
            	it.putExtra("productTitle", prod.getTitle());
            	it.putExtra("productId", prod.getId());
    			startActivity(it);
            	break;
            case R.id.face_btn:
                if (addPicImg.isShown())
                {
                    addPicImg.setVisibility(View.GONE);
                }
                if (null == mFaceHelper)
                {
                    mFaceHelper = new SelectFaceHelper(ChatActivity.this, faceView);
                    mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
                }
                if (faceView.isShown())
                {
                    faceView.setVisibility(View.GONE);
                }
                else
                {
                    faceView.setVisibility(View.VISIBLE);
                    hideInputManager(ChatActivity.this);
                }
                break;
            case R.id.send_btn:
                if (StringUtils.isEmpty(messageEdt.getText().toString()))
                {
                    ToastUtil.showToast(ChatActivity.this, "不能发送空消息");
                    return;
                }
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.getSender().setUserId(PreferenceUtil.getUserId());
                messageInfo.setContent(messageEdt.getText().toString());
                messageInfo.getSender().setAvatar(PreferenceUtil.getString("avatar"));
                messageInfo.setCreatetime(DateUtil.getCurrentTimeStr());
                if (isTeam)
                {
                    messageInfo.setPattern("group_chat");
                }
                else
                {
                    messageInfo.setPattern("chat");
                }
                messageList.add(messageInfo);
                messageAdapter.notifyDataSetChanged();
                sendMessage(messageInfo);
                messageEdt.setText("");
                hideInputManager(ChatActivity.this);
                msgListView.setSelection(messageList.size() - 1);
                break;
            case R.id.add_pic:
                new SelectPicDialog(ChatActivity.this, mOperationListener).show();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        super.onSaveInstanceState(savedInstanceState);
        Log.e("onSaveInstanceState", "path-->" + path);
        savedInstanceState.putString("path", path);
        savedInstanceState.putString("queueId", queueId);
        savedInstanceState.putString("title", title);
        savedInstanceState.putSerializable("messageList", (Serializable)messageList);
        savedInstanceState.putBoolean("isFromQueue", isFromQueue);
        savedInstanceState.putBoolean("isTeam", isTeam);
        savedInstanceState.putString("receiveId", receiveId);
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
//        if (arg0 == 0 && arg1 == RESULT_OK && null != arg2)
//        {
//            Uri selectedImage = arg2.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            
//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            System.out.println(">>>>>"+picturePath);
//            sendImageMessage(picturePath);
//        }
//        else if (arg0 == 1 && arg1 == RESULT_OK)
//        {
//            if (null == path)
//                return;
//            sendImageMessage(path);
//        }
        if (arg0 == 0 && arg1 == 222 && null != arg2)
          {
        	ArrayList<String>  lists = new ArrayList<String>();
            
            lists=arg2.getExtras().getStringArrayList("qwer");
            System.out.println(lists);
            for (int i = 0; i < lists.size(); i++) {
            	sendImageMessage(lists.get(i));
			}
            
          }else if (arg0 == 1&& arg1 == Activity.RESULT_OK){
        	  Log.i("TEST", "camera----start--->"+path);
        	  if(path != null){
        		  sendImageMessage(path);
        	  }
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
                        new Intent(ChatActivity.this, TestPicActivity.class);
                    startActivityForResult(intent1, 0);
                    break;
                    
                case 2:
                    String haveSD = Environment.getExternalStorageState();
                    if (!haveSD.equals(Environment.MEDIA_MOUNTED))
                    {
                        Toast.makeText(ChatActivity.this, "存储卡不可用", Toast.LENGTH_LONG).show();
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
    
    private void getMessageQueueDetail()
    {
        RequestParams params = new RequestParams();
        params.put("queueId", queueId);
        params.put("pagenum", pagenum);// 当前页码
        params.put("pagesize", pagesize);// 页码数
        params.put("msgId", "");
        HttpUtil.get(Urls.message_queue_url, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                msgListView.stopRefresh();
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                msgListView.stopRefresh();
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("ChatActivity", "查询消息详情接口", response.toString());
                
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
                         JSONObject productJson = dataJson.optJSONObject("product");
                         if(productJson!=null)
                         {
                        		prod.setId(productJson.optString("id"));
                        		prod.setTitle(productJson.optString("title"));
                        		prod.setTitleImg( productJson.optString("titleImg"));
                            		get.setVisibility(View.VISIBLE);
                            		ImageLoader.getInstance().displayImage(Urls.imgHost + prod.getTitleImg(),
                            				proPic, AppConfig.options(R.drawable.user_defult_photo));
                            		proTitle.setText(prod.getTitle());
                            		
                            		
                            	
                            	
                        	 
                             
                            
                         }else{

                     		get.setVisibility(View.GONE);
                         }
                         if(productJson!=null)
                         {
                        	 
                         
                         JSONObject productCJson = productJson.optJSONObject("creater");
                         if(productCJson!=null)
                         {
                        	 
                        	  prosTitle.setText( productCJson.optString("nickName"));
                         }}
                       
                        JSONArray messageArray = dataJson.optJSONArray("messages");
                        List<MessageInfo> tempList = getMessageInfos(messageArray);
                        
                        messageList.addAll(tempList);
                        
                        Collections.sort(messageList,new Comparator<MessageInfo>() {

							@Override
							public int compare(MessageInfo lhs, MessageInfo rhs) {
								String time1 = lhs.getCreatetime();
								String time2 = rhs.getCreatetime();
								return time1.compareTo(time2);
							}
						});
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
                    ToastUtil.showToast(ChatActivity.this, response.optString("msg"));
                }
            }
            
        });
    }
    
    private void sendMessage(final MessageInfo newMessage)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            JSONArray receiversArray = new JSONArray();
            receiversArray.put(receiveId);
            jsonObject.put("receivers", receiversArray);
            JSONObject messageJson = new JSONObject();
            messageJson.put("title", title);
            messageJson.put("content", StringUtils.strConvertUnicode(newMessage.getContent()));
            messageJson.put("image", newMessage.getImage());
            messageJson.put("pattern", newMessage.getPattern());
            messageJson.put("productId", "");
            jsonObject.put("message", messageJson);
            jsonObject.put("queueId", queueId);
            System.out.println("jsonObject=" + jsonObject.toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            HttpUtil.post(Urls.send_message_url, stringEntity, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
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
//                    ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
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
                    LogUtil.i("ChatActivity", "发送消息接口=", response.toString());
                    if (response.optString("code").equals("00000"))
                    {
                        newMessage.setId(response.optString("data"));
                        MessageInfoDB.getInstance().insertData(newMessage);
                        messageAdapter.notifyDataSetChanged();
                        msgListView.setSelection(messageList.size() - 1);
                    }
                    else
                    {
                        //ToastUtil.showToast(ChatActivity.this, "errorCode=" + response.optString("code"));
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
    
    /*
     * 获取上传凭证 接口
     */
    private void getUploadTokenUrl(final MessageInfo newMessage, final byte[] fileByte)
    {
        
        RequestParams params = new RequestParams();
        params.put("bucket", "tripaway");// 存储空间
        HttpUtil.get(Urls.getUploadToken_url, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                
                super.onSuccess(statusCode, headers, response);
                if ("00000".equals(response.optString("code")))
                {
                    uploadToken = response.optJSONObject("data").optString("token");
                    testUploadToYunCode(newMessage, fileByte);
                }
                else
                {
                    // ToastUtil.showToast(ChatActivity.this, "头像修改失败");
                    // for (int i = 0; i < messageList.size(); i++)
                    // {
                    // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                    // {
                    newMessage.setSendStatus("1");
                    // break;
                    // }
                    // }
                    messageAdapter.notifyDataSetChanged();
                    msgListView.setSelection(messageList.size() - 1);
                }
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
                // for (int i = 0; i < messageList.size(); i++)
                // {
                // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                // {
                // messageList.get(i).setSendStatus("1");
                // break;
                // }
                // }
                newMessage.setSendStatus("1");
                messageAdapter.notifyDataSetChanged();
                msgListView.setSelection(messageList.size() - 1);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
                // for (int i = 0; i < messageList.size(); i++)
                // {
                // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                // {
                // messageList.get(i).setSendStatus("1");
                // break;
                // }
                // }
                newMessage.setSendStatus("1");
                messageAdapter.notifyDataSetChanged();
                msgListView.setSelection(messageList.size() - 1);
            }
        });
    }
    
    /*
     * 上传图片 到七牛云
     */
    private void testUploadToYunCode(final MessageInfo newMessage, final byte[] fileByte)
    {
        if (uploadToken != null)
        {
            TripawayApplication.uploadManager.put(fileByte, null, uploadToken, new UpCompletionHandler()
            {
                @Override
                public void complete(String arg0, ResponseInfo arg1, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    if (arg1.isOK())
                    {
                        String imageKey = response.optString("key");
                        if (!StringUtils.isEmpty(imageKey))
                        {
                            ImageLoader.getInstance().loadImage(Urls.imgHost + imageKey, null);
                        }
                        newMessage.setImage(imageKey);
                        sendMessage(newMessage);
                    }
                    else
                    {
                        // for (int i = 0; i < messageList.size(); i++)
                        // {
                        // if (messageList.get(i).getCreatetime().equals(newMessage.getCreatetime()))
                        // {
                        // messageList.get(i).setSendStatus("1");
                        // break;
                        // }
                        // }
                        newMessage.setSendStatus("1");
                        messageAdapter.notifyDataSetChanged();
                        msgListView.setSelection(messageList.size() - 1);
                    }
                    
                }
                
            }, new UploadOptions(null, null, false, new UpProgressHandler()
            {
                
                @Override
                public void progress(String arg0, double arg1)
                {
                    // TODO Auto-generated method stub
                    LogUtil.e("testUploadToYunCode", "progress", arg1 + "");
                }
            }, null));
        }
    }
    
    private void sendImageMessage(String picturePath)
    {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.getSender().setUserId(PreferenceUtil.getUserId());
        messageInfo.setContent("");
        messageInfo.setLocalImage(picturePath);
        // messageInfo.setImage(picturePath);
        messageInfo.getSender().setAvatar(PreferenceUtil.getString("avatar"));
        messageInfo.setCreatetime(DateUtil.getCurrentTimeStr());
        if (isTeam)
        {
            messageInfo.setPattern("group_chat");
        }
        else
        {
            messageInfo.setPattern("chat");
        }
        messageList.add(messageInfo);
        messageAdapter.notifyDataSetChanged();
        // File file = new File(picturePath);
        byte[] result = getNewPicture(picturePath);
        getUploadTokenUrl(messageInfo, result);
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
        if (!StringUtils.isEmpty(messageInfo.getImage()))
        {
            sendMessage(messageInfo);
        }
        else
        {
            if (!StringUtils.isEmpty(messageInfo.getLocalImage()))
            {
                byte[] result = getNewPicture(messageInfo.getLocalImage());
                getUploadTokenUrl(messageInfo, result);
            }
            else
            {
                sendMessage(messageInfo);
            }
        }
    }
    
    @Override
    public void onRefresh()
    {
        // TODO Auto-generated method stub
        if (getApplication() != null)
        {
        	if(isNotice){
        		if(messageList.size()%pagesize==0&&messageList.size()>0)
        			getNotice();
        		else 
        			 msgListView.stopRefresh();
        		return;
        	}
        	if(StringUtils.isEmpty(queueId)){
	       		 msgListView.stopRefresh();
	       		return;
	       	}
        	
            getMessageQueueDetail();
        }
        else
        {
            Log.e("ChatActivity", "聊天页面被回收");
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
        PushDemoReceiver.ehList.remove(mEventHandler);
    }
    
    /**
     * 查询未读消息
     */
    private void getUnReadMessage()
    {
        RequestParams params = new RequestParams();
        params.put("queueId", queueId);
        params.put("pagenum", 1);// 当前页码
        params.put("pagesize", pagesize);// 页码数
        if (messageList.size() > 0)
        {
            params.put("msgId", messageList.get(messageList.size() - 1).getId());
        }
        else
        {
            params.put("msgId", "");
        }
        HttpUtil.get(Urls.message_queue_url, params, new JsonHttpResponseHandler()
        {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                LogUtil.i("ChatActivity", "查询消息详情接口", response.toString());
                String code = response.optString("code");
                if (code.equals("00000"))
                {
                    JSONObject dataJson = response.optJSONObject("data");
                    if (dataJson != null && dataJson.length() > 0)
                    {
                        // JSONObject productJson = dataJson.optJSONObject("product");
                        JSONArray messageArray = dataJson.optJSONArray("messages");
                        List<MessageInfo> tempList = getMessageInfos(messageArray);
                        messageList.addAll(tempList);
                        Collections.sort(messageList);
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
//                    ToastUtil.showToast(ChatActivity.this, "errorCode=" + response.optString("code"));
                }
            }
            
        });
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
                messageInfo.setCreatetime(messageJson.optString("createtime"));
                messageInfo.setContent(messageJson.optString("content"));
                messageInfo.setId(messageJson.optString("id"));
                messageInfo.setTitle(messageJson.optString("title"));
                messageInfo.setPattern(messageJson.optString("pattern"));
                if (!StringUtils.isEmpty(messageJson.optString("image")))
                {
                    messageInfo.setImage(messageJson.optString("image"));
                }
                else
                {
                    messageInfo.setImage(messageJson.optString("image"));
                }
                JSONObject productJson = messageJson.optJSONObject("product");
                if (productJson != null && productJson.length() > 0)
                {
                    ProductNewInfo productInfo = new ProductNewInfo();
                    JSONArray topicJsonArray = productJson.optJSONArray("topics");
                    if (topicJsonArray != null && !topicJsonArray.equals(""))
                    {
                        ArrayList<String> topics = new ArrayList<String>();
                        for (int j = 0; j < topicJsonArray.length(); j++)
                        {
                            // LogUtil.i(TAG, "topicJsonArray", topicJsonArray.optString(j));
                            topics.add(topicJsonArray.optString(j));
                        }
                        productInfo.setTopics(topics);
                    }
                    productInfo.setExpStart(productJson.optString("expStart"));
                    productInfo.setExpend(productJson.optString("expend"));
                    JSONObject userJSON = productJson.optJSONObject("creater");
                    if (userJSON != null && !userJSON.toString().equals(""))
                    {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setGender(userJSON.optString("sex"));
                        userInfo.setAddress(userJSON.optString("address"));
                        userInfo.setStatus(userJSON.optString("status"));
                        userInfo.setEmail(userJSON.optString("email"));
                        userInfo.setNickname(userJSON.optString("nickName"));
                        userInfo.setUserId(userJSON.optString("userId"));
                        userInfo.setRole(userJSON.optString("role"));
                        userInfo.setPermission(userJSON.optString("permission"));
                        userInfo.setAvatar(userJSON.optString("avatar"));
                        userInfo.setIntroduction(userJSON.optString("introduction"));
                        userInfo.setMobile(userJSON.optString("mobile"));
                        JSONArray tagsJsonArray = userJSON.optJSONArray("tags");
                        if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                        {
                            ArrayList<String> tags = new ArrayList<String>();
                            for (int j = 0; j < tagsJsonArray.length(); j++)
                            {
                                tags.add(tagsJsonArray.optString(j));
                            }
                            userInfo.setTags(tags);
                        }
                        productInfo.setUser(userInfo);
                    }
                    productInfo.setId(productJson.optString("id"));
                    productInfo.setDistance(productJson.optString("distance"));
                    productInfo.setTitle(productJson.optString("title"));
                    productInfo.setPoiCount(productJson.optString("poiCount"));
                    productInfo.setPrice(productJson.optString("price"));
                    productInfo.setDays(productJson.optString("days"));
                    productInfo.setDescription(productJson.optString("description"));
                    productInfo.setPriceMax(productJson.optString("priceMax"));
                    productInfo.setTitleImg(productJson.optString("titleImg"));
                    productInfo.setMaxMember(productJson.optString("maxMember"));
                    productInfo.setProductType(productJson.optString("productType"));
                    productInfo.setServices(productJson.optString("serviceCodes"));
                    messageInfo.setProductInfo(productInfo);
                }
                JSONObject senderJson = messageJson.optJSONObject("sender");
                if (senderJson != null && senderJson.length() > 0)
                {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setGender(senderJson.optString("sex"));
                    userInfo.setAddress(senderJson.optString("address"));
                    userInfo.setStatus(senderJson.optString("status"));
                    userInfo.setEmail(senderJson.optString("email"));
                    userInfo.setNickname(senderJson.optString("nickName"));
                    userInfo.setUserId(senderJson.optString("userId"));

                    JSONObject orgJson = senderJson.optJSONObject("orgRole");
                    OrgRole ro=new OrgRole();
                    if(orgJson!=null)
                    {

                        ro.setRoleCode(orgJson.optString("roleCode"));
                        ro.setRoleName(orgJson.optString("roleName"));
                    }
                    userInfo.setOrgRole(ro);
                    userInfo.setRole(senderJson.optString("role"));
                    userInfo.setPermission(senderJson.optString("permission"));
                    userInfo.setAvatar(senderJson.optString("avatar"));
                    userInfo.setIntroduction(senderJson.optString("introduction"));
                    userInfo.setMobile(senderJson.optString("mobile"));
                    JSONArray tagsJsonArray = senderJson.optJSONArray("tags");
                    if (tagsJsonArray != null && tagsJsonArray.length() > 0)
                    {
                        ArrayList<String> tags = new ArrayList<String>();
                        for (int j = 0; j < tagsJsonArray.length(); j++)
                        {
                            tags.add(tagsJsonArray.optString(j));
                        }
                        userInfo.setTags(tags);
                    }
                    messageInfo.setSender(userInfo);
                }
                MessageInfoDB.getInstance().insertData(messageInfo);
                tempList.add(messageInfo);
            }
        }
        return tempList;
    }
    
    /**
     * 事件监听EventHandler
     */
    private PushDemoReceiver.EventHandler mEventHandler = new PushDemoReceiver.EventHandler()
    {
        @Override
        public void onMessage(String message)
        {
            // TODO Auto-generated method stub
            // Message handlerMsg = mHandler.obtainMessage(0x000);
            // handlerMsg.obj = message;
            // mHandler.sendMessage(handlerMsg);
        	if(isNotice) getNotice();
        	else
            getUnReadMessage();
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
        }
    };
    
    private void initBroadcastReceiver(){
    	IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.bcinfo.tripaway.exitteamTalk");
       registerReceiver(broadcastReceiver, myIntentFilter);
    }
    
    private  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if("com.bcinfo.tripaway.exitteamTalk".equals(action)){
    			finish();
    		}
    	}
    	};
    
    /**
     * 接收消息通知Handler
     */
    // private Handler mHandler = new Handler()
    // {
    // public void handleMessage(Message msg)
    // {
    // switch (msg.what)
    // {
    // case 0x000:
    // getUnReadMessage();
    // break;
    // default:
    // break;
    // }
    // }
    // };
    	
    	
    	 /**
         * 查询通知
         */
        private void getNotice()
        {
            RequestParams params = new RequestParams();
            params.put("userId", PreferenceUtil.getUserId());
            params.put("pagenum", pagenum);// 当前页码
            params.put("pagesize", pagesize);// 页码数
            params.put("type", "Client");
            HttpUtil.get(Urls.message_notice_url, params, new JsonHttpResponseHandler()
            {
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
                    msgListView.stopRefresh();
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    ToastUtil.showToast(ChatActivity.this, "throwable=" + throwable.getMessage());
                    msgListView.stopRefresh();
                }
                
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    msgListView.stopRefresh();
                    LogUtil.i("ChatActivity", "查询消息详情接口", response.toString());
                    String code = response.optString("code");
                    if (code.equals("00000"))
                    {
                        JSONArray dataJsons = response.optJSONArray("data");
                        if (dataJsons != null && dataJsons.length() > 0)
                        {
                        	for(int i=0;i<dataJsons.length();i++){
									JSONObject msgObj = dataJsons
											.optJSONObject(i);
									MessageInfo messageInfo = new MessageInfo();
									messageInfo.getSender().setUserId("-1");

									messageInfo.getSender().setAvatar("-1");
									messageInfo.setContent(msgObj
											.optString("content"));
									messageInfo.setId(msgObj
											.optString("id"));
									messageInfo.setCreatetime(msgObj
											.optString("sendTime"));
									messageInfo.setPattern("notice");
									messageList.add(messageInfo);
									
                        	}
							messageAdapter.notifyDataSetChanged();
							if(pagenum==1){
							msgListView.post(new Runnable() {  
							    @Override  
							    public void run() {  
							    	msgListView.requestFocusFromTouch();//获取焦点  
							    	msgListView.setSelection(msgListView.getHeaderViewsCount());  
							    }  
							}); 
							}
							if(dataJsons.length()%pagesize==0)
							++pagenum;
                        }
                    }
                    else
                    {
//                        ToastUtil.showToast(ChatActivity.this, "errorCode=" + response.optString("code"));
                    }
                }
                
            });
        }
}
