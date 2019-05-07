package com.rounima.markbook.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rounima.markbook.R;
import com.rounima.markbook.adapter.ListViewAdapter;
import com.rounima.markbook.imp.CallBack;
import com.rounima.markbook.model.Note;
import com.rounima.markbook.model.Note_Table;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CallBack {
    ImageButton add_note;
    List<Note> list;
    ListView listView;
    ListViewAdapter adapter;
    TextView cancel;
    TextView delete;
    RelativeLayout r2;
     static int statusHeight =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setHalfTransparent();
        setFitSystemWindow(false);
        //initLeftMenu();
        initView();
        showList();
        IntentFilter filter = new IntentFilter(EditNoteActivity.action);
        registerReceiver(broadcastReceiver, filter);
    }
    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    protected void setDrawerLayoutFitSystemWindow() {
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            int statusBarHeight = getStatusHeight(this);
            if (contentViewGroup == null) {
                contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            }
            if (contentViewGroup instanceof DrawerLayout) {
                DrawerLayout drawerLayout = (DrawerLayout) contentViewGroup;
                drawerLayout.setClipToPadding(true);
                drawerLayout.setFitsSystemWindows(false);
                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
                    View child = drawerLayout.getChildAt(i);
                    child.setFitsSystemWindows(false);
                    child.setPadding(0,statusBarHeight, 0, 0);
                }

            }
        }
    }
    public static int getStatusHeight(Context context) {
        if (statusHeight <= 0) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
                statusHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    public void initLeftMenu(){
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        //menu.setShadowDrawable(R.drawable.shadow);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.setOffsetFadeDegree(0.4f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);

    }
    public void initView(){
        add_note = findViewById(R.id.add_note);
        add_note.setOnClickListener(this);
        listView = findViewById(R.id.item_list);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);
        r2 = findViewById(R.id.r2);
    }
    public void showList(){
        list = SQLite.select().from(Note.class).orderBy(Note_Table.addTime,false).queryList();
        Log.i("1234","size="+list.size());
        if(adapter==null){
            adapter = new ListViewAdapter(list,this);
        }else{
            adapter.notifyDataSetChanged();
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(MainActivity.this,EditNoteActivity.class);
                it.putExtra("noteId",list.get(position).getNoteId());
                startActivity(it);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startDeleteAll();
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_note:
                clickAddNote();
                break;
            case R.id.cancel:
                cancel();
                break;
            case R.id.delete:
                delete();
            default:break;
        }
    }
    public void clickAddNote(){
        Intent it = new Intent(this,EditNoteActivity.class);
        startActivity(it);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Note note = SQLite.select().from(Note.class).where(Note_Table.noteId.eq(intent.getLongExtra("noteId",0))).querySingle();
            int i = 0;
            for(Note n : list){
                if(n.getNoteId()==note.getNoteId()){
                    list.remove(n);
                    list.add(0,note);
                    break;
                }
                i++;
            }
            if(i==list.size()){
                list.add(0,note);
            }
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void sendOnclick() {
        startDeleteAll();
    }
    /**准备批量删除操作*/
    public void startDeleteAll(){
        setListIsShowCheckBox(true,list);
        adapter.notifyDataSetChanged();
        r2.setVisibility(View.VISIBLE);
    }
    /**设置list的选择框是否显示*/
    public void setListIsShowCheckBox(boolean isShow,List<Note> l){
        for(Note note :l){
            note.setIsShowBox(isShow);
            note.setIsSelected(false);
        }
    }
    /**取消删除*/
    public void cancel(){
        setListIsShowCheckBox(false,list);
        adapter.notifyDataSetChanged();
        r2.setVisibility(View.GONE);
    }
    /**删除*/
    public void delete(){
        Iterator<Note> iterator = list.iterator();
        while(iterator.hasNext()){
            Note note = iterator.next();
            if(note.getIsSelected()){
                iterator.remove();
                note.delete();
            }
        }
        cancel();
    }
}
