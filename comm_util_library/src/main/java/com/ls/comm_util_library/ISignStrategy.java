package com.ls.comm_util_library;

import java.util.Map;

public interface ISignStrategy<T> {
    String onSign(T unSign);
}
