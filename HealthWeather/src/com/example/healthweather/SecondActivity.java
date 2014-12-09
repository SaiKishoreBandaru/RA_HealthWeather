package com.example.healthweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SecondActivity extends Activity implements OnClickListener{
	Button hspButton , phButton , proffButton;
	String emotion="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	

		setContentView(R.layout.genre);

		hspButton = (Button)findViewById(R.id.hspt_lay);
		phButton =(Button)findViewById(R.id.ph_lay);
	

		hspButton.setOnClickListener(this);
		phButton.setOnClickListener(this);
		

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//SecondActivity m=new SecondActivity();
	
		switch (v.getId()) {
		case R.id.hspt_lay:

			Intent intent =new Intent(SecondActivity.this, MapNewActivity.class);
			intent.putExtra("PLACE_POSIT",0);
			startActivity(intent);
			break;
		case R.id.ph_lay:

			Intent intent1 =new Intent(SecondActivity.this, MapNewActivity.class);
			intent1.putExtra("PLACE_POSIT",1);

			startActivity(intent1);
			break;
		
		default:
			break;
		}
	

	
	
	}
	}