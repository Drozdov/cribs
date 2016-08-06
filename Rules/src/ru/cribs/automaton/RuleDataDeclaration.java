package ru.cribs.automaton;

public class RuleDataDeclaration extends RuleDataEps {

	public enum DeclarationType { BEGIN, END }
	
	public final String name;
	public final DeclarationType declType;
	
	public RuleDataDeclaration(String name, DeclarationType declType) {
		this.name = name;
		this.declType = declType;
	}
	
}
