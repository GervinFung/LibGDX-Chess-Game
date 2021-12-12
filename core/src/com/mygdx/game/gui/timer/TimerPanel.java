package com.mygdx.game.gui.timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.player.Player;
import com.mygdx.game.gui.GuiUtils;
import com.mygdx.game.gui.gameScreen.GameScreen;

public final class TimerPanel extends Table {

    public static final int SIZE = GuiUtils.GAME_BOARD_SR_SIZE / 2;

    private final PlayerTimerTable whitePlayerTimerTable, blackPlayerTimerTable;
    private boolean continueTimer, pauseTimerOption, noTimer;
    private TIMER_PANEL_DIRECTION timer_panel_direction;

    private enum TIMER_PANEL_DIRECTION {
        FLIPPED {
            @Override
            void flip(final TimerPanel timerPanel) {
                timerPanel.clearChildren();
                timerPanel.add(timerPanel.whitePlayerTimerTable).size(TimerPanel.SIZE).row();
                timerPanel.add(timerPanel.blackPlayerTimerTable).size(TimerPanel.SIZE);
            }

            @Override
            TIMER_PANEL_DIRECTION getOpposite() {
                return NORMAL;
            }
        },
        NORMAL {
            @Override
            void flip(final TimerPanel timerPanel) {
                timerPanel.clearChildren();
                timerPanel.add(timerPanel.blackPlayerTimerTable).size(TimerPanel.SIZE).row();
                timerPanel.add(timerPanel.whitePlayerTimerTable).size(TimerPanel.SIZE);
            }

            @Override
            TIMER_PANEL_DIRECTION getOpposite() {
                return FLIPPED;
            }
        };

        abstract void flip(final TimerPanel timerPanel);

        abstract TIMER_PANEL_DIRECTION getOpposite();

        private void update(final TimerPanel timerPanel, final GameScreen gameScreen) {
            if (gameScreen.getGameBoard().isGameEnd()) {
                return;
            }
            gameScreen.getGameBoard().displayTimeOutMessage(gameScreen.getChessBoard(), gameScreen.getStage());
            if (gameScreen.getChessBoard().currentPlayer().getLeague().isWhite()) {
                timerPanel.whitePlayerTimerTable.updateTimer(gameScreen.getChessBoard().currentPlayer());
            } else {
                timerPanel.blackPlayerTimerTable.updateTimer(gameScreen.getChessBoard().currentPlayer());
            }
        }
    }

    public TimerPanel() {
        this.setVisible(true);
        this.whitePlayerTimerTable = new PlayerTimerTable(Color.WHITE, Color.BLACK, "White Player");
        this.continueTimer = true;
        this.pauseTimerOption = false;
        this.noTimer = false;
        this.blackPlayerTimerTable = new PlayerTimerTable(Color.BLACK, Color.WHITE, "Black Player");
        this.timer_panel_direction = TIMER_PANEL_DIRECTION.NORMAL;
        this.add(this.blackPlayerTimerTable).size(SIZE).row();
        this.add(this.whitePlayerTimerTable).size(SIZE);
    }

    //setter
    public void setPauseTimerOption(final boolean pauseTimerOption) {
        this.pauseTimerOption = pauseTimerOption;
    }

    public void continueTimer(final boolean continueTimer) {
        this.continueTimer = continueTimer;
    }

    //getter
    public boolean isPauseTimerOption() {
        return this.pauseTimerOption;
    }

    public boolean isTimerContinue() {
        return this.continueTimer;
    }

    public boolean isNoTimer() {
        return this.noTimer;
    }

    public void changeTimerPanelDirection() {
        this.timer_panel_direction = this.timer_panel_direction.getOpposite();
        this.continueTimer = true;
        this.timer_panel_direction.flip(this);
    }

    public void resetTimer(final Player whitePlayer, final Player blackPlayer) {
        this.noTimer = whitePlayer.isNoTimer() && blackPlayer.isNoTimer();
        this.whitePlayerTimerTable.resetTimer(whitePlayer);
        this.blackPlayerTimerTable.resetTimer(blackPlayer);
    }

    public void update(final GameScreen gameScreen) {
        this.timer_panel_direction.update(this, gameScreen);
    }

    private static final class PlayerTimerTable extends Table {

        private final Label timerLabel;

        private PlayerTimerTable(final Color panelColor, final Color labelColor, final CharSequence text) {
            super(GuiUtils.UI_SKIN);
            final Label label = new Label(text + " Timer", GuiUtils.UI_SKIN);
            label.setColor(labelColor);
            this.add(label).row();
            this.timerLabel = new Label(this.getTimeFormat(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND), GuiUtils.UI_SKIN);
            this.timerLabel.setColor(labelColor);
            this.add(timerLabel);
            this.setVisible(true);
            this.setBackground(new NinePatchDrawable(new NinePatch(GuiUtils.GET_TILE_TEXTURE_REGION("white"), panelColor)));
        }

        private String getTimeFormat(final int minute, final int second, final int millisecond) {
            if (millisecond / 10 == 0) {
                if (second / 10 == 0) {
                    return minute + " : 0" + second + " : 0" + millisecond;
                } else {
                    return minute + " : " + second + " : 0" + millisecond;
                }
            } else {
                if (second / 10 == 0) {
                    return minute + " : 0" + second + " : " + millisecond;
                } else {
                    return minute + " : " + second + " : " + millisecond;
                }
            }
        }

        private void updateTimer(final Player player) {
            player.countDown();
            this.timerLabel.setText(this.getTimeFormat(player.getMinute(), player.getSecond(), player.getMillisecond()));
        }

        private void resetTimer(final Player player) {
            if (!player.isNoTimer()) {
                this.timerLabel.setText(this.getTimeFormat(player.getMinute(), player.getSecond(), player.getMillisecond()));
            } else {
                this.timerLabel.setText("No Timer");
            }
        }
    }
}