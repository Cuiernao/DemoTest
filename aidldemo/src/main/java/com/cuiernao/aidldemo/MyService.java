package com.cuiernao.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service
{

    public MyService()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return new MyBinder();
    }

    class MyBinder extends IBookManger.Stub
    {
     private List<Book> bookList=new ArrayList<>();
        @Override
        public List<Book> getBootList() throws RemoteException {
            try {
               Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bookList;
        }

        @Override
        public void addBooK(Book book) throws RemoteException {
            bookList.add(book);
            Log.d("cui", "addBooK: "+book.getBookName());
        }
    }
}