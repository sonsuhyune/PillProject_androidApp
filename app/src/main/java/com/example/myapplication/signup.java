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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class signup extends AppCompatActivity {

    final Context context = this;

    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        et_id = (EditText) findViewById(R.id.signup_ID_text);
        et_pw = (EditText) findViewById(R.id.signup_PW_text);
        et_pw_chk = (EditText) findViewById(R.id.signup_chkPW_text);
    }

    public void signup_button(View v) {

        /* 버튼을 툴렀을 때 동작 */
        sId = et_id.getText().toString().trim();
        sPw = et_pw.getText().toString().trim();
        sPw_chk = et_pw_chk.getText().toString().trim();

        if (sId.equals("")||sPw.equals("")||sPw_chk.equals("")) {
            Toast.makeText(getApplicationContext(), "정보를 모두 기입해주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            /* 비밀번호 두개가 일치하는지 */
            if (sPw.equals(sPw_chk)) {
                /* 패스워드 확인이 됨 */
                registDB rdb = new registDB();
                rdb.execute();
            } else {
                Toast.makeText(getApplicationContext(), "비밀번호가 불일치합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* ID, PW DB에 저장 */
    public class registDB extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://203.255.176.79:8000/snclib_join.php");
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
                Log.e("RECV DATA", data);

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

            /* 서버에서 응답 */
            Log.e("RECV DATA",data);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            if (data.equals("0")) {
                Log.e("RESULT", "성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("회원가입")
                        .setMessage("성공적으로 등록되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else {
                Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("회원가입")
                        .setMessage("등록중 에러가 발생했습니다! errcode : " + data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }

    public void after_back(View v) {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(getApplicationContext()," 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}

