package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WalkGraphNode {
	
	public Map<RuleData, WalkGraphNode> edges = new HashMap<>();
	public Set<Node> reduced = new HashSet<>();
	
	public boolean tryReduce(Automaton automaton) {
		Node start = automaton.getStart();
		return tryReduce(automaton, start, this, new ArrayList<>());
	}
	
	protected boolean tryReduce(Automaton automaton, Node curNode, WalkGraphNode start,
			Iterable<RuleData> datas) {
		boolean result = false;
		if (curNode.isTermainal && !reduced.contains(curNode)) {
			reduced.add(curNode);
			result = true;
			List<RuleData> destDatas = automaton.getSequence(curNode, datas);
			start.reduce(destDatas, this);
		}
		for (Entry<RuleData, WalkGraphNode> entry : edges.entrySet()) {
			RuleData data = entry.getKey();
			Node nextInAutomaton = curNode.getRule(data);
			if (nextInAutomaton == null)
				continue;
			WalkGraphNode nextInGraph = entry.getValue();
			result |= nextInGraph.tryReduce(automaton, nextInAutomaton, start,
					new ConcatIterable<RuleData>(datas, data));
		}
		return result;
	}
	
	private void reduce(List<RuleData> destDatas, WalkGraphNode endNode) {
		WalkGraphNode current = this;
		RuleData lastData = destDatas.get(destDatas.size() - 1);
		for (RuleData data : destDatas) {
			WalkGraphNode node = data == lastData ? endNode : new WalkGraphNode();
			current.edges.put(data, node);
			current = node;
		}
	}
	
	private class ConcatIterable<T> implements Iterable<T> {

		private final Iterable<T> base;
		private final T newElement;
		
		public ConcatIterable(Iterable<T> base, T newElement) {
			this.base = base;
			this.newElement = newElement;
		}
		
		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {

				private final Iterator<T> baseIterator = base.iterator();
				private boolean newElementUsed;
				
				@Override
				public boolean hasNext() {
					return baseIterator.hasNext() || !newElementUsed;
				}

				@Override
				public T next() {
					if (baseIterator.hasNext())
						return baseIterator.next();
					newElementUsed = true;
					return newElement;
				}
			};
		}
		
	}
}
