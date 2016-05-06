package com.test.wllnon.sirenxinyi.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.wllnon.sirenxinyi.R;

import java.io.FileNotFoundException;

public class PicturePikerActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final int REQUEST_CHOOSE_PICTURE = 1;

    private Button takePhotoBtn;
    private Button chooseAlbumBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_piker);

        setResult(ChatActivity.RESULT_CANCEL);

        findViews();
        setListeners();
    }

    private void findViews() {
        takePhotoBtn = (Button) findViewById(R.id.choose_from_taking);
        chooseAlbumBtn = (Button) findViewById(R.id.choose_from_album);
        cancelBtn = (Button) findViewById(R.id.cancel_picture_pike);
    }

    private void setListeners() {
        takePhotoBtn.setOnClickListener(this);
        chooseAlbumBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void chooseFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, REQUEST_CHOOSE_PICTURE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_from_album:
                chooseFromAlbum();
                break;
            case R.id.choose_from_taking:
                break;
            case R.id.cancel_picture_pike:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PICTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        setResult(ChatActivity.RESULT_SUCCESS, data);
                        finish();
                        break;
                }
                break;
        }
    }

}
