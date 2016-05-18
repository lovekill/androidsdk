package sdk.grayweb.com.slientsdk;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by engine on 16/5/17.
 */
public class GrayWebApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getApplicationContext());
    }
}
