package sdk.grayweb.com.slientsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import sdk.grayweb.com.slientsdk.action.InitAction;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(this,GrayService.class);
//        startService(intent);
        setContentView(R.layout.activity_main);
        new InitAction(this).doAction();
//        ConnectionSource connectionSource =
//                new AndroidConnectionSource(SQLiteHelperOrm.getInstance(this));
//        new OpenAction(this).doAction();
//        try {
//            Dao<OpenPackage, String> dao= SQLiteHelperOrm.getInstance(this).getDao(OpenPackage.class);
//            OpenPackage openPackage = new OpenPackage();
//            openPackage.name="Here";
//            openPackage.packageName="com.zhidian.here";
//            openPackage.opentime=System.currentTimeMillis();
//            dao.create(openPackage);
//            OpenPackage p = dao.queryForAll().get(0);
//            Log.e("Xe",p.name) ;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
