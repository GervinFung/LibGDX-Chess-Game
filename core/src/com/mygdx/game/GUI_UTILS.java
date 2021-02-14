package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum GUI_UTILS implements Disposable {
    UTILS;
    //private
    private final TextureAtlas GAME_TEXTURE_ATLAS = new TextureAtlas(Gdx.files.internal("gameTextureAtlas.atlas"));
    private String GET_PIECE_REGION(final Piece piece) { return piece.getLeague().toString().substring(0, 1) + piece.toString() ; }

    //public
    public final int GAME_BOARD_SR_SIZE = 600, TILE_SIZE = 75;
    public final String MOVE_LOG_STATE = "MOVE_LOG_STATE";
    public final Preferences MOVE_LOG_PREF = Gdx.app.getPreferences("MoveLogPreferences");
    public final Skin UI_SKIN = new Skin(Gdx.files.internal("UISKIN2/uiskin2.json"));
    public final TextureRegion WHITE_TEXTURE_REGION = this.GAME_TEXTURE_ATLAS.findRegion("whitePixel");
    public final TextureRegion TRANSPARENT_TEXTURE_REGION = this.GAME_TEXTURE_ATLAS.findRegion("transparent");
    public final NinePatchDrawable MOVE_HISTORY_1 = new NinePatchDrawable(new NinePatch(this.WHITE_TEXTURE_REGION, Color.valueOf("#2A2B2D")));
    public final NinePatchDrawable MOVE_HISTORY_2 = new NinePatchDrawable(new NinePatch(this.WHITE_TEXTURE_REGION, Color.valueOf("#1C1C1B")));
    public final NinePatchDrawable WHITE_CAPTURED = new NinePatchDrawable(new NinePatch(this.WHITE_TEXTURE_REGION, Color.valueOf("#171515")));
    public final NinePatchDrawable BLACK_CAPTURED = new NinePatchDrawable(new NinePatch(this.WHITE_TEXTURE_REGION, Color.valueOf("#2D2926")));

    public final TextureRegion GET_PIECE_TEXTURE_REGION(final Piece piece) { return this.GAME_TEXTURE_ATLAS.findRegion(this.GET_PIECE_REGION(piece)); }

    public final TextureRegion GET_TILE_TEXTURE_REGION(final String region) { return this.GAME_TEXTURE_ATLAS.findRegion(region); }

    //Previous and current tile
    public final Color HUMAN_PREVIOUS_TILE = new Color(102/255f, 255/255f, 102/255f, 1), HUMAN_CURRENT_TILE = new Color(50/255f, 205/255f, 50/255f, 1);
    public final Color AI_PREVIOUS_TILE = Color.PINK, AI_CURRENT_TILE = new Color(1, 51/255f, 51/255f, 1);

    //Board color
    public enum TILE_COLOR {
        CLASSIC {
            @Override
            public Color DARK_TILE() { return new Color(181/255f, 136/255f, 99/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return new Color(240/255f, 217/255f, 181/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(105/255f, 105/255f, 105/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(169/255f, 169/255f, 169/255f, 1); }
        },

        DARK_BLUE {
            @Override
            public Color DARK_TILE() { return new Color(29/255f, 61/255f, 99/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return Color.WHITE; }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(105/255f, 105/255f, 105/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(169/255f, 169/255f, 169/255f, 1); }
        },

        LIGHT_BLUE{
            @Override
            public Color DARK_TILE() { return new Color(137/255f, 171/255f, 227/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return Color.WHITE; }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(105/255f, 105/255f, 105/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(169/255f, 169/255f, 169/255f, 1); }
        },

        BUMBLEBEE{
            @Override
            public Color DARK_TILE() { return new Color(64/255f, 64/255f, 64/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return new Color(254/255f, 231/255f, 21/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(105/255f, 105/255f, 105/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(169/255f, 169/255f, 169/255f, 1); }
        },

        DARK_GRAY{
            @Override
            public Color DARK_TILE() { return new Color(105/255f, 105/255f, 105/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return Color.WHITE; }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(1, 252/255f, 84/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(1, 253/255f, 156/255f, 1); }
        },

        LIGHT_GRAY{
            @Override
            public Color DARK_TILE() { return new Color(177/255f, 179/255f, 179/255f, 1); }
            @Override
            public Color LIGHT_TILE() { return Color.WHITE; }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() { return new Color(1, 252/255f, 84/255f, 1); }
            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() { return new Color(1, 253/255f, 156/255f, 1); }
        };

        public abstract Color DARK_TILE();
        public abstract Color LIGHT_TILE();
        public abstract Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE();
        public abstract Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE();
    }

    public final List<TILE_COLOR> BOARD_COLORS = allBoardColors();

    private List<TILE_COLOR> allBoardColors() {
        final List<TILE_COLOR> BOARD_COLORS = new ArrayList<>();

        BOARD_COLORS.add(TILE_COLOR.CLASSIC);
        BOARD_COLORS.add(TILE_COLOR.DARK_BLUE);
        BOARD_COLORS.add(TILE_COLOR.DARK_GRAY);
        BOARD_COLORS.add(TILE_COLOR.LIGHT_BLUE);
        BOARD_COLORS.add(TILE_COLOR.LIGHT_GRAY);
        BOARD_COLORS.add(TILE_COLOR.BUMBLEBEE);

        return Collections.unmodifiableList(BOARD_COLORS);
    }

    @Override
    public void dispose() {
        GAME_TEXTURE_ATLAS.dispose();
        UI_SKIN.dispose();
    }
}