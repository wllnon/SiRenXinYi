package com.test.wllnon.sirenxinyi.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.DeviceCardData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class DeviceCardViewHolder extends BaseCardViewHolder implements View.OnClickListener {
    private ImageButton image;
    private ImageButton stop;
    private ImageButton delete;
    private TextView name;

    private Constant.DeviceState state;

    private int position = 0;

    private DeviceItemOnClickListener listener;

    public DeviceCardViewHolder(Activity activity, View view) {
        super(activity, Constant.DEVICE_ECG, view);
        findView(view);
        bindOnClickListener();
    }

    private void findView(View view) {
        stop = (ImageButton) view.findViewById(R.id.stop_device);
        delete = (ImageButton) view.findViewById(R.id.delete_device);
        name = (TextView) view.findViewById(R.id.name_device);
        image = (ImageButton) view.findViewById(R.id.image_device);
    }

    private void bindOnClickListener() {
        stop.setOnClickListener(this);
        delete.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    public void changeState() {

    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.stop_device:
                    listener.stopOnClick(position);
                    break;
                case R.id.delete_device:
                    listener.deleteOnClick(position);
                    break;
                case R.id.image_device:
                    listener.imageOnClick(position);
            }
        }
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        super.setCardData(baseCardData);
        if (baseCardData == null) {
            return;
        }

        DeviceCardData deviceCardData = (DeviceCardData) baseCardData;
        setNameText(deviceCardData.getName());
        setState(deviceCardData.getState());
        setImageSrc(deviceCardData.getImageId());
        setCardKind(baseCardData.getCardKind());

        switch (state) {
            case DEVICE_ON:
                stop.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_18dp);
                break;
            case DEVICE_OFF:
                stop.setBackgroundResource(R.drawable.ic_play_circle_filled_black_18dp);
                break;
            case DEVICE_WAIT:
                stop.setBackgroundResource(R.drawable.ic_more_horiz_grey_600_36dp);
                break;
            default:
                stop.setBackgroundResource(R.drawable.ic_play_circle_filled_black_18dp);
        }
    }

    public void setCardData(BaseCardData baseCardData, int position) {
        setCardData(baseCardData);
        this.position = position;
    }

    public void setNameText(String nameType) {
        if (nameType != null) {
            this.name.setText(nameType);
        } else {
            this.name.setText(getCardKind());
        }
    }

    public void setImageSrc(int imageId) {
        if (imageId != 0) {
            this.image.setBackgroundResource(imageId);
        }
    }

    public void setState(Constant.DeviceState state) {
        this.state = state;
    }

    public void setOnClickListener(DeviceItemOnClickListener listener) {
        this.listener = listener;
    }

    public interface DeviceItemOnClickListener {
        void stopOnClick(int position);
        void deleteOnClick(int position);
        void imageOnClick(int position);
    }

}
