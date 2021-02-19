package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public final class DesktopLauncher {

	public static void main (final String[] arg) {
		final Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setWindowedMode(1200, 640);
		configuration.setWindowIcon(Files.FileType.Internal, "chess_logo.png");
		new Lwjgl3Application(new MyGdxGame(), configuration);
	}
}