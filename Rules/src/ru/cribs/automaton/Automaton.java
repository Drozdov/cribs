package ru.cribs.automaton;

public class Automaton {
	
	private Node startNode = new Node();
	
	public void union(Node node) {
		startNode = new Uniter().unite(startNode, node);
	}
	
	public Node getStart() {
		return startNode;
	}
}
