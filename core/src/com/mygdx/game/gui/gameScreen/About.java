package com.mygdx.game.gui.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.gui.ChessGame;
import com.mygdx.game.gui.GuiUtils;

public final class About implements Screen {

    private final Stage stage;

    public About(final ChessGame chessGame) {
        this.stage = new Stage(new FitViewport(GuiUtils.WORLD_WIDTH, GuiUtils.WORLD_HEIGHT), new SpriteBatch());
        Gdx.input.setInputProcessor(this.stage);
        final Table table = new Table(GuiUtils.UI_SKIN);
        table.add("About Game").padBottom(20).row();
        table.add(new Image(GuiUtils.LOGO)).padBottom(20).row();
        table.add(this.aboutText()).padBottom(20).row();
        table.add(this.backButton(chessGame)).padBottom(20);
        table.setFillParent(true);
        this.stage.addActor(table);
    }

    public Stage getStage() {
        return this.stage;
    }

    private TextButton backButton(final ChessGame chessGame) {
        final TextButton textButton = new TextButton("Back to Menu", GuiUtils.UI_SKIN);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                Gdx.input.setInputProcessor(chessGame.getWelcomeScreen().getStage());
                chessGame.setScreen(chessGame.getWelcomeScreen());
            }
        });
        return textButton;
    }

    private String aboutText() {
        return "This lightweight application is about a simple chess game that implemented MiniMax AI concept\n\n" +
                "AlphaBeta-pruning, Pawn Structure Analysis and Move Ordering which maximize the search time of MiniMax.\n\n" +
                "In this game, you can choose to play against yourself or your friend or an AI, range from Level 1 to Level 10\n\n" +
                "1. Start a new game with different timer, board color, flip board.\n\n2. Save a game\n\n3. Load a saved game\n\n4. Export game in FEN format.\n\n5. Import game in FEN format.\n\n6. Undo moves";
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act(delta);
        this.stage.getBatch().begin();
        this.stage.getBatch().draw(GuiUtils.BACKGROUND, 0, 0);
        this.stage.getBatch().end();
        this.stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.stage.getBatch().dispose();
    }

    @Deprecated
    public void show() {
    }

    @Deprecated
    public void pause() {
    }

    @Deprecated
    public void resume() {
    }

    @Deprecated
    public void hide() {
    }

}