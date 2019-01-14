package com.example.admin.memorynew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class details extends AppCompatActivity  {
    int version = 1,raw;
    String name;
    static ListView listView;
    static SingerAdapter adapter;
    static ArrayList<detailsItem> items = new ArrayList<detailsItem>();
    member.MyDatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new SingerAdapter(items, getApplicationContext());
        //adapter.addItem(new detailsItem("2018.01.01","돈내놔","51000"));

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        insert(name);
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

    }

    public void insert(String name) {
        try {
            helper = new member.MyDatabaseOpenHelper(details.this, member.MyDatabaseOpenHelper.tableName, null, version);
            database = helper.getWritableDatabase();

            sql = "SELECT * FROM " + helper.tableName + " WHERE name= '" + name + "'";

            cursor = database.rawQuery(sql, null);
            raw=cursor.getCount();
            items.clear();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                String day = cursor.getString(4);
                String story = cursor.getString(2);
                String money = cursor.getString(3);
                int number = cursor.getInt(0);

                adapter.addItem(new detailsItem(day, story, money, number,name));

            }
        } catch (Exception e) {
        }
    }
}

class SingerAdapter extends BaseAdapter {
    ArrayList<detailsItem> items;
    Context context;

    int version=1;
    public SingerAdapter(ArrayList<detailsItem> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(detailsItem item) {
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        detailsItemView view = new detailsItemView(context);
        detailsItem item = items.get(position);
        view.setDay(item.getDay());
        view.setMoney(item.getMoney());
        view.setStory(item.getStory());
        view.setNumber(item.getNumber(),item.getName(),item.getMoney());
       // Log.d("리스트","다시여기로");
        return view;
    }


}
