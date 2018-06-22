package com.xingfu.aidlclientdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private Button bindServiceBtn;
    private Button getServiceBtn;
    private Button unbindServiceBtn;
    private Button sendMessageBtn;
    private TextView messageTv;

    /**
     * aidl交互接口
     */
    private IMyAidlInterface mAidlService=null;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Person person=(Person) msg.obj;
                    messageTv.setText(person.getName());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindServiceBtn=findViewById(R.id.bindServiceBtn);
        getServiceBtn=findViewById(R.id.getServiceBtn);
        sendMessageBtn=findViewById(R.id.sendServiceBtn);
        unbindServiceBtn=findViewById(R.id.relieveServiceBtn);
        messageTv=findViewById(R.id.messageTv);

        bindServiceBtn.setOnClickListener(this);
        getServiceBtn.setOnClickListener(this);
        sendMessageBtn.setOnClickListener(this);
        unbindServiceBtn.setOnClickListener(this);
        messageTv.setOnClickListener(this);

        getServiceBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bindServiceBtn:
               /* Intent intent=new Intent("com.xingfu.AIDLService");
                intent.setPackage("com.xingfu.aidlservicedemo");*/
                Intent intent=new Intent("com.xingfu.PersonService");
                intent.setPackage("com.xingfu.aidlservicedemo");
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                ToastUtil.showShort(this,"已经绑定服务！");
                break;
            case R.id.getServiceBtn:
                   /* if(mAidlService!=null){
                        ToastUtil.showShort(this,"获取服务的返回值为："+mAidlService.getServiceName()+"！");
                    }else{
                        ToastUtil.showShort(this,"似乎还没有绑定服务！^_^");
                    }*/
                break;
            case R.id.sendServiceBtn:
                Random random=new Random();
                int number=random.nextInt(26)+65;
                try{
                    mAidlService.addPerson(new Person("小"+(char)number));
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                break;
            case R.id.relieveServiceBtn:
                if (mConnection != null&&mAidlService.asBinder().isBinderAlive()) {
                    try {
                        mAidlService.unregisterListener(listener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    unbindService(mConnection);
                }
                break;

        }
    }

    /**
     * 客户端处理逻辑细节部分，实现业务逻辑接口中的方法，利用handler发送信息
     */
    private IOnNewPersonArrivedListener listener=new IOnNewPersonArrivedListener(){
        @Override
        public void onNewPersonArrived(Person person) throws RemoteException {
            handler.obtainMessage(1,person).sendToTarget();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    /**
     * 连接服务对象，处理服务已经连接和断开连接的逻辑
     */
    ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibinder) {
            Log.d(TAG,"onServiceConnected");
            mAidlService=IMyAidlInterface.Stub.asInterface(ibinder);

            try{
                //设置死亡代理
                ibinder.linkToDeath(mDeathRecipient,0);
            }catch (RemoteException e){
                e.printStackTrace();
            }

            try{
                //客户端处理逻辑
                mAidlService.registerListener(listener);

            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidlService=null;
        }
    };

    /**
     * 监听Binder是否死亡
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mAidlService == null) {
                return;
            }
            mAidlService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mAidlService = null;
            //重新绑定
            Intent intent=new Intent("com.xingfu.PersonService");
            intent.setPackage("com.xingfu.aidlservicedemo");
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null&&mAidlService.asBinder().isBinderAlive()) {
            try {
                mAidlService.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mConnection);
        }

    }
}
