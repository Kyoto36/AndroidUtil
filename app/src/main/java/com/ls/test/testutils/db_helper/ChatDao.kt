package com.ls.test.testutils.db_helper

import android.content.Context
import android.database.Cursor
import com.ls.comm_util_library.LogUtils
import java.lang.Exception

/**
 * @ClassName: ChatDao
 * @Description:
 * @Author: ls
 * @Date: 2020/11/5 15:05
 */
class ChatDao(uid: String, context: Context) {
    private val mChatDBHelper = ChatDBHelper.get(uid, context)
    private val mSelfUid = uid
    private val mTableNamePrefix = uid + "_"

    private fun createTable(uid: String) {
        val createSql = "CREATE TABLE IF NOT EXISTS `${mTableNamePrefix + uid}`(" +
                "`${Message.ID}` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`${Message.SEND_UID}` TEXT," +
                "`${Message.RECV_UID}` TEXT," +
                "`${Message.CONTENT}` TEXT," +
                "`${Message.TIME}` INTEGER," +
                "`${Message.STATE}` INTEGER)"
        LogUtils.d("147258","createTable createSql $createSql")
        try {
            mChatDBHelper.writableDatabase.execSQL(createSql)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isExists(uid: String): Boolean {
        var result = false
        var cursor: Cursor? = null
        val querySql = "SELECT count(*) FROM sqlite_master WHERE name = '${mTableNamePrefix + uid}'"
        LogUtils.d("147258","isExists querySql $querySql")
        try {
            cursor = mChatDBHelper.readableDatabase.rawQuery(
                querySql,
                null
            )
            if (cursor.moveToNext()) {
                val count = cursor.getInt(0)
                result = count > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return result
    }

    fun getMaxId(uid: String): Long {
        var maxId = 0L
        var cursor: Cursor? = null
        val querySql = "SELECT max(${Message.ID}) FROM '${mTableNamePrefix + uid}'"
        LogUtils.d("147258","getMaxId querySql $querySql")
        try {
            cursor = mChatDBHelper.readableDatabase.rawQuery(
                querySql,
                null
            )
            if (cursor.moveToNext()) {
                maxId = cursor.getLong(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return maxId
    }

    fun getMinId(uid: String): Long {
        var maxId = 0L
        var cursor: Cursor? = null
        val querySql = "SELECT min(${Message.ID}) FROM '${mTableNamePrefix + uid}'"
        LogUtils.d("147258","getMinId querySql $querySql")
        try {
            cursor = mChatDBHelper.readableDatabase.rawQuery(
                querySql,
                null
            )
            if (cursor.moveToNext()) {
                maxId = cursor.getLong(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return maxId
    }

    fun insertNews(uid: String, messages: MutableList<Message?>?) {
        if (messages.isNullOrEmpty()) {
            return
        }
        messages.forEach { insert(uid, it) }
    }

    fun insert(uid: String, message: Message?) {
        if (message == null) {
            return
        }
        createTable(uid)
        val insertSql =
            "INSERT INTO '${mTableNamePrefix + uid}' VALUES(NULL,'${message.sendUid}','${message.recvUid}','${message.content}',${message.time},${message.state})"
        LogUtils.d("147258","insert insertSql $insertSql")
        try {
            mChatDBHelper.writableDatabase.execSQL(insertSql)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(uid: String, id: Long) {
        if (isExists(uid)) {
            val deleteSql = "DELETE FROM '${mTableNamePrefix + uid}' WHERE ${Message.ID} = $id"
            LogUtils.d("147258","delete deleteSql $deleteSql")
            mChatDBHelper.writableDatabase.execSQL(deleteSql)
        }
    }

    fun getMessages(uid: String, id: Long, pageSize: Int): MutableList<Message?>? {
        if (!isExists(uid)) {
            return null
        }
        var tempId = id
        if (id >= 0) {
            tempId = getMaxId(uid) + 1
        }
        val list = ArrayList<Message?>()
        val selectSql =
            "SELECT * FROM '${mTableNamePrefix + uid}' WHERE ${Message.ID} < $tempId ORDER BY ${Message.ID} DESC LIMIT $pageSize"
        LogUtils.d("147258","getMessages selectSql $selectSql")
        var cursor: Cursor? = null
        try {
            cursor = mChatDBHelper.readableDatabase.rawQuery(selectSql, null)
            while (cursor.moveToNext()) {
                list.add(Message.convert(cursor))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }

}