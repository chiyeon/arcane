package com.kata.arcane.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kata.arcane.ArcaneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = ArcaneGame.WIDTH;
		config.height = ArcaneGame.HEIGHT;

		new LwjglApplication(new ArcaneGame(), config);
	}
}
