
package com.xingfu.aidlservicedemo;

import com.xingfu.aidlservicedemo.Person;

interface IOnNewPersonArrivedListener {
    void onNewPersonArrived(in Person person);
}
