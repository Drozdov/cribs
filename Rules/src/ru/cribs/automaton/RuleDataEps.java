package ru.cribs.automaton;

public class RuleDataEps {

	public enum DeclarationType { EPS, BEGIN, END }
	
	public final String name;
	public final DeclarationType declType;
	
	public RuleDataEps(String name, DeclarationType declType) {
		this.name = name;
		this.declType = declType;
	}
	
	@Override
	public String toString() {
		return name + "_" + declType;
	}
	
	public static RuleDataEps create() {
		return new RuleDataEps("", DeclarationType.EPS);
	}

}
