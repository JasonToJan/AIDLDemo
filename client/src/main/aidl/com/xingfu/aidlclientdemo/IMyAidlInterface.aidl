package com.xingfu.aidlclientdemo;

import com.xingfu.aidlclientdemo.Person;
import com.xingfu.aidlclientdemo.IOnNewPersonArrivedListener;

interface IMyAidlInterface {
    List<Person> getPersonList();
    void addPerson(in Person person);
    void registerListener(IOnNewPersonArrivedListener listener);
    void unregisterListener(IOnNewPersonArrivedListener listener);

}
