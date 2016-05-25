package sdk.grayweb.com.slientsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public  void init(){
        Log.e("aaa","start init");
        Intent intent = new Intent(this,GrayService.class);
        startService(intent);
    }
}
