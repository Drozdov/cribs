package ru.cribs.automaton;

public class RuleData {
	private final boolean isString;
	private final String name;
	private final String comment;
	
	public RuleData(boolean isString, String name, String comment) {
		this.isString = isString;
		this.name = name;
		this.comment = comment;
	}
	
	public RuleData(boolean isString, String name) {
		this(isString, name, null);
	}

	public boolean isString() {
		return isString;
	}

	public String getName() {
		return name;
	}
	
	public String getComment() {
		return comment;
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
