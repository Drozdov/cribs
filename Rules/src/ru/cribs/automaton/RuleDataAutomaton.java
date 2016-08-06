package ru.cribs.automaton;

public class RuleDataAutomaton extends RuleData {
	
	private AutomatonData data;

	public RuleDataAutomaton(AutomatonData data) {
		this.data = data;
	}

	@Override
	public boolean isEps() {
		return false;
	}

	@Override
	public AutomatonData createAutomatonData() {
		return data;
	}
	
	@Override
	public String toString() {
		return data.getName();
	}
	
	
}
