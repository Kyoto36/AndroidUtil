package com.ls.test.testutils.db_helper;

import android.database.Cursor;

import com.ls.retrofit_library.db.DownloadInfo;

/**
 * @ClassName: Message
 * @Description:
 * @Author: ls
 * @Date: 2020/11/5 10:37
 */
public class Message {
    public static final String ID = "id";
    public static final String SEND_UID = "send_uid";
    public static final String RECV_UID = "recv_uid";
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public static final String STATE = "state";
    private long id = -1;
    private String sendUid;
    private String recvUid;
    private String content;
    private long time;
    private int state = 0; // 0正常状态，1发送失败

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getRecvUid() {
        return recvUid;
    }

    public void setRecvUid(String recvUid) {
        this.recvUid = recvUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sendUid='" + sendUid + '\'' +
                ", recvUid='" + recvUid + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", state=" + state +
                '}';
    }

    public static Message convert(Cursor cursor){
        Message message = new Message();
        message.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        message.setSendUid(cursor.getString(cursor.getColumnIndex(SEND_UID)));
        message.setRecvUid(cursor.getString(cursor.getColumnIndex(RECV_UID)));
        message.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
        message.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
        message.setState(cursor.getInt(cursor.getColumnIndex(STATE)));
        return message;
    }
}
