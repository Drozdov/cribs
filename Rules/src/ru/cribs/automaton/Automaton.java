package ru.cribs.automaton;

import java.util.HashMap;
import java.util.Map;

public class Automaton {
	
	private Node startNode = new Node();
	private Map<Node, Chain> rules = new HashMap<>();
	
	public void union(Node node) {
		startNode = new Uniter().unite(startNode, node);
	}
	
	public Node getStart() {
		return startNode;
	}
	
	public void addRule(Chain leftChain, Chain rightChain) {
		leftChain.nodeEnd.isTermainal = true;
		union(leftChain.getWithoutEps());
		rules.put(leftChain.nodeEnd, rightChain);
	}
}
