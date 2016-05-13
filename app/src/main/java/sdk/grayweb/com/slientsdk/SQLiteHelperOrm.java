package sdk.grayweb.com.slientsdk;

import android.app.Notification;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

import sdk.grayweb.com.slientsdk.model.ActionModel;
import sdk.grayweb.com.slientsdk.model.ApkModel;
import sdk.grayweb.com.slientsdk.model.DownloadModel;
import sdk.grayweb.com.slientsdk.model.OpenPackage;

/**
 * Created by engine on 16/5/11.
 */
public class SQLiteHelperOrm extends OrmLiteSqliteOpenHelper {
    private static SQLiteHelperOrm instance ;
    public static SQLiteHelperOrm getInstance(Context context){
        if (instance==null){
            instance = new SQLiteHelperOrm(context);
        }
        return instance ;
    }
    private SQLiteHelperOrm(Context context) {
        super(context, DATABASE_PATH, null, 1);
        init(context);
    }

    public static final String DATABASE_PATH = Environment
            .getExternalStorageDirectory() + "/imp.db";
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                SQLiteDatabase.OPEN_READONLY);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion , int newVersion) {
    }
    public void init(Context context){
        File f = new File(SQLiteHelperOrm.DATABASE_PATH);
        if (!f.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                    SQLiteHelperOrm.DATABASE_PATH,null);
            this.onCreate(db);
            db.close();
            try {
                TableUtils.createTable(connectionSource, OpenPackage.class) ;
                TableUtils.createTable(connectionSource, DownloadModel.class);
                TableUtils.createTable(connectionSource, ApkModel.class);
                TableUtils.createTable(connectionSource, ActionModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
