// IAidlCallBack.aidl
package com.xf.test1;

import com.xf.test1.Entity;

// Declare any non-default types here with import statements

interface IAidlCallBack {

    void callBack(String name);

    void callBackEntity(in Entity entity);
}
