package com.adoctor.adoctor.DB;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

/**
 * Column 정보 서술 클래스
 * @author Hyeon
 */
class Column {
	final String Name, Type;
	Column(String name, String type) { Name = name; Type = type; }
}

/**
 * Column[] 생성 Helper 클래스
 * @author Hyeon
 */
class ColumnBuilder
{
	private ArrayList<Column> columns;
	
	ColumnBuilder() { columns = new ArrayList<Column>(); }
	ColumnBuilder(int capacity) { columns = new ArrayList<Column>(capacity); }
	
	ColumnBuilder add(String name, String type) { columns.add(new Column(name, type)); return this; }
	Column[] get() { return columns.toArray(new Column[columns.size()]); }
}

/**
 * Table 추상화
 * @author Hyeon
 */
abstract class Table {
	final String tableName;
	final Column[] columns;
	
	/**
	 * Table 클래스 생성자. 외부 패키지로부터의 인스턴스화는 금지되어있음
	 * @param TableName 생성될 테이블 이름
	 * @param Columns 생성될 테이블의 칼럼 정보
	 */
	Table(String TableName, Column[] Columns)
	{
		this.tableName = TableName;
		this.columns = Columns;
	}
	
	/**
	 * 테이블 최초 생성시 실행되는 메서드
	 * @param db 테이블을 추가할 데이터베이스 객체
	 */
	public void onCreate(SQLiteDatabase db)
	{
		String query = "CREATE TABLE "+tableName+" (";
		boolean isFirst = true;
		for(Column col : columns) {
			if(isFirst) isFirst = false; else query += ',';
			query += col.Name + ' ' + col.Type;
		}
		query += ");";
		
		db.execSQL(query);
	}
}
