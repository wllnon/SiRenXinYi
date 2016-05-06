package com.test.wllnon.sirenxinyi.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.activity.DeviceListActivity;
import com.test.wllnon.sirenxinyi.activity.ECGActivity;
import com.test.wllnon.sirenxinyi.activity.LoginActivity;
import com.test.wllnon.sirenxinyi.adapter.RecyclerViewAdapter;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.event.DeviceStateMessageEvent;
import com.test.wllnon.sirenxinyi.service.DeviceConnectService;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.DeviceCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;
import com.test.wllnon.sirenxinyi.viewholder.DeviceCardViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/3/29.
 */
public class DeviceFragment extends Fragment {
    public static final String TAG = "DeviceFragment";

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private DeviceRecyclerAdapter adapter;

    private DeviceConnectService service;

    private List<BaseCardData> dataList;

    private ServiceConnection connection = new ServiceConnection();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_device, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_device);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_device);

        updateCardData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new DeviceRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setupBluetooth()) {
                    return;
                }

                Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                // REQUEST_CONNECT_DEVICE means adding new device (Because this is not my code, here maybe weired)
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(new Intent(getActivity(), DeviceConnectService.class),
                connection,
                Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(connection);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeviceStateMessageEvent messageEvent) {
        Iterator<BaseCardData> iterator = dataList.iterator();
        int position = 0;
        while (iterator.hasNext()) {
            DeviceCardData deviceCardData = (DeviceCardData) iterator.next();
            if (deviceCardData.getBluetoothAddress().equals(messageEvent.address)) {
                deviceCardData.setState(messageEvent.state);
                adapter.notifyItemChanged(position);
                break;
            }
            ++position;
        }
        EventBus.getDefault().removeStickyEvent(messageEvent);

        String toastText = "";
        switch (messageEvent.state) {
            case DEVICE_ON:
                toastText = getString(R.string.device_connected);
                break;
            case DEVICE_OFF:
                toastText = getString(R.string.device_connect_failed);
                break;
            case DEVICE_WAIT:
                toastText = getString(R.string.device_connecting);
                break;
        }
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
    }

    private void updateCardData() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
    }

    private boolean setupBluetooth() {
        return service.setupBluetooth();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CONNECT_DEVICE means adding new device (Because this is not my code, here maybe weired)
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                // TODO: 2016/3/30 should differ types of different devices
                DeviceCardData cardData = new DeviceCardData();
                cardData.setBluetoothName(data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_NAME));
                cardData.setBluetoothAddress(data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS));
                cardData.setCardKind(Constant.DEVICE_ECG);
                cardData.setName("我的心电设备");
                cardData.setImageId(R.drawable.img_device_background);
                cardData.setState(Constant.DeviceState.DEVICE_OFF);
                adapter.addItem(cardData);
            }
        }
    }

    public class ServiceConnection implements android.content.ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = ((DeviceConnectService.Binder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO: 2016/3/31 noting to do
        }
    }

    public class DeviceRecyclerAdapter extends RecyclerViewAdapter<BaseCardViewHolder> {

        DeviceCardViewHolder.DeviceItemOnClickListener listener = new DeviceCardViewHolder.DeviceItemOnClickListener() {
            @Override
            public void stopOnClick(int position) {
                if (!setupBluetooth()) { return; }

                DeviceCardData cardData = (DeviceCardData) dataList.get(position);
                Constant.DeviceState state = cardData.getState();

                switch (state) {
                    case DEVICE_ON:
                        service.startConnection();
                        break;
                    case DEVICE_OFF:
                        service.stopConnection();
                        break;
                }
            }

            @Override
            public void deleteOnClick(int position) {
                final int positionToDelete = position;
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .setCancelText("No, cancel plz.")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmText("Yes,delete it!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            removeItem(positionToDelete);
                            sweetAlertDialog.setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .showCancelButton(false)
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    }).show();
            }

            @Override
            public void imageOnClick(int position) {
                // TODO: 2016/3/31
                startActivity(new Intent(getActivity(), ECGActivity.class));
            }
        };

        @Override
        public BaseCardViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
            BaseCardViewHolder viewHolder = null;
            switch (viewType) {
                case Constant.DEVICE_ECG:
                    DeviceCardViewHolder deviceViewHolder = new DeviceCardViewHolder(getActivity(),
                            LayoutInflater.from(getActivity()).inflate(R.layout.cardview_device, container, false));
                    deviceViewHolder.setOnClickListener(listener);

                    viewHolder = deviceViewHolder;
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return dataList.get(position).getCardKind();
        }

        @Override
        public void onBindViewHolder(BaseCardViewHolder viewHolder, int position) {
            DeviceCardViewHolder deviceCardViewHolder = (DeviceCardViewHolder) viewHolder;
            deviceCardViewHolder.setCardData(dataList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void removeItem(int position) {
            dataList.remove(position);
            adapter.notifyItemRemoved(position);
        }

        public void addItem(BaseCardData cardData) {
            int position = dataList.size();
            dataList.add(position, cardData);
            adapter.notifyItemInserted(position);
        }
    }
}
