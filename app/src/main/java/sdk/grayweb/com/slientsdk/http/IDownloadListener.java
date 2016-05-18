package sdk.grayweb.com.slientsdk.http;

import com.liulishuo.filedownloader.BaseDownloadTask;

import java.io.File;

/**
 * 作者： Mr.wrong on 2016/5/12 17 06
 */
public interface IDownloadListener {
public  void downloadSuccess(String path, String url);
public void downloadFail(String url, String message) ;
        }
