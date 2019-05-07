package com.rounima.markbook.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.rounima.markbook.database.MBDataBase;

import java.text.SimpleDateFormat;
import java.util.Date;

@Table(database = MBDataBase.class)
public class Note extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private long noteId;
    @Column
    private String content;
    @Column
    private Date addTime;
    //是否选中 虚拟字段
    private boolean isSelected = false;
    //是否显示选择框
    private boolean isShowBox = false;
    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsShowBox() {
        return isShowBox;
    }

    public void setIsShowBox(boolean showBox) {
        isShowBox = showBox;
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Note{\ncontent:"+content+",\naddTime="+sdf.format(addTime)+"\n}";
    }
}
