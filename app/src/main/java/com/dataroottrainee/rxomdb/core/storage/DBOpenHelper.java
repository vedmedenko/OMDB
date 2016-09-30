package com.dataroottrainee.rxomdb.core.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.dataroottrainee.rxomdb.injection.ApplicationContext;
import com.dataroottrainee.rxomdb.util.ConstantsManager;

import javax.inject.Inject;

public class DBOpenHelper extends SQLiteOpenHelper {

    @Inject
    public DBOpenHelper(@NonNull @ApplicationContext Context context) {
        super(context, ConstantsManager.DATABASE_NAME, null, ConstantsManager.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(MovieModel.CREATE_TABLE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieModel.TABLE_NAME);
        onCreate(db);
    }
}
