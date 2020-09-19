package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;



public class add_pill_user extends AppCompatActivity {
    final Context context = this;
    search_result sr = new search_result(); //company이름을 받아오기 위한
    login login_ = new login();  // user id를 받아오기 위한
    String img_path = "temp.jpg";
    String company;
    String user_id =login_.sId;
    String pill_name;
    private Button button;
    static final String TAG = "ProfileActivityTAG";
    //RequestCode
    private String mTmpDownloadImageUri;
    private Bitmap img;
    private ImageView mImageView;
    final static int PICK_IMAGE = 1; //갤러리에서 사진선택
    final static int CAPTURE_IMAGE = 2;  //카메라로찍은 사진선택
    private String mCurrentPhotoPath;
    EditText et_nickname;
    String nickname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mImageView = findViewById(R.id.imageView);
        et_nickname = (EditText) findViewById(R.id.nick);


        setContentView(R.layout.add_pill_user);
        button = findViewById(R.id.button); ////
        mImageView = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener(){
            @Override //이미지 불러오기기
            public void onClick(View v) {
                photoDialogRadio(); //갤러리에서 불러오기 or 사진찍어서 불러오기
            }
        });
    }

    @Override //갤러리에서 이미지 불러온 후 행동
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
                // 이미지 표시
                mImageView.setImageBitmap(img);
                Log.d(TAG, "갤러리 inputStream: " + data.getData());
                Log.d(TAG, "갤러리 사진decodeStream: " + img);

                mTmpDownloadImageUri = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (resultCode == RESULT_OK) {
                try {
                    File file = new File(mCurrentPhotoPath);
                    InputStream in = getContentResolver().openInputStream(Uri.fromFile(file));
                    img = BitmapFactory.decodeStream(in);
                    mImageView.setImageBitmap(img);
                    in.close();

                    mTmpDownloadImageUri = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void photoDialogRadio() {
        final CharSequence[] PhotoModels = {"갤러리에서 가져오기", "카메라로 촬영 후 가져오기", "기본사진으로 하기"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle("프로필사진 설정");
        alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(add_pill_user.this, PhotoModels[item] + "가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                if (item == 0) { //갤러리
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE);
                } else if (item == 1) { //카메라찍은 사진가져오기
                    takePictureFromCameraIntent();
                } else { //기본화면으로하기
                    mImageView.setImageResource(R.drawable.camera2);
                    img = null;
                    mTmpDownloadImageUri = null;
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
    private void takePictureFromCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
            }
        }
    }
    //카메라로 촬영한 이미지를파일로 저장해주는 함수
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void after_back(View v) {
        Intent intent = new Intent(getApplicationContext(), add_pill.class);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(getApplicationContext()," 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
    //<여기부터 mysql 연동>
    public void save(View v){
        //nickname=et_nickname.getText().toString().trim();
        nickname = "test";
        company = sr.pill_company;
        pill_name = sr.pill_name;
        //user_id = login_.sId;
        //user_id="suhyune";
        img_path = "temp.jpg";
        save_DB save_in = new save_DB();
        save_in.execute();


    }
    /* DB에 저장 */
    public class save_DB extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + user_id + "&u_nick=" + nickname + "&pill_name=" + pill_name + "&img_path=" + img_path+"";
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://203.255.176.79:8000/add_pillinfo.php");
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
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA", data);
                androidx.appcompat.app.AlertDialog.Builder alertBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                if (data == "0"){
                    alertBuilder

                            .setMessage("저장완료!")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    androidx.appcompat.app.AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }



    }

}
