package com.ls.permission

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

import java.util.HashMap

class PermissionFragment : Fragment() {
    private var mGrants: HashMap<String,Boolean>? = null
    private var mCallback:((Map<String,Boolean>) -> Unit)? = null

    @TargetApi(Build.VERSION_CODES.M)
    fun request(vararg permissions: String,callback: (Map<String,Boolean>) -> Unit) {
        mGrants = HashMap()
        mCallback = callback
        requestPermissions(permissions, Permissions.PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.PERMISSION_CODE) {
            for (i in permissions.indices){
                Log.d("123123",permissions[i] + " " + (grantResults[i] == PackageManager.PERMISSION_GRANTED))
                mGrants!![permissions[i]] = grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
            mCallback!!.invoke(mGrants!!)
        }
    }
}
