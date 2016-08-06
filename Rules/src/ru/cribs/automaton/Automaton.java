package ru.cribs.automaton;

public class Automaton {
	
	private Node<AutomatonData> startNode = new Node<>();
	
	public void union(Node<AutomatonData> node) {
		startNode = new Uniter<AutomatonData>().unite(startNode, node);
	}
	
	public Node<AutomatonData> getStart() {
		return startNode;
	}
}
