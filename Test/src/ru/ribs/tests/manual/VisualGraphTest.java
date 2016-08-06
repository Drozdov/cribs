package ru.ribs.tests.manual;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.automaton.Automaton;
import ru.cribs.automaton.AutomatonData;
import ru.cribs.automaton.Rule;
import ru.cribs.automaton.Node;
import ru.cribs.rules.RulesLexer;
import ru.cribs.rules.RulesParser;
import ru.cribs.rules.RulesPerformer;

public class VisualGraphTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		chainsTest();
	}
	
	private static void chainsTest() throws FileNotFoundException, IOException {
		ANTLRInputStream input = new ANTLRInputStream(new FileReader("rules.in"));
		RulesLexer lexer = new RulesLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RulesParser parser = new RulesParser(tokens);

		RulesPerformer chainsGetter = new RulesPerformer();

		ParseTree tree = parser.rules();
		Iterable<Rule> chains = chainsGetter.getRules(tree);
		
		Automaton automaton = new Automaton();
		for (Rule chain : chains) {
			Node<AutomatonData> automatonNode = chain.toAutomaton();
			automaton.union(automatonNode);
		}
		
		automaton.getStart().saveToDot("out.dot");
		
	}

}
