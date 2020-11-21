package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.myapplication.login.sId;
import static com.example.myapplication.saved_pill.saved_pill;

public class show_pill extends AppCompatActivity {

    private static String TAG = "show_pill_test_MainActivity";

    private static final String TAG_JSON="user_result";
    private static final String TAG_IMG = "img";
    private static final String TAG_NAME = "pill_name";
    private static final String TAG_NICKNAME ="pill_nickname";


    File img_internal_dir;
    ListView mlistView;
    ListViewAdapter adapter;
    String mJsonString;
    TextView my_pill_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pill);

        adapter = new ListViewAdapter() ;
        mlistView = (ListView) findViewById(R.id.listview1);
        my_pill_textview = (TextView) findViewById(R.id.my_pill_text);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        img_internal_dir = cw.getDir("imgageDir", this.MODE_PRIVATE);

        //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2), "부르펜", "해열제");


        GetData task = new GetData();
        task.execute();



    }

    private class GetData extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(show_pill.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            mJsonString = result;

            System.out.println(mJsonString);
            showResult();
            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    saved_pill = adapter.getPillName(position);
                    System.out.println(saved_pill);
                    saved_pill();
                }
            });
        }

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id="+sId+"";
            System.out.println(param);
            Log.e("POST",param);

            try {
                /* 서버연결 */
                URL url = new URL("http://203.255.176.79:8000/user_pill.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = httpURLConnection.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private Drawable loadImagaeFromStorage(String img_file_name){
        Drawable img = null;
        try{
            File f = new File(img_internal_dir, img_file_name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img = new BitmapDrawable(b);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return img;
    }

    private void showResult(){

        // 리스트뷰 참조 및 Adapter달기


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            if (jsonArray.length() == 0) my_pill_textview.setText("복용 중인 알약이 없습니다");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String img_file_name = item.getString(TAG_IMG);
                String name = item.getString(TAG_NAME);
                String nickname = item.getString(TAG_NICKNAME);

                Drawable img = loadImagaeFromStorage(img_file_name);

                adapter.addItem(img, name, nickname);
            }

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    public void after_back(View v) {
        Intent intent = new Intent(getApplicationContext(), after_login.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Toast.makeText(getApplicationContext(), " 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
    public void saved_pill() {
        Intent intent = new Intent(getApplicationContext(), saved_pill.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);

        //Toast.makeText(getApplicationContext()," 자세히 보기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }

}
