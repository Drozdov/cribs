package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Queue;
import java.util.Set;

public class ChainNode extends Node {

	protected Map<RuleDataEps, ChainNode> eps = new HashMap<>();

	@Override
	public Node getRule(RuleData item) {
		return getIsomorphicNode().getRule(item);
	}
		
	@Override
	public Collection<Node> getChildren() {
		return getIsomorphicNode().getChildren();
	}
	
	@Override
	public Collection<RuleData> getRulesData() {
		return getIsomorphicNode().getRulesData();
	}
		
	@Override
	protected List<Node.PrintingRuleStructure> getPrintingStructures() {
		List<Node.PrintingRuleStructure> result = super.getPrintingStructures();
		for (Entry<RuleDataEps, ChainNode> entry : eps.entrySet()) {
			result.add(new PrintingRuleStructure(entry.getKey().toString(), entry.getValue()));
		}
		return result;
	}

	public Node getIsomorphicNode() {
		Node result = new Node();
		Set<ChainNode> visited = new HashSet<>();
		Queue<ChainNode> toVisit = new LinkedList<>();
		toVisit.add(this);
		
		while (!toVisit.isEmpty()) {
			ChainNode node = toVisit.poll();
			if (visited.contains(node))
				continue;
			visited.add(node);
			
			for (Entry<RuleDataEps, ChainNode> data : node.eps.entrySet()) {
				toVisit.add(data.getValue());
			}
			
			for (Entry<RuleData, Node> data : node.rules.entrySet()) {
				result.rules.put(data.getKey(), data.getValue());
			}
			
		}
		
		return result;
	}
	
	public List<RuleDataEps> getEpsRulesSkipped(RuleData item) {
		List<RuleDataEps> result = null;
		for (Entry<RuleDataEps, ChainNode> data : eps.entrySet()) {
			List<RuleDataEps> res = data.getValue().getEpsRulesSkipped(item);
			if (res != null) {
				result = res;
				result.add(data.getKey());
				break;
			}
		}
		if (result == null && rules.containsKey(item)) {
			result = new ArrayList<>();
		}
		return result;
	}

	public ChainNode add(RuleData data) {
		ChainNode node = new ChainNode();
		rules.put(data, node);
		return node;
	}
	
	public ChainNode add(RuleDataEps data) {
		ChainNode node = new ChainNode();
		eps.put(data, node);
		return node;
	}
	
}
