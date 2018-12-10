package com.example.wcedla.selltea;

import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wcedla.selltea.adapter.HotGridAdapter;
import com.example.wcedla.selltea.tool.SystemTool;

import java.util.Arrays;
import java.util.List;

public class ShowSearchWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_show_search_window);
        final EditText editText=findViewById(R.id.search_text);
        final LinearLayout doSearch=findViewById(R.id.do_search);
        GridView hotGridView=findViewById(R.id.hot_grid);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
            }
        });
        doSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //Log.d("wcedla", "图形点击"+inputMethodManager.isActive());
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(doSearch.getWindowToken(), 0);
                    }
                }
                //Toast.makeText(ShowSearchWindowActivity.this,"点击",Toast.LENGTH_SHORT).show();
                if(!editText.getText().toString().equals(""))
                {
                    Intent showSearchResult=new Intent(ShowSearchWindowActivity.this,SearchActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("name",editText.getText().toString());
                    showSearchResult.putExtras(bundle);
                    startActivity(showSearchResult);
                }
                else
                {
                    Toast.makeText(ShowSearchWindowActivity.this,"请输入搜索内容再搜索！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final String[] hotTitle = new String[]{"普洱", "铁观音", "红茶", "绿茶", "乌龙", "大红袍", "丁香叶", "茉莉花"};
        HotGridAdapter hotGridAdapter=new HotGridAdapter(this,Arrays.asList(hotTitle));
        hotGridView.setAdapter(hotGridAdapter);
        hotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showSearchResult=new Intent(ShowSearchWindowActivity.this,SearchActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",hotTitle[position]);
                showSearchResult.putExtras(bundle);
                startActivity(showSearchResult);
            }
        });

    }
}
