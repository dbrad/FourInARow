package com.bteam.fiar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		final Button close = (Button) findViewById(R.id.closeAbout);
		close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
