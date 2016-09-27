package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.cribs.automaton.RuleDataEps.DeclarationType;

public class Chain {

	public ChainNode nodeStart, nodeEnd;
	
	public Chain() {
		nodeStart = nodeEnd = new ChainNode();
	}
	
	public Chain(RuleData data) {
		nodeStart = new ChainNode();
		nodeEnd = new ChainNode();
		nodeStart.edges.put(data, nodeEnd);
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
	
	public Map<String, List<RuleData>> getVariables(Iterable<RuleData> datas) {
		Map<String, List<RuleData>> result = new HashMap<String, List<RuleData>>();
		Map<String, List<RuleData>> temp = new HashMap<String, List<RuleData>>();
		Node node = nodeStart;
		for (RuleData data : datas) {
			Node next = node.getRule(data);
			
			for (List<RuleData> filling : temp.values()) {
				filling.add(data);
			}
			
			if (next == null)
				return null;
			if (node instanceof ChainNode) {
				ChainNode chainNode = (ChainNode) node;
				List<RuleDataEps> eps = chainNode.getEpsRulesSkipped(data);
				for (RuleDataEps epsData : eps) {
					switch (epsData.declType) {
					case BEGIN:
						temp.put(epsData.name, new ArrayList<RuleData>());
						break;
					case END:
						List<RuleData> list = temp.get(epsData.name);
						result.put(epsData.name, list);
						break;
					default:
						break;
					}
				}
			}
			node = next;
		}
		return result;
	}

	public HashMap<Node, Node> getWithoutEps() {
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
				automatonNode.edges.put(curData, automatonNode2);
			}
		}
		return map;
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
