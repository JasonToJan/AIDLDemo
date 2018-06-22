package com.xf.test2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xf.test1.Entity;
import com.xf.test1.IAidlCallBack;
import com.xf.test1.IMyAidlInterface;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface mIMyAidlInterfaceService;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyAidlInterfaceService = IMyAidlInterface.Stub.asInterface(service);
            try {
                mIMyAidlInterfaceService.registerCallBack(mIAidlCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mIMyAidlInterfaceService.unregisterCallBack(mIAidlCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mIMyAidlInterfaceService = null;
        }
    };


    private IAidlCallBack mIAidlCallBack = new IAidlCallBack.Stub() {
        @Override
        public void callBack(String name) throws RemoteException {
            Log.e("info---->","服务端给我发消息啦:"+name);
            ToastUtil.showShort(MainActivity.this,"服务端给我发消息啦:"+name);
        }

        @Override
        public void callBackEntity(Entity entity) {
            Log.e("info---->","服务端给我发实体消息啦："+entity.toString());
            ToastUtil.showShort(MainActivity.this,"服务端给我发实体消息啦："+entity.toString());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null)
            unbindService(mServiceConnection);
    }

    /**
     * 绑定服务
     * @param view
     */
    public void doClick(View view) {
        Intent intent = new Intent();
        intent.setAction("com.xf.aidl");
        intent.setPackage("com.xf.test1");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * 获取服务器发送过来的消息
     * @param v
     */
    public void doAction(View v) {
        if (mIMyAidlInterfaceService != null) {
            try {
                String newstr = mIMyAidlInterfaceService.getStr("Hello world");
                int pid = mIMyAidlInterfaceService.getPid();
                Log.e("info---->", newstr + " pid:" + pid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端自己设置实体，发送给服务端
     * 通过随机数设置实体参数
     * @param v
     */
    public void doActionSetEntity(View v){
        if (mIMyAidlInterfaceService != null) {
            try {
               Random random=new Random();
               mIMyAidlInterfaceService.setEntity(new Entity("客户端",random.nextInt(100),random.nextDouble()));

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
