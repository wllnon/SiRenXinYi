package com.test.wllnon.sirenxinyi.pojo;

import android.text.TextUtils;

import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.sql.Time;

/**
 * Created by Administrator on 2016/5/4.
 */
public class AudioMessage extends Message {
    private File file;
    private String filePath;
    private long length;
    private RecordType recordType;

    public AudioMessage(int id, File file, String filePath, long length, RecordType recordType, Time time) {
        super(id, MessageType.AUDIO_MESSAGE, "这是一条语音信息", time);

        this.file = file;
        this.length = length;
        this.recordType = recordType;

        if (TextUtils.isEmpty(filePath) && file != null) {
            this.filePath = file.getPath();
        } else {
            this.filePath = filePath;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    @Override
    public IMMessage getIMMessage() {
        return MessageBuilder.createAudioMessage("1301671535@qq.com", SessionTypeEnum.P2P, getFile(), getLength());
    }

}
