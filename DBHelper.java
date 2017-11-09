package in.andante.drawbm;

/*SQLite部分*/

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import in.andante.drawbm.PenView;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DBHelper";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME ="DrawBmDBv.db";
	public Activity _context;
	public List<Pos> posList;//Posクラスの座標データを扱うリスト
	PenView penview;

	public DBHelper( Context context ){//コンストラクタ
		
		super(context, DB_NAME, null, DB_VERSION);
		_context = (Activity)context;
		penview = new PenView(_context);
		penview.posList = new ArrayList<Pos>();
		
	}
	
	/*SQLiteへデータ追加*/
	public void insert(){
		
		SQLiteDatabase db1 = super.getWritableDatabase();//読み書き用
		ContentValues values = new ContentValues();//テーブルに含まれるカラムをキーとし、カラムに対して設定したい値をペアとして保存する
		
	    try{
	    	/*データ追加部分*/
	    	for(Pos p : penview.posList){
	    		values.put("X", p.X);
	            values.put("Y", p.Y);
	            values.put("Z_pressure",p.pressure);
	            db1.insert("account", null, values);
			}
	    }
	    finally{
	        db1.close();
	        Toast.makeText(_context, "Insert成功", Toast.LENGTH_SHORT).show();  
	    }
	    
	  /*  long id = 0;
	    //成功or失敗メッセージ
	    if (id == -1) {  
	        Toast.makeText(_context, "Insert失敗", Toast.LENGTH_SHORT).show();  
	    } else {   
	        Toast.makeText(_context, "Insert成功", Toast.LENGTH_SHORT).show();  
	    }   */  
	    
	}
	
	
@Override
public void onCreate(SQLiteDatabase db) {
	
		db.beginTransaction();//トランザクション処理開始
		
		/*テーブル作成?*/
		try{
			db.execSQL(
					"CREATE TABLE PersonalData(" +
					"   xpoint INTEGER," +
					"   ypoint INTEGER," +
					"   zpressure INTEGER" +
					")"
			);
	        db.setTransactionSuccessful();//成功
	        Log.i(TAG,"テーブルが作成されました");
		} 
		catch(Exception e){//例外発生
			e.printStackTrace();
		}
		finally {
	        db.endTransaction();//トランザクション処理終了
		}
		
}
	
/*SQL取り出し部分
private void buttonRowQuery(){  
        
        //rawQueryメソッドでデータを取得  
		SQLiteDatabase db2 = super.getReadableDatabase();//読み取り用オブジェクト作成
        StringBuilder text = new StringBuilder();//StringBuilder与えられたデータを文字列に変換  
        
        try{  
        	//SQL文全回収
        	String sql    = "SELECT * FROM DB_ITEM";
        	            
        	//SQL文の実行
            Cursor cursor = db2.rawQuery(sql.toString(), null);   
      
            //カーソル開始位置を先頭にする
            cursor.moveToFirst();
             
            //（.moveToFirstの部分はとばして）for文を回す
            for (int i = 1; i <= cursor.getCount(); i++) {
                //SQL文の結果から、必要な値を取り出す（x,y,pressureの3点を区切って取り出す）
            	  text.append(cursor.getString(0)).append(",");
            	  text.append(cursor.getString(1)).append(",");
                  text.append(cursor.getInt(2)).append("\n");
                  cursor.moveToNext();//行替え
            }
            cursor.close();//SQL閉じる 
        }finally{  
            db2.close();//DB閉じる
        }  
        
    }  */  
	
@Override
/*データベースのバージョンが変更の場合に呼び出される*/
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS test_table");
    onCreate(db);
}
	
public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	onUpgrade(db, oldVersion, newVersion);
}

}