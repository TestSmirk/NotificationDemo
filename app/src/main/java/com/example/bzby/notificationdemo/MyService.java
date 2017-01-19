package com.example.bzby.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class MyService extends Service {
    //Service 的生命周期是 https://developer.android.com/guide/components/services.html?hl=zh-cn
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");

    }

    //首先启动通知后,这时通知栏已经有三个按钮 播放 前进后退.
    public void startNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification build = builder.setSmallIcon(R.mipmap.ic_launcher)
                //settitle text 都可以不写
                .setContentTitle("this is title")
                .setContentText("this is text")
                .setCustomContentView(getBigRemoteView())
                .build();
        NotificationManager systemService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        systemService.notify(0, build);

    }

    private RemoteViews getBigRemoteView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_remote_view);
        //这个intent很重要.它相当于点击后执行了onStartCommand方法.并在intent中存入了是哪个action,也可以putInt
        Intent intent = new Intent(this, MyService.class);
        intent.setAction("action1");
        remoteViews.setOnClickPendingIntent(R.id.notification_play, PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return remoteViews;
    }

    private static final String TAG = "MyService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    ///这个是通过内部类获取这个Service对象,然后就可以在Activity中操作.
    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}
