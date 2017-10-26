package in.andante.drawbm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.lang.Math;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PenView extends View {
	private float oldx = 0f;//書き始めx座標
	private float oldy = 0f;//書き始めy座標
	private float nextx = 0f;//途中x座標
	private float nexty = 0f;//途中y座標
	private float eventDuration2 = 0f;//タッチ時間
	private float pressure;//筆圧
	/*private double distance;//2点間距離
	private double radian;//2点間角度（ラジアン）
	private double ra;//2点間角度（度）*/
	private Bitmap bmp = null;
	private Canvas bmpCanvas;
	private Paint paint;
	private Paint paint1;
	public Activity _context;
	public List<Pos> posList;//Posクラスの座標データを扱うリスト
 
public PenView(Context context) {//コンストラクタ
	
	super(context);
	_context = (Activity)context;
	
	//画面描写
	paint = new Paint();
	paint.setColor(Color.BLACK);
	paint.setAntiAlias(true);
	paint.setStyle(Paint.Style.STROKE);
	paint.setStrokeWidth(20);
	paint.setStrokeCap(Paint.Cap.ROUND);
	paint.setStrokeJoin(Paint.Join.ROUND);
	//文字描写
	paint1 = new Paint();//メモリ消費するかも
	paint1.setAntiAlias(true);
	paint1.setTextSize(40);
	paint1.setColor(Color.BLACK);
	
	this.posList = new ArrayList<Pos>();//ArrayList<型> 変数名 = new ArrayList<型>()　型はクラス
	
}
 
protected void onSizeChanged(int w, int h, int oldw, int oldh) {//Bitmap
	
	super.onSizeChanged(w,h,oldw,oldh);
	bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
	bmpCanvas = new Canvas(bmp);
	bmpCanvas.drawColor(0xFFFFFFFF);//Black

}
 
public void onDraw(Canvas canvas) {//Canvas
	
	canvas.drawBitmap(bmp, 0, 0, null);//描写
	canvas.drawText("Pressure:" + pressure, 0, 40, paint1);
	canvas.drawText("X座標:" + nextx, 0, 80, paint1);
	canvas.drawText("Y座標:" + nexty, 0, 120, paint1);

}

////////////////////////動作ごと//////////////////////////////////////////
@Override
public boolean performClick(){//指を離した場合
    
	super.performClick();
    return true;

}

public boolean onTouchEvent(MotionEvent e){//指が触れた場合

	switch(e.getAction()){
		case MotionEvent.ACTION_DOWN: //最初のポイント
			pressure = e.getPressure();//筆圧
			oldx = e.getX();
			oldy = e.getY();
			nextx = e.getX();
			nexty = e.getY();
			posList.add(new Pos(e.getX(), e.getY(),pressure));//poslistへ座標および筆圧を追加
			break;
		case MotionEvent.ACTION_MOVE: //途中のポイント
			pressure = e.getPressure();
			bmpCanvas.drawLine(nextx, nexty, e.getX(), e.getY(), paint);//最初のポイントから次のポイントまで線を引く
			posList.add(new Pos(e.getX(), e.getY(),pressure));
			nextx = e.getX();//初期値へ代入
			nexty = e.getY();
			invalidate();//再描写（onDrawを呼び出す）
			break;
		case MotionEvent.ACTION_UP://最後のポイント
			pressure = e.getPressure();
            break;
		case MotionEvent.ACTION_CANCEL://異常終了時（ACTION_UPと同処理）
			pressure = e.getPressure();
		default:
			break;
	}
	return true;
	
}
 
public void clearDrawList(){//クリア処理
	
	bmpCanvas.drawColor(Color.WHITE);
	invalidate();
	this.posList.clear();//PosListクリア
	pressure=0;
	/*eventDuration2=0;
	distance=0;
	radian=0;
	ra=0;*/

}

public void insert(){//データベースにデータ挿入
	
	ContentValues values = new ContentValues();//テーブルに含まれるカラムをキーとし、カラムに対して設定したい値をペアとして保存する
	DBHelper db0 = new DBHelper(_context);
	SQLiteDatabase db1 = db0.getWritableDatabase();//読み書き用
	long id = 0;
	
    try{
    	for(Pos p : this.posList){//posにデータ追加
    		values.put("X", p.X);
            values.put("Y", p.Y);
            values.put("z_pressure",p.pressure);
		}
    	db1.insert("account", null, values);
    }
    finally{
        db1.close();
    }
    
    //成功or失敗メッセージ
    if (id == -1) {  
        Toast.makeText(_context, "Insert失敗", Toast.LENGTH_SHORT).show();  
    } else {   
        Toast.makeText(_context, "Insert成功", Toast.LENGTH_SHORT).show();  
    }     
    
}
 
public void saveToFile(){
	
	//認識されない場合
	if(!sdcardWriteReady()){
		Toast.makeText(_context, "SDcardが認識されません。", Toast.LENGTH_SHORT).show();
		return;
	}
	//認識された場合
	try{
		
		File file0 = new File(Environment.getExternalStorageDirectory().getPath()+"/DrawBmData/");//画像用
		
		try{
			if(!file0.exists()){//ファイルが存在しなければ作成
				file0.mkdir();
			}
		}catch(SecurityException e){}
 	
		// 日付でファイル名を作成　
		Date mDate = new Date();
		SimpleDateFormat fileName = new SimpleDateFormat("yyyy_MM_dd'at'HH_mm_ss",Locale.US);//US時刻
	 
		FileOutputStream fos0 = new FileOutputStream(new File(file0, fileName.format(mDate) + ".jpg"));//画像用
		
		FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath()+"/DrawBmCSV/"+fileName.format(mDate)+".csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
		
		bmp.compress(CompressFormat.JPEG, 100, fos0);
		
		for(Pos p : this.posList){//posへ追加
			pw.print(p.X);
			pw.print(",");
			pw.print(p.Y);
			pw.print(",");
			pw.print(p.pressure);
			pw.print("\n");
		}
		
		fos0.flush();
		fos0.close();
		pw.close();
		
		Toast.makeText(_context, "Saved!", Toast.LENGTH_SHORT).show();
		
	} catch(Exception e) {
		Toast.makeText(_context, "Error!", Toast.LENGTH_SHORT).show();
		
	}
	
}

private boolean sdcardWriteReady(){
	
	 String state = Environment.getExternalStorageState();
	 return (Environment.MEDIA_MOUNTED.equals(state));

}

}