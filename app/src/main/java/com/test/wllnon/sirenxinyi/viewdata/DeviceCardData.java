package com.test.wllnon.sirenxinyi.viewdata;

import com.test.wllnon.sirenxinyi.constant.Constant;

/**
 * Created by Administrator on 2016/3/30.
 */
public class DeviceCardData extends BaseCardData {
    private String name;
    private int imageId;

    private String bluetoothName;
    private String bluetoothAddress;
    private Constant.DeviceState state;

    public DeviceCardData() {
        setCardKind(Constant.DEVICE_ECG);
    }

    @Override
    public void setCardData(BaseCardData baseCardData) {
        if (baseCardData == null) {
            return;
        }
        DeviceCardData deviceCardData = (DeviceCardData) baseCardData;
        this.setName(deviceCardData.getName());
        this.setImageId(deviceCardData.getImageId());
        this.setCardKind(deviceCardData.getCardKind());

        this.setState(deviceCardData.getState());

        this.setBluetoothName(deviceCardData.getBluetoothName());
        this.setBluetoothAddress(deviceCardData.getBluetoothAddress());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public Constant.DeviceState getState() {
        return state;
    }

    public void setState(Constant.DeviceState state) {
        this.state = state;
    }
}
