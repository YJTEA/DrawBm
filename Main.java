package in.andante.drawbm;

/*アプリのメイン画面処理*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity{

	/*Called when the activity is first created.*/
	@Override
public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);	
	
	/*avtivity_main.xmlのViewを画面(Activity)に追加*/
	setContentView(R.layout.avtivity_main);
	
	/*.getApplicationでインスタンス生成*/
	final Exchange exchange = (Exchange) this.getApplication();
	
	/*データ(入力されたID)をセット*/
	final EditText editText = (EditText)this.findViewById(R.id.Edit_Text);
	
	final TextView textView = (TextView) findViewById(R.id.text_view);
	
	/*send_buttonのビューのオブジェクトを追加*/
	Button sendButton = (Button) findViewById(R.id.Send_Button);
	
	
	/*ボタンがクリックされた時に呼び出されるコールバックリスナーを登録*/
	sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			//*自動生成されたメソッド・スタブ(ボタンがクリックされた時*/
			public void onClick(View v) {
				
				/*エディットテキストのテキストを取得*/
                String text = editText.getText().toString();
                
                /*値(ID)を読み出し(Exchangeのメソッド)*/
                Exchange.setTestString(text);
                
                /*取得した文字をTextViewにセット*/
                textView.setText(text);
                
                Intent intent = new Intent(getApplication(), DrawBm.class);
                /*次の画面へ*/
				startActivity(intent);
				
			}
	});
	
	
}

}