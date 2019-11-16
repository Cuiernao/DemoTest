package com.cuiernao.aidldemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
   private MyService.MyBinder myBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClassName("com.cuiernao.aidldemo", "com.cuiernao.aidldemo.MyService");
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }
    /* 定义一个ServicesCounnection对象*/
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 当Activity与Service连接成功时回调该方法
         *
         * @param componentName
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //获取Service的OnBind方法所返回的MyBinder对象
            myBinder = (MyService.MyBinder) iBinder;

        }

        /**
         * 断开时调用
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void onlick(View view) {
        try {
            List<Book> books= myBinder.getBootList();
            String bookMsg="";
            for (Book book : books) {
                bookMsg+=book.getBookId() +" "+book.getBookName()+"\n";
            }
            Toast.makeText(MainActivity.this, bookMsg, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
