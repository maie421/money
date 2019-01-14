package com.example.admin.memorynew;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class chage extends AppCompatActivity {
    EditText editText,editText1;
    TextView textView;
    Button button;
    Cursor cursor;
    int version=1,number1;
    String name,story,money,day;

    public static Activity AActivity;

    member.MyDatabaseOpenHelper helper;
    SQLiteDatabase database;
    String sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chage);

        editText=(EditText)findViewById(R.id.editText2);//금액
        editText1=(EditText)findViewById(R.id.editText4);//내용
        textView=(TextView)findViewById(R.id.TextView);//날짜
        button=(Button)findViewById(R.id.button2);//확인

        Intent intent=getIntent();
        number1=Integer.parseInt(intent.getStringExtra("number3"));
        Log.d("리스트","chage 넘어오는 값:"+number1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatePicker();
            }

        });
        try {
            helper=new member.MyDatabaseOpenHelper(chage.this,member.MyDatabaseOpenHelper.tableName,null,version);
            database=helper.getWritableDatabase();
            sql=" SELECT * FROM list WHERE ID='"+number1+"'";
            cursor=database.rawQuery(sql,null); //이름//내용//금액//날짜
            cursor.moveToNext();
            name=cursor.getString(1);
            story=cursor.getString(2);
            money=cursor.getString(3);
            day=cursor.getString(4);
        }catch (Exception e){ }
        finally {
            editText.setText(money);
            editText1.setText(story);
            textView.setText(day);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Money=Integer.parseInt(editText.getText().toString());
                String coin=money;
                helper.ListUpdate(database,Money,textView.getText().toString(),editText1.getText().toString(),number1);
                Money-=Integer.parseInt(coin);
                helper.fullList(database,name,Money);
                Intent intent = new Intent(getApplicationContext(),details.class);
                intent.putExtra("name",name);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //FLAG_ACTIVITY_CLEAR_TOP는 새로 생성하려는 액티비티와 동일한 액티비티가
                // 스택에 있을경우 동일한 액티비티 위의 모든 액티비티를
                // 종료시키고 기존 액티비티를 새로 생성된 액티비티로 교체한다는 플래그입니다.
                startActivity(intent);
                finish();
            }
        });
    }
    private void DialogDatePicker(){
        Calendar c=Calendar.getInstance();

        int cyear=c.get(Calendar.YEAR);
        int cmonth=c.get(Calendar.MONTH);
        int cday=c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                day = String.valueOf(year)+"년"+String.valueOf(month + 1)+"월"+String.valueOf(dayOfMonth)+"일";
                textView.setText(day);
            }
        };
        DatePickerDialog alert=new DatePickerDialog(this,mDateSetListener,cyear,cmonth,cday);
        alert.show();
    }
    /*@Override
    protected void onDestroy(){      //액티비티가 종료될 때의 메서드
        super.onDestroy();
        Log.d("리스트","액티비티 종료");
        setResult(0); // 여기에 넣는 int형 정수는 MainActivity의 onActivityResult안에서 requestCode로 들어간다.
    }*/
}
