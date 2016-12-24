package com.bcinfo.tripaway.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.util.LruCache;

public class CacheUtils {

/*
 * 设置引用来缓冲图片数据
 * 
 * 
 */
	private static LruCache<String, Bitmap> cache;
	private static HashMap<String, SoftReference<Bitmap>> softs;

	private CacheUtils() {
	}

	private static CacheUtils utils;

	public static CacheUtils getInstance() {
		if (utils == null) {
			utils = new CacheUtils();
			softs = new HashMap<String, SoftReference<Bitmap>>();
			long memory = Runtime.getRuntime().maxMemory();
			cache = new LruCache<String, Bitmap>((int) (memory / 8)) {
			
				@Override
				protected int sizeOf(String key, Bitmap value) {
					// TODO Auto-generated method stub
					return value.getByteCount();
				}

		
				@Override
				protected void entryRemoved(boolean evicted, String key,
						Bitmap oldValue, Bitmap newValue) {
					
					softs.put(key, new SoftReference<Bitmap>(oldValue));

				}
			};

		}
		return utils;
	}

	public Bitmap getBitmap(String icon) {
		
		Bitmap result = cache.get(icon);
		if (result == null) {
			
			SoftReference<Bitmap> map = softs.get(icon);
			if (map != null) {
				result = map.get();
				return result;
			} else {
				return null;
			}
		}
		return result;
	}

	public void saveBitmap(String key, Bitmap bit) {
		cache.put(key, bit);
	}

}
