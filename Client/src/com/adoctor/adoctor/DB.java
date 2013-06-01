/**
 * 
 */
package com.adoctor.adoctor;

import android.provider.BaseColumns;

/**
 * 데이터베이스 추상화
 * @author Hyeon
 *
 */
public class DB {

	/**
	 * @author Hyeon
	 *
	 */
	public static class ScreenLog implements BaseColumns {
		public static final String TABLE_NAME = "ScreenLog";
		
		public static void Insert(long Time, boolean State)
		{
			// TODO : 구현하기(Insert)
		}
		
		public static ScreenLog[] SelectAll()
		{
			// TODO : 구현하기(SelectAll)
			return null;
		}
		
		public static void Flush()
		{
			// TODO : 구현하기(Flush)
		}
		
		public long Time;
		public boolean State;
		
		public ScreenLog(long Time, boolean State)
		{
			this.Time = Time;
			this.State = State;
		}
	}

}
