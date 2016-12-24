package com.bcinfo.tripaway.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.net.HttpUtil;
import com.bcinfo.tripaway.net.Urls;
import com.bcinfo.tripaway.view.date.DayPicker;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 发起结伴
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年1月29日 上午10:56:13
 */
public class AddGoWithActivity extends BaseActivity implements OnClickListener
{
    private TextView themes;
    
    private TextView predictDate;
    
    private int mYear = 0;
    
    private int mMonth = 0;
    
    private int mDay = 0;
    
    /**
     * 日期选择时间（精确到天）对话框
     */
    private AlertDialog dateDialog;
    
    /**
     * 日期选择时间
     */
    private DayPicker mdayPicker;
    
    private EditText start_address, destination_address, title, description;
    
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.add_go_with_activity);
        predictDate = (TextView)findViewById(R.id.predict_time);
        
        start_address = (EditText)findViewById(R.id.go_with_start_address);
        destination_address = (EditText)findViewById(R.id.go_with_destination_address);
        title = (EditText)findViewById(R.id.go_with_title);
        description = (EditText)findViewById(R.id.go_with_description);
        TextView commit_button = (TextView)findViewById(R.id.commit_button);
        
        LinearLayout layout_back_button = (LinearLayout)findViewById(R.id.layout_back_button);
        themes = (TextView)findViewById(R.id.go_with_theme);
        RelativeLayout layout_predict_time = (RelativeLayout)findViewById(R.id.layout_predict_time);
        RelativeLayout themeLayout = (RelativeLayout)findViewById(R.id.go_with_theme_layout);
        layout_predict_time.setOnClickListener(this);
        themeLayout.setOnClickListener(this);
        commit_button.setOnClickListener(this);
        layout_back_button.setOnClickListener(this);
        setDateTime();
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId())
        {
            case R.id.layout_back_button:
                finish();
                break;
            case R.id.layout_predict_time:
                // DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                // dateSetListener, mYear, mMonth, mDay);
                // datePickerDialog.updateDate(mYear, mMonth, mDay);
                // datePickerDialog.show();
                dayPickerDialog();
                break;
            case R.id.go_with_theme_layout:
                intent = new Intent(this, GoWithThemeActivity.class);
                startActivityForResult(intent, 0);
                activityAnimationOpen();
                break;
            case R.id.commit_button:
                if (checkinfo())
                {
                    testAddGoWithUrl();
                }
                break;
            case R.id.ok_button_date:
                Calendar calendar = mdayPicker.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String notify = year + "年" + (month + 1) + "月" + day + "日";
                predictDate.setText(notify);
                predictDate.setTextColor(getResources().getColor(R.color.dark_gray));
                dateDialog.cancel();
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 0)
            {
                ArrayList<String> themeList = data.getStringArrayListExtra("themeList");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < themeList.size(); i++)
                {
                    builder.append(themeList.get(i));
                    if (i < themeList.size() - 1)
                    {
                        builder.append(",");
                    }
                }
                themes.setText(builder.toString());
            }
        }
    }
    
    /**
     * 设置日期
     */
    private void setDateTime()
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        // updateDateDisplay();
    }
    
    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDateDisplay();
        }
    };
    
    /**
     * 更新开始日期显示
     */
    private void updateDateDisplay()
    {
        StringBuilder beginBuilder = new StringBuilder();
        beginBuilder.append(mYear);
        beginBuilder.append("-");
        beginBuilder.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1));
        beginBuilder.append("-");
        beginBuilder.append((mDay < 10) ? "0" + mDay : mDay);
        predictDate.setText(beginBuilder);
    }
    
    /**
     * 日期选择
     */
    private void dayPickerDialog()
    {
        dateDialog = new AlertDialog.Builder(this).create();
        dateDialog.show();
        Window window = dateDialog.getWindow();
        window.setContentView(R.layout.day_picker_dialog);
        mdayPicker = (DayPicker)window.findViewById(R.id.alarm_date_picker);
        Button ok = (Button)window.findViewById(R.id.ok_button_date);
        Button cancel = (Button)window.findViewById(R.id.cancel_button_date);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                dateDialog.cancel();
            }
        });
    }
    
    /**
     * 判断条件是否全部填写
     */
    private boolean checkinfo()
    {
        if (predictDate.getText().toString().equals("预计时间") || predictDate.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "日期不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (start_address.getText().equals("填写结伴出发地") || start_address.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "出发地不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (destination_address.getText().equals("填写结伴目的地") || destination_address.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "目的地不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (title.getText().equals("为你的结伴填写个标题") || title.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "标题不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (description.getText().equals("为你的结伴填写个描述") || description.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "描述不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (themes.getText().equals("主题") || themes.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(), "主题不能为空哦!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    
    /**
     * test 发起结伴接口
     */
    private void testAddGoWithUrl()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("planTime", predictDate.getText().toString());
            jsonObject.put("startPoint", start_address.getText().toString());
            jsonObject.put("endPoint", destination_address.getText().toString());
            jsonObject.put("title", title.getText().toString());
            jsonObject.put("description", description.getText().toString());
            JSONArray topicsArray = new JSONArray();
            String[] themesarr = themes.getText().toString().split(",");
            for (int i = 0; i < themesarr.length; i++)
            {
                topicsArray.put(themesarr[i]);
            }
            jsonObject.put("topics", topicsArray);
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpUtil.post(Urls.together_url, stringEntity, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("", "1111---- code=" + statusCode);
                    Log.d("", "1111---- code=" + response);
                    if ("00000".equals(response.optString("code")))
                    {
                        
                    }
                }
                
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
                {
                    Log.d("", "1111---- code=" + statusCode);
                    Log.d("", "1111---- code=" + responseString);
                    Log.d("", "1111---- code=" + throwable);
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
