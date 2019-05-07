package com.rounima.markbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rounima.markbook.R;
import com.rounima.markbook.model.Note;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    List<Note> list ;
    Context mcontext;
    public ListViewAdapter(List<Note> list,Context context){
        this.list = list;
        this.mcontext = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        if(convertView==null){
            view = inflater.inflate(R.layout.list_item,null);
        }else{
            view = convertView;
        }
        TextView text1 = view.findViewById(R.id.text1);
        String content = list.get(position).getContent();
        content = content.replace("\n"," ");
        if(content.length()>20){
            content = content.substring(0,20)+"...";
        }
        text1.setText(content);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TextView text2 = view.findViewById(R.id.text2);
        text2.setText(sdf.format(list.get(position).getAddTime()));
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setChecked(false);
        if(list.get(position).getIsShowBox()){
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    list.get(position).setIsSelected(isChecked);
                    Log.i("weibiao",position+"-"+isChecked);
                }
            });
        }else {
            checkBox.setVisibility(View.GONE);
        }
        return view;
    }

}
