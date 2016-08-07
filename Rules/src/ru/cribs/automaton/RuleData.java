package ru.cribs.automaton;

public class RuleData {
	private final boolean isString;
	private final String name;
	public String comment;
	
	public RuleData(boolean isString, String name) {
		this.isString = isString;
		this.name = name;
	}

	public boolean isString() {
		return isString;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RuleData))
			return false;
		
		RuleData data = (RuleData)obj;
		return data.isString == isString && data.name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
	
}
