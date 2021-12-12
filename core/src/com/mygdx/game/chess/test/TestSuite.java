package com.mygdx.game.chess.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        BoardTest.class,
        CastlingTest.class,
        CheckMateTest.class,
        FenParserTest.class,
        MiniMaxTest.class,
        PawnStructureTest.class,
        PlayerTest.class,
        StaleMateTest.class,
})

public final class TestSuite {
    private TestSuite() {
        throw new RuntimeException("Should not instantiate ProgramTestSuite");
    }
}
