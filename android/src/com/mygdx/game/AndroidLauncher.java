package com.mygdx.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GUI.board.GUI_UTILS;
import com.mygdx.game.chess.engine.FEN.FenUtilities;

public final class AndroidLauncher extends AndroidApplication {

	private final MyGdxGame myGdxGame;

	public AndroidLauncher() { myGdxGame = new MyGdxGame(); }

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		this.initialize(this.myGdxGame, config);
	}

	@Override
	public void onBackPressed() {
		this.myGdxGame.getGameTimerPanel().continueTimer(false);
		new AlertDialog.Builder(this)
				.setTitle("Exit Game")
				.setMessage("Request confirmation to exit game and save the current one")
				.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						GUI_UTILS.MOVE_LOG_PREF.putString(GUI_UTILS.MOVE_LOG_STATE, FenUtilities.getGameData(myGdxGame.getMoveHistory().getMoveLog(), myGdxGame.getChessBoard()));
						GUI_UTILS.MOVE_LOG_PREF.flush();
						myGdxGame.getGameTimerPanel().continueTimer(true);
						startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
					}
				}).setNegativeButton("no", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						myGdxGame.getGameTimerPanel().continueTimer(true);
						startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
					}
				}).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						myGdxGame.getGameTimerPanel().continueTimer(true);
					}
				})
				.show();
	}
}
