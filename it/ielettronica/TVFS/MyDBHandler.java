/*
 * Copyright (c) 2016. iElettronica.it
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package it.ielettronica.TVFS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class MyDBHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "SitesDB.db";
    private static final String TABLE_SITE = "site";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_STATIC_NAME = "_static_name";
    private static final String COLUMN_LINK = "_link";
    private static final String COLUMN_ABOUT = "_about";
    private static final String COLUMN_IMAGE = "_image";
    private static final String COLUMN_TYPE = "_typestream";
    private static final String COLUMN_ORIGIN = "_fromNew";
    private static final String COLUMN_POSITION = "_position";
    private static final String COLUMN_USE = "_use";
    private static final String COLUMN_FAVORITE = "_favorite";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SITE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_STATIC_NAME + " TEXT, " +
                COLUMN_LINK + " TEXT, " +
                COLUMN_ABOUT + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_POSITION + " INTEGER, " +
                COLUMN_USE + " INTEGER, " +
                COLUMN_TYPE + " INTEGER, " +
                COLUMN_ORIGIN + " INTEGER, " +
                COLUMN_FAVORITE + " BOOLEAN " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        onCreate(db);
    }


    private boolean isExistInDB(String NameSite) {
        Cursor c = null;
        SQLiteDatabase db = getReadableDatabase();
        try {

            String query = "SELECT COUNT(*) as CT FROM " + TABLE_SITE + " WHERE "+ COLUMN_STATIC_NAME +" = \"" + NameSite + "\"";
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                if (c.getInt(0)==0) { return Boolean.FALSE; } else { return Boolean.TRUE;}
            }
            return Boolean.FALSE;
        }
        finally {
            if (c != null) {
                c.close();
            } else {
                return Boolean.FALSE;
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private int gerNumberLines() {
        SQLiteDatabase db = getWritableDatabase();
        int ctval;
        String query = "SELECT COUNT(*) as CT FROM " + TABLE_SITE + " WHERE 1";

        // curson poijnt to the location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        ctval=c.getInt(c.getColumnIndex("CT"));
        c.close();
        db.close();

        return ctval;
    }

    //edit a new row to the database
    public boolean editSite(StackSite stackSite) {
        return Boolean.TRUE;
    }

    //add a new row to the database
    public boolean addSite(StackSite stackSite) {

        String NameStaticToAdd;
        NameStaticToAdd=stackSite.getStaticName();
        SQLiteDatabase db = getWritableDatabase();



        if (!isExistInDB(NameStaticToAdd)) {

            int ValPos = gerNumberLines();

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, stackSite.getName());
            values.put(COLUMN_STATIC_NAME, NameStaticToAdd);
            values.put(COLUMN_LINK, stackSite.getLink());
            values.put(COLUMN_ABOUT, stackSite.getAbout());
            values.put(COLUMN_IMAGE, stackSite.getImgUrl());
            values.put(COLUMN_TYPE, stackSite.getTypeStream());
            values.put(COLUMN_ORIGIN, stackSite.getOrigin());
            values.put(COLUMN_POSITION, ValPos);
            values.put(COLUMN_USE, 1);
            values.put(COLUMN_FAVORITE, Boolean.TRUE);
            db = getWritableDatabase();
            db.insert(TABLE_SITE, null, values);
            db.close();
            return true;
        } else {
            return false;
        }

    }

    public boolean addSiteBefore(StackSite stackSite, int position) {

        String NameStaticToAdd;
        NameStaticToAdd=stackSite.getStaticName();

        if (!isExistInDB(NameStaticToAdd)) {

            if (position == -1) {
                return addSite(stackSite);
            } else {
                int OldPosition;
                StackSite curStackSite;
                SQLiteDatabase db = getWritableDatabase();
                String query = "SELECT * FROM " + TABLE_SITE + " ORDER BY " + COLUMN_POSITION + " ASC";

                // curson poijnt to the location in your results
                Cursor c = db.rawQuery(query, null);
                c.moveToFirst();

                while (!c.isAfterLast()) {
                    if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                        curStackSite = new StackSite();
                        curStackSite.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                        curStackSite.setStaticName(c.getString(c.getColumnIndex(COLUMN_STATIC_NAME)));
                        curStackSite.setLink(c.getString(c.getColumnIndex(COLUMN_LINK)));
                        curStackSite.setTypeStream(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
                        curStackSite.setAbout(c.getString(c.getColumnIndex(COLUMN_ABOUT)));
                        curStackSite.setImgUrl(c.getString(c.getColumnIndex(COLUMN_IMAGE)));
                        curStackSite.setOrigin(c.getInt(c.getColumnIndex(COLUMN_ORIGIN)));
                        OldPosition = c.getPosition();
                        if (OldPosition < position) {
                            curStackSite.setPosition(OldPosition);
                        } else {
                            curStackSite.setPosition(OldPosition + 1);
                        }
                        updateSite(curStackSite);
                    }
                    c.moveToNext();
                }

                c.close();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, stackSite.getName());
                values.put(COLUMN_STATIC_NAME, NameStaticToAdd);
                values.put(COLUMN_LINK, stackSite.getLink());
                values.put(COLUMN_ABOUT, stackSite.getAbout());
                values.put(COLUMN_IMAGE, stackSite.getImgUrl());
                values.put(COLUMN_ORIGIN, stackSite.getOrigin());
                values.put(COLUMN_TYPE, stackSite.getTypeStream());
                values.put(COLUMN_POSITION, position);
                values.put(COLUMN_USE, 1);
                values.put(COLUMN_FAVORITE, Boolean.TRUE);
                db = getWritableDatabase();
                db.insert(TABLE_SITE, null, values);
                db.close();
                return true;
            }
        } else {
            return false;
        }

    }

    //update the row in the database
    public void updateSite(StackSite stackSite, String OldSiteName) {
        ContentValues values = new ContentValues();
        if (stackSite.getName()!=null) {values.put(COLUMN_NAME, stackSite.getName());}
        if (stackSite.getStaticName()!=null) {values.put(COLUMN_STATIC_NAME, stackSite.getStaticName());}
        if (stackSite.getLink()!=null) {values.put(COLUMN_LINK, stackSite.getLink());}
        if (stackSite.getTypeStream() >=0 ) {values.put(COLUMN_TYPE, stackSite.getTypeStream());}
        if (stackSite.getAbout()!=null) {values.put(COLUMN_ABOUT, stackSite.getAbout());}
        if (stackSite.getImgUrl()!=null) {values.put(COLUMN_IMAGE, stackSite.getImgUrl());}
        if (stackSite.getPosition()>0) {values.put(COLUMN_POSITION, stackSite.getPosition());}
        if (stackSite.getOrigin()>=0) {values.put(COLUMN_ORIGIN,stackSite.getOrigin());}
        //values.put(COLUMN_USE, 1);
        //values.put(COLUMN_FAVORITE, Boolean.TRUE);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_SITE, values, COLUMN_NAME + "=\"" + OldSiteName + "\"", null);
        db.close();
    }

    //update the row in the database
    public void updateSite(StackSite stackSite) {

        String OldSiteName =stackSite.getStaticName();
        updateSite(stackSite,OldSiteName);
    }


    // Delete product from databqse
    public void deleteSite(String SiteName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SITE + " WHERE " + COLUMN_NAME + "=\"" + SiteName + "\";");


        int OldPosition;
        StackSite curStackSite;
        String query = "SELECT * FROM " + TABLE_SITE + " ORDER BY " + COLUMN_POSITION + " ASC";

        // curson poijnt to the location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int cc =0;
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                curStackSite = new StackSite();
                curStackSite.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                curStackSite.setStaticName(c.getString(c.getColumnIndex(COLUMN_STATIC_NAME)));
                curStackSite.setLink(c.getString(c.getColumnIndex(COLUMN_LINK)));
                curStackSite.setTypeStream(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
                curStackSite.setAbout(c.getString(c.getColumnIndex(COLUMN_ABOUT)));
                curStackSite.setImgUrl(c.getString(c.getColumnIndex(COLUMN_IMAGE)));
                curStackSite.setOrigin(c.getInt(c.getColumnIndex(COLUMN_ORIGIN)));
                curStackSite.setPosition(c.getPosition());
                cc = cc+1;
                updateSite(curStackSite);
            }
            c.moveToNext();
        }

        c.close();
        db.close();


    }




    public List<StackSite> getStackSites() {

        List<StackSite> stackSites;

        // List of StackSites that we will return

        stackSites = new ArrayList<StackSite>();



        StackSite curStackSite;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SITE + " ORDER BY " + COLUMN_POSITION +" ASC";

        // curson poijnt to the location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                curStackSite = new StackSite();
                curStackSite.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                curStackSite.setStaticName(c.getString(c.getColumnIndex(COLUMN_STATIC_NAME)));
                curStackSite.setLink(c.getString(c.getColumnIndex(COLUMN_LINK)));
                curStackSite.setAbout(c.getString(c.getColumnIndex(COLUMN_ABOUT)));
                curStackSite.setTypeStream(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
                curStackSite.setImgUrl(c.getString(c.getColumnIndex(COLUMN_IMAGE)));
                curStackSite.setOrigin(c.getInt(c.getColumnIndex(COLUMN_ORIGIN)));
                stackSites.add(curStackSite);
            }
            c.moveToNext();
        }

        c.close();
        db.close();
        //return dbString;


        // return the populated list.
        return stackSites;
    }


    public List<String> getNamesFromStackSites() {

        List<String> NameFromStackSites;

        // List of StackSites that we will return

        NameFromStackSites = new ArrayList<String>();




        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SITE + " ORDER BY " + COLUMN_POSITION +" ASC";

        // curson poijnt to the location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String NameSite;
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                NameSite=c.getString(c.getColumnIndex(COLUMN_NAME));
                NameFromStackSites.add(NameSite);
            }
            c.moveToNext();
        }

        c.close();
        db.close();
        //return dbString;


        // return the populated list.
        return NameFromStackSites;
    }


}
