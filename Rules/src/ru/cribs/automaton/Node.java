package ru.cribs.automaton;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Node {
	
	private static Iterator<RuleData> fromArray(RuleData[] array) {
		return Arrays.asList(array).iterator();
	}
	
	protected Map<RuleData, Node> rules = new HashMap<>();
	public boolean isTermainal;
	protected int magic;
	
	public static Node create(Iterator<RuleData> values) {
		if (!values.hasNext()) {
			Node node = new Node();
			node.isTermainal = true;
			return node;
		}
		
		Node node = new Node();
		RuleData key = values.next();
		node.rules.put(key, create(values));
		return node;
	}
	
	public Node get(Iterator<RuleData> items) {
		if (!items.hasNext()) {
			return isTermainal ? this : null;
		}
		RuleData key = items.next();
		Node node = getRule(key);
		if (node == null)
			return null;
		return node.get(items);
	}
	
	public boolean test(RuleData[] items) {
		return get(fromArray(items)) != null;
	}
	
	public Node getRule(RuleData item) {
		if (!rules.containsKey(item))
			return null;
		return rules.get(item);
	}
	
	public Collection<RuleData> getRulesData() {
		return rules.keySet();
	}
	
	public Collection<Node> getChildren() {
		return rules.values();
	}
	
	public Iterable<Node> getAllDescendents() {
		final Node cur = this;
		return new Iterable<Node>() {

			@Override
			public Iterator<Node> iterator() {
				return new BfsNodeIterator(cur);
			}
		};
	}
	
	public void saveToDot(String file) {
		try (PrintWriter out = new PrintWriter(file)) {
			out.println("digraph g {");
			printGraphEdges(out, new LabelsGenerator(), MagicGenerator.generateMagic());
			out.println("}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void printGraphEdges(PrintWriter out, LabelsGenerator labelsGenerator, int magic)
	{
		if (this.magic == magic)
			return;
		this.magic = magic;
		
		String nodeLabel = labelsGenerator.getLabel(this);
		for (PrintingRuleStructure toPrint : getPrintingStructures()) {
			String nodeLabel2 = labelsGenerator.getLabel(toPrint.destNode);
			out.println(String.format("%s -> %s [label = \"%s\"]",
					nodeLabel, nodeLabel2, toPrint.label));
			toPrint.destNode.printGraphEdges(out, labelsGenerator, magic);
		}
	}
	
	protected List<PrintingRuleStructure> getPrintingStructures() {
		List<PrintingRuleStructure> result = new ArrayList<PrintingRuleStructure>();
		for (Entry<RuleData, Node> entry : rules.entrySet()) {
			result.add(new PrintingRuleStructure(entry.getKey().toString(), entry.getValue()));
		}
		return result;
	}
	
	protected class PrintingRuleStructure {
		public final String label;
		public final Node destNode;
		
		public PrintingRuleStructure(String label, Node destNode) {
			super();
			this.label = label;
			this.destNode = destNode;
		}
		
	}
	
	protected class LabelsGenerator {
		
		private int cur;
		private final HashMap<Node, String> labels = new HashMap<>();
		
		public String generateLabel() {
			return (char)('A' + cur / 10) + "" + (cur++ % 10);
		}
		
		public String getLabel(Node node) {
			if (!labels.containsKey(node)) {
				labels.put(node, generateLabel());
			}
			return labels.get(node);
		}
	}
	
	private class BfsNodeIterator implements Iterator<Node> {

		private int magic = MagicGenerator.generateMagic();
		private Queue<Node> bfsQueue = new LinkedList<>();
		
		public BfsNodeIterator(Node root) {
			bfsQueue.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !bfsQueue.isEmpty();
		}

		@Override
		public Node next() {
			Node elem = bfsQueue.poll();
			for (Node child : elem.getChildren()) {
				if (child.magic != magic) {
					bfsQueue.add(child);
					child.magic = magic;
				}
			}
			return elem;
		}
		
	}
	
}
