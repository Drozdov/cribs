package ru.cribs.automaton;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.cribsreader.CribsReader;
import ru.cribs.cribsreader.CribsReaderLexer;
import ru.cribs.cribsreader.CribsReaderParser;

public class RuleDataReader {
	
	public Iterable<RuleData> readRuleDatas(String inp) {
		ANTLRInputStream input = new ANTLRInputStream(inp);
		CribsReaderLexer lexer = new CribsReaderLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CribsReaderParser parser = new CribsReaderParser(tokens);
		ParseTree tree = parser.startRule();

		CribsReader reader = new CribsReader();
		reader.visit(tree);
		return reader.ruleDatas;
	}
	
	public static void main(String[] args) {
		
		System.out.println();
		
		Iterable<RuleData> datas = new RuleDataReader().readRuleDatas("1M+1L dance figure of 8 on own sides");
		
		for (RuleData data : datas) {
			System.out.println(data);
		}
	}
	
}
