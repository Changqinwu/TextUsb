package com.dfqm.hxyh.textusb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {

            Toast.makeText(context, "静态广播接收器设备插入", Toast.LENGTH_LONG);

        } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {

            Toast.makeText(context, "静态广播接收器设备拔出", Toast.LENGTH_LONG);

        }else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {

            Toast.makeText(context, "设备移除", Toast.LENGTH_LONG);

        }else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {

            Toast.makeText(context, "设备插入", Toast.LENGTH_LONG);
        }


    }
}
