package com.dom.a10174987.meproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HorizontalLayoutEx parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        parentLayout = (HorizontalLayoutEx) findViewById(R.id.container);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 3; i++) {
            View child = inflater.inflate(R.layout.childcontent, parentLayout, false);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) child.getLayoutParams();
//            params.height = getDisplayMetrics(this).heightPixels;
//            params.height = 600;
//            params.width = getDisplayMetrics(this).widthPixels;
            createList(child, i);
            parentLayout.addView(child);
        }
    }

    private void createList(View view, int index) {
        TextView head = (TextView) view.findViewById(R.id.head);
        ListView list = (ListView) view.findViewById(R.id.list);

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add("" + i);
        }
        head.setText("head " + index);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.item_content, datas);
        list.setAdapter(adapter);
    }

    private DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics out = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(out);
        return out;
    }
}
