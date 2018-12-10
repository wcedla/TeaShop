package com.example.wcedla.selltea.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wcedla.selltea.R;
import com.example.wcedla.selltea.tool.CodeUtils;
import com.example.wcedla.selltea.tool.HttpTool;
import com.example.wcedla.selltea.tool.JsonTool;
import com.example.wcedla.selltea.tool.SystemTool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    Activity myActivity;
    int number;
    View view;
    boolean isRegistering=false;

    @Override
    public void onAttach(Context context) {

        myActivity = (Activity) context;
        //number = getArguments().getInt("number");  //获取参数
        super.onAttach(context);
    }

    public static RegisterFragment newInstance(int i) {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", i);
        registerFragment.setArguments(bundle);   //设置参数
        return registerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_register_layout,container,false);
        final ImageView imageView=view.findViewById(R.id.code);
        Button registerBtn=view.findViewById(R.id.register_btn);
        final EditText userNameTbx=view.findViewById(R.id.register_username);
        final EditText pwdTbx=view.findViewById(R.id.register_pwd);
        final EditText emailTbx=view.findViewById(R.id.register_email);
        final EditText codeTbx=view.findViewById(R.id.register_code);



        imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                Log.d(TAG, "onCreateView: "+CodeUtils.getInstance().getCode());
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRegistering)
                {
                    Snackbar.make(view,"请不要频繁提交注册!",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                isRegistering=true;
                String userName=userNameTbx.getText().toString();
                String pwd=pwdTbx.getText().toString();
                String email=emailTbx.getText().toString();
                final String code=codeTbx.getText().toString().toLowerCase();
                if(!userName.equals("") &&!pwd.equals("")&&!email.equals("")&&!code.equals(""))
                {
                    if(userName.length()<3)
                    {
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(view,"输入的账号小于三个字符",Snackbar.LENGTH_SHORT).show();
                                imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                isRegistering=false;
                            }
                        });
                        return;
                    }
                    if(pwd.length()<6)
                    {
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(view,"输入的密码小于六个字符",Snackbar.LENGTH_SHORT).show();
                                imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                isRegistering=false;
                            }
                        });
                        return;
                    }
                    if(!SystemTool.isEmail(email))
                    {
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(view,"输入的邮箱格式有误！",Snackbar.LENGTH_SHORT).show();
                                imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                isRegistering=false;
                            }
                        });
                        return;
                    }
                    if(!code.equals(CodeUtils.getInstance().getCode().toLowerCase()))
                    {
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(view,"验证码错误",Snackbar.LENGTH_SHORT).show();
                                imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                isRegistering=false;
                            }
                        });
                        return;
                    }
                    String url="http://192.168.191.1:8080/SqlServerMangerForAndroid/registerServlet?username="+userName+"&password="+pwd+"&email="+email;
                    HttpTool.doHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            myActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(view,"连接服务器失败！",Snackbar.LENGTH_SHORT).show();
                                    isRegistering=false;
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData=response.body().string();
                            int result=JsonTool.getRegisterResult(responseData);
                            Log.d(TAG, "注册结果"+result);
                            if(result==0)
                            {
                                myActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(view,"恭喜你，注册成功！",Snackbar.LENGTH_SHORT).show();
                                        isRegistering=false;
                                    }
                                });
                            }
                            else if(result==-1)
                            {
                                myActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(view,"该用户名已被注册！",Snackbar.LENGTH_SHORT).show();
                                        imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                        isRegistering=false;
                                    }
                                });
                            }
                            else
                            {
                                myActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(view,"注册失败！",Snackbar.LENGTH_SHORT).show();
                                        imageView.setImageBitmap(CodeUtils.getInstance().createBitmap());
                                        isRegistering=false;
                                    }
                                });
                            }
                        }
                    });
                }else
                {
                    Snackbar.make(view,"请按要求填写相关信息!",Snackbar.LENGTH_SHORT).show();
                    isRegistering=false;
                }
            }
        });
        return view;
    }
}
