package com.mygdx.game;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GUI.board.ChessGame;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.chess.engine.FEN.FenUtilities;

public final class AndroidLauncher extends AndroidApplication {

	private final ChessGame chessGame;

	public AndroidLauncher() { chessGame = new ChessGame(); }

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		this.initialize(this.chessGame, config);
	}

	@Override
	public void onBackPressed() {
		if (Gdx.input.getInputProcessor().equals(this.chessGame.getGameScreen().getStage())) {
			this.chessGame.getGameScreen().getGameTimerPanel().continueTimer(false);
			new AlertDialog.Builder(this)
					.setTitle("Exit Game")
					.setMessage("Request confirmation to exit game and save the current one")
					.setPositiveButton("yes", (dialog, which) -> {
						GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(chessGame.getGameScreen().getMoveHistory().getMoveLog(), chessGame.getGameScreen().getChessBoard()));
						GUI_UTILS.MOVE_LOG_PREF.flush();
						chessGame.getGameScreen().getGameTimerPanel().continueTimer(true);
						startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
					}).setNegativeButton("no", (dialog, which) -> {
				chessGame.getGameScreen().getGameTimerPanel().continueTimer(true);
				startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
			}).setNeutralButton("cancel", (dialog, which) -> chessGame.getGameScreen().getGameTimerPanel().continueTimer(true))
					.show();
		}
	}
}
