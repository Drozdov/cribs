package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.List;

public class WalkGraph {

	private final List<WalkGraphNode> nodes = new ArrayList<>();
	private final WalkGraphNode nodeStart, nodeEnd;
	
	public WalkGraph(Iterable<RuleData> initialSequence) {
		nodeStart = new WalkGraphNode();
		nodes.add(nodeStart);
		WalkGraphNode current = nodeStart;
		for (RuleData data : initialSequence) {
			WalkGraphNode node = new WalkGraphNode();
			nodes.add(node);
			current.edges.put(data, node);
			current = node;
		}
		nodeEnd = current;
	}
	
	
	
}
