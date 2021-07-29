package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.gui.ChessGame;
import com.mygdx.game.gui.GuiUtils;
import com.mygdx.game.chess.engine.FEN.FenUtilities;

public final class DesktopLauncher {

	private final ChessGame chessGame;

	private DesktopLauncher() { this.chessGame = new ChessGame(); }

	public static void main (final String[] arg) { new DesktopLauncher().createGame(); }

	private void createGame() { new Lwjgl3Application(this.chessGame, this.generateConfiguration()); }

	private Lwjgl3ApplicationConfiguration generateConfiguration() {
		final Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setWindowedMode(1200, 640);
		configuration.setWindowIcon(Files.FileType.Internal, "chess_logo.png");
		//avoid overheat issues just in case
		configuration.setIdleFPS(15);
		configuration.setForegroundFPS(60);

		configuration.setWindowListener(new Lwjgl3WindowAdapter() {
			@Override
			public boolean closeRequested() {
				DesktopLauncher.this.showExitDialog();
				return false;
			}
		});

		return configuration;
	}

	private void showExitDialog() {
		final Label label;
		final String dialogTitle = "Exit Game Confirmation";
		if (this.chessGame.getScreen().equals(this.chessGame.getGameScreen())) {
			DesktopLauncher.this.chessGame.getGameScreen().getGameTimerPanel().continueTimer(false);
			label = new Label("Request confirmation to exit game and save current one", GuiUtils.UI_SKIN);
			label.setColor(Color.BLACK);
			new Dialog(dialogTitle, GuiUtils.UI_SKIN) {
				@Override
				protected void result(final Object object) {
					this.remove();
					if (object == null) {
						DesktopLauncher.this.chessGame.getGameScreen().getGameTimerPanel().continueTimer(true);
						return;
					}
					if ((Boolean) object) {
						GuiUtils.MOVE_LOG_PREF.putString(GuiUtils.MOVE_LOG_STATE, FenUtilities.getGameData(
								DesktopLauncher.this.chessGame.getGameScreen().getMoveHistory().getMoveLog(),
								DesktopLauncher.this.chessGame.getGameScreen().getChessBoard()));
						GuiUtils.MOVE_LOG_PREF.flush();
					}
					Gdx.app.exit();
				}
			}.button("Yes", true)
					.button("No", false)
					.button("Cancel")
					.text(label)
					.show(this.chessGame.getGameScreen().getStage());
			return;

		} else if (this.chessGame.getScreen().equals(this.chessGame.getAboutScreen()) || this.chessGame.getScreen().equals(this.chessGame.getWelcomeScreen())) {
			label = new Label("Request confirmation to exit game", GuiUtils.UI_SKIN);
			label.setColor(Color.BLACK);
			new Dialog(dialogTitle, GuiUtils.UI_SKIN) {
				@Override
				protected void result(final Object object) {
					this.remove();
					if ((Boolean) object) { Gdx.app.exit(); }
				}
			}.button("Yes", true)
					.button("No", false)
					.text(label)
					.show((this.chessGame.getScreen().equals(this.chessGame.getAboutScreen()) ? this.chessGame.getAboutScreen().getStage() : this.chessGame.getWelcomeScreen().getStage()));
			return;
		}
		throw new IllegalStateException("Should not reach here as there are ONLY 3 GAME SCREENS");
	}
}