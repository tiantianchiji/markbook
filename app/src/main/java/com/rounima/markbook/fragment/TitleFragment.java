package com.rounima.markbook.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rounima.markbook.R;
import com.rounima.markbook.imp.CallBack;
import com.rounima.markbook.util.AlertDialogUtil;
import com.rounima.markbook.util.ScreenUtil;

public class TitleFragment extends Fragment implements View.OnClickListener{
    private CallBack callBack;
    SlidingMenu menu;
    PopupWindow popupWindow;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_top_menu,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLeftMenu();
        ImageView img = getActivity().findViewById(R.id.img2);
        img.setOnClickListener(this);
        ImageView img1 = getActivity().findViewById(R.id.img1);
        img1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img2:
                dropMenu(v);
                break;
            case R.id.img1:
                menu.toggle();
                break;
            default:break;
        }
    }
    /**弹出左侧菜单*/
    public void showLeftMenu(){
        menu = new SlidingMenu(getContext());
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
        menu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        final TextView t2 = getActivity().findViewById(R.id.left_menu_t2);
        if(t2==null)
            Log.i("weibiao","t2 is null");
        t2.setOnClickListener(t2Listener);
    }
    View.OnClickListener t2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialogUtil.showDialog(getContext(),"嘻嘻","要功能自己写啊",true);
        }
    };

    public void dropMenu(View view){
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.drop_menu,null);
         popupWindow = new PopupWindow(contentView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.drop_menu_background));
        int windowPos[] = calculatePopWindowPos(view, contentView);
        int xOff = 20;// 可以自己调整偏移
        windowPos[0] -= xOff;
        popupWindow.showAtLocation(view,Gravity.TOP | Gravity.START,windowPos[0],windowPos[1]);
        TextView delete_all = contentView.findViewById(R.id.delete_all);
        delete_all.setOnClickListener(dListener);
    }
    View.OnClickListener dListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callBack = (CallBack) getActivity();
            callBack.sendOnclick();
            popupWindow.dismiss();
        }
    };
    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    private  int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtil.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtil.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

}
