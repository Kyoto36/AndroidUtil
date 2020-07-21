package com.ls.permission;

import android.Manifest;
import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Permissions {
    public static final int PERMISSION_CODE = 10001;
    public static final String TAG = "permission-tag";


    public static Permissions with(Fragment fragment){
        return with(fragment.getChildFragmentManager());
    }

    public static Permissions with(FragmentActivity activity){
        return with(activity.getSupportFragmentManager());
    }

    private static Permissions with(FragmentManager manager){
        Permissions permissions = new Permissions();
        permissions.mFragmentMannger = manager;
        return permissions;
    }

    private Permissions(){}

    private PermissionFragment findFragment(FragmentManager manager){
        PermissionFragment fragment = (PermissionFragment) manager.findFragmentByTag(TAG);
        if(fragment == null){
            fragment = new PermissionFragment();
            manager.beginTransaction().add(fragment,TAG).commitNow();
        }
        return fragment;
    }

    private FragmentManager mFragmentMannger;
    private PermissionFragment mPermissionFragment;
    private String[] mPermissions;

    public Permissions request(String... permissions){
        mPermissions = permissions;
        mPermissionFragment = findFragment(mFragmentMannger);
        return this;
    }

    public Permissions requestStorage(){
        return request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void callback(final Callback callback){
        if(mPermissionFragment == null || mPermissions == null){
            throw new NullPointerException("call Permissions.with(activity|fragment).request(String... permissions).callback");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onResult(true);
        }
        mPermissionFragment.request(mPermissions, new Function1<Map<String, Boolean>, Unit>() {
            @Override
            public Unit invoke(Map<String, Boolean> grants) {
                callback.onResult(isGrant(grants));
                return null;
            }
        });
    }

    private boolean isGrant(Map<String, Boolean> grants){
        for (Map.Entry<String,Boolean> entry: grants.entrySet()){
            if(!entry.getValue()) return false;
        }
        return true;
    }

    public interface Callback{
        void onResult(boolean success);
    }
}
