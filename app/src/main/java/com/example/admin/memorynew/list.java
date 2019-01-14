package com.example.admin.memorynew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

public class list extends AppCompatActivity {
    String[] list1;
    ListView listView;

    EditText editText;

    ListView listView1;
    static SingerAdapter adapter1;

    static ArrayList<SingerItem> items = new ArrayList<SingerItem>();

    int version = 1;
    String sql;
    Cursor cursor;
    member.MyDatabaseOpenHelper helper;
    SQLiteDatabase database;
    String[] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Button button = (Button) findViewById(R.id.button);

        // spinner.setOnItemClickListener(this);
        /*
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        list1 = new String[3];
        list1[0] = "전체화면입니다";
        list1[1] = "갚아야할돈";
        list1[2] = "빌려준돈";

        //ArrayAdapter spinnerAdapter = null;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "선택된 아이템" + spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        listView1 = (ListView) findViewById(R.id.listView);

        adapter1 = new SingerAdapter(items, getApplicationContext());
        insertlist();
        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), details.class);
                SingerItem item = (SingerItem) adapter1.getItem(position);
                //
                intent.putExtra("name",item.getName());
                startActivityForResult(intent, 1);
                //Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//추가
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), add.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //String Name = data.getStringExtra("name");
            //Toast.makeText(this, Name, Toast.LENGTH_SHORT).show();
            //String sql = "SELECT * FROM " + "fullList" + " WHERE name= '" + "asd" + "'";
            //cursor = database.rawQuery(sql, null); //열로 검사해서 1이 나오면 존재
            // if (cursor.getCount() < 0) {
            insertlist();
            Log.d("리스트", "추가 확인:list.class");
            listView1.setAdapter(adapter1);
            // }
        }
    }

    public void insertlist() {
        try {
            helper = new member.MyDatabaseOpenHelper(list.this, member.MyDatabaseOpenHelper.tableName, null, version);
            database = helper.getWritableDatabase();

            sql = "SELECT *FROM " + helper.listFull;
            cursor = database.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            items.clear();//다시 불러오기
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String str_name = cursor.getString(0);   // 첫번째 속성

                String str_money = cursor.getString(1);   // 두번째 속성

                adapter1.addItem(new SingerItem(str_name, str_money, R.mipmap.ic_launcher));
            }
        } catch (SQLException e) {
        }
    }

    private long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            ////전체 종료/////
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            //////////////
        }
    }

    class SingerAdapter extends BaseAdapter {

        ArrayList<SingerItem> items;
        Context context;

        public SingerAdapter(ArrayList<SingerItem> items, Context context) {
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

        public void addItem(SingerItem item) {
            items.add(item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingerItemView1 view = new SingerItemView1(context);
            SingerItem item = items.get(position);
            view.setName(item.getName());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());
            return view;
        }
    }
}


