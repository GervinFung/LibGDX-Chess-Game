package com.mygdx.game.GUI.gui.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GUI.gui.ChessGame;
import com.mygdx.game.GUI.gui.GUI_UTILS;

public final class WelcomeScreen implements Screen {

    private final Stage stage;

    public WelcomeScreen(final ChessGame chessGame) {
        this.stage = new Stage(new FitViewport(GUI_UTILS.WORLD_WIDTH, GUI_UTILS.WORLD_HEIGHT), new SpriteBatch());

        Gdx.input.setInputProcessor(this.stage);
        Gdx.graphics.setTitle("LibGDX Simple Parallel Chess 2.0");

        final Table table = new Table(GUI_UTILS.UI_SKIN);

        final int WIDTH = 200;

        table.add("Welcome to LibGDX Simple Parallel Chess 2.0").padBottom(20).row();
        table.add(new Image(GUI_UTILS.LOGO)).padBottom(20).row();
        table.add(this.startGameButton(chessGame)).width(WIDTH).padBottom(20).row();
        table.add(this.loadGameButton(chessGame)).width(WIDTH).padBottom(20).row();
        table.add(this.aboutButton(chessGame)).width(WIDTH).padBottom(20).row();
        table.add(this.exitGameButton()).width(WIDTH).padBottom(20);

        table.setFillParent(true);

        this.stage.addActor(table);
    }

    public Stage getStage() { return this.stage; }

    private TextButton startGameButton(final ChessGame chessGame) {
        final TextButton textButton = new TextButton("Start Game", GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                chessGame.gotoGameScreen(GameScreen.BOARD_STATE.NEW_GAME, GameScreen.BOARD_STATE.NEW_GAME.getBoard(chessGame.getGameScreen()));
            }
        });
        return textButton;
    }

    private TextButton exitGameButton() {
        final TextButton textButton = new TextButton("Exit Game", GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        return textButton;
    }

    private TextButton aboutButton(final ChessGame chessGame) {
        final TextButton textButton = new TextButton("About Game", GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.input.setInputProcessor(chessGame.getAboutScreen().getStage());
                chessGame.setScreen(chessGame.getAboutScreen());
            }
        });
        return textButton;
    }

    private TextButton loadGameButton(final ChessGame chessGame) {
        final TextButton textButton = new TextButton("Load Game", GUI_UTILS.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                try {
                    chessGame.gotoGameScreen(GameScreen.BOARD_STATE.LOAD_GAME, GameScreen.BOARD_STATE.LOAD_GAME.getBoard(chessGame.getGameScreen()));
                } catch (final RuntimeException e) {
                    final Label label = new Label("No game to load", GUI_UTILS.UI_SKIN);
                    label.setColor(Color.BLACK);
                    new Dialog("Load Game", GUI_UTILS.UI_SKIN).text(label).button("Ok").show(stage);
                }
            }
        });
        return textButton;
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.getBatch().begin();
        this.stage.getBatch().draw(GUI_UTILS.BACKGROUND, 0, 0);

        this.stage.getBatch().end();
        this.stage.draw();
    }

    @Override
    public void resize (final int width, final int height) { this.stage.getViewport().update(width, height, true); }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.stage.getBatch().dispose();
    }

    @Deprecated
    public void show() {}
    @Deprecated
    public void pause() {}
    @Deprecated
    public void resume() {}
    @Deprecated
    public void hide() {}

}