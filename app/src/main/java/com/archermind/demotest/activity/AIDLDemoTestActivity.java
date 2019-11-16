package com.archermind.demotest.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.archermind.demotest.R;
import com.cuiernao.aidldemo.Book;
import com.cuiernao.aidldemo.IBookManger;

import java.util.Random;

public class AIDLDemoTestActivity extends AppCompatActivity {
    private String TAG=getClass().getName();
    private IBookManger iMyAidlInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidldemo_test);
        Intent intent = new Intent();
        intent.setClassName("com.cuiernao.aidldemo", "com.cuiernao.aidldemo.MyService");
        bindService(intent, new ServiceConnection()
        {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Log.d(TAG, "onServiceConnected: ");
                iMyAidlInterface = IBookManger.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        }, BIND_AUTO_CREATE);
    }

    public void onClick(View view)
    {
        try
        {  Random random=new Random();
            int bookId=random.nextInt(9999);
            iMyAidlInterface.addBooK(new Book(bookId,"测试"+bookId));
            Toast.makeText(AIDLDemoTestActivity.this, iMyAidlInterface.getBootList()+"", Toast.LENGTH_SHORT).show();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
