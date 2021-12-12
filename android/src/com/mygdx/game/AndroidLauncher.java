package com.mygdx.game;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.gui.ChessGame;
import com.mygdx.game.gui.GuiUtils;

public final class AndroidLauncher extends AndroidApplication {

	private final ChessGame chessGame;

	public AndroidLauncher() { this.chessGame = new ChessGame(); }

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		this.initialize(this.chessGame, config);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onBackPressed() {
		if (this.chessGame.getScreen().equals(this.chessGame.getGameScreen())) {
			this.chessGame.getGameScreen().getGameTimerPanel().continueTimer(false);
			new AlertDialog.Builder(this)
					.setTitle("Exit Game")
					.setMessage("Request confirmation to exit game and save the current one")
					.setPositiveButton("yes", (dialog, which) -> {
						GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(chessGame.getGameScreen().getMoveHistory().getMoveLog(), chessGame.getGameScreen().getChessBoard()));
						GuiUtils.MOVE_LOG_PREF.flush();
						chessGame.getGameScreen().getGameTimerPanel().continueTimer(true);
						this.finishAffinity();
						System.exit(0);
						startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
					}).setNegativeButton("no", (dialog, which) -> {
				chessGame.getGameScreen().getGameTimerPanel().continueTimer(true);
				startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
			}).setNeutralButton("cancel", (dialog, which) -> chessGame.getGameScreen().getGameTimerPanel().continueTimer(true))
					.show();
			return;
		} else if (this.chessGame.getScreen().equals(this.chessGame.getAboutScreen()) || this.chessGame.getScreen().equals(this.chessGame.getWelcomeScreen())) {
			new AlertDialog.Builder(this)
					.setTitle("Exit Game")
					.setMessage("Request confirmation to exit game")
					.setPositiveButton("yes", (dialog, which) -> {
						this.finishAffinity();
						System.exit(0);
						startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
					}).setNegativeButton("no", null).show();
			return;
		}
		throw new IllegalStateException("Should not reach here as there are ONLY 3 GAME SCREENS");
	}
}
