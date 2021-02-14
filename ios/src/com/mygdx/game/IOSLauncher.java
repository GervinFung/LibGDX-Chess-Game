package com.mygdx.game;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public final class IOSLauncher extends IOSApplication.Delegate {

    @Override
    protected IOSApplication createApplication() { return new IOSApplication(new MyGdxGame(), new IOSApplicationConfiguration()); }

    public static void main(final String[] argv) {
        final NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}