package com.george5613.test.callback;

public interface ActivityStatusCallback {

    public void onActivityTouch();

    default public void onTimeout() {}
}
