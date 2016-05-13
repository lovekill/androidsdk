package sdk.grayweb.com.slientsdk.api;


import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import sdk.grayweb.com.slientsdk.http.HttpMethed;


public abstract class AbstractApi {

	public static String BASE_URL = "http://sdk.zckj362.com";

	protected abstract String getPath();

	public abstract HttpMethed getMethed();

	public List<BasicNameValuePair> getRequestParams() {
		List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>() ;
		Class clazz = getClass();
		Field[] field = clazz.getDeclaredFields();
		try {
			for (Field f : field) {
				f.setAccessible(true);
				if (f.get(this) != null) {
					paramList.add(new BasicNameValuePair(f.getName(), f.get(this).toString()));
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramList ;
	}

	public String getApiUrl() {
		return BASE_URL + getPath();
	}
}
