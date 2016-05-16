package sdk.grayweb.com.slientsdk.api;

import sdk.grayweb.com.slientsdk.http.HttpMethed;

/**
 * Created by engine on 16/5/12.
 */
public class InitApi extends AbstractApi{
    public String imei ;
    public String imsi ;
    public String installApp ;
    public String phoneName ;
    public int chanelid  ;
    @Override
    protected String getPath() {
        return "init";
    }

    @Override
    public HttpMethed getMethed() {
        return HttpMethed.POST;
    }
}
