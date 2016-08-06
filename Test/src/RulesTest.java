import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import ru.cribs.automaton.Automaton;
import ru.cribs.automaton.AutomatonData;
import ru.cribs.automaton.Rule;
import ru.cribs.automaton.RuleDataAutomaton;
import ru.cribs.automaton.Node;
import ru.cribs.rules.RulesLexer;
import ru.cribs.rules.RulesParser;
import ru.cribs.rules.RulesPerformer;

public class RulesTest {

	private static <T> Iterator<T> fromArray(T[] array) {
		return Arrays.asList(array).iterator();
	}
	
	/*@Test
	public void test1() {
		Node<String> node1 = Node.create(fromArray(new String[]{"a", "b", "c"}));
		Node<String> node2 = Node.create(fromArray(new String[]{"a", "c", "c"}));
		Node<String> node3 = Node.create(fromArray(new String[]{"a", "a", "c"}));
		Node<String> node4 = Node.create(fromArray(new String[]{"a", "a", "c"}));
		Node<String> node5 = Node.create(fromArray(new String[]{"a", "a", "c"}));
		
		Node<String> node = new Node<>();
		node.union(node1);
		node.union(node2);
		node.union(node3);
		node.union(node4);
		node.union(node5);
		
		assertTrue(node.test(new String[]{"a", "b", "c"}));
		assertTrue(node.test(new String[]{"a", "c", "c"}));
		assertFalse(node.test(new String[]{"a", "b", "c", "d"}));
		assertFalse(node.test(new String[]{"a"}));
	}*/
		
	@Test
	public void test2() {
		Rule chain = new Rule(new RuleDataAutomaton(new AutomatonData(true, "abc")));
		Node<AutomatonData> node = chain.toAutomaton();
		Automaton automaton = new Automaton();
		automaton.union(node);
		boolean res = automaton.getStart().test(new AutomatonData[] { new AutomatonData(true, "abc") });
		assertTrue(res);
		boolean res2 = automaton.getStart().test(new AutomatonData[] { new AutomatonData(true, "cde") });
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
		
		Iterable<Rule> chains = chainsGetter.getRules(tree);
		
		Automaton automaton = new Automaton();
		for (Rule chain : chains) {
			Node<AutomatonData> automatonNode = chain.toAutomaton();
			automaton.union(automatonNode);
		}
		
		boolean resTrue = automaton.getStart().test(new AutomatonData[] {
				new AutomatonData(true, "own sides")
		});
		
		boolean resFalse = automaton.getStart().test(new AutomatonData[] {
				new AutomatonData(true, "error string")
		});
		
		assertTrue(resTrue);
		assertFalse(resFalse);
	}

}
