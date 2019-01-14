package com.example.admin.memorynew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class detailsItemView extends LinearLayout {
    TextView day, story, money;
    Button button,button1;
    Context mContext;

    member.MyDatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    Cursor cursor;

    int version = 1;

    public detailsItemView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }
    public void init(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.details_view, this, true);

        day = (TextView) findViewById(R.id.textView);//날짜
        story = (TextView) findViewById(R.id.textView2);//내용
        money = (TextView) findViewById(R.id.textView1);//금액
        button = (Button) findViewById(R.id.button1);//취소
        button1=(Button)findViewById(R.id.button);//수정

    }

    public void setDay(String day) {
        this.day.setText(day);
    }

    public void setStory(String story) {
        this.story.setText(story);
    }

    public void setMoney(String money) {
        this.money.setText(money);
    }

    public void setNumber(final int number, final String name, final String money) {
        //Button button1 = (Button) findViewById(R.id.button1);
        //helper=new member.MyDatabaseOpenHelper(this,member.MyDatabaseOpenHelper.tableName,null,version);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                helper = new member.MyDatabaseOpenHelper(getContext(), member.MyDatabaseOpenHelper.tableName, null, version);
                database = helper.getWritableDatabase();
                helper.delete(database, number);
                //Intent intent = new Intent(getContext(),details.class);
                //mContext.startActivity(intent);
                //myListView.invalidateViews();
                int money1 = (Integer.parseInt(money)) * -1;
                helper.fullList(database, name, money1);
                listdetalil(name);
                details.listView.setAdapter(details.adapter);
                Log.d("리스트", "버튼됨");
                // Toast.makeText(getContext(), "안되는 이유", Toast.LENGTH_SHORT).show();
            }
        });
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),chage.class);
                int number1=number;
                String ID=String.valueOf(number1);
                intent.putExtra("number3",ID);
                mContext.startActivity(intent);
            }
        });
    }

    public void listdetalil(String name) {
        try {

            sql = "SELECT * FROM " + helper.tableName + " WHERE name= '" + name + "'";

            details.items.clear();

            cursor = database.rawQuery(sql, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                String day = cursor.getString(4);
                String story = cursor.getString(2);
                String money = cursor.getString(3);
                int number = cursor.getInt(0);
                //String name=cursor.getString(1);

                //int number = cursor.getInt(0);
                details.adapter.addItem(new detailsItem(day, story, money, number, name));
            }
        } catch (Exception e) {
        }
    }
}
