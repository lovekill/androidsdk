package sdk.grayweb.com.slientsdk.api;

import android.content.Context;

import sdk.grayweb.com.slientsdk.http.HttpMethed;
import sdk.grayweb.com.slientsdk.util.DivesUtil;

/**
 * Created by engine on 16/5/12.
 */
public class ActionApi extends AbstractApi {
    public String imei ;
    public String packageName ;
    public int type ;
    public ActionApi(Context context,String packageName,Action action){
        this.imei= DivesUtil.getInstance(context).getDeviceCode();
        switch (action){
            case DOWNLOAD:
                type=1 ;
                break;
            case INSTALL:
                type = 2 ;
                break;
            case OPEN:
                type=3 ;
                break;
        }
        this.packageName = packageName ;
    }
    @Override
    protected String getPath() {
        return "action";
    }

    @Override
    public HttpMethed getMethed() {
        return HttpMethed.POST;
    }
}
