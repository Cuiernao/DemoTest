// IBookManger.aidl
package com.cuiernao.aidldemo;

// Declare any non-default types here with import statements
import com.cuiernao.aidldemo.Book;
interface IBookManger {
   List<Book> getBootList();
    void addBooK(in Book book);
}
