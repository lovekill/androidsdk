package sdk.grayweb.com.slientsdk.http;

import com.liulishuo.filedownloader.BaseDownloadTask;

/**
 * 作者： Mr.wrong on 2016/5/12 17 06
 */
public interface IDownloadListener {
    public  void downloadSuccess(BaseDownloadTask task);
    public void downloadFail(BaseDownloadTask task, String message) ;
}
