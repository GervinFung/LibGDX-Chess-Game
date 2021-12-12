package com.mygdx.game.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.google.common.collect.ImmutableList;
import com.mygdx.game.chess.engine.pieces.Piece;

public final class GuiUtils {

    //dialog
    public static final boolean IS_SMARTPHONE = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    public static final int PAD = 20;
    public static final int WIDTH = IS_SMARTPHONE ? 150 : 300;
    //private
    private static final TextureAtlas GAME_TEXTURE_ATLAS = new TextureAtlas(Gdx.files.internal("gameTextureAtlas.atlas"));
    public static final TextureRegion BACKGROUND = GET_TILE_TEXTURE_REGION("welcome");
    public static final TextureRegion LOGO = GET_TILE_TEXTURE_REGION("chess_logo");
    public static final TextureRegion WHITE_TEXTURE_REGION = GAME_TEXTURE_ATLAS.findRegion("white");
    public static final TextureRegion TRANSPARENT_TEXTURE_REGION = GAME_TEXTURE_ATLAS.findRegion("transparent");
    //public
    public static final int WORLD_WIDTH = 1200, WORLD_HEIGHT = 640;
    public static final int GAME_BOARD_SR_SIZE = 600, TILE_SIZE = 75;
    public static final String MOVE_LOG_STATE = "MOVE_LOG_STATE";
    public static final Preferences MOVE_LOG_PREF = Gdx.app.getPreferences("MoveLogPreferences");
    public static final Skin UI_SKIN = new Skin(Gdx.files.internal("UISKIN2/uiskin2.json"));
    public static final NinePatchDrawable MOVE_HISTORY_1 = new NinePatchDrawable(new NinePatch(WHITE_TEXTURE_REGION, Color.valueOf("#2A2B2D")));
    public static final NinePatchDrawable MOVE_HISTORY_2 = new NinePatchDrawable(new NinePatch(WHITE_TEXTURE_REGION, Color.valueOf("#1C1C1B")));
    public static final NinePatchDrawable WHITE_CAPTURED = new NinePatchDrawable(new NinePatch(WHITE_TEXTURE_REGION, Color.valueOf("#171515")));
    public static final NinePatchDrawable BLACK_CAPTURED = new NinePatchDrawable(new NinePatch(WHITE_TEXTURE_REGION, Color.valueOf("#2D2926")));
    //Previous and current tile
    public static final Color HUMAN_PREVIOUS_TILE = new Color(102 / 255f, 255 / 255f, 102 / 255f, 1), HUMAN_CURRENT_TILE = new Color(50 / 255f, 205 / 255f, 50 / 255f, 1);
    public static final Color AI_PREVIOUS_TILE = Color.PINK, AI_CURRENT_TILE = new Color(1, 51 / 255f, 51 / 255f, 1);
    public static final ImmutableList<TILE_COLOR> BOARD_COLORS = ImmutableList.of(TILE_COLOR.CLASSIC, TILE_COLOR.DARK_BLUE, TILE_COLOR.DARK_GRAY, TILE_COLOR.LIGHT_BLUE, TILE_COLOR.LIGHT_GRAY, TILE_COLOR.BUMBLEBEE);

    private GuiUtils() {
        throw new IllegalStateException("Cannot instantiate GuiUtils");
    }

    private static String GET_PIECE_REGION(final Piece piece) {
        return piece.getLeague().toString().charAt(0) + piece.toString();
    }

    public static TextureRegion GET_PIECE_TEXTURE_REGION(final Piece piece) {
        return GAME_TEXTURE_ATLAS.findRegion(GET_PIECE_REGION(piece));
    }

    public static TextureRegion GET_TILE_TEXTURE_REGION(final String region) {
        return GAME_TEXTURE_ATLAS.findRegion(region);
    }

    public static void dispose() {
        GAME_TEXTURE_ATLAS.dispose();
        UI_SKIN.dispose();
    }

    //Board color
    public enum TILE_COLOR {
        CLASSIC {
            @Override
            public Color DARK_TILE() {
                return new Color(181 / 255f, 136 / 255f, 99 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return new Color(240 / 255f, 217 / 255f, 181 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(105 / 255f, 105 / 255f, 105 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(169 / 255f, 169 / 255f, 169 / 255f, 1);
            }
        },

        DARK_BLUE {
            @Override
            public Color DARK_TILE() {
                return new Color(29 / 255f, 61 / 255f, 99 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return Color.WHITE;
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(105 / 255f, 105 / 255f, 105 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(169 / 255f, 169 / 255f, 169 / 255f, 1);
            }
        },

        LIGHT_BLUE {
            @Override
            public Color DARK_TILE() {
                return new Color(137 / 255f, 171 / 255f, 227 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return Color.WHITE;
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(105 / 255f, 105 / 255f, 105 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(169 / 255f, 169 / 255f, 169 / 255f, 1);
            }
        },

        BUMBLEBEE {
            @Override
            public Color DARK_TILE() {
                return new Color(64 / 255f, 64 / 255f, 64 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return new Color(254 / 255f, 231 / 255f, 21 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(105 / 255f, 105 / 255f, 105 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(169 / 255f, 169 / 255f, 169 / 255f, 1);
            }
        },

        DARK_GRAY {
            @Override
            public Color DARK_TILE() {
                return new Color(105 / 255f, 105 / 255f, 105 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return Color.WHITE;
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(1, 252 / 255f, 84 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(1, 252 / 255f, 84 / 255f, 0.95f);
            }
        },

        LIGHT_GRAY {
            @Override
            public Color DARK_TILE() {
                return new Color(177 / 255f, 179 / 255f, 179 / 255f, 1);
            }

            @Override
            public Color LIGHT_TILE() {
                return Color.WHITE;
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE() {
                return new Color(1, 252 / 255f, 84 / 255f, 1);
            }

            @Override
            public Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE() {
                return new Color(1, 252 / 255f, 84 / 255f, 0.95f);
            }
        };

        public abstract Color DARK_TILE();

        public abstract Color LIGHT_TILE();

        public abstract Color HIGHLIGHT_LEGAL_MOVE_DARK_TILE();

        public abstract Color HIGHLIGHT_LEGAL_MOVE_LIGHT_TILE();
    }
}