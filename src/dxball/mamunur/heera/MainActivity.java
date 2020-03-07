package dxball.mamunur.heera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static int levelSelect;
	public static int clearedLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = getSharedPreferences("DXBallData", Context.MODE_PRIVATE);
		clearedLevel = sharedPreferences.getInt("ClearedLevel", 1);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		SharedPreferences sharedPreferences = getSharedPreferences("DXBallData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
    	editor.putInt("ClearedLevel", clearedLevel);
    	editor.commit ();
    	
    	clearedLevel = sharedPreferences.getInt("ClearedLevel", 1);
    	
		super.onResume();
	}

	public void LevelOne(View v) {
		levelSelect = 1;
		Intent intent = new Intent(this, GameCanvas.class);
		startActivity(intent);
	}

	public void LevelTwo(View v) {
		if (clearedLevel >= 2) {
			levelSelect = 2;
			Intent intent = new Intent(this, GameCanvas.class);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"You Haven't Cleared Level 1 Yet.\nPlease Clear That First To Play Level 2", Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void LevelThree(View v) {
		if (clearedLevel >= 3) {
			levelSelect = 3;
			Intent intent = new Intent(this, GameCanvas.class);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"You Haven't Cleared Level 2 Yet.\nPlease Clear That First To Play Level 3", Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void Exit (View v)
	{
		SharedPreferences sharedPreferences = getSharedPreferences("DXBallData", Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
    	editor.putInt("ClearedLevel", clearedLevel);
    	editor.commit ();
		finish ();
	}
	
	@Override
	protected void onDestroy() {
		SharedPreferences sharedPreferences = getSharedPreferences("DXBallData", Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
    	editor.putInt("ClearedLevel", clearedLevel);
    	editor.commit ();
		super.onDestroy();
	}
}
