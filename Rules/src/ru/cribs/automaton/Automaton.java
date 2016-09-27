package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automaton {
	
	private Node startNode = new Node();
	private Map<Node, AutomatonRule> rules = new HashMap<>();
	
	public void union(Node node) {
		startNode = new Uniter().unite(startNode, node);
	}
	
	public Node getStart() {
		return startNode;
	}
	
	public void addRule(Chain leftChain, Chain rightChain) {
		leftChain.nodeEnd.isTermainal = true;
		Map<Node, Node> convereted = leftChain.getWithoutEps();
		union(convereted.get(leftChain.nodeStart));
		rules.put(convereted.get(leftChain.nodeEnd), new AutomatonRule(leftChain, rightChain));
	}
	
	public List<RuleData> getSequence(Node endNode, Iterable<RuleData> datas) {
		AutomatonRule rule = rules.get(endNode);
		if (rule == null)
			return null;
		return rule.getSequence(datas);		
	}
	
	private class AutomatonRule {
		Chain originalChain, destChain;
		
		public AutomatonRule(Chain originalChain, Chain destChain) {
			this.originalChain = originalChain;
			this.destChain = destChain;
		}
		
		public List<RuleData> getSequence(Iterable<RuleData> datas) {
			Map<String, List<RuleData>> variables = originalChain.getVariables(datas);
			
			List<RuleData> result = new ArrayList<>();
			
			for (Node node : destChain.nodeStart.getAllDescendents()) {
				for (RuleData data : node.edges.keySet()) {
					String name = data.getName();
					if (!data.isString() && variables.containsKey(name)) {
						result.addAll(variables.get(name));
					} else {
						result.add(data);
					}
				}
			}
			
			return result;
		}
	}
}
