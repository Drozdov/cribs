package ru.cribs.automaton;

import java.util.Arrays;
import java.util.Iterator;

public class Node extends AbstractNode<Node> {
	
	private static Iterator<RuleData> fromArray(RuleData[] array) {
		return Arrays.asList(array).iterator();
	}
	
	public boolean isTermainal;

	public static Node create(Iterator<RuleData> values) {
		if (!values.hasNext()) {
			Node node = new Node();
			node.isTermainal = true;
			return node;
		}
		
		Node node = new Node();
		RuleData key = values.next();
		node.edges.put(key, create(values));
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
		if (!edges.containsKey(item))
			return null;
		return (Node) edges.get(item);
	}
	
	
}
