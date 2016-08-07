import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import ru.cribs.automaton.Automaton;
import ru.cribs.automaton.RuleData;
import ru.cribs.automaton.Chain;
import ru.cribs.automaton.Node;
import ru.cribs.rules.RulesLexer;
import ru.cribs.rules.RulesParser;
import ru.cribs.rules.RulesPerformer;

public class RulesTest {
		
	@Test
	public void test2() {
		Chain chain = new Chain(new RuleData(true, "abc"));
		Node node = chain.toAutomaton();
		//Node node = chain.nodeStart;
		Automaton automaton = new Automaton();
		automaton.union(node);
		boolean res = automaton.getStart().test(new RuleData[] { new RuleData(true, "abc") });
		assertTrue(res);
		boolean res2 = automaton.getStart().test(new RuleData[] { new RuleData(true, "cde") });
		assertFalse(res2);
	}
	
	@Test
	public void test3() throws FileNotFoundException, IOException {
		ANTLRInputStream input = new ANTLRInputStream(new FileReader("rules.in"));
		RulesLexer lexer = new RulesLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RulesParser parser = new RulesParser(tokens);

		RulesPerformer chainsGetter = new RulesPerformer();

		ParseTree tree = parser.rules();
		
		Iterable<Chain> chains = chainsGetter.getRules(tree);
		
		Automaton automaton = new Automaton();
		for (Chain chain : chains) {
			//Node automatonNode = chain.toAutomaton();
			automaton.union(chain.nodeStart);
		}
		
		boolean resTrue = automaton.getStart().test(new RuleData[] {
				new RuleData(true, "own sides")
		});
		
		boolean resFalse = automaton.getStart().test(new RuleData[] {
				new RuleData(true, "error string")
		});
		
		assertTrue(resTrue);
		assertFalse(resFalse);
	}

}
