package com.rounima.markbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rounima.markbook.R;
import com.rounima.markbook.model.Note;
import com.rounima.markbook.model.Note_Table;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String action = "EditNoteActivity.action";
    EditText text1;//内容输入框
    ImageView img1;//返回按钮
    ImageView img2;//保存按钮
    long noteId = 0;
    String TAG = "EDITNOTELOG";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initView();
        try{
            Bundle extras = getIntent().getExtras();
            noteId =extras.getLong("noteId");
            Note note = SQLite.select().from(Note.class).where(Note_Table.noteId.eq(noteId)).querySingle();
            if(note!=null){
                text1.setText(note.getContent());
            }
        }catch (Exception e){
            //do noting
        }
    }
    public void initView(){
        text1 = findViewById(R.id.text1);
        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(this);
        img2 = findViewById(R.id.img2);
        img2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                this.finish();
                break;
            case R.id.img2:
                save();
                break;
            default:break;
        }
    }
    public void save(){
        String content = text1.getText().toString();
        if(content.trim().length()==0){
            return ;
        }
        Note note = new Note();
        note.setContent(content);
        Date date = new Date();
        note.setAddTime(date);
        Boolean isSaved;
        if(noteId>0){
            note.setNoteId(noteId);
            isSaved = note.update();
        }else{
            isSaved = note.save();
        }
        if(isSaved){
            Toast.makeText(this,"保存成功!",Toast.LENGTH_SHORT).show();
            noteId = note.getNoteId();
            Intent it = new Intent(action);
            it.putExtra("noteId",note.getNoteId());
            sendBroadcast(it);
        }else{
            Toast.makeText(this,"保存失败!",Toast.LENGTH_SHORT).show();
        }
    }
}
