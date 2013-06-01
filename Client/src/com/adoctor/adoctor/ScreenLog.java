/**
 * 
 */
package com.adoctor.adoctor;

import android.provider.BaseColumns;

/**
 * ScreenLog 테이블 추상화
 * @author Hyeon
 */
public class ScreenLog implements BaseColumns {
	public static final String TABLE_NAME = "ScreenLog";
	public static final String COLUMN_NAME_TIME = "Time";
	public static final String COLUMN_NAME_STATE = "State";
}
