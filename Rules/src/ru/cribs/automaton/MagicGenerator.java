package ru.cribs.automaton;

public class MagicGenerator {
	private MagicGenerator() {
	}
	
	private static int magicNumber;
	
	public static int generateMagic() {
		return ++magicNumber;
	}
}
