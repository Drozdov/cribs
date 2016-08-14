package ru.cribs.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WalkGraphNode {
	
	public Map<RuleData, WalkGraphNode> edges = new HashMap<>();
	public Set<Node> reduced = new HashSet<>();
	
	public boolean tryRuduce(Automaton automaton) {
		return tryReduce(automaton.getStart(), this);
	}
	
	protected boolean tryReduce(Node node, WalkGraphNode start) {
		boolean result = false;
		if (node.isTermainal && !reduced.contains(node)) {
			reduced.add(node);
			result = true;
			start.reduce();
		}
		for (Entry<RuleData, WalkGraphNode> entry : edges.entrySet()) {
			RuleData data = entry.getKey();
			Node nextInAutomaton = node.getRule(data);
			if (nextInAutomaton == null)
				continue;
			WalkGraphNode nextInGraph = entry.getValue();
			result |= nextInGraph.tryReduce(nextInAutomaton, start);
		}
		return result;
	}
	
	private void reduce() {
		
	}
}
