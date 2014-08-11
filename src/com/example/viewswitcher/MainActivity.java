package com.example.viewswitcher;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {
    public static final int NUMBER_PER_SCREEN = 12;
    
    public static class DataItem {
        public String dataName;
        public Drawable drawable;
    }

    private ArrayList<DataItem> items = new ArrayList<MainActivity.DataItem>();
    private int screenNo = -1;
    private int screenCount;
    private ViewSwitcher viewSwitcher;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        inflater = LayoutInflater.from(MainActivity.this);
        
        for(int i = 0; i < 40; i++) {
            String label = "" + i;
            Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
            DataItem item = new DataItem();
            item.dataName = label;
            item.drawable = drawable;
            items.add(item);
        }
        
        screenCount = items.size() % NUMBER_PER_SCREEN == 0 ? items.size() / NUMBER_PER_SCREEN : items.size() / NUMBER_PER_SCREEN + 1;
        viewSwitcher = (ViewSwitcher)findViewById(R.id.viewSwitcher);
        viewSwitcher.setFactory(new ViewFactory() {
            
            @Override
            public View makeView() {
                return inflater.inflate(R.layout.slidelistview, null);
            }
        });
        
        next(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void next(View v)
    {
        if (screenNo < screenCount - 1)
        {
            screenNo++;
            // 为ViewSwitcher的组件显示过程设置动画
            viewSwitcher.setInAnimation(this, R.anim.slide_in_right);
            // 为ViewSwitcher的组件隐藏过程设置动画
            viewSwitcher.setOutAnimation(this, R.anim.slide_out_left);
            // 控制下一屏将要显示的GridView对应的 Adapter
            ((GridView) viewSwitcher.getNextView()).setAdapter(adapter);
            // 点击右边按钮，显示下一屏，
            // 学习手势检测后，也可通过手势检测实现显示下一屏.
            viewSwitcher.showNext();  // ①
        }
    }

    public void prev(View v)
    {
        if (screenNo > 0)
        {
            screenNo--;
            // 为ViewSwitcher的组件显示过程设置动画
            viewSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
            // 为ViewSwitcher的组件隐藏过程设置动画
            viewSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);
            // 控制下一屏将要显示的GridView对应的 Adapter
            ((GridView) viewSwitcher.getNextView()).setAdapter(adapter);
            // 点击左边按钮，显示上一屏，当然可以采用手势
            // 学习手势检测后，也可通过手势检测实现显示上一屏.
            viewSwitcher.showPrevious();   // ②
        }
    }
    
    // 该BaseAdapter负责为每屏显示的GridView提供列表项
    private BaseAdapter adapter = new BaseAdapter()
    {
        @Override
        public int getCount()
        {
            // 如果已经到了最后一屏，且应用程序的数量不能整除NUMBER_PER_SCREEN
            if (screenNo == screenCount - 1
                    && items.size() % NUMBER_PER_SCREEN != 0)
            {
                // 最后一屏显示的程序数为应用程序的数量对NUMBER_PER_SCREEN求余
                return items.size() % NUMBER_PER_SCREEN;
            }
            // 否则每屏显示的程序数量为NUMBER_PER_SCREEN
            return NUMBER_PER_SCREEN;
        }

        @Override
        public DataItem getItem(int position)
        {
            // 根据screenNo计算第position个列表项的数据
            return items.get(screenNo * NUMBER_PER_SCREEN + position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position
                , View convertView, ViewGroup parent)
        {
            View view = convertView;
            if (convertView == null)
            {
                // 加载R.layout.labelicon布局文件
                view = inflater.inflate(R.layout.labelicon, null);
            }
            // 获取R.layout.labelicon布局文件中的ImageView组件，并为之设置图标
            ImageView imageView = (ImageView)
                    view.findViewById(R.id.imageview);
            imageView.setImageDrawable(getItem(position).drawable);
            // 获取R.layout.labelicon布局文件中的TextView组件，并为之设置文本
            TextView textView = (TextView) 
                    view.findViewById(R.id.textview);
            textView.setText(getItem(position).dataName);
            return view;
        }
    };
}
