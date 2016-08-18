package com.example.user.pracetice_play_video;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.pracetice_play_video.DB.DBhelper;

import java.io.File;

/**
 * Created by User on 2016/8/17.
 */
public class recording extends Activity {
    private Context context;
    private EditText video_name;
    private Button start_recording;

    private String videoname;
    private String filepath;

    //database
    private DBhelper dbhelper;
    private SQLiteDatabase db;
    private Cursor maincursor;
    public static final String VIDEONAME = "videoname";
    private long id;//拍攝影片的id
    //database//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);
        context=this;

        init();
    }

    private void init(){
        video_name=(EditText)findViewById(R.id.editText);
        start_recording=(Button)findViewById(R.id.button3);
        dbhelper = new DBhelper(context);
        db=dbhelper.getWritableDatabase();

        start_recording.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v){
                videoname=video_name.getText().toString();

                maincursor=db.query(
                        "videos",
                        new String[]{"_id", "videoname", "path"},
                        "videoname="+"'"+videoname+"'",
                        null,
                        null,
                        null,
                        null
                );
                if(videoname.length()==0){
                    Toast.makeText(context, "請輸入影片名稱", Toast.LENGTH_SHORT).show();
                }
                else if(maincursor.getCount()>0){//影片名稱重複
                    Toast.makeText(context, "影片名稱重複！請重新輸入！", Toast.LENGTH_SHORT).show();
                }
                else{
                    //影片紀錄
                    String sdPath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();//I don't know why , but this line is must to br ad
                    filepath=sdPath+"/Pictures/Practice_play_video/"+videoname+".mp4";//total file path

                    //開啟影片意圖
                    //請求run time 時的 user permission
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                    else{//假若已經給予權限
                        File file;
                        file = new File(filepath);
                        String action;
                        action = MediaStore.ACTION_VIDEO_CAPTURE;
                        Intent it = new Intent(action);
                        // 輸出參數：相機拍照後存入指定路徑
                        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//輸出    putExtra("變數名稱","值")
                        // 回調型 intent
                        startActivityForResult(it, 100);
                    }
                    //開啟影片意圖//
                    ContentValues cv=new ContentValues();
                    cv.put("videoname",videoname);
                    cv.put("path",filepath);
                    id = db.insert("videos",null,cv);//將資料儲存到videos資料表
                }
            }
        });
        //設立按鍵監聽，按下按鈕後，把影片名稱與路徑存入資料庫，並呼叫錄製影片的intent
    }

    //請求run time 時的 user permission，假若使用者點選同意，則開啟錄影
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//在android6之後才需要'
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File file;
                file = new File(filepath);
                String action;
                action = MediaStore.ACTION_VIDEO_CAPTURE;
                Intent it = new Intent(action);
                // 輸出參數：相機拍照後存入指定路徑
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//輸出    putExtra("變數名稱","值")
                // 回調型 intent
                startActivityForResult(it, 100);
            }
            else {
                Toast.makeText(context, "需要允許開啟相機，才能拍攝影片！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //影片拍完時，會執行的程式碼(回調型意圖的特色)
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==100){
            Toast.makeText(context, "_id：" + id, Toast.LENGTH_SHORT).show();
        }
    }

}