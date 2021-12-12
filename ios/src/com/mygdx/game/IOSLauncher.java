package com.mygdx.game;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mygdx.game.gui.ChessGame;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public final class IOSLauncher extends IOSApplication.Delegate {

    private final ChessGame chessGame;

    public IOSLauncher() {
        this.chessGame = new ChessGame();
    }

    public static void main(final String[] argv) {
        final NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        return new IOSApplication(this.chessGame, new IOSApplicationConfiguration());
    }
}