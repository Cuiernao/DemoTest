package com.cuiernao.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int BookId;
    private String BookName;

    public Book(int bookId, String bookName) {
        BookId = bookId;
        BookName = bookName;
    }

    public int getBookId() {
        return BookId;
    }

    public void setBookId(int bookId) {
        BookId = bookId;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.BookId);
        dest.writeString(this.BookName);
    }

    protected Book(Parcel in) {
        this.BookId = in.readInt();
        this.BookName = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
