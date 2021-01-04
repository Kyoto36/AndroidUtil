package com.ls.comm_util_library

import android.graphics.Bitmap
import android.util.LruCache

/**
 * @ClassName: BitmapCache
 * @Description:
 * @Author: ls
 * @Date: 2020/9/18 18:30
 */
open class BitmapCache {
    protected val mCache = object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 10).toInt()){
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            //计算一个元素的缓存大小
            return value?.byteCount ?: 0
        }
    }

    /**
     * 添加图片到 LruCache
     *
     * @param key
     * @param bitmap
     */
    open fun addBitmap(key: String?, bitmap: Bitmap?) {
        mCache.put(key, bitmap)
    }

    /**
     * 从缓存中获取图片
     *
     * @param key
     * @return
     */
    open fun getBitmap(key: String?): Bitmap? {
        return mCache.get(key)
    }
}