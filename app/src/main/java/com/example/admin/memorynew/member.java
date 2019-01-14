package com.example.admin.memorynew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.example.admin.memorynew.details.adapter;
import static com.example.admin.memorynew.details.listView;


public class member extends AppCompatActivity {
    ListView list;

    EditText et_id, et_pw, et_pw_chk,et_name;
    String sId, sPw, sPw_chk,sIn;
    Button cancel;

    int version = 1;
    static MyDatabaseOpenHelper helper;
    static SQLiteDatabase database=null;

    static String sql;
    static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        et_name = (EditText) findViewById(R.id.name);//이름
        et_id = (EditText) findViewById(R.id.id);//이름
        et_pw = (EditText) findViewById(R.id.pw);//비밀번호
        et_pw_chk = (EditText) findViewById(R.id.pw_h);//비밀번호


        sIn = et_name.getText().toString();
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////////////빈칸체크/////////////
               if(et_name.getText().toString().length() == 0){
                    Toast.makeText(member.this, "이름을 적어주세요", Toast.LENGTH_SHORT).show();
                }else if(et_id.getText().toString().length() == 0){
                    Toast.makeText(member.this, "아이디을 적어주세요", Toast.LENGTH_SHORT).show();
                }else if(et_pw.getText().toString().length() == 0){
                    Toast.makeText(member.this, "비밀번호을 적어주세요", Toast.LENGTH_SHORT).show();
                }else if(et_pw_chk.getText().toString().length() == 0){
                    Toast.makeText(member.this, "비밀번호 확인을 적어주세요", Toast.LENGTH_SHORT).show();
                }
               /////////////빈칸체크/////////////

