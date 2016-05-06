package com.test.wllnon.sirenxinyi.event;

import com.test.wllnon.sirenxinyi.constant.Constant;

/**
 * Created by Administrator on 2016/3/31.
 */
public class DeviceStateMessageEvent {
    public String address;
    public Constant.DeviceState state;

    public DeviceStateMessageEvent() {}

    public DeviceStateMessageEvent(String address, Constant.DeviceState state) {
        this.address  = address;
        this.state = state;
    }
}
