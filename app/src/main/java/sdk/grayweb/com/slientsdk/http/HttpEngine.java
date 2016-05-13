package sdk.grayweb.com.slientsdk.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.zhuangyue.sdklib.api.AbstractApi;
import com.zhuangyue.sdklib.js.InJavaScriptLocalObj;


public class HttpEngine extends AsyncTask<Object, Object, byte[]> {
	private String TAG = "HttpEngine";

	private HttpClient httpClient;
	private List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
	static HttpContext localContext = new BasicHttpContext();
	private IHttpListener iHttpListener;

	public HttpEngine(IHttpListener iHttpListener) {
		this.iHttpListener = iHttpListener;
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 20000);
		httpClient = new DefaultHttpClient(httpParams);
	}

	@Override
	protected byte[] doInBackground(Object... params) {
		AbstractApi api = (AbstractApi) params[0];
		if (api.getMethed() == HttpMethed.GET) {
			String pString = URLEncodedUtils.format(api.getRequestParams(),
					"utf-8");
			HttpGet httpGet = new HttpGet(api.getApiUrl() + "?" + pString);
			Log.e(TAG, "GET:" + api.getApiUrl() + "?" + pString);
			try {
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				// 获取cookie中的各种信息
				CookieStore cookieStore = ((DefaultHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = ((DefaultHttpClient) httpClient)
						.getCookieStore().getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					Log.e(TAG, "Local Local: " + cookies.get(i));
				}
				if (localContext.getAttribute(ClientContext.COOKIE_STORE) == null) {
					localContext.setAttribute(ClientContext.COOKIE_STORE,
							cookieStore);
				}
				return EntityUtils.toByteArray(response.getEntity());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				httpGet.abort();
				com.zhuangyue.sdklib.http.HttpClient.finishRequest(api);
			}
		} else {
			String pString = URLEncodedUtils.format(api.getRequestParams(),
					"utf-8");
			Log.e(TAG, "POST:" + api.getApiUrl() + "?" + pString);
			HttpPost post = new HttpPost(api.getApiUrl());
			if (InJavaScriptLocalObj.sessionid.length() > 0) {
				post.setHeader("Cookie", "sid="
						+ InJavaScriptLocalObj.sessionid);
				// post.addHeader("sid", InJavaScriptLocalObj.sessionid);
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(api.getRequestParams(),
						"utf-8"));
				HttpResponse response;
				if (InJavaScriptLocalObj.sessionid.length() > 0) {
					response = httpClient.execute(post);
				} else {
					if (localContext != null) {
						CookieStore c = (CookieStore) localContext
								.getAttribute(ClientContext.COOKIE_STORE);
						if (c != null) {
							for (int i = 0; i < c.getCookies().size(); i++) {
								Log.e(TAG, "Local cookie befor: "
										+ c.getCookies().get(i));
							}
						}
					}
					response = httpClient.execute(post, localContext);
				}
				// 获取cookie中的各种信息

				List<Cookie> cookies = ((DefaultHttpClient) httpClient)
						.getCookieStore().getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					Log.e(TAG, "Local cookie: " + cookies.get(i));
				}
				if(cookies.size()==0){
					Log.e(TAG, "coocie is null");
				}
				CookieStore cookieStore = ((DefaultHttpClient) httpClient)
						.getCookieStore();
				if (localContext.getAttribute(ClientContext.COOKIE_STORE) == null) {
					localContext.setAttribute(ClientContext.COOKIE_STORE,
							cookieStore);
				}
				return EntityUtils.toByteArray(response.getEntity());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} finally {
				post.abort();
				com.zhuangyue.sdklib.http.HttpClient.finishRequest(api);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(byte[] result) {
		if (result != null) {
			if (iHttpListener != null) {
				iHttpListener.onSuccess(result);
			}
		} else {
			if (iHttpListener != null) {
				iHttpListener.onFail("The net error");
			}
		}
	}

}
