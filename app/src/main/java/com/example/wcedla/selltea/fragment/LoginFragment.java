package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.wcedla.selltea.LoginActivity;
import com.example.wcedla.selltea.MainActivity;
import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.RegisterActivity;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    Activity myActivity;
    int number=0;
    View view;
    boolean logining=false;

    @Override
    public void onAttach(Context context) {

        myActivity = (Activity) context;
        if(getArguments()!=null)
        {
            number = getArguments().getInt("number");  //获取参数
        }
        super.onAttach(context);
    }

    public static LoginFragment newInstance(int i) {
        LoginFragment loginFrame = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", i);
        loginFrame.setArguments(bundle);   //设置参数
        return loginFrame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view=getLayoutInflater().inflate(R.layout.frame_login_layout,container,false);
        final TextView userNameTbx=view.findViewById(R.id.login_username);
        final TextView pwdTbx=view.findViewById(R.id.login_pwd);
        final Button loginBtn=view.findViewById(R.id.login_btn);
        TextView forgetPassword=view.findViewById(R.id.forget_password_tbx);
        TextView register=view.findViewById(R.id.register_tbx);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTbx.setFocusable(false);
                pwdTbx.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTbx.setFocusable(false);
                pwdTbx.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                Intent registerIntent=new Intent(myActivity,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTbx.setFocusable(false);
                pwdTbx.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });
        userNameTbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameTbx.setFocusable(true);
                userNameTbx.setFocusableInTouchMode(true);
                userNameTbx.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(userNameTbx,InputMethodManager.SHOW_FORCED);


            }
        });
        userNameTbx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pwdTbx.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pwdTbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdTbx.setFocusable(true);
                pwdTbx.setFocusableInTouchMode(true);
                pwdTbx.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(pwdTbx,0);

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logining)
                {
                    Snackbar.make(view,"请不要频繁登录！",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                logining=true;
                userNameTbx.setFocusable(false);
                pwdTbx.setFocusable(false);
                InputMethodManager inputMethodManager = (InputMethodManager) myActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                {
                    if (inputMethodManager.isActive())
                    {
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                if(!userNameTbx.getText().toString().equals("")&&!pwdTbx.getText().toString().equals(""))
                {
                    String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/loginServlet?username="+userNameTbx.getText().toString()+"&password="+pwdTbx.getText().toString();
                    HttpTool.doHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            myActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(view,"连接服务器失败！",Snackbar.LENGTH_SHORT).show();
                                    logining=false;
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData=response.body().string();
                            boolean result=JsonTool.getStatus(responseData);
                            if(result)
                            {
                                SharedPreferences.Editor loginEditor = myActivity.getSharedPreferences("login", MODE_PRIVATE).edit();
                                loginEditor.putBoolean("isLogin",true);
                                loginEditor.putString("username",userNameTbx.getText().toString());
                                loginEditor.apply();
                                if(number>0) {
                                    myActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivity.MyHandler handler = new MainActivity.MyHandler((MainActivity) myActivity);
                                            handler.sendEmptyMessage(1);
                                        }
                                    });

                                }
                                else
                                {
                                    myActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LoginActivity.MyHandler handler=new LoginActivity.MyHandler((LoginActivity)myActivity);
                                            handler.sendEmptyMessage(1);
                                        }
                                    });
                                }
                               //Log.d(TAG, "成功");
                                myActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(view,"登录成功！",Snackbar.LENGTH_SHORT).show();
                                        logining=false;
                                    }
                                });
                            }
                            else
                            {
                                //Log.d(TAG, "失败"+responseData);
                                myActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(view,"账号或者密码错误，请重试！",Snackbar.LENGTH_SHORT).show();
                                        logining=false;
                                    }
                                });

                            }
                        }
                    });
                }
                else
                {
                    Snackbar.make(view,"请输入账号密码！",Snackbar.LENGTH_SHORT).show();
                    logining=false;
                }
            }
        });
        return  view;
    }
}
