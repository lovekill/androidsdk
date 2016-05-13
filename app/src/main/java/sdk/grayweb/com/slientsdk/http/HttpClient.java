package sdk.grayweb.com.slientsdk.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import sdk.grayweb.com.slientsdk.api.AbstractApi;

public class HttpClient {
	private static Map<String, HttpEngine> requestMap = new HashMap<String, HttpEngine>();

	public static void request(AbstractApi api, IHttpListener listener) {
		if (api.getApiUrl().startsWith("http:")) {
			if (listener != null && listener.getContext() != null) {
				if (detect(listener.getContext())) {
					listener.startRequest(api);
					HttpEngine engine = new HttpEngine(listener);
					engine.execute(api);
					requestMap.put(api.getApiUrl(), engine);
				} else {
					listener.onFail("当前网络不可用");
				}
			} else {
				new HttpEngine(listener).execute(api);
			}
		} else {
			return;
		}
	}

	public static boolean detect(Context act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	public static void canceRequest(AbstractApi api) {
		HttpEngine engine = requestMap.get(api.getApiUrl());
		if (engine != null) {
			boolean b = engine.cancel(true);
			requestMap.remove(api.getApiUrl());
		}
	}

	public static void finishRequest(AbstractApi api) {
		requestMap.remove(api.getApiUrl());
	}
}
