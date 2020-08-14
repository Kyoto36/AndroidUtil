package com.ls.retrofit_library.db

import android.content.Context
import com.ls.retrofit_library.db.DBHelper
import com.ls.retrofit_library.db.DownloadInfo
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

    fun save(info: DownloadInfo) {
        val list = query(info.url, info.savePath)
        if (list.isEmpty() || list.size > 1) {
            delete(list)
            insert(info)
        } else {
            info.id = list[0].id
            update(info)
        }
    }

    fun update(info: DownloadInfo){
        if(info.id == -1L){
            insert(info)
            return
        }
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("update ${DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" set ")
        sqlBuilder.append("${DownloadInfo.URL}='${info.url}',")
        sqlBuilder.append("${DownloadInfo.SAVE_PATH}='${info.savePath}',")
        sqlBuilder.append("${DownloadInfo.TOTAL_SIZE}=${info.totalSize},")
        sqlBuilder.append("${DownloadInfo.ALREADY_SIZE}=${info.alreadySize},")
        sqlBuilder.append("${DownloadInfo.UPDATE_TIME}=${Date().time},")
        sqlBuilder.append("${DownloadInfo.DOWN_STATE}=${info.downState}")
        sqlBuilder.append(" where ${DownloadInfo.ID}=${info.id}")
        val db = mDBHelper.writableDatabase
        db.execSQL(sqlBuilder.toString())
        db.close()
    }

    fun insert(info: DownloadInfo) {
        val db = mDBHelper.writableDatabase
        db.execSQL("insert into ${DownloadInfo.TABLE_NAME} values (null,'${info.url}','${info.savePath}',${info.totalSize},${info.alreadySize},${Date().time},${info.downState})")
        db.close()
    }

    fun delete(infos: List<DownloadInfo>) {
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
        db.execSQL("delete from ${DownloadInfo.TABLE_NAME} where ${DownloadInfo.ID} in $ids")
        db.close()
    }

    fun delete(info: DownloadInfo) {
        val db = mDBHelper.writableDatabase
        db.execSQL("delete from ${DownloadInfo.TABLE_NAME} where ${DownloadInfo.ID}=${info.id}")
        db.close()
    }

    fun query(url: String, savePath: String, totalSize: Long): List<DownloadInfo> {
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select * from ${DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" where ")
        sqlBuilder.append("${DownloadInfo.URL}='$url'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DownloadInfo.SAVE_PATH}='$savePath'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DownloadInfo.TOTAL_SIZE}=$totalSize")
        return query(sqlBuilder.toString())
    }

    fun query(url: String, savePath: String): List<DownloadInfo> {
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select * from ${DownloadInfo.TABLE_NAME}")
        sqlBuilder.append(" where ")
        sqlBuilder.append("${DownloadInfo.URL}='$url'")
        sqlBuilder.append(" and ")
        sqlBuilder.append("${DownloadInfo.SAVE_PATH}='$savePath'")
        return query(sqlBuilder.toString())
    }

    fun list(): List<DownloadInfo>{
        val sqlBuilder = StringBuilder()
        sqlBuilder.append("select * from ${DownloadInfo.TABLE_NAME}")
        return query(sqlBuilder.toString())
    }

    private fun query(sql: String): List<DownloadInfo> {
        val downloadInfos = ArrayList<DownloadInfo>()
        val db = mDBHelper.readableDatabase
        val cursor = db.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            downloadInfos.add(DownloadInfo.convert(cursor))
        }
        cursor.close()
        return downloadInfos
    }
}