package in.andante.drawbm;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DrawBm extends Activity {

PenView penview;
//DBHelper dbhelper;
DBHelper db0;
public Activity _context;

/*Called when the activity is first created.*/
@Override
public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	penview = new PenView(this);//PenViewクラスのインスタンス生成
	setContentView(penview);
    //Exchange exchange = (Exchange) this.getApplication();
    //String str = exchange.getTestString();
	//dbhelper = new DBHelper(this);
	db0 = new DBHelper(this);  //DBHelperクラスのインスタンス生成
	SQLiteDatabase db1 = db0.getWritableDatabase();//読み書き用
	db1.close();


}

/*メニューの生成イベント*/
@Override
public boolean onCreateOptionsMenu(Menu menu) {//menu->draw_bm.xmlで記述
	
	super.onCreateOptionsMenu(menu);
	getMenuInflater().inflate(R.menu.draw_bm,menu);  
	return true;

}

/*メニューがクリックされた時のイベント*/
@Override
public boolean onOptionsItemSelected(MenuItem item) {

	switch ( item.getItemId() ) {
		case R.id.item1:
			penview.clearDrawList(); 
			break;
		case R.id.item2:
			penview.saveToFile();
			db0.insert();
			penview.clearDrawList(); 
			break;
		case R.id.item3:
			moveTaskToBack(true);
			break;
	}
 
	return true;
}

}