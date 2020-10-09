package com.example.assignment2.Subsciber;

import java.io.IOException;

public interface HelperSubscriber<T>  {
    void onNext(T t) throws IOException;
}
