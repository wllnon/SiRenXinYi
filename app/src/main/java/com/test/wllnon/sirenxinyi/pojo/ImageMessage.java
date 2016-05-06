package com.test.wllnon.sirenxinyi.pojo;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ImageMessage extends Message {
    private File file;
    private String filePath;
    public Uri uri;

    public ImageMessage(int id, File file, String filePath, Time time) {
        super(id, MessageType.PICTURE_MESSAGE, "这是一条图片信息", time);

        if (file == null && !TextUtils.isEmpty(filePath)) {
            this.file = new File(filePath);
        } else {
            this.file = file;
        }

        if (TextUtils.isEmpty(filePath) && file != null) {
            this.filePath = file.getPath();
        } else {
            this.filePath = filePath;
        }
    }

    public ImageMessage(int id, Activity context, Uri uri, Time time) {
        this(id, new File(uri.getPath()), null, time);
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public IMMessage getIMMessage() {
        return MessageBuilder.createImageMessage("1301671535@qq.com", SessionTypeEnum.P2P, getFile());
    }

}
