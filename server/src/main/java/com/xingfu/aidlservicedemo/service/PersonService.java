package com.xingfu.aidlservicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xingfu.aidlservicedemo.IMyAidlInterface;
import com.xingfu.aidlservicedemo.IOnNewPersonArrivedListener;
import com.xingfu.aidlservicedemo.Person;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: 另外一个服务-处理Person的服务
 *
 * @version V1.0.0
 * Createdate: 2018/6/22
 * @author: wja 1211241203@qq.com
 */
public class PersonService extends Service{

    /**
     * RemoteCallbackList是专门用于删除跨进程listener的接口，泛型，支持任意的AIDL接口
     */
    private RemoteCallbackList<IOnNewPersonArrivedListener> mListener=new RemoteCallbackList<>();

    /**
     * 线程安全的写时复制
     */
    private CopyOnWriteArrayList<Person> persons=new CopyOnWriteArrayList<>();

    /**
     * 高并发的情况下，只有一个线程能够访问这个属性值
     */
    private AtomicBoolean isServiceDestory=new AtomicBoolean(false);

    /**
     * 服务开始时
     */
    @Override
    public void onCreate() {
        super.onCreate();
        persons.add(new Person("小A"));

        new Thread(new ServiceWork()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMyService();
    }

    @Override
    public void onDestroy() {
        isServiceDestory.set(true);
        super.onDestroy();
    }

    public class IMyService extends IMyAidlInterface.Stub{

        @Override
        public List<Person> getPersonList() {
            return persons;
        }

        @Override
        public void addPerson(Person person) {
            Log.i("添加人数","之前的Person人数为："+persons.size());
            persons.add(person);
        }

        @Override
        public void registerListener(IOnNewPersonArrivedListener listener) {
            mListener.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewPersonArrivedListener listener) {
            mListener.unregister(listener);
        }
    }

    private void onNewPerson(Person person) throws Exception{
        persons.add(person);
        int n=mListener.beginBroadcast();//获取接口数量（猜测）
        for(int i=0;i<n;i++){//遍历接口
            IOnNewPersonArrivedListener l=mListener.getBroadcastItem(i);//获取当前接口
            if (l!=null){
                try {
                    l.onNewPersonArrived(person);//服务端通过这个向客户端发送消息
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListener.finishBroadcast();

    }

    private class ServiceWork implements Runnable{
        @Override
        public void run() {
            while (!isServiceDestory.get()){
                try {
                    Thread.sleep(5000);
                }catch (Exception e){

                }
                int i=persons.size()+1;
                Person person=new Person("小"+(char)(i+64));
                try {
                    onNewPerson(person);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
