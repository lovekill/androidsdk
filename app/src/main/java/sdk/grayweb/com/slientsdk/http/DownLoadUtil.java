package sdk.grayweb.com.slientsdk.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.security.MessageDigest;


/**
 * 作者： Mr.wrong on 2016/5/12 17 19
 */
public class DownLoadUtil {
    private Context context;
    private String url;
    public DownLoadUtil(Context context, String url){
        this.context=context;
        this.url=url;
    }
    private IDownloadListener listener;
    private NetWorkBroadCast networkBroadcast;
    private BaseDownloadTask downloadTask;
    private int downLoadId;

    public void setIDownloadListener(IDownloadListener listener){
        this.listener=listener;
    }
    public  void   downLoadApp(){
        //首先创建广播
        networkBroadcast = new NetWorkBroadCast();
        //注册广播
     //   registerNetworkReceiver();

        final String md5Name = getMd5(url);
        //没带后缀
        final String appStartPath= Environment.getExternalStorageDirectory() + "/" +"UserInfo"+ "/" +md5Name+"."+"apk";
       /*带后缀*/
        final String appEndPath= Environment.getExternalStorageDirectory() + "/" +"UserInfo"+"/"+md5Name + "." + "apk";
        //先把旧文件删除
        final File oldFile = new File(appStartPath);
        if(oldFile.exists()){
            //如果是没下载完成的包，就把包进行删除
            oldFile.delete();
        }
        //开始下载时包的名字   ， 下载完成后再修改为另外一个名字
        String path= appStartPath;
        /*请求下载*/     /*把id共享*/ //FileDownloader.getImpl().pause(id);可以暂停下载
         downloadTask = FileDownloader.getImpl().create(url).setPath(path).setListener(new FileDownloadSampleListener() {
             @Override
             protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                 super.progress(task, soFarBytes, totalBytes);

             }

             @Override
             protected void error(BaseDownloadTask task, Throwable e) {
                 super.error(task, e);
                 listener.downloadFail(task, e.getMessage());
                 //当出现错误 把旧的包删除
//              if (oldFile.exists()) {
//                  //如果是没下载完成的包，就把包进行删除
//                  oldFile.delete();
//              }
                 System.out.println("hahahhaha");

             }

             @Override
             protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                 super.paused(task, soFarBytes, totalBytes);

             }

             @Override
             protected void completed(BaseDownloadTask task) {
                 super.completed(task);
                 listener.downloadSuccess(task);

                 //完成后把名字修改
//              File f = new File(appStartPath);
//              boolean b = f.renameTo(new File(appEndPath));
//              if (b) {
//                  //修改成功 打开app 静默安装 todo
//
//
//              } else {
//                  //失败的话,把无效包删除
//                  f.delete();
//              }
                 //包的名称： MD5后的包的名称   包的路径   getEndFileName（md5Name）
             }

             @Override
             protected void warn(BaseDownloadTask task) {
                 super.warn(task);
             }
         });

        //标记下载的任务
        downLoadId = downloadTask.start();
    //    Toast.makeText(context,"haha",Toast.LENGTH_LONG).show();

    }




  private String  getFilePath(String newUrl){
        final String md5Name1 = getMd5(newUrl);
        final String path= Environment.getExternalStorageDirectory() + "/" +"UserInfo"+"/"+md5Name1 + "." + "apk";
        final File file = new File(path);
        if(file.exists()){
            return  path;
        }else {
            return null;
        }
    }

    class NetWorkBroadCast extends BroadcastReceiver {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null&& NetworkInfo.State.CONNECTED == wifiState) {
                //  context.startService(intent2);
                //有wifi的情况
         //          downLoadApp();
                //开始下载
            }else{
                //没wifi的情况   暂停下载
                FileDownloader.getImpl().pause(downLoadId);

            }
        }

    }


    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkBroadcast, filter);
    }



    private void unRegisterNetworkReceiver() {
        context.unregisterReceiver(networkBroadcast);
    }
    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static String getMd5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

}
