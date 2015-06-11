/**
 * class: Score
 * desc: Highscore dialog to display the best user scores.
 */
package com.bteam.fiar;

import static com.bteam.fiar.Constants.*;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Score extends Activity implements OnClickListener {
	private static FIARData data;

	String[] COLUMNS = { HS_NAME, HS_SCORE };
	String ORDER_BY = HS_SCORE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);
		data = new FIARData(this.getApplicationContext());

		Button btnClose = (Button) findViewById(R.id.buttonCloseScore);
		btnClose.setOnClickListener(this);

		final TableLayout tl = (TableLayout) findViewById(R.id.tableScore);

		SQLiteDatabase db = data.getReadableDatabase();
		Cursor cursor = db.query(HS_TABLE_NAME, COLUMNS, null, null, null, null, ORDER_BY);
		startManagingCursor(cursor);

		while (cursor.moveToNext()) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			TextView t1 = new TextView(this);

			t1.setText(cursor.getString(0));
			t1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TableRow.LayoutParams margin = (TableRow.LayoutParams) t1.getLayoutParams();
			margin.setMargins(10, 5, 10, 0);
			tr.addView(t1, margin);

			TextView t2 = new TextView(this);

			t2.setText(String.valueOf(cursor.getInt(1)));
			t2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			margin = (TableRow.LayoutParams) t2.getLayoutParams();
			margin.setMargins(10, 5, 10, 0);

			tr.addView(t2, margin);
			tl.addView(tr);
		}
		db.close();
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
