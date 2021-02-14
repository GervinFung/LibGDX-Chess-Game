package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chess.engine.board.MoveLog;
import com.mygdx.game.MyGdxGame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class DesktopLauncher {

	public static void main (final String[] arg) {
		final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.width = 1200;
		configuration.height = 640;
		configuration.addIcon("chess_logo.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), configuration);
	}
}