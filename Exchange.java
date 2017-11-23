package in.andante.drawbm;

import android.app.Application;

public class Exchange extends Application {

    private static String testString = "default";
    
    @Override
    public void onCreate() {
    }
 
    /*入力された値(ID)を返す*/
    public String getTestString() {
        return testString;
    }
 
    /*入力された値(ID)を読み出し*/
    public static void setTestString(String str) {
        testString = str;
    }

}
