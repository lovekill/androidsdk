package sdk.grayweb.com.slientsdk.http;

import java.io.IOException;



import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import sdk.grayweb.com.slientsdk.api.AbstractApi;


public abstract class TextHttpListener implements IHttpListener,
		OnCancelListener {
	private Context mContext;
	private AbstractApi mAbstractApi;
	private String TAG = getClass().getSimpleName() ;

	public TextHttpListener(Context context) {
		this.mContext = context;
	}

	@Override
	public void onSuccess(byte[] response) {
		try {
			String text = new String(response, "utf-8");
			Log.e("TextHttpListener", text);
			JSONObject jsonObject = new JSONObject(text);
			if (jsonObject.optInt("code")==0) {
				onSuccess(text);
			}else {
				Log.e(TAG,"api fail erorCode="+jsonObject.optInt("code"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			onJsonError(e);
		}
	}

	public abstract void onSuccess(String text);

	public void onJsonError(JSONException e){
		Log.e(TAG,e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onFail(String text) {
		Log.v("TextHttpLister", text);
		if (mContext != null) {
			Toast.makeText(mContext, "加载失败，请检查网络！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return mContext;
	}

	@Override
	public void startRequest(AbstractApi api) {
		this.mAbstractApi = api;
	}

	public void onCancel(DialogInterface dialog) {
		HttpClient.canceRequest(mAbstractApi);
	}
}
