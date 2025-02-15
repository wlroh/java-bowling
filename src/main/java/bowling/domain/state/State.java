package bowling.domain.state;

import bowling.domain.Score;

public interface State {

    int NO_HIT = 0;
    int ONE_HIT = 1;
    int TWO_HIT = 2;

    State bowl(int hit);
    boolean isFinish();
    boolean isProgressing();
    boolean hasBonusChance();
    int bowlingCount();
    String description();
    Score score();
    Score calculateAdditionalScore(Score score);
}
