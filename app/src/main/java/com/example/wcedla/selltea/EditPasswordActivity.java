package com.example.wcedla.selltea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wcedla.selltea.communication.EventBean;
import com.example.wcedla.selltea.fragment.MeFragment;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditPasswordActivity extends AppCompatActivity {

    String userName;
    private String TAG="wcedla";
    EditText passwordEditor;
    EditText newPasswordEditor;
    LinearLayout passwordEditRoot;
    Button savepasswordChanged;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemTool.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.activity_edit_password);
        SharedPreferences loginPreference = getSharedPreferences("login", MODE_PRIVATE);
        userName = loginPreference.getString("username", "");
//        Log.d(TAG, "获得用户名"+userName);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        passwordEditor=findViewById(R.id.me_password_edit);
        newPasswordEditor=findViewById(R.id.me_new_password_edit);
        passwordEditRoot=findViewById(R.id.password_edit_root);
        savepasswordChanged=findViewById(R.id.edit_password_save_change);
        passwordEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditor.setFocusable(true);
                passwordEditor.setFocusableInTouchMode(true);
                passwordEditor.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(passwordEditor,InputMethodManager.SHOW_FORCED);
            }
        });
        newPasswordEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordEditor.setFocusable(true);
                newPasswordEditor.setFocusableInTouchMode(true);
                newPasswordEditor.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(newPasswordEditor,InputMethodManager.SHOW_FORCED);
            }
        });
        passwordEditRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditor.setFocusable(false);
                newPasswordEditor.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(passwordEditor.getWindowToken(), 0);
                    }
                }
            }
        });
        savepasswordChanged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditor.setFocusable(false);
                newPasswordEditor.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(passwordEditor.getWindowToken(), 0);
                    }
                }
                if((passwordEditor.getText().toString().length()>=6&&passwordEditor.getText().toString().length()<=15)&&
                        (newPasswordEditor.getText().toString().length()>=6&&newPasswordEditor.getText().toString().length()<=15))
                {
                    progressDialog = new ProgressDialog(EditPasswordActivity.this);
                    progressDialog.setTitle("修改密码");
                    progressDialog.setMessage("正在修改...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    setNewUserName();
                }
                else
                {
                    Toast.makeText(EditPasswordActivity.this,"密码字符需大于6位字符，小于15位字符！",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setNewUserName()
    {
        String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/PasswordEditServlet?username="+userName+"&password="+passwordEditor.getText().toString().trim()+"&newpassword="+newPasswordEditor.getText().toString().trim();
//        Log.d(TAG, "得到字符串"+url);
        HttpTool.doHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(EditPasswordActivity.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                int result=JsonTool.getEditPasswordStatus(responseData);
                if(result==0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(EditPasswordActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        }
                    });

                    finish();
                }
                else if(result==-1)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(EditPasswordActivity.this,"原密码输入错误，修改失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(EditPasswordActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
