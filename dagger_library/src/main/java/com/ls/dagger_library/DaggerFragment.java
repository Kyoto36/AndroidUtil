package com.ls.dagger_library;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ls.comm_util_library.IBaseFragment;

/**
 * @ClassName: DaggerFragment
 * @Description:
 * @Author: ls
 * @Date: 2020/9/10 18:22
 */
public abstract class DaggerFragment extends Fragment implements IBaseFragment {
    @Override
    public void onAttach(@NonNull Context context) {
        CustomInjection.inject(this);  // 一处声明，处处依赖注入
        super.onAttach(context);
    }
}
