package cn.sin.news;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent; 
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about);

        new Handler().postDelayed(
        	new Runnable(){
			public void run() {
				//建立跳转意图
				Intent intent=new Intent(WelcomeActivity.this,MainAvtivity.class);
				startActivity(intent);
				finish();
				//淡入淡出
				overridePendingTransition(R.anim.zoomin,R.anim.zoomout); 
				}
			}, 
			200);
       
    }
}