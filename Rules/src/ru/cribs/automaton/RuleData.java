package ru.cribs.automaton;

public abstract class RuleData {
	
	public abstract boolean isEps();
	
	public abstract AutomatonData createAutomatonData();
	
}
