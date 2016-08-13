package ru.cribs.automaton;

import java.util.HashMap;

import ru.cribs.automaton.RuleDataEps.DeclarationType;

public class Chain {

	public ChainNode nodeStart, nodeEnd;
	
	public Chain() {
		nodeStart = nodeEnd = new ChainNode();
	}
	
	public Chain(RuleData data) {
		nodeStart = new ChainNode();
		nodeEnd = new ChainNode();
		nodeStart.rules.put(data, nodeEnd);
	}
	
	public void concat(Chain chain) {
		nodeEnd.eps.put(RuleDataEps.create(), chain.nodeStart);
		nodeEnd = chain.nodeEnd;
	}
	
	public void parallel(Chain chain) {
		nodeStart.eps.put(RuleDataEps.create(), chain.nodeStart);
		chain.nodeEnd.eps.put(RuleDataEps.create(), nodeEnd);
	}
	
	public void cycle() {
		nodeEnd.eps.put(RuleDataEps.create(), nodeStart);
	}
	
	public void allowSkip() {
		nodeStart.eps.put(RuleDataEps.create(), nodeEnd);
	}
	
	public void star() {
		cycle();
		allowSkip();
	}
	
	public void name(String name) {
		ChainNode start = new ChainNode();
		ChainNode end = new ChainNode();

		start.eps.put(new RuleDataEps(name, DeclarationType.BEGIN), nodeStart);
		nodeEnd.eps.put(new RuleDataEps(name, DeclarationType.END), end);
		
		nodeStart = start;
		nodeEnd = end;

	}

	public Node getWithoutEps() {
		nodeEnd.isTermainal = true;
		Node data = new Node();
		NodesMap map = new NodesMap();
		map.put(nodeStart, data);
		
		for (Node node : nodeStart.getAllDescendents()) {
			Node automatonNode = map.getSafe(node);
			automatonNode.isTermainal = node.isTermainal;
			
			for (RuleData curData : node.getRulesData()) {
				Node rule = node.getRule(curData);
				Node automatonNode2 = map.getSafe(rule);
				automatonNode.rules.put(curData, automatonNode2);
			}
		}
		return data;
	}
	
	private class NodesMap extends HashMap<Node, Node> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Node getSafe(Node origNode) {
			if (containsKey(origNode))
				return get(origNode);
			
			Node node = new Node();
			put(origNode, node);
			return node;
		}
	}
	
}
