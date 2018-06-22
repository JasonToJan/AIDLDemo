package com.xf.test1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AIDLService extends Service {


    private AIDLImp mBinder;


    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new AIDLImp(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.KillAll();
    }
}
