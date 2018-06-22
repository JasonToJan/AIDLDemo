package com.xingfu.aidlservicedemo;

import com.xingfu.aidlservicedemo.Person;
import com.xingfu.aidlservicedemo.IOnNewPersonArrivedListener;

interface IMyAidlInterface {
    List<Person> getPersonList();
    void addPerson(in Person person);
    void registerListener(IOnNewPersonArrivedListener listener);
    void unregisterListener(IOnNewPersonArrivedListener listener);

}
