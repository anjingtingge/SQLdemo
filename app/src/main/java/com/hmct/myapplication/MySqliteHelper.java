package com.hmct.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;

/**
 * Created by ShaoQuanwei on 2017/2/15.
 */

public class MySqliteHelper extends SQLiteOpenHelper {

    private String TAG = "MySqliteHelper";
    private boolean order_by;
    /*表名*/
    private final String TABLE_NAME_PERSON = "food";
    /*临时表面*/
    private final String TABLE_NAME_TEMP_PERSON = "temp_food";
    /*id字段*/
    private final String VALUE_ID = "_id";
    private final String VALUE_NAME = "name";
    private final String VALUE_STORE = "store_day";

    /*创建表语句 语句对大小写不敏感 create table 表名(字段名 类型，字段名 类型，…)*/
    private final String CREATE_PERSON = "create table " + TABLE_NAME_PERSON + "(" +
            VALUE_ID + " integer primary key," +
            VALUE_NAME + " text ," +
            VALUE_STORE + " integer" +
            ")";


    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        Log.e(TAG, "-------> MySqliteHelper");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建表
        db.execSQL(CREATE_PERSON);

        Log.e(TAG, "-------> onCreate");
    }

    //数据库升级时调用
    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade（）方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        System.out.println("更新数据库版本为:"+newVersion);
    }


    /**
     * 查询全部数据
     */
    public List<PersonModel> queryAllPersonData() {

        //查询全部数据
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_PERSON, null, null, null, null, null, null, null);
        List<PersonModel> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {



                int id = cursor.getInt(cursor.getColumnIndex(VALUE_ID));
                String name = cursor.getString(cursor.getColumnIndex(VALUE_NAME));
                int store_day = cursor.getInt(cursor.getColumnIndex(VALUE_STORE));


                PersonModel model = new PersonModel();
                model.setId(id);
                model.setStore_day(store_day);
                model.setName(name);


                list.add(model);
                //移动到下一位
                cursor.moveToNext();
            }
        }

        cursor.close();
        getWritableDatabase().close();

        return list;
    }




    public PersonModel addPersonDataReturnID(PersonModel model) {
        //把数据添加到ContentValues
        ContentValues values = new ContentValues();
        values.put(VALUE_NAME, model.getName());
        values.put(VALUE_STORE, model.getStore_day());

        //添加数据到数据库
        long index = getWritableDatabase().insert(TABLE_NAME_PERSON, null, values);

        //不等于-1表示添加成功(可以看insert源码)
//    public long insert(String table, String nullColumnHack, ContentValues values) {
//        try {
//            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
//        } catch (SQLException e) {
//            Log.e(TAG, "Error inserting " + values, e);
//            return -1;
//        }
//    }
        if (index != -1) {
            model.setId(index);
            return model;
        } else {
            return null;
        }
    }

    /**
     * 方法删除数据库数据
     */
    public void deletePersonData(PersonModel model) {
        //where后跟条件表达式 =,!=,>,<,>=,<=
        //多条件  and or

        //删除数据库里的model数据 因为_id具有唯一性。
        getWritableDatabase().delete(TABLE_NAME_PERSON, null, null);
        /*//删除数据库里 _id = 1 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+"=?",new String[]{"1"});
        //删除 age >= 18 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_AGE+">=?",new String[]{"18"});
        //删除 id > 5 && age <= 18 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">?"+" and "+VALUE_AGE +"<=?",new String[]{"5","18"});
        //删除 id > 5 || age <= 18 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">?"+" or "+VALUE_AGE +"<=?",new String[]{"5","18"});
        //删除数据库里 _id != 1 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+"!=?",new String[]{"1"});
        //删除所有 _id >= 7 的男生
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ISBOY+"=?"+" and "+VALUE_ID+">=?",new String[]{"1","7"});
        //删除所有 _id >= 7 和 _id = 3 的数据
        getWritableDatabase().delete(TABLE_NAME_PERSON,VALUE_ID+">=?"+" or "+VALUE_ID+"=?",new String[]{"7","3"});*/

    }

    /**
     * 方法修改数据库数据
     */
    public void updatePersonData(PersonModel model) {
        //条件表达式 =,!=,>,<,>=,<=
        //多条件 and or  and和or都可以无限连接
        //多条件示例 _id>=? and _id<=?
        //多条件示例 _id>=? or _id=? or _id=?

        //将数据添加至ContentValues
        ContentValues values = new ContentValues();
//        values.put(VALUE_NAME, model.getName());
        values.put(VALUE_STORE, model.getStore_day());


        //修改model的数据
        getWritableDatabase().update(TABLE_NAME_PERSON, values, VALUE_NAME + "=?", new String[]{"西红柿"});
        /*//将 _id>20 的数据全部修改成model  适合重置数据
        getWritableDatabase().update(TABLE_NAME_PERSON,values,VALUE_ID+">?",new String[]{"20"});
        //将 _id>=30 && _id<=40 的数据全部修改成model  适合重置数据
        getWritableDatabase().update(TABLE_NAME_PERSON,values,VALUE_ID+">=? and "+VALUE_ID+"<=?",new String[]{"30","40"});
        //将 _id>=40 || _id=30 || _id=20的 age 修改成18 (需先将model的数据修成成18) 这里and 和 or 的效果时一样的 因为_id是唯一的
        int count = getWritableDatabase().update(TABLE_NAME_PERSON,values,VALUE_ID+">=?"+" or "+VALUE_ID+"=?"+" or "+VALUE_ID+"=?",new String[]{"40","30","20"});*/

        // count 返回被修改的条数  >0 表示修改成功
//        Log.e(TAG, "" + VALUE_ID + ">=? and " + VALUE_ID + "<=?");
//        Log.e(TAG, "" + VALUE_ID + ">=?" + " or " + VALUE_ID + "=?" + " or " + VALUE_ID + "=?");

    }
}
