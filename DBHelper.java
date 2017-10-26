package in.andante.drawbm;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import in.andante.drawbm.PenView;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME ="DrawBmDBv.db";
	Context mContext;
	public Activity _context;

	public DBHelper( Context context ){//コンストラクタ
		
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
		
	}
	
@Override
public void onCreate(SQLiteDatabase db) {//データベースがない場合に作成される
	
		String DB_ITEM = "Create table DB_ITEM ("   
                  + "Xpoint"
                  + "Ypoint"  
                  + "Pressure)";  
		
		db.beginTransaction();//トランザクション処理開始
		
		try{
	        // テーブル作成を実行
	        db.execSQL(DB_ITEM);
	        db.setTransactionSuccessful();//成功
		} 
		catch(Exception e){// 例外発生
			e.printStackTrace();
		}
		finally {
	        db.endTransaction();//トランザクション処理終了
		}
		
}
	
/* 
SQL部分実行 
*/  
private void buttonRowQuery(){  
        
        //rawQueryメソッドでデータを取得  
        DBHelper db2 = new DBHelper(_context);//Activityのcontext
        SQLiteDatabase db3 = db2.getReadableDatabase();//読み取り用オブジェクト作成
        StringBuilder text = new StringBuilder();//StringBuilder与えられたデータを文字列に変換  
        
        try{  
        	//SQL文全回収
        	String sql    = "SELECT * FROM DB_ITEM";
        	            
        	//SQL文の実行
            Cursor cursor = db3.rawQuery(sql.toString(), null);   
      
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
            db3.close();//DB閉じる
        }  
        
    }  
	
	@Override
	//データベースのバージョンが変更の場合に呼び出される
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS test_table");
        onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}