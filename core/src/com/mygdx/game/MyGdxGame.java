package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.mygdx.game.board.GameBoard;
import com.mygdx.game.gameMenu.GameMenu;
import com.mygdx.game.moveHistory.MoveHistory;
import com.mygdx.game.timer.TimerPanel;

public final class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Board chessBoard;

	private GameBoard gameBoard;
	private GameBoard.DisplayOnlyBoard displayOnlyBoard;
	private MoveHistory moveHistory;
	private TimerPanel gameTimerPanel;
	private GameMenu.AIButton aiButton;

	//setter
	public void updateChessBoard(final Board board) { this.chessBoard = board; }

	//getter
	public Board getChessBoard() { return this.chessBoard; }
	public GameBoard getGameBoard() { return this.gameBoard; }
	public GameBoard.DisplayOnlyBoard getDisplayOnlyBoard() { return this.displayOnlyBoard; }
	public Stage getStage() { return this.stage; }
	public MoveHistory getMoveHistory() { return this.moveHistory; }
	public TimerPanel getGameTimerPanel() { return this.gameTimerPanel; }
	public GameMenu.AIButton getAiButton() { return this.aiButton; }

	@Override
	public void create () {
		this.stage = new Stage(new FitViewport(1200, 640), new SpriteBatch());

		Gdx.input.setInputProcessor(stage);
		Gdx.graphics.setTitle("Simple Chess 2.0");

		final VerticalGroup verticalGroup = new VerticalGroup();

		final HorizontalGroup horizontalGroup = new HorizontalGroup();

		horizontalGroup.addActor(this.initGameInformation());
		horizontalGroup.addActor(this.initGameBoard());
		horizontalGroup.addActor(this.initTimer());

		verticalGroup.setFillParent(true);
		verticalGroup.addActor(this.initGameMenu());
		verticalGroup.addActor(horizontalGroup);

		this.stage.addActor(verticalGroup);
	}

	private Stack initGameBoard() {
		final Stack stack = new Stack();

		this.chessBoard = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
		this.gameBoard = new GameBoard(this);
		this.displayOnlyBoard = new GameBoard.DisplayOnlyBoard();

		stack.add(this.displayOnlyBoard);
		stack.add(this.gameBoard);

		return stack;
	}

	private Table initTimer() {
		this.gameTimerPanel = new TimerPanel();
		return this.gameTimerPanel;
	}

	private Table initGameInformation() {
		this.moveHistory = new MoveHistory();
		return this.moveHistory;
	}

	private Table initGameMenu() {
		final Table table = new Table();

		table.add(new GameMenu.NewGameButton(this)).width(150);
		table.add(new GameMenu.SaveGameButton(this)).width(150);
		table.add(new GameMenu.LoadGameButton(this)).width(150);

		table.add(new GameMenu.FlipBoardButton(this)).width(150);
		table.add(new GameMenu.BoardColorButton(this)).width(150);
		this.aiButton = new GameMenu.AIButton(this);
		table.add(this.aiButton).width(150);
		table.add(new GameMenu.UndoButton(this)).width(150);
		table.add(new GameMenu.ExitGameButton(this)).width(150);

		return table;
	}

	@Override
	public void resize (final int width, final int height) { this.stage.getViewport().update(width, height, true); }

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
		if (this.gameTimerPanel.isTimerContinue()) {
			this.gameTimerPanel.update(this);
		}
		this.stage.draw();
	}

	@Override
	public void dispose() {
		this.stage.dispose();
		GUI_UTILS.UTILS.dispose();
		this.stage.getBatch().dispose();
	}
}