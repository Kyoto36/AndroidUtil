package com.ls.comm_util_library;

public interface IDoubleResultListener<P1,P2,R> {
    R onResult(P1 p1,P2 p2);
}
