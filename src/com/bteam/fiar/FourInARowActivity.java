package com.bteam.fiar;

import static com.bteam.fiar.Constants.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FourInARowActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button hotSeat = (Button) findViewById(R.id.buttonHot);
		hotSeat.setOnClickListener(this);

		Button aiGame = (Button) findViewById(R.id.buttonAI);
		aiGame.setOnClickListener(this);

		Button about = (Button) findViewById(R.id.buttonAbout);
		about.setOnClickListener(this);

		Button score = (Button) findViewById(R.id.buttonScore);
		score.setOnClickListener(this);
		
		Button exit = (Button) findViewById(R.id.buttonExit);
		exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonHot:
			Intent ih;
			String plyr1 = getSharedPreferences("FIAR", MODE_PRIVATE).getString(HS_SESSION_PLAYER1, "");
			String plyr2 = getSharedPreferences("FIAR", MODE_PRIVATE).getString(HS_SESSION_PLAYER2, "");
			if (plyr1.equals("") || plyr2.equals("")) {
				ih = new Intent(this, SelectPlayer.class);
			} else {
				ih = new Intent(this, Game.class);
				ih.putExtra("player1", plyr1);
				ih.putExtra("player2", plyr2);
				String pieces = getSharedPreferences("FIAR", MODE_PRIVATE).getString(HS_SESSION_PIECES, "");
				if (!pieces.equals("")) {
					ih.putExtra("board", pieces);
				}
			}
			ih.putExtra("hotseat", true);
			startActivity(ih);
			break;
		case R.id.buttonAI:
			Intent iai = new Intent(this, Game.class);
			String plyr = getSharedPreferences("FIAR", MODE_PRIVATE).getString(HS_SESSION_PLAYER1, "");
			if (plyr.equals("")) {
				iai = new Intent(this, SelectPlayer.class);
			} else {
				iai = new Intent(this, Game.class);
				iai.putExtra("player1", plyr);
				String pieces = getSharedPreferences("FIAR", MODE_PRIVATE).getString(AI_SESSION_PIECES, "");
				if (!pieces.equals("")) {
					iai.putExtra("board", pieces);
				}
			}
			iai.putExtra("hotseat", false);
			startActivity(iai);
			break;
		case R.id.buttonAbout:
			Intent ia = new Intent(this, About.class);
			startActivity(ia);
			break;
		case R.id.buttonScore:
			Intent is = new Intent(this, Score.class);
			startActivity(is);
			break;
		case R.id.buttonExit:
			finish();
			break;
		}
	}
}