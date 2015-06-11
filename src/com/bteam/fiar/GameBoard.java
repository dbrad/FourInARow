/**
 * class: GameBoard
 * desc: Object representation and renderer of the 4 in a Row game board.
 */
package com.bteam.fiar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.View;

public class GameBoard extends View {

	final static int DEFAULT = -1;
	int[][] pieces;
	Pair<Integer, Integer> lastPlayed;
	private boolean disabled = false;

	public GameBoard(Context context) {
		super(context);
		pieces = new int[7][6];
		newGame();
	}

	public void newGame() {
		for (int i = 0; i < 7; ++i) {
			for (int j = 0; j < 6; ++j) {
				pieces[i][j] = DEFAULT;
			}
		}
		disabled = false;
	}

	public void disable() {
		disabled = true;
	}

	public boolean is_disabled() {
		return disabled;
	}

	public String stringify() {
		String sboard = "";

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				sboard += pieces[i][j];
				if (j < 5)
					sboard += "|";
			}
			if (i < 6)
				sboard += "$";
		}

		return sboard;
	}

	public void deStringify(String sboard) {
		String[] cols = sboard.split("[$]");
		for (int i = 0; i < 7; i++) {
			String[] cells = cols[i].split("[|]");
			for (int j = 0; j < 6; j++) {
				pieces[i][j] = Integer.parseInt(cells[j]);
			}
		}
	}

	public boolean doTurn(float x, int c) {
		boolean result = false;
		if (!disabled) {
			for (int i = 1; i <= 7; i++) {
				if (x < i * getWidth() / 7.0) {
					result = placePiece(i - 1, c);
					break;
				}
			}
		}
		return result;
	}

	public boolean doTurn(int col, int c) {
		boolean result = false;
		if (!disabled) {
			result = placePiece(col, c);
		}
		return result;
	}

	private boolean placePiece(int column, int c) {
		for (int i = 5; i >= 0; i--) {
			if (pieces[column][i] == DEFAULT) {
				pieces[column][i] = c;
				lastPlayed = Pair.create(column, i);
				return true;
			}
		}
		return false;
	}

	public int is_won() {
		int x = lastPlayed.first;
		int y = lastPlayed.second;
		int player = pieces[x][y];
		int x1, x2, y1, y2;

		// horizontal
		x1 = x2 = x;
		// right from last played
		while (x1 < 7 && pieces[x1][y] == player)
			++x1;
		// right from last played
		while (x2 >= 0 && pieces[x2][y] == player)
			--x2;
		if (x1 - x2 > 4)
			return player;

		// vertical
		y1 = y2 = y;
		// up from last played
		while (y1 < 6 && pieces[x][y1] == player)
			++y1;
		// down from last played
		while (y2 >= 0 && pieces[x][y2] == player)
			--y2;
		if (y1 - y2 > 4)
			return player;

		// diagonal \
		x1 = x2 = x;
		y1 = y2 = y;
		// down and right from last played
		while (y1 >= 0 && x1 < 7 && pieces[x1][y1] == player) {
			++x1;
			--y1;
		}
		// up and left from last played
		while (y2 < 6 && x2 >= 0 && pieces[x2][y2] == player) {
			--x2;
			++y2;
		}
		if (x1 - x2 > 4)
			return player;

		// diagonal /
		x1 = x2 = x;
		y1 = y2 = y;
		// up and right from last played
		while (y1 < 6 && x1 < 7 && pieces[x1][y1] == player) {
			++y1;
			++x1;
		}
		// down and left form last played
		while (y2 >= 0 && x2 >= 0 && pieces[x2][y2] == player) {
			--y2;
			--x2;
		}
		if (x1 - x2 > 4)
			return player;

		return -1;
	}

	public int won_in(int c) {
		int count = 0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				if (pieces[i][j] == c)
					++count;
			}
		}
		return count;
	}

	public boolean is_tie() {
		for (int i = 0; i < 7; ++i) {
			if (pieces[i][0] == DEFAULT)
				return false;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.YELLOW);
		canvas.drawPaint(paint);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		for (int i = 0; i < 6; i++) {
			canvas.drawLine(0, i * getHeight() / 6.0f, getWidth(), i * getHeight() / 6.0f, paint);
		}

		for (int i = 0; i < 7; i++) {
			canvas.drawLine(i * getWidth() / 7.0f, 0, i * getWidth() / 7.0f, getHeight(), paint);
		}

		for (int fx = 1; fx < 15; fx += 2) {
			for (int fy = 1; fy < 13; fy += 2) {
				paint.setColor(Color.BLACK);
				canvas.drawCircle(fx * getWidth() / 14.0f, fy * getHeight() / 12.0f, (getWidth() / 7.0f) * 0.28f, paint);
				if (pieces[(fx - 1) / 2][(fy - 1) / 2] != DEFAULT)
					paint.setColor(pieces[(fx - 1) / 2][(fy - 1) / 2]);
				else
					paint.setColor(Color.WHITE);
				canvas.drawCircle(fx * getWidth() / 14.0f, fy * getHeight() / 12.0f, (getWidth() / 7.0f) * 0.25f, paint);
			}
		}

	}

}
