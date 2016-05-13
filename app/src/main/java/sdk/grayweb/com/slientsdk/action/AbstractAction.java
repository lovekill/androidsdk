package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sdk.grayweb.com.slientsdk.SQLiteHelperOrm;
import sdk.grayweb.com.slientsdk.model.ActionModel;
import sdk.grayweb.com.slientsdk.model.ApkModel;
import sdk.grayweb.com.slientsdk.model.InstallApk;
import sdk.grayweb.com.slientsdk.model.OpenPackage;

/**
 * Created by engine on 16/5/11.
 */
public abstract class AbstractAction implements IAction {
    protected Context mContext;
    protected Dao<OpenPackage, String> openPackageDao;
    protected Dao<ApkModel, String> apkModelDao;
    protected Dao<ActionModel, String> actionDao;

    public AbstractAction(Context context) {
        this.mContext = context;
        ConnectionSource connectionSource =
                new AndroidConnectionSource(SQLiteHelperOrm.getInstance(context));
        try {
            openPackageDao = SQLiteHelperOrm.getInstance(context).getDao(OpenPackage.class);
            apkModelDao = SQLiteHelperOrm.getInstance(context).getDao(ApkModel.class);
            actionDao= SQLiteHelperOrm.getInstance(context).getDao(ActionModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected List<InstallApk> installedApks() {
        List<InstallApk> list = new ArrayList<InstallApk>();
        List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packages) {
            InstallApk apk = new InstallApk();
            apk.name = p.applicationInfo.loadLabel(
                    mContext.getPackageManager()).toString();
            apk.packageName=p.packageName ;
            list.add(apk);
        }
        return list;
    }
    protected boolean hasInstall(String packagename){
        List<InstallApk> installApks = installedApks();
        for (InstallApk apk:installApks){
            if (apk.packageName.equals(packagename)){
                return true ;
            }
        }
        return false;
    }
}
