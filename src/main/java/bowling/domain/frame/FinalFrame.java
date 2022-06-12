package bowling.domain.frame;

import bowling.domain.FrameNo;
import bowling.domain.Score;
import bowling.domain.state.State;
import bowling.domain.state.StateFactory;
import bowling.exception.CannotCalculateScore;
import bowling.exception.InvalidBoundStateException;
import bowling.exception.NotCreateFrameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FinalFrame implements Frame {

    private static final int LAST_CHANCE = 1;
    private static final int TOTAL_HIT_COUNT_WITH_BONUS = 3;
    private static final int FIRST_STATE_INDEX = 0;
    private static final int SECOND_STATE_INDEX = 1;
    private static final String DELIMITER = "|";

    private final FrameNo frameNo;
    private final List<State> states = new ArrayList<>();

    public FinalFrame(FrameNo frameNo) {
        this.states.add(StateFactory.initialState());
        this.frameNo = frameNo;
    }

    @Override
    public Frame next() throws NotCreateFrameException {
        throw new NotCreateFrameException(frameNo.toInt());
    }

    @Override
    public void bowling(int hit) {
        if (currentState().isFinish()) {
            State state = StateFactory.nextState(this);
            states.add(state.bowl(hit));
            return;
        }
        renewState(currentState().bowl(hit));
    }

    @Override
    public Score calculate(Score score) {
        Score calculatedScore = state(FIRST_STATE_INDEX).calculateAdditionalScore(score);
        if (calculatedScore.hasAdditionalScoreCount()) {
            return calculateScoreToSecondState(calculatedScore);
        }
        return calculatedScore;
    }

    private State state(int index) {
        if (states.size() < index + 1) {
            throw new InvalidBoundStateException(index);
        }
        return states.get(index);
    }

    private Score calculateScoreToSecondState(Score score) {
        try {
            return state(SECOND_STATE_INDEX).calculateAdditionalScore(score);
        } catch (InvalidBoundStateException e) {
            throw new CannotCalculateScore();
        }
    }

    private int lastStateIndex() {
        return states.size() - 1;
    }

    private State currentState() {
        return state(lastStateIndex());
    }

    private void renewState(State state) {
        states.remove(lastStateIndex());
        states.add(state);
    }

    private boolean remainBonusChance() {
        if (states.isEmpty()) {
            return false;
        }
        if (!currentState().hasBonusChance()) {
            return false;
        }
        return totalBowlingCount() < TOTAL_HIT_COUNT_WITH_BONUS;
    }

    private int totalBowlingCount() {
        return states.stream()
                .mapToInt(State::bowlingCount)
                .sum();
    }

    @Override
    public boolean isFinish() {
        return currentState().isFinish() && !remainBonusChance();
    }

    @Override
    public boolean hasLastBonusChance() {
        if (!currentState().isFinish()) {
            throw new IllegalStateException();
        }
        if (!currentState().hasBonusChance()) {
            return false;
        }
        return TOTAL_HIT_COUNT_WITH_BONUS - totalBowlingCount() == LAST_CHANCE;
    }

    @Override
    public FrameNo frameNo() {
        return frameNo;
    }

    @Override
    public Score score() {
        if (!isFinish()) {
            throw new CannotCalculateScore();
        }
        return states.stream()
                .map(State::score)
                .reduce(Score::sum)
                .orElseThrow(CannotCalculateScore::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalFrame that = (FinalFrame) o;
        return Objects.equals(frameNo, that.frameNo) && Objects.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frameNo, states);
    }

    @Override
    public String toString() {
        return states.stream()
                .map(State::description)
                .collect(Collectors.joining(DELIMITER));
    }
}
