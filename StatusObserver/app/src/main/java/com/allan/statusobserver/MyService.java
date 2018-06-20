package com.allan.statusobserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
    public MyService() {
    }

    IMyRemote.Stub mStub = new IMyRemote.Stub() {

        @Override
        public void func(int s) throws RemoteException {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;

    }
}
