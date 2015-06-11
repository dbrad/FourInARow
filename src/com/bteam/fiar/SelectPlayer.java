package com.bteam.fiar;

import static com.bteam.fiar.Constants.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SelectPlayer extends Activity implements OnClickListener {
	private static FIARData data;
	String[] COLUMNS = { USR_NAME };

	boolean HOTSEAT = false;

	Spinner spinP1;
	Spinner spinP2;
	EditText txtP1;
	EditText txtP2;
	Button btnNewP1;
	Button btnNewP2;
	Button btnBack;
	Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		data = new FIARData(this.getApplicationContext());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			HOTSEAT = extras.getBoolean("hotseat");
		}

		spinP1 = (Spinner) findViewById(R.id.spinnerP1);
		spinP2 = (Spinner) findViewById(R.id.spinnerP2);
		txtP1 = (EditText) findViewById(R.id.txtP1);
		txtP2 = (EditText) findViewById(R.id.txtP2);
		btnNewP1 = (Button) findViewById(R.id.buttonNewP1);
		btnNewP2 = (Button) findViewById(R.id.buttonNewP2);
		btnStart = (Button) findViewById(R.id.buttonPlyrStart);
		btnBack = (Button) findViewById(R.id.buttonPlyrBack);

		btnNewP1.setOnClickListener(this);
		btnNewP2.setOnClickListener(this);
		btnStart.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		loadPlayers(spinP1);
		if (HOTSEAT) {
			loadPlayers(spinP2);
		} else {
			spinP2.setEnabled(false);
			spinP2.setVisibility(View.INVISIBLE);
			txtP2.setEnabled(false);
			txtP2.setVisibility(View.INVISIBLE);
			btnNewP2.setEnabled(false);
			btnNewP2.setVisibility(View.INVISIBLE);
		}
	}

	private void loadPlayers(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);

		SQLiteDatabase db = data.getReadableDatabase();
		Cursor cursor = db.query(USR_TABLE_NAME, COLUMNS, null, null, null, null, null);
		startManagingCursor(cursor);
		if (cursor.getCount() <= 0)
			adapter.add("Player");
		while (cursor.moveToNext()) {
			adapter.add(cursor.getString(0));
		}
		db.close();

		spinner.setAdapter(adapter);
	}

	private void addPlayer(EditText txt) {
		SQLiteDatabase db = data.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USR_NAME, txt.getText().toString());
		db.insertOrThrow(USR_TABLE_NAME, null, values);
		db.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonNewP1:
			txtP1.setEnabled(true);
			spinP1.setEnabled(false);
			break;
		case R.id.buttonNewP2:
			txtP2.setEnabled(true);
			spinP2.setEnabled(false);
			break;
		case R.id.buttonPlyrStart:
			Intent ig = new Intent(this, Game.class);
			String p1Name = "";
			if (txtP1.isEnabled()) {
				addPlayer(txtP1);
				p1Name = txtP1.getText().toString();
			} else {
				p1Name = spinP1.getSelectedItem().toString();
			}
			getSharedPreferences("FIAR", MODE_PRIVATE).edit().putString(HS_SESSION_PLAYER1, p1Name).commit();
			ig.putExtra("player1", p1Name);

			if (HOTSEAT) {
				String p2Name = "";
				if (txtP2.isEnabled()) {
					addPlayer(txtP2);
					p2Name = txtP2.getText().toString();
				} else {
					p2Name = spinP2.getSelectedItem().toString();
				}
				getSharedPreferences("FIAR", MODE_PRIVATE).edit().putString(HS_SESSION_PLAYER2, p2Name).commit();
				ig.putExtra("player2", p2Name);
			}

			ig.putExtra("hotseat", HOTSEAT);
			startActivity(ig);
			finish();
			break;
		case R.id.buttonPlyrBack:
			finish();
			break;
		}

	}
}
