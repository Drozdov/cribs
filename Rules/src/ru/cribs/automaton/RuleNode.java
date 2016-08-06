package ru.cribs.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class RuleNode extends Node<RuleData> {

	protected Map<RuleData, RuleNode> eps = new HashMap<>();

	@Override
	public Node<RuleData> getRule(RuleData item) {
		return getIsomorphicNode().getRule(item);
	}
		
	@Override
	public Collection<Node<RuleData>> getChildren() {
		return getIsomorphicNode().getChildren();
	}
	
	@Override
	public Collection<RuleData> getRulesData() {
		return getIsomorphicNode().getRulesData();
	}
	
	@Override
	protected Map<RuleData, Node<RuleData>> getAllRules() {
		Map<RuleData, Node<RuleData>> result = new HashMap<>(super.getAllRules());
		for (Entry<RuleData, RuleNode> entry : eps.entrySet()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public Node<RuleData> getIsomorphicNode() {
		Node<RuleData> result = new Node<RuleData>();
		Set<RuleNode> visited = new HashSet<>();
		Queue<RuleNode> toVisit = new LinkedList<>();
		toVisit.add(this);
		
		while (!toVisit.isEmpty()) {
			RuleNode node = toVisit.poll();
			if (visited.contains(node))
				continue;
			visited.add(node);
			
			for (Entry<RuleData, RuleNode> data : node.eps.entrySet()) {
				toVisit.add(data.getValue());
			}
			
			for (Entry<RuleData, Node<RuleData>> data : node.rules.entrySet()) {
				result.rules.put(data.getKey(), data.getValue());
			}
			
		}
		
		return result;
	}

	public RuleNode add(RuleData data) {
		RuleNode node = new RuleNode();
		if (data.isEps()) {
			eps.put(data, node);
		} else {
			rules.put(data, node);
		}
		return node;
	}
	
	public String toString() {
		for (RuleData key : rules.keySet()) {
			return "ChainNode " + key.toString();
		}
		for (RuleData key : eps.keySet()) {
			return "ChainNode " + key.toString();
		}
		return null;
	}
	
	
}
