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
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.GUI.board.board.GameBoard;
import com.mygdx.game.GUI.board.gameMenu.GameMenu;
import com.mygdx.game.GUI.board.gameMenu.AIButton;
import com.mygdx.game.GUI.board.gameMenu.GameOption;
import com.mygdx.game.GUI.board.gameMenu.GamePreference;
import com.mygdx.game.GUI.board.moveHistory.MoveHistory;
import com.mygdx.game.GUI.board.timer.TimerPanel;

public final class MyGdxGame extends ApplicationAdapter {

	private Stage stage;
	private Board chessBoard;

	private GameBoard gameBoard;
	private GameBoard.DisplayOnlyBoard displayOnlyBoard;
	private MoveHistory moveHistory;
	private TimerPanel gameTimerPanel;

	//setter
	public void updateChessBoard(final Board board) { this.chessBoard = board; }

	//getter
	public Board getChessBoard() { return this.chessBoard; }
	public GameBoard getGameBoard() { return this.gameBoard; }
	public GameBoard.DisplayOnlyBoard getDisplayOnlyBoard() { return this.displayOnlyBoard; }
	public Stage getStage() { return this.stage; }
	public MoveHistory getMoveHistory() { return this.moveHistory; }
	public TimerPanel getGameTimerPanel() { return this.gameTimerPanel; }

	@Override
	public void create () {
		this.stage = new Stage(new FitViewport(1200, 640), new SpriteBatch());

		Gdx.input.setInputProcessor(stage);
		Gdx.graphics.setTitle("LibGDX Simple Parallel Chess 2.0");

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

		this.chessBoard = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
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
		final int BUTTON_WIDTH = 250;
		table.add(new GameMenu(this)).width(BUTTON_WIDTH);
		table.add(new GamePreference(this)).width(BUTTON_WIDTH);
		table.add(new GameOption(this)).width(BUTTON_WIDTH);
		table.add(new AIButton(this)).width(BUTTON_WIDTH);
		return table;
	}

	@Override
	public void resize (final int width, final int height) { this.stage.getViewport().update(width, height, true); }

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
		if (this.getGameBoard().isAIThinking()) {
			this.getGameBoard().getArtificialIntelligence().getProgressBar().setValue(this.getGameBoard().getArtificialIntelligence().getMoveCount());
		}
		if (this.gameTimerPanel.isTimerContinue() && !this.gameTimerPanel.isPauseTimerOption()) {
			this.gameTimerPanel.update(this);
			if (this.gameBoard.isAIPlayer(this.chessBoard.currentPlayer()) && this.chessBoard.currentPlayer().isTimeOut()) {
				this.gameBoard.getArtificialIntelligence().setStopAI(true);
			}
		}
		this.stage.getBatch().begin();
		this.stage.getBatch().end();
		this.stage.draw();
	}

	@Override
	public void dispose() {
		this.stage.dispose();
		this.stage.getBatch().dispose();
	}
}