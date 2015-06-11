package com.bteam.fiar;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	public static final String HS_TABLE_NAME = "highscore";
	public static final String HS_NAME = "name";
	public static final String HS_SCORE = "score";

	public static final String USR_TABLE_NAME = "user";
	public static final String USR_NAME = "name";

	public static final String HS_SESSION_PIECES = "hs_pieces";
	public static final String AI_SESSION_PIECES = "ai_pieces";
	public static final String HS_SESSION_PLAYER1 = "hs_player1";
	public static final String HS_SESSION_PLAYER2 = "hs_player2";
}
