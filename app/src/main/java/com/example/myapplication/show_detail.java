package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class show_detail extends AppCompatActivity {

    private static String TAG = "show_detail";

    private static final String TAG_JSON="detail_info";
    private static final String TAG_NAME = "name";
    private static final String TAG_INGREDIENT = "ingredient";
    private static final String TAG_EFFICIENCY = "efficiency";
    private static final String TAG_CAPACITY = "capacity";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_WARNING = "warning";

    static String pill = null;
    static Drawable pill_img = null;
    static String pill_comp = null;


    ListView mlistView;
    ListViewAdapterDetail adapter;
    String mJsonString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;


        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("기본 정보") ;
        tabHost1.addTab(ts1) ;


        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("효능 효과") ;
        tabHost1.addTab(ts2) ;
        TextView tab2 = (TextView)findViewById(R.id.tab2);
        tab2.setText("효능 효과"); //여기에다가 정보 입력


        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("주의 사항") ;
        tabHost1.addTab(ts3) ;
        TextView tab3 = (TextView)findViewById(R.id.tab3);
        tab3.setText("주의사항"); //여기에다가 정보 입력항

        adapter = new  ListViewAdapterDetail();
        mlistView = (ListView) findViewById(R.id.tab1_listView);

        GetData task = new GetData();
        task.execute();
    }

    private class GetData extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(show_detail.this,
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
        }

        @Override
        protected String doInBackground(Void... unused) {

            /*show result의 click한 곳 값 받아오기*/
            //String pill = "부광메티마졸정";

            /* 인풋 파라메터값 생성 */
            String param = "pill="+pill+"";
            System.out.println(param);
            Log.e("POST",param);

            try {
                /* 서버연결 */
                URL url = new URL("http://203.255.176.79:8000/show_detail.php");
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

    private void showResult(){

        // 리스트뷰 참조 및 Adapter달기


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String ingredient = item.getString(TAG_INGREDIENT);
                String name = item.getString(TAG_NAME);
                String capacity = item.getString(TAG_CAPACITY);
                String company = item.getString(TAG_COMPANY);

                String warning = item.getString(TAG_WARNING);
                String efficiency = item.getString(TAG_EFFICIENCY);


                adapter.addItem(name, company, ingredient, capacity);

                TextView tab2 = (TextView)findViewById(R.id.tab2);
                tab2.setText(efficiency); //여기에다가 정보 입력


                TextView tab3 = (TextView)findViewById(R.id.tab3);
                tab3.setText(warning); //여기에다가 정보 입력항

                if (i==0){
                    pill_comp = company;
                }

            }

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }



    public void add_pill(View v) {
        Intent intent = new Intent(getApplicationContext(), add_pill_user.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);

        Toast.makeText(getApplicationContext()," 내 알약으로 등록하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }

    public void show_detail_back(View v) {
        Intent intent = new Intent(getApplicationContext(), search_result_after_login.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(getApplicationContext()," 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}