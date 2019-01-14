package com.example.admin.memorynew;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class add  extends Activity implements OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1; //메모리할당 한번만 final 값변경 X
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_IMAGE_CROP = 3;

    private ImageView mPhotoImageView;
    private EditText editText, editText3, story, day;
    TextView TextView;

    Bitmap photo;
    Button btn, album_btn, camera_btn;

    //public static final String tableName="list";
    member.MyDatabaseOpenHelper helper;
    SQLiteDatabase database;
    String date_selected;

    String mCurrentPhotoPath;
    Uri photoURI, albumURI = null;
    Boolean album = false;
    Date currentDate;
    //int iYear,iMONTH,iDay;
    int version = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);

        album_btn = (Button) findViewById(R.id.button3);
        album_btn.setOnClickListener(this);

        camera_btn = (Button) findViewById(R.id.button);
        //dayButton=(Button)findViewById(R.id.dayButton);//날짜
        camera_btn.setOnClickListener(this);

        mPhotoImageView = (ImageView) findViewById(R.id.imageView2);

        editText3 = (EditText) findViewById(R.id.editText3);//이름
        editText = (EditText) findViewById(R.id.editText2);//금액
        story = (EditText) findViewById(R.id.editText4);//내용
        TextView = (TextView) findViewById(R.id.TextView);//날짜

        getDateToday(); //헌재 날짜 불러와서 값변경
        TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatePicker();
                Toast.makeText(add.this, "날짜 선택", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void getDateToday() {
        ///현재 날짜
        currentDate = new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("M");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        date_selected = sdfYear.format(currentDate) + "년" + sdfMon.format(currentDate) + "월" + sdfDay.format(currentDate) + "일";
        TextView.setText(date_selected);
    }

    private void DialogDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_selected = String.valueOf(year) + "년" + String.valueOf(monthOfYear + 1) + "월" + String.valueOf(dayOfMonth) + "일";
                        TextView.setText(date_selected);
                        //Toast.makeText(add.this, "Selected Date is ="+ date_selected, Toast.LENGTH_SHORT).show();
                    }
                };
        DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        alert.show();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button2) {//확인
            // helper=new member.MyDatabaseOpenHelper()
            helper = new member.MyDatabaseOpenHelper(add.this, member.MyDatabaseOpenHelper.tableName, null, version);
            database = helper.getWritableDatabase();
            helper.insert(database, editText3.getText().toString(), story.getText().toString(), Integer.parseInt(editText.getText().toString()), date_selected);

            //byte[] applcon = getByteArrayFromDrawable(photo);
            helper.fullList(database, editText3.getText().toString(), Integer.parseInt(editText.getText().toString()));
            Intent intent = new Intent(getApplicationContext(), list.class);
            //intent.putExtra("name",editText3.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            //startActivity(intent);
            finish();
        } else if (v.getId() == R.id.button3) {//사진 불러오기
            doTakeAlbumAction();
        } else if (v.getId() == R.id.button) {//사진 찍기
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "createImageFile Failed", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }



    private File createImageFile() throws IOException{
        String imageFileName="tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        File storageDir=new File(Environment.getExternalStorageDirectory(),imageFileName);
        mCurrentPhotoPath=storageDir.getAbsolutePath();//실행시킨 위치 정보도 함께 반환
        return storageDir;
    }
    private void doTakeAlbumAction(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }
    private void cropImage(){
        Log.d("리스트","crop");
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(photoURI,"image/*");
        cropIntent.putExtra("scale",true);
        cropIntent.putExtra( "aspectX",1);
        cropIntent.putExtra( "aspectY", 1);
        cropIntent.putExtra( "outputX", 200);
        cropIntent.putExtra( "outputY", 200);

        Log.d("들어왔다","자르기");
        if(album==false){
            cropIntent.putExtra("output",photoURI);
        }else if(album==true){
            cropIntent.putExtra("output",albumURI);
        }
        startActivityForResult(cropIntent,REQUEST_IMAGE_CROP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode !=RESULT_OK){
            Toast.makeText(this, "anAci", Toast.LENGTH_SHORT).show();
        }else{*/
            switch (requestCode){
                case REQUEST_TAKE_PHOTO:
                    album=true;
                    /////앨범선택 크롭/////
                    File albumFile=null;
                    try{
                        albumFile=createImageFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if(albumFile != null){
                        albumURI=Uri.fromFile(albumFile);
                    }
                    ///////////////
                    photoURI=data.getData();
                    Bitmap image_bitmap=null;
                    try{//수신사
                        image_bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),photoURI);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    mPhotoImageView.setImageBitmap(image_bitmap);
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Log.d("리스트","2");
                    cropImage();
                    break;
                case REQUEST_IMAGE_CROP:
                    photo=BitmapFactory.decodeFile(photoURI.getPath());
                    mPhotoImageView.setImageBitmap(photo);
                    Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    if(album==false){
                        mediaScanIntent.setData(photoURI);
                    }else if(album==true){
                        album=false;
                        mediaScanIntent.setData(albumURI);
                    }
                    this.sendBroadcast(mediaScanIntent); //송신
                    break;
            }
        }
}


