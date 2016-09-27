package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	
	public void reduce(Automaton automaton) {
		boolean changing = true;
		while (changing) {
			changing = false;
			for (WalkGraphNode node : allNodes()) {
				changing |= node.tryReduce(automaton);
			}
		}
	}
	
	public Iterable<WalkGraphNode> allNodes() {
		
		return new Iterable<WalkGraphNode>() {
			
			@Override
			public Iterator<WalkGraphNode> iterator() {
				return new Iterator<WalkGraphNode>() {
					
					private Queue<WalkGraphNode> nodes;
					
					private void checkQueue() {
						if (nodes == null) {
							nodes = new LinkedList<>();
							nodes.add(nodeStart);
 						}
					}
					
					@Override
					public WalkGraphNode next() {
						if (!hasNext())
							return null;
						WalkGraphNode result = nodes.poll();
						for (WalkGraphNode node : result.edges.values()) {
							nodes.add(node);
						}
						return result;
					}
					
					@Override
					public boolean hasNext() {
						checkQueue();
						return !nodes.isEmpty();
					}
				};
			}
		};
		
	}
	
}
