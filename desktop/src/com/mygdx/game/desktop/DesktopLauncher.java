package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public final class DesktopLauncher {

	public static void main (final String[] arg) {
		final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.width = 1200;
		configuration.height = 640;
		configuration.addIcon("chess_logo.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), configuration);
	}
}