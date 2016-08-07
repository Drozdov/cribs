package ru.cribs.rules;

import java.io.FileReader;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.automaton.Chain;

public class Runner {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream(new FileReader("rules.in"));
		RulesLexer lexer = new RulesLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RulesParser parser = new RulesParser(tokens);

		RulesPerformer chainsGetter = new RulesPerformer();

		ParseTree tree = parser.rules();
		
		Iterable<Chain> rules = chainsGetter.getRules(tree);
		
		for (Chain rule : rules) {
			System.out.println(rule);
		}
	}
}