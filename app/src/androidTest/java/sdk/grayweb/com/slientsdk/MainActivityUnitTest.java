package sdk.grayweb.com.slientsdk;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;


import java.util.List;

import sdk.grayweb.com.slientsdk.action.DownloadAction;
import sdk.grayweb.com.slientsdk.action.InitAction;
import sdk.grayweb.com.slientsdk.action.InstallAction;
import sdk.grayweb.com.slientsdk.model.ApkModel;

/**
 * Created by engine on 16/5/17.
 */
public class MainActivityUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity activity ;
    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity() ;
    }


    @SmallTest
    public void testInit(){
       InitAction action = new InitAction(activity);
        action.doAction();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ApkModel> list = action.getAllApkModel();
        assertTrue(list!=null&&list.size()>0);
    }

    @SmallTest
    public void testDownloadAction(){
        DownloadAction action = new DownloadAction(activity) ;
        assertTrue(action.doAction()>0);
    }
    @SmallTest
    public void testInstallAction(){
       new InstallAction(activity).doAction();
    }
}
