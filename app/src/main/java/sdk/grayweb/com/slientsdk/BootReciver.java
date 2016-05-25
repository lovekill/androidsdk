package sdk.grayweb.com.slientsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by engine on 16/5/23.
 */
public class BootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       Intent i = new Intent(context,GrayService.class);
        context.startService(i);
    }
}
