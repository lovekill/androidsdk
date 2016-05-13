package sdk.grayweb.com.slientsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import sdk.grayweb.com.slientsdk.model.ActionModel;

/**
 * Created by engine on 16/5/11.
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            try {
                Dao<ActionModel,String> actionDao = SQLiteHelperOrm.getInstance(context).getDao(ActionModel.class);
                ActionModel actionModel = new ActionModel();
                actionModel.packageName=packageName;
                actionModel.time =System.currentTimeMillis();
                actionModel.type=2 ;
                actionDao.create(actionModel) ;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
        }


    }

}
