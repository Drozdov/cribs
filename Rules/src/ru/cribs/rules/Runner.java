package ru.cribs.rules;

import java.io.FileReader;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.automaton.Automaton;

public class Runner {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream(new FileReader("rules.in"));
		RulesLexer lexer = new RulesLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RulesParser parser = new RulesParser(tokens);

		Automaton automaton = new Automaton();
		RulesPerformer chainsGetter = new RulesPerformer(automaton);

		ParseTree tree = parser.rules();
		chainsGetter.parse(tree);
	}
}