package com.ls.comm_util_library;

public interface IBaseFragment {
    // 是否拦截返回键
    boolean onBack();
    void setInteractive(IInteractiveFragment2Activity interactive);
}
