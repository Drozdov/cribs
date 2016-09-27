package ru.cribs.automaton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ru.cribs.helpers.ConcatIterable;

public class WalkGraphNode extends AbstractNode<WalkGraphNode> {
	
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

}
