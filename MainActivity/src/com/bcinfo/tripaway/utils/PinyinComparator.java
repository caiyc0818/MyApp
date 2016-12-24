package com.bcinfo.tripaway.utils;

import java.util.Comparator;

import com.bcinfo.tripaway.bean.AreaInfo;

public class PinyinComparator  implements Comparator<AreaInfo> {  
  
    public int compare(AreaInfo o1, AreaInfo o2) {  
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
        if (o2.getSortLetter().equals("#")) {  
            return -1;  
        } else if (o1.getSortLetter().equals("#")) {  
            return 1;  
        } else {  
            return o1.getSortLetter().compareTo(o2.getSortLetter());  
        }  
    }  
}  
