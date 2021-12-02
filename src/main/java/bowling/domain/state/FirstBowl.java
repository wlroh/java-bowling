package bowling.domain.state;

import bowling.domain.Pins;

public class FirstBowl extends RunningState {
	private final Pins first;

	private FirstBowl(Pins first) {
		this.first = first;
	}

	public static State create(Pins pins) {
		return new FirstBowl(pins);
	}

	@Override
	public State bowl(Pins pins) {
		if (first.isSpare(pins)) {
			return Spare.create(first, pins);
		}
		return Miss.create(first, pins);
	}

	@Override
	public String symbol() {
		return first.toString();
	}
}
