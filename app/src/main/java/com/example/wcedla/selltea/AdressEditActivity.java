package com.example.wcedla.selltea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wcedla.selltea.communication.EventBean;
import com.example.wcedla.selltea.communication.EventManager;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdressEditActivity extends AppCompatActivity {

    String adressId;
    String adrsssName;
    String adressPhone;
    String adressText;
    EditText adressEditName;
    EditText adressEditPhone;
    EditText adressEditText;
    Button adressEditSave;
    Button adressEditDelete;

    ProgressDialog progressDialog;
    List<String> newAdressInfo = new ArrayList<>();
    EventBean eventBean = new EventBean();
    boolean isEditNewItem;
    String sqlStr;
    //long newAddId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_adress_edit);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences loginPreference = getSharedPreferences("login", MODE_PRIVATE);
        userName = loginPreference.getString("username", "");
        //Log.d("wcedla", "得到数据"+adressId+","+adrsssName+","+adressPhone+","+adressText);
        adressEditName = findViewById(R.id.adress_edit_username);
        adressEditPhone = findViewById(R.id.adress_edit_phone);
        adressEditText = findViewById(R.id.adress_edit_text);
        adressEditSave = findViewById(R.id.adress_edit_save);
        adressEditDelete = findViewById(R.id.adress_edit_delete);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(adressEditName, InputMethodManager.SHOW_FORCED);
        if (bundle != null) {
            adressId = bundle.getString("adressId");
            adrsssName = bundle.getString("adressName");
            adressPhone = bundle.getString("adressPhone");
            adressText = bundle.getString("adressText");
            adressEditName.setText(adrsssName);
            adressEditName.setSelection(adrsssName.length());
            adressEditPhone.setText(adressPhone);
            adressEditText.setText(adressText);
            adressEditDelete.setVisibility(View.VISIBLE);
            isEditNewItem = false;
        } else {
            isEditNewItem = true;
        }
        adressEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBean.what = 1;
//                newAdressInfo.add(adressId);
//                newAdressInfo.add(adressEditName.getText().toString().trim());
//                newAdressInfo.add(adressEditPhone.getText().toString().trim());
//                newAdressInfo.add(adressEditText.getText().toString().trim());
//                eventBean.obj = newAdressInfo;
                progressDialog = new ProgressDialog(AdressEditActivity.this);
                progressDialog.setTitle("保存地址信息");
                progressDialog.setMessage("正在保存...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (!adressEditName.getText().toString().trim().equals("") &&
                        !adressEditPhone.getText().toString().trim().equals("") &&
                        !adressEditText.getText().toString().trim().equals("")) {
                    if (!isEditNewItem) {
                        sqlStr = "update adress set adressname='" + adressEditName.getText().toString().trim()
                                + "',adressphone='" + adressEditPhone.getText().toString().trim()
                                + "',adresstext='" + adressEditText.getText().toString().trim() + "' where id=" + adressId;
                        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                        applyChange(url);
                    } else {
                        insetNewItem();
                    }

                } else {
                    progressDialog.cancel();
                    Toast.makeText(AdressEditActivity.this, "请正确填写信息！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        adressEditDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog=new AlertDialog.Builder(AdressEditActivity.this);
                alertDialog.setTitle("删除警告！");
                alertDialog.setMessage("确定删除吗？");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventBean.what = 1;
                        progressDialog = new ProgressDialog(AdressEditActivity.this);
                        progressDialog.setTitle("保存地址信息");
                        progressDialog.setMessage("正在保存...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        String sqlStr="delete from adress where id="+adressId;
                        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
                        applyChange(url);
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });


    }

    private void applyChange(String url) {

        //Log.d("wcedla", "连接字符串"+url);
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(AdressEditActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                boolean result = JsonTool.getStatus(responseData);
                if (result) {
                    //EventManager.callEvent(eventBean);
                    AdressManageActivity.activityEventManger.callEvent(eventBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(AdressEditActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(AdressEditActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void insetNewItem() {

//        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=select Max(id) from adress&cmd=executeQuery";
//        HttpTool.doHttpRequest(url, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.cancel();
//                        Toast.makeText(AdressEditActivity.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                newAddId = JsonTool.getAdressMaxId(responseData);
//                if (newAddId < 1) {
//                    progressDialog.cancel();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(AdressEditActivity.this, "数据获取失败！", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    newAddId += 1;
////                    call.cancel();
//                    confireAdd();
//                }
//            }
//        });

        sqlStr = "insert into adress values('" +
                userName + "','" +
                adressEditName.getText().toString().trim() + "','" +
                adressEditPhone.getText().toString().trim() + "','" +
                adressEditText.getText().toString().trim() + "')";
        String url = "http://192.168.191.1:8080/SqlServerMangerForAndroid/SqlExcuteServlet?sql=" + sqlStr;
        //Log.d("wcedla", "内容" + url);
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(AdressEditActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                boolean result = JsonTool.getStatus(responseData);
                if (result) {
                    //EventManager.callEvent(eventBean);
                    AdressManageActivity.activityEventManger.callEvent(eventBean);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(AdressEditActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(AdressEditActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
