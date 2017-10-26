package in.andante.drawbm;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;//SQLite用

public class DrawBm extends Activity {

PenView penview;
	
/*Called when the activity is first created.*/
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	penview= new PenView(this);
	setContentView(penview);
	//setContentView(R.layout.activity_sub);
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
			penview.insert();
			penview.saveToFile();
			penview.clearDrawList(); 
			break;
		case R.id.item3:
			moveTaskToBack(true);
			break;
	}
 
	return true;
}

}//クラスDrawBm終了