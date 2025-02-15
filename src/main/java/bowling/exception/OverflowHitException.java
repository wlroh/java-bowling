package bowling.exception;

import bowling.domain.Hit;

public class OverflowHitException extends IllegalArgumentException {

    private static final String MESSAGE = "%d 개를 넘길 수 없습니다. 입력한 Hit 수: %d, %d";

    public OverflowHitException(int hit, int anotherHit) {
        super(String.format(MESSAGE, Hit.MAX_NUMBER, hit, anotherHit));
    }
}
