package com.xf.test1;

import android.content.Context;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class AIDLImp extends IMyAidlInterface.Stub {


    private final Context mContext;
    private RemoteCallbackList<IAidlCallBack> mCallbackList = new RemoteCallbackList<>();

    private Entity myEntity;

    public AIDLImp(Context context) {
        mContext = context;
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public Entity getEntity() {
        return myEntity;
    }

    @Override
    public Entity setEntity(Entity entity) {
        if(entity!=null){
            myEntity=entity;
        }
        return myEntity;
    }

    @Override
    public String getStr(String str) throws RemoteException {
        //mContext.startActivity(new Intent(mContext, MainActivity.class));
        return "xxx:" + str;
    }

    @Override
    public int getPid() throws RemoteException {
        //sendMsg();
        sendEntityMsg();
        return Process.myPid();
    }

    @Override
    public void registerCallBack(IAidlCallBack callback) throws RemoteException {
        mCallbackList.register(callback);
    }

    @Override
    public void unregisterCallBack(IAidlCallBack callback) throws RemoteException {
        mCallbackList.unregister(callback);
    }

    private void sendMsg() {
        int len = mCallbackList.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).callBack("xxxxxx");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        mCallbackList.finishBroadcast();
    }

    private void sendEntityMsg(){
        int len = mCallbackList.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                if(myEntity==null){
                    mCallbackList.getBroadcastItem(i).callBackEntity(new Entity("xxx",11,22.0));
                }else{
                    mCallbackList.getBroadcastItem(i).callBackEntity(myEntity);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        mCallbackList.finishBroadcast();
    }

    public void KillAll() {
        mCallbackList.kill();
    }


}
