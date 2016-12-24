package com.bcinfo.tripaway.fragment;

import java.util.List;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.adapter.ExperienceAdapter;
import com.bcinfo.tripaway.bean.Experiences;
import com.bcinfo.tripaway.view.MyListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class  TourGuideExperienceFragment extends Fragment  
{  
    private  MyListView listView;
    private  ExperienceAdapter adapter;
    private  List<Experiences> list;
    
    public TourGuideExperienceFragment(List<Experiences> list){
    	this.list=list;
    }
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  	
    	
        View view = inflater.inflate(R.layout.experience_fragment,null);//注意不要指定父视图  
        listView=(MyListView) view.findViewById(R.id.experience_list_view); 
        adapter=new ExperienceAdapter(getActivity(),list);
     
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
        
        return view;  
    }  
    
   
    
   
}  
