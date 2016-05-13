package sdk.grayweb.com.slientsdk.http;



import android.content.Context;

import sdk.grayweb.com.slientsdk.api.AbstractApi;


public interface IHttpListener {
	public Context getContext() ;
	public void  onSuccess(byte[] response) ;
	public void  onFail(String text) ;
	public void startRequest(AbstractApi api) ;
}
