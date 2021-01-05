package com.ls.test.testutils.db_helper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ls.comm_util_library.ThreadUtils
import com.ls.test.testutils.R
import kotlinx.android.synthetic.main.activity_d_b_helper.*

class DBHelperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_b_helper)
        val dao = ChatDao("10001",this)
        select1.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val list = dao.getMessages("10002",0,20)
                message1.post {
                    message1.text = ""
                    list?.forEach {
                        if(it != null){
                            message1.append(it.toString() + "\n")
                        }
                    }
                }
            })
        }

        add1.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val message = Message()
                message.sendUid = "10001"
                message.recvUid = "10002"
                message.content = "44444"
                message.time = System.currentTimeMillis() / 1000
                message.state = 0
                dao.insert("10002",message)
                select1.post { select1.performClick() }
            })
        }

        delete1.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                dao.delete("10002",dao.getMinId("10002"))
                select1.post { select1.performClick() }
            })
        }

        select2.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val list = dao.getMessages("10003",0,20)
                message2.post {
                    message2.text = ""
                    list?.forEach {
                        if(it != null){
                            message2.append(it.toString() + "\n")
                        }
                    }
                }
            })
        }

        add2.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val message = Message()
                message.sendUid = "10001"
                message.recvUid = "10002"
                message.content = "44444"
                message.time = System.currentTimeMillis() / 1000
                message.state = 0
                dao.insert("10003",message)
                select2.post { select2.performClick() }
            })
        }

        delete2.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                dao.delete("10003",dao.getMinId("10003"))
                select2.post { select2.performClick() }
            })
        }

        val dao1 = ChatDao("10002",this)

        select3.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val list = dao1.getMessages("10001",0,20)
                message3.post {
                    message3.text = ""
                    list?.forEach {
                        if(it != null){
                            message3.append(it.toString() + "\n")
                        }
                    }
                }
            })
        }

        add3.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                val message = Message()
                message.sendUid = "10001"
                message.recvUid = "10002"
                message.content = "44444"
                message.time = System.currentTimeMillis() / 1000
                message.state = 0
                dao1.insert("10001",message)
                select3.post { select3.performClick() }
            })
        }

        delete3.setOnClickListener {
            ThreadUtils.execIO(Runnable {
                dao1.delete("10004",dao.getMinId("10001"))
                select3.post { select3.performClick() }
            })
        }
    }
}