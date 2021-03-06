import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import junit.framework.Assert;
import ru.cribs.automaton.Automaton;
import ru.cribs.automaton.RuleData;
import ru.cribs.automaton.WalkGraph;
import ru.cribs.helpers.ConcatIterable;
import ru.cribs.automaton.Chain;
import ru.cribs.automaton.Node;
import ru.cribs.rules.RulesLexer;
import ru.cribs.rules.RulesParser;
import ru.cribs.rules.RulesPerformer;

public class RulesTest {
		
	@Test
	public void test1() {
		Chain chain = new Chain(new RuleData(true, "abc"));
		Node node = chain.nodeStart;
		chain.nodeEnd.isTermainal = true;
		Automaton automaton = new Automaton();
		automaton.union(node);
		boolean res = automaton.getStart().test(new RuleData[] { new RuleData(true, "abc") });
		assertTrue(res);
		boolean res2 = automaton.getStart().test(new RuleData[] { new RuleData(true, "cde") });
		assertFalse(res2);
	}
	
	@Test
	public void test2() throws FileNotFoundException, IOException {
		ANTLRInputStream input = new ANTLRInputStream(new FileReader("rules.in"));
		RulesLexer lexer = new RulesLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RulesParser parser = new RulesParser(tokens);

		ParseTree tree = parser.rules();
		Automaton automaton = new Automaton();
		new RulesPerformer(automaton).parse(tree);
		
		boolean resTrue = automaton.getStart().test(new RuleData[] {
				new RuleData(true, "own sides")
		});
		
		boolean resFalse = automaton.getStart().test(new RuleData[] {
				new RuleData(true, "error string")
		});
		
		assertTrue(resTrue);
		assertFalse(resFalse);
		
		
	}
	
	@Test
	public void concatIterableTest() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		Iterable<Integer> iterable = new ConcatIterable<Integer>(list, 5);
		iterable = new ConcatIterable<Integer>(iterable, 10);
		Iterable<Integer> iterable2 = new ConcatIterable<Integer>(list, 0);
		Integer[] result = new Integer[] {1, 2, 5, 10};
		Integer[] result2 = new Integer[] {1, 2, 0};
		assertIterables(iterable, result);
		assertIterables(iterable2, result2);
	}
	
	private static <T> void assertIterables(Iterable<T> iterable, T[] array) {
		int i = 0;
		for (T t : iterable) {
			assertTrue(array.length > i);
			assertEquals(t, array[i++]);
		}
		assertTrue(i == array.length);
	}

}
