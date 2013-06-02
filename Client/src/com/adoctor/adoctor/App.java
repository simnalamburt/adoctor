/**
 * 
 */
package com.adoctor.adoctor;

import android.app.Application;
import android.content.Context;

/**
 * 어플리케이션 구현 클래스
 * 현재 어플리케이션에 연결된 Context를 Static-way로 취득하기 위해 구현됨
 * @author Hyeon
 */
public class App extends Application {
	private static Context context;

	/**
	 * onCreate() 오버라이딩
	 * 어플리케이션에 연결된 Context를 저장한다.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		App.context = getApplicationContext();
	}
	
	/**
	 * 현재 어플리케이션에 연결되어있는 Context를 반환함
	 * @return
	 */
	public static Context getContext() {
		return App.context;
	}
}
