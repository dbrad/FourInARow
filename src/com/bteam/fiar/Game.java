package com.bteam.fiar;

import static com.bteam.fiar.Constants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity implements OnClickListener {

	private class Player {
		public int color;
		public String name;
		public boolean isbot;

		public Player(String n, int c) {
			isbot = false;
			name = n;
			color = c;
		}
	}

	private GameBoard board;
	private FIARData data;

	static boolean HOTSEAT;
	Player playerOne;
	Player playerTwo;

	Player currentPlayer;

	Button btnNewGame;
	Button btnGiveUp;
	Button btnMenu;
	Button btnEnd;

	AI ai;

	TextView txtName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		data = new FIARData(this.getApplicationContext());

		board = new GameBoard(this);
		((LinearLayout) findViewById(R.id.boardLayout)).addView(board, 0);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			HOTSEAT = extras.getBoolean("hotseat");
			playerOne = new Player(extras.getString("player1"), Color.RED);
			playerTwo = new Player(extras.getString("player2"), Color.BLACK);
		}

		currentPlayer = playerOne;

		ai = new AI(playerOne.color, playerTwo.color);

		btnNewGame = (Button) findViewById(R.id.buttonNewGame);
		btnNewGame.setOnClickListener(this);

		btnGiveUp = (Button) findViewById(R.id.buttonGiveUp);
		btnGiveUp.setOnClickListener(this);

		btnMenu = (Button) findViewById(R.id.buttonMenu);
		btnMenu.setOnClickListener(this);

		btnEnd = (Button) findViewById(R.id.buttonEnd);
		btnEnd.setOnClickListener(this);

		txtName = (TextView) findViewById(R.id.txtCurPlyr);
		txtName.setText(currentPlayer.name);

		if (!HOTSEAT) {
			playerTwo.name = "BOT";
			playerTwo.isbot = true;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		String tmp = "";
		if (HOTSEAT)
			tmp = getSharedPreferences("FIAR", MODE_PRIVATE).getString(HS_SESSION_PIECES, "");
		else
			tmp = getSharedPreferences("FIAR", MODE_PRIVATE).getString(AI_SESSION_PIECES, "");
		if (!tmp.equals(""))
			board.deStringify(tmp);
		else
			board.newGame();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!board.is_disabled())
			saveBoard();
	}

	private void addHighScore() {
		SQLiteDatabase db = data.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(HS_NAME, currentPlayer.name);
		values.put(HS_SCORE, board.won_in(currentPlayer.color));
		db.insertOrThrow(HS_TABLE_NAME, null, values);
		db.close();
	}

	private void switchPlayer() {
		currentPlayer = (currentPlayer == playerOne) ? playerTwo : playerOne;
		txtName.setText(currentPlayer.name);
	}

	private void checkWin() {
		if (board.is_won() == currentPlayer.color) {
			if (!currentPlayer.isbot)
				addHighScore();
			String msg = currentPlayer.name + " won in " + board.won_in(currentPlayer.color) + "!";
			declareEnd(msg);
		} else if (board.is_tie())
			declareEnd("Game was a tie!");
	}

	private void declareEnd(String msg) {
		btnNewGame.setEnabled(true);
		btnGiveUp.setEnabled(false);
		board.disable();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void saveBoard() {
		String game = board.stringify();
		if (HOTSEAT)
			getSharedPreferences("FIAR", MODE_PRIVATE).edit().putString(HS_SESSION_PIECES, game).commit();
		else
			getSharedPreferences("FIAR", MODE_PRIVATE).edit().putString(AI_SESSION_PIECES, game).commit();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (board.doTurn(event.getX(), currentPlayer.color)) {
				board.invalidate();
				// check for a win
				checkWin();
				// switch current player
				switchPlayer();
				if (!HOTSEAT) {
					int col = ai.determineMove(board.pieces, currentPlayer.color);
					board.doTurn(col, currentPlayer.color);
					checkWin();
					switchPlayer();
				}
				// redraw
				board.invalidate();
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonMenu:
			if (!board.is_disabled())
				saveBoard();
			else {
				if (HOTSEAT)
					getSharedPreferences("FIAR", MODE_PRIVATE).edit().remove(HS_SESSION_PIECES).commit();
				else
					getSharedPreferences("FIAR", MODE_PRIVATE).edit().remove(AI_SESSION_PIECES).commit();
			}
			finish();
			break;
		case R.id.buttonGiveUp:
			Player winner = (currentPlayer == playerOne) ? playerTwo : playerOne;
			String msg = winner.name + " won in " + board.won_in(winner.color) + "!";
			declareEnd(msg);
			break;
		case R.id.buttonNewGame:
			board.newGame();
			btnNewGame.setEnabled(false);
			btnGiveUp.setEnabled(true);
			board.invalidate();
			break;
		case R.id.buttonEnd:
			getSharedPreferences("FIAR", MODE_PRIVATE).edit().clear().commit();
			board.disable();
			finish();
			break;
		}
	}
}
