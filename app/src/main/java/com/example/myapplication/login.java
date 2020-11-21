package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;

public class login  extends AppCompatActivity {

    final Context context = this;

    EditText et_id, et_pw;
    static String sId, sPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_id = (EditText) findViewById(R.id.ID_text);
        et_pw = (EditText) findViewById(R.id.PW_text);

    }


    public void login_button(View v) {
        try {
            sId = et_id.getText().toString();
            sPw = et_pw.getText().toString();
        }
        catch (NullPointerException e)
        {
            Log.e("err", e.getMessage());
        }

        loginDB IDB = new loginDB();
        IDB.execute();
    }

    public class loginDB extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            Log.e("POST",param);
            try {
                /* 서버연결 */
                URL url = new URL("http://203.255.176.79:8000/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA",data);

                if(!data.equals("0"))
                {
                    // 정상 작동
                    Log.e("RESULT","성공적으로 처리되었습니다!");
                    //Intent intent = new Intent(getApplicationContext(), after_login.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //startActivity(intent);
                    //overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);
                }
                else
                {
                    Log.e("RESULT","에러 발생! ERRCODE = " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            if(data.equals("1"))
            {
                Intent intent = new Intent(getApplicationContext(), after_login.class);
                startActivity(intent);
                overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);
                finish();

            }
            else if(data.equals("0")||data.equals("Can not find ID"))
            {
                if (data.equals("0")) Log.e("RESULT", "비밀번호가 일치하지 않습니다.");
                else Log.e("RESULT", "등록되지 않은 아이디입니다.");
                alertBuilder
                        //.setTitle("알림")
                        .setMessage("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        //.setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }

    }

    public void signup_button(View v) {
        Intent intent = new Intent(getApplicationContext(), signup.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Toast.makeText(getApplicationContext(),"회원가입 하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
    public void login_back(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Toast.makeText(getApplicationContext()," 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}
