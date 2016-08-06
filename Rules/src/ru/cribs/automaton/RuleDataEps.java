package ru.cribs.automaton;

public class RuleDataEps extends RuleData {
	
	@Override
	public boolean isEps() {
		return true;
	}

	@Override
	public AutomatonData createAutomatonData() {
		return null;
	}
	
	@Override
	public String toString() {
		return "eps";
	}

}
