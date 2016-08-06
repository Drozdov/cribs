package ru.cribs.automaton;

public class AutomatonData {
	private final boolean isString;
	private final String name;
	
	public AutomatonData(boolean isString, String name) {
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
		if (!(obj instanceof AutomatonData))
			return false;
		
		AutomatonData data = (AutomatonData)obj;
		return data.isString == isString && data.name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString() {
		return name;
	}
	
	
	
}