                else if(et_pw.getText().toString().equals(et_pw_chk.getText().toString())) {
                    helper = new MyDatabaseOpenHelper(member.this, MyDatabaseOpenHelper.membertableName, null, version);
                    database = helper.getWritableDatabase();
                    ///////존제하는 아이디 검색////////
                   sql = "SELECT id FROM "+ helper.membertableName + " WHERE id = '" + et_id.getText().toString() + "'";
                   cursor = database.rawQuery(sql, null); //열로 검사해서 1이 나오면 존재
                   if(cursor.getCount()==1){
                       Toast.makeText(getApplicationContext(),"이미 존재하는 아이디 입니다",Toast.LENGTH_SHORT).show();
                       ////////////////////////////
                   }else {
                       helper.insertName(database, et_name.getText().toString(), et_id.getText().toString(), et_pw.getText().toString());
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       //intent.putExtra("result","확인");
                      // intent.putExtra("result",1);
                       setResult(Activity.RESULT_OK, intent);
                       finish();
                   }
               }
               else
               {
                   Toast.makeText(getApplicationContext(),"비밀번호와 비밀번호 확인이 서로 다릅니다",Toast.LENGTH_SHORT).show();
               }
            }
        });
        cancel=(Button)findViewById(R.id.button1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                //intent.putExtra("result","취소");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }



    public static class MyDatabaseOpenHelper extends SQLiteOpenHelper
    {
        public static final String membertableName= "member";
        public static final String tableName= "list";
        public static final String listFull="fullList";

        public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            createTablelist(db);
            createTable(db);
            createFullList(db);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        public void createTable(SQLiteDatabase db)
        {
            String sql = "CREATE TABLE " + membertableName+ "(name text,id text,pass text)";
            try
            {
                db.execSQL(sql);
            }
            catch (SQLException e)
            {
            }
        }
        public  void createTablelist(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + tableName + "(ID INTEGER PRIMARY KEY   AUTOINCREMENT,name text,story text,money text,day text)";
            try{
                db.execSQL(sql);
            }catch (SQLException e){ }
        }
        public void createFullList(SQLiteDatabase db){
            String sql="CREATE TABLE "+ listFull + "(name text,money int)";
            try{
                db.execSQL(sql);
            }catch (SQLException e){}
        }
        public void insertName(SQLiteDatabase db, String name,String id,String pass)
        {
            db.beginTransaction();
            try
            {
                String sql = "insert into " + membertableName + " " + "(name,id,pass) values( '" + name + "', '" + id + "','" + pass + "')";
                db.execSQL(sql);//연결
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                db.endTransaction();
            }
        }
        ///////////////////////////
        /*
        public void fullList(SQLiteDatabase db, String Name, int Money,Bitmap d) {
            db.beginTransaction();
            //SQLiteDatabase db=null;
            String sql = "SELECT * FROM "+ listFull + " WHERE name= '" + Name + "'";
            cursor = db.rawQuery(sql, null); //열로 검사해서 1이 나오면 존재
            if(cursor.getCount()>=1){
                try {
                    cursor.moveToNext();
                    int nameMoney = cursor.getInt(1);
                    Money += nameMoney;
                    //db.execSQL("update mytable set name='Park' where id=5;");
                    //sql ="update fullList set money= "+Money+" where name="+Name;
                    if(Money==0){
                        db.execSQL("DELETE FROM " + listFull + " WHERE name= '" + Name + "'");
                    }else {
                        db.execSQL("update fullList set money= " + Money + " WHERE name= '" + Name + "'");
                    }
                    Log.d("리스트","업데이트");

                    db.setTransactionSuccessful();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }else {
                try {
                    Log.d("리스트", "listfull 넣어짐");
                    //sql = "insert into " + listFull + " " + "(name,money) values( '" + Name + "', '" + Money+ "')";
                    //sql="insert into fullList(name,money,img) values(?,?,?)";

                    SQLiteStatement p=db.compileStatement("insert into fullList(name,money,img) values(?,?,?)");

                    //byte[] applcon = getByteArrayFromDrawable(d);
                    p.bindLong(2, Money);
                    p.bindString(1, Name);
                    //p.bindBlob(3,applcon);

                    p.execute();

                    //db.execSQL(sql);//연결
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("리스트",e.getMessage());
                } finally {
                    //db.endTransaction();
                }
            }
        }*/
        ///////////////////////////
        public void fullList(SQLiteDatabase db, String Name, int Money) {
            db.beginTransaction();
            String sql = "SELECT * FROM "+ listFull + " WHERE name= '" + Name + "'";
            cursor = db.rawQuery(sql, null); //열로 검사해서 1이 나오면 존재
            if(cursor.getCount()>=1){
                try {
                    cursor.moveToNext();
                    int nameMoney = cursor.getInt(1);
                    Money += nameMoney;
                    //db.execSQL("update mytable set name='Park' where id=5;");
                    //sql ="update fullList set money= "+Money+" where name="+Name;
                    if(Money==0){
                        db.execSQL("DELETE FROM " + listFull + " WHERE name= '" + Name + "'");
                    }else {
                        db.execSQL("update fullList set money= " + Money + " WHERE name= '" + Name + "'");
                    }
                    Log.d("리스트","업데이트");
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }else {
                try {
                    sql = "insert into " + listFull + " " + "(name,money) values( '" + Name + "', '" + Money+ "')";
                    db.execSQL(sql);//연결
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.d("리스트", "listfull 넣어짐X");
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        }

        ///////////////////////////

        public byte[] getByteArrayFromDrawable(Bitmap d){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();

            d.compress(Bitmap.CompressFormat.PNG,100,stream);
            Log.d("리스트","안됨?");
            byte[] data=stream.toByteArray();

            return  data;
        }

        public void insert(SQLiteDatabase db, String name, String story, int money,String day) {
            db.beginTransaction();
            try {
                String sql = "insert into " + tableName + " " + "(name,story,money,day) values( '" + name + "', '" + story + "', '" + money + "', '" + day + "')";
                db.execSQL(sql);//연결
                db.setTransactionSuccessful();
                Log.d("list","들어감");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        public void ListUpdate(SQLiteDatabase db,int Money,String day,String story,int number){
            db.beginTransaction();
            try {
                db.execSQL("update list set " + "money= '" + Money +"'"+"," + "day= '" + day +"'"+","+ "story= '" + story+"'"+" WHERE ID= '" + number + "'");
                db.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
            }
        }
        public void delete(SQLiteDatabase db,int number){
            db.beginTransaction();
            try{
                String sql = "DELETE FROM " + tableName + " WHERE ID= '" +number+"'";
                Log.d("리스트",sql);
                db.execSQL(sql);
                db.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
            }

        }
    }
}
