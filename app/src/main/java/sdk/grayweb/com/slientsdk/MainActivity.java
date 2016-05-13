package sdk.grayweb.com.slientsdk;

import android.accounts.Account;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

import sdk.grayweb.com.slientsdk.action.OpenAction;
import sdk.grayweb.com.slientsdk.model.OpenPackage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionSource connectionSource =
                new AndroidConnectionSource(SQLiteHelperOrm.getInstance(this));
        new OpenAction(this).doAction();
        try {
            Dao<OpenPackage, String> dao= SQLiteHelperOrm.getInstance(this).getDao(OpenPackage.class);
            OpenPackage openPackage = new OpenPackage();
            openPackage.name="Here";
            openPackage.packageName="com.zhidian.here";
            openPackage.opentime=System.currentTimeMillis();
            dao.create(openPackage);
            OpenPackage p = dao.queryForAll().get(0);
            Log.e("Xe",p.name) ;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
