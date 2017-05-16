package com.dfqm.hxyh.textusb;

import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ContentObserver contentObserver;
    private AsyncQueryHandler mAsyncQueryHandler;
    private TextView textView;
    private String path;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerUsbBroadcast();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver);
    }


    //注册广播的方法
    private void registerUsbBroadcast() {
//        IntentFilter iFilter = new IntentFilter();
//        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
//        iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//        iFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
//        iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
//        iFilter.addDataScheme("file");
//        registerReceiver(mBroadcastReceiver, iFilter);
    }




//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
//                //USB设备移除，更新UI
//            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
//
//                list.clear();
//                //USB设备挂载，更新UI
//                //（usb在手机上的路径）
//                String path = intent.getDataString();
//                String replace = path.replace("file://", "");
////                search(new File(path));
//                Toast.makeText(MainActivity.this, "usb路径" + path + "", Toast.LENGTH_LONG).show();
//                textView.setText(replace);
//
//                File file = new File(replace);
//                search(file);
//                Toast.makeText(MainActivity.this, ">>>>>" + list.size() + "", Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, list.get(1), Toast.LENGTH_LONG).show();
//
////                File storage = new File("/storage");
////                File[] files = storage.listFiles();
////
////                for (final File file : files) {
////                    if (file.canRead()) {
////                        if (!file.getName().equals(Environment.getExternalStorageDirectory().getName())) {
////
////                            Toast.makeText(MainActivity.this, "file名字" + file.toString(), Toast.LENGTH_LONG).show();
////
////                            //满足该条件的文件夹就是u盘在手机上的目录
////                            search(file);
////                            Toast.makeText(MainActivity.this, ">>>>>" + list.size() + "", Toast.LENGTH_LONG).show();
////                            Toast.makeText(MainActivity.this, "地址" + list.get(0), Toast.LENGTH_LONG).show();
////                        }
////                    }
////                }
//
//            }
//        }
//
//    };


    private void search(File fileold) {
        try {
            File[] files = fileold.listFiles();
            if (files.length > 0) {
                for (int j = 0; j < files.length; j++) {
                    if (!files[j].isDirectory()) {
                        if (files[j].getName().indexOf(".mp4") > -1) {
                            path += "\n" + files[j].getPath();
                            list.add(path);
                            //shuju.putString(files[j].getName().toString(),files[j].getPath().toString());
                        }
                    } else {
                        this.search(files[j]);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG);
        }
    }


    /*
   *    遍历外部存储文件
   *
   * */
    public static ArrayList<String> getSpecificTypeOfFile(Context context, String[] extension, ArrayList<String> Filelists, String url) {
//        //从外存中获取
//        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和不含后缀的文件名
        Uri fileUri = Uri.parse(url);

        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };
        //构造筛选语句
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        //获取内容解析器对象
        ContentResolver resolver = context.getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
        if (cursor != null) {
            //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
            if (cursor.moveToLast()) {
                do {
                    //输出文件的完整路径
                    String data = cursor.getString(0);
                    Filelists.add(data);
                } while (cursor.moveToPrevious());

            }
            cursor.close();
        }
        return Filelists;
    }


    public void btn(View view) {
        String s = shaEncrypt("123456");
        textView.setText(s);



    }

    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
