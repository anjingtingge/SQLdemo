package com.hmct.myapplication;

import android.annotation.SuppressLint;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button button_add,button_serach,button_delete,button_edit;
    private MySqliteHelper mySqliteHelper;
    private Context context;
    private List<PersonModel> mList;
    private Random mRandom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mList = new ArrayList<>();
        mRandom = new Random();

        button_add= (Button) findViewById(R.id.add);
        button_serach= (Button) findViewById(R.id.serach);
        button_delete= (Button) findViewById(R.id.delete);
        button_edit= (Button) findViewById(R.id.edit);

        mySqliteHelper = new MySqliteHelper(context, "Test_DB", null, 1);

        button_add.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this,"加",Toast.LENGTH_SHORT ).show();
                addData();

            }

        });

        button_serach.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this,"加",Toast.LENGTH_SHORT ).show();
                if (mList == null || mList.size() == 0) {
                    Toast.makeText(context, "添加数据", Toast.LENGTH_SHORT).show();

                    return;
                }


                mList.clear();
                mList.addAll(mySqliteHelper.queryAllPersonData());

                for (int i=0;i<mList.size();i++)
                {
                    Log.e("mlist",mList.get(i).getName());
                    Log.e("mlist", String.valueOf(mList.get(i).getStore_day()));
                    Log.e("mlist", String.valueOf(mList.get(i).getId()));

                }

            }

        });

        button_delete.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                if (mList == null || mList.size() == 0) {
                    Toast.makeText(context, "添加数据", Toast.LENGTH_SHORT).show();

                    return;
                }
//                Toast.makeText(MainActivity.this,"加",Toast.LENGTH_SHORT ).show();
                mySqliteHelper.deletePersonData(mList.get(0));

            }

        });

        button_edit.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast") @Override
            public void onClick(View v) {
                if (mList == null || mList.size() == 0) {
                    Toast.makeText(context, "添加数据", Toast.LENGTH_SHORT).show();

                    return;
                }
                PersonModel model = new PersonModel();
//                model.setName("西红柿");
                model.setStore_day(10);
                long i=mList.get(0).getId();
                model.setId(i);
                mySqliteHelper.updatePersonData(model);

            }

        });
    }


    /**
     * 添加数据
     */
    private void addData() {

        PersonModel model = new PersonModel();
        model.setName("苹果");
        model.setStore_day(7);


        mySqliteHelper.addPersonDataReturnID(model);
        mList.add(0, model);

        Log.e("添加",mList.get(0).getName());

//        if (isSucc) {
//
//            ToastUtils.show(context, "添加数据成功");
//            mList.add(0, model);
//            mAdapter.notifyDataSetChanged();
//        } else {
//            ToastUtils.show(context, "添加失败");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mList.clear();

        mList.addAll(mySqliteHelper.queryAllPersonData());

    }


}
