/**
 * Original algorithm for this AI was obtained from:
 * http://cboard.cprogramming.com/game-programming/53886-connect-four-ai-demo.html
 * 
 * Revised by us to suit our needs in this application.
 */
package com.bteam.fiar;

public class AI {

	int DEFAULT = -1;

	int maxMoveScore = 100;
	int notValid = 101;
	int playerOne;
	int playerTwo;

	AI(int p1, int p2) {
		playerOne = p1;
		playerTwo = p2;
	}

	int[] moveScore = new int[7];

	int[][] move(int[][] board, int col, int player) {
		int[][] new_board = board;

		for (int i = 5; i >= 0; --i) {
			if (new_board[col][i] == DEFAULT) {
				new_board[col][i] = player;
				break;
			}
		}

		return new_board;
	}

	int[][] undo(int[][] board, int col, int player) {
		int[][] new_board = board;

		int y = 0;
		while (y < 6 && new_board[col][y] == DEFAULT)
			++y;
		if (new_board[col][y] == player)
			new_board[col][y] = DEFAULT;

		return new_board;
	}

	int getScore(int[][] board) {
		int score = 0;

		for (int x = 0; x < 7; x++) {
			int cscore = 3 - x;
			if (cscore < 0)
				cscore = -cscore;
			cscore = 3 - cscore;

			for (int y = 0; y < 6; y++) {
				int rscore = 3 - y;
				if (rscore < 0)
					rscore = -rscore;
				rscore = 3 - rscore;

				if (board[x][y] == playerOne) {
					score += cscore + rscore;
				} else if (board[x][y] == playerTwo) {
					score -= cscore + rscore;
				}
			}
		}

		return score;
	}

	static int is_won(int[][] b, int x, int y) {
		int player = b[x][y];
		int x1, x2, y1, y2;

		// horizontal
		x1 = x2 = x;
		// right from last played
		while (x1 < 7 && b[x1][y] == player)
			++x1;
		// right from last played
		while (x2 >= 0 && b[x2][y] == player)
			--x2;
		if (x1 - x2 > 4)
			return player;

		// vertical
		y1 = y2 = y;
		// up from last played
		while (y1 < 6 && b[x][y1] == player)
			++y1;
		// down from last played
		while (y2 >= 0 && b[x][y2] == player)
			--y2;
		if (y1 - y2 > 4)
			return player;

		// diagonal \
		x1 = x2 = x;
		y1 = y2 = y;
		// down and right from last played
		while (y1 >= 0 && x1 < 7 && b[x1][y1] == player) {
			++x1;
			--y1;
		}
		// up and left from last played
		while (y2 < 6 && x2 >= 0 && b[x2][y2] == player) {
			--x2;
			++y2;
		}
		if (x1 - x2 > 4)
			return player;

		// diagonal /
		x1 = x2 = x;
		y1 = y2 = y;
		// up and right from last played
		while (y1 < 6 && x1 < 7 && b[x1][y1] == player) {
			++y1;
			++x1;
		}
		// down and left form last played
		while (y2 >= 0 && x2 >= 0 && b[x2][y2] == player) {
			--y2;
			--x2;
		}
		if (x1 - x2 > 4)
			return player;

		return -1;
	}

	int getLastY(int[] col) {
		int y = 0;
		while (y < 6 && col[y] == DEFAULT)
			++y;
		return y;
	}

	int determineScore(int[][] board, int currentPlayer, int lastX, int numRecursion, int alpha, int beta) {
		int lastY = getLastY(board[lastX]);
		int winner = is_won(board, lastX, lastY);
		if (winner == playerOne)
			return maxMoveScore / numRecursion;
		else if (winner == playerTwo)
			return -maxMoveScore / numRecursion;

		if (numRecursion >= 4)
			return getScore(board);

		if (currentPlayer == playerOne) {
			// player one wants Max score
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					int score = determineScore(board, playerTwo, m, numRecursion + 1, alpha, beta);
					board = undo(board, m, currentPlayer);
					if (score > alpha)
						alpha = score;
					if (alpha >= beta)
						return alpha;
				}
			}
			return alpha;
		} else if (currentPlayer == playerTwo) {
			// player two wants min score
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					int score = determineScore(board, playerOne, m, numRecursion + 1, alpha, beta);
					board = undo(board, m, currentPlayer);
					if (score < beta)
						beta = score;
					if (alpha >= beta)
						return beta;
				}
			}
			return beta;
		}
		return 0;
	}

	int minmax(int[][] board, int currentPlayer, int numRecursion, int lastX) {
		int lastY = getLastY(board[lastX]);
		int winner = is_won(board, lastX, lastY);
		if (winner == playerOne)
			return maxMoveScore / numRecursion;
		else if (winner == playerTwo)
			return -maxMoveScore / numRecursion;

		if (numRecursion >= 3)
			return 0;

		if (currentPlayer == playerOne) {
			// player one wants Max score
			int maxScore = -notValid;
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					int score = minmax(board, playerTwo, numRecursion + 1, m);
					board = undo(board, m, currentPlayer);
					if (score > maxScore) {
						maxScore = score;
					}
				}
			}
			return maxScore;
		} else if (currentPlayer == playerTwo) {
			// player two wants min score
			int minScore = notValid;
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					int score = minmax(board, playerOne, numRecursion + 1, m);
					board = undo(board, m, currentPlayer);
					if (score < minScore)
						minScore = score;
				}
			}
			return minScore;
		}
		return 0;
	}

	int determineMove(int[][] board, int currentPlayer) {

		if (currentPlayer == playerOne) {
			// look at all moves and get the "best" one
			int maxScore = -notValid;
			int bestMove = 0;
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					// int score = minmax(board, playerTwo, 0, m);
					int score = determineScore(board, playerTwo, m, 1, -notValid, notValid);
					moveScore[m] = score;

					if (score >= maxScore) {
						maxScore = score;
						bestMove = m;
					}

					board = undo(board, m, currentPlayer);
				} else
					moveScore[m] = notValid;
			}
			return bestMove;
		} else if (currentPlayer == playerTwo) {
			int minScore = notValid;
			int minMove = 0;
			for (int m = 0; m < 7; ++m) {
				if (board[m][0] == DEFAULT) {
					board = move(board, m, currentPlayer);
					// int score = minmax(board, playerOne, 0, m );
					int score = determineScore(board, playerOne, m, 1, -notValid, notValid);
					moveScore[m] = score;

					if (score < minScore) {
						minScore = score;
						minMove = m;
					}

					board = undo(board, m, currentPlayer);
				} else
					moveScore[m] = notValid;
			}
			return minMove;
		} else
			return 0;
	}
}