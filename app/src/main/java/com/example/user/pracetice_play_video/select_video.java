package com.example.user.pracetice_play_video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.user.pracetice_play_video.DB.DBhelper;

/**
 * Created by User on 2016/8/17.
 */
public class select_video extends Activity {

    private Context context;
    private ListView listView=null;
    private DBhelper dbhelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private Cursor maincursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_video);
        context=this;

        init();
        refreshListView();
    }
    // 重新整理ListView（將資料重新匯入）
    private void refreshListView() {
        if (maincursor == null) {
            // 1.取得查詢所有資料的cursor
            maincursor = db.rawQuery(
                    "SELECT _id, videoname,path  FROM videos", null);
            // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
            adapter = new SimpleCursorAdapter(context, R.layout.row,
                    maincursor,
                    new String[] { "_id", "videoname", "path" },
                    new int[] { R.id.video_Id, R.id.video_name},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            // 3.注入適配器
            listView.setAdapter(adapter);
        } else {
            if (maincursor.isClosed()) { // 彌補requery()不會檢查cursor closed的問題
                maincursor = null;
                refreshListView();
            } else {
                maincursor.requery(); // 若資料龐大不建議使用此法（應改用 CursorLoader）
                adapter.changeCursor(maincursor);
                adapter.notifyDataSetChanged();
            }
        }
    }

    // OnItemClick 監聽器
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // 取得 Cursor
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            String path=cursor.getString(2);

            //喚醒播放影片的意圖
            Intent intent = null;
            Bundle extra=new Bundle();
            extra.putString("filepath",path);
            intent=new Intent(context,watching.class);
            intent.putExtras(extra);

            //注意有些沒有影片，要用try     catch
            startActivity(intent);
            //喚醒播放影片的意圖//
        }
    }



    public void init(){
        dbhelper=new DBhelper(context);
        db=dbhelper.getWritableDatabase();

        listView =(ListView)findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyView));
        listView.setOnItemClickListener(new MyOnItemClickListener());
    }
}
