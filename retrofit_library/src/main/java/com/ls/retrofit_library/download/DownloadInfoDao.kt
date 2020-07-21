package com.ls.retrofit_library.download

import android.content.Context
import com.ls.retrofit_library.download.DBHelper
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * @ClassName: DBDao
 * @Description:
 * @Author: ls
 * @Date: 2020/3/27 14:20
 */
class DownloadInfoDao(context: Context) {

    private val mDBHelper = DBHelper(context)

    fun save(info: DBHelper.DownloadInfo) {
        val list = query(info.url, info.savePath, info.totalSize)
        if (list.isEmpty() || list.size > 1) {
            delete(list)
            insert(info)
        } else {
            info.id = list[0].id
            update(info)
        }
    }

    fun update(info: DBHelper.DownloadInfo){
        if(info.id == -1L){
            insert(info)
            return
        }
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("update ${DBHelper.DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" set ")
        sqlBuilder.append("${DBHelper.DownloadInfo.URL}='${info.url}',")
        sqlBuilder.append("${DBHelper.DownloadInfo.SAVE_PATH}='${info.savePath}',")
        sqlBuilder.append("${DBHelper.DownloadInfo.TOTAL_SIZE}=${info.totalSize},")
        sqlBuilder.append("${DBHelper.DownloadInfo.ALREADY_SIZE}=${info.alreadySize},")
        sqlBuilder.append("${DBHelper.DownloadInfo.UPDATE_TIME}=${Date().time},")
        sqlBuilder.append("${DBHelper.DownloadInfo.DOWN_STATE}=${info.downState}")
        sqlBuilder.append(" where ${DBHelper.DownloadInfo.ID}=${info.id}")
        val db = mDBHelper.writableDatabase
        db.execSQL(sqlBuilder.toString())
        db.close()
    }

    fun insert(info: DBHelper.DownloadInfo) {
        val db = mDBHelper.writableDatabase
        db.execSQL("insert into ${DBHelper.DownloadInfo.TABLE_NAME} values (null,'${info.url}','${info.savePath}',${info.totalSize},${info.alreadySize},${Date().time},${info.downState})")
        db.close()
    }

    fun delete(infos: List<DBHelper.DownloadInfo>) {
        if (infos.isEmpty()) {
            return
        }
        var ids = StringBuilder()
        ids.append("(")
        for (info in infos) {
            ids.append(info.id).append(",")
        }
        ids = ids.delete(ids.length - 1, ids.length)
        ids.append(")")
        val db = mDBHelper.writableDatabase
        db.execSQL("delete from ${DBHelper.DownloadInfo.TABLE_NAME} where ${DBHelper.DownloadInfo.ID} in $ids")
        db.close()
    }

    fun delete(info: DBHelper.DownloadInfo) {
        val db = mDBHelper.writableDatabase
        db.execSQL("delete from ${DBHelper.DownloadInfo.TABLE_NAME} where ${DBHelper.DownloadInfo.ID}=${info.id}")
        db.close()
    }

    fun query(url: String, savePath: String, totalSize: Long): List<DBHelper.DownloadInfo> {
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select * from ${DBHelper.DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" where ")
        sqlBuilder.append("${DBHelper.DownloadInfo.URL}='$url'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DBHelper.DownloadInfo.SAVE_PATH}='$savePath'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DBHelper.DownloadInfo.TOTAL_SIZE}=$totalSize")
        return query(sqlBuilder.toString())
    }

    fun query(url: String, savePath: String): List<DBHelper.DownloadInfo> {
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select * from ${DBHelper.DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" where ")
        sqlBuilder.append("${DBHelper.DownloadInfo.URL}='$url'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DBHelper.DownloadInfo.SAVE_PATH}='$savePath'")
        return query(sqlBuilder.toString())
    }

    private fun query(sql: String): List<DBHelper.DownloadInfo> {
        val downloadInfos = ArrayList<DBHelper.DownloadInfo>()
        val db = mDBHelper.readableDatabase
        val cursor = db.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            downloadInfos.add(DBHelper.DownloadInfo.convert(cursor))
        }
        cursor.close()
        return downloadInfos
    }
}