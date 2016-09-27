package ru.cribs.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Uniter {
	
	// Yeah, map inside another map seems to be rather complicated, but what to do?
	// Node -> (Node -> Node)
	private Map<Node, Map<Node, Node>> map;
	
	public Node unite(Node node1, Node node2) {
		map = new HashMap<>();
		Node result = new Node();
		unite(result, node1, node2, MagicGenerator.generateMagic());
		return result;
	}
	
	private Node getUnion(Node node1, Node node2) {
		if (!map.containsKey(node1)) {
			map.put(node1, new HashMap<Node, Node>());
		}
		Map<Node, Node> map2 = map.get(node1);
		if (!map2.containsKey(node2)) {
			Node node = new Node();
			map2.put(node2, node);
		}
		return map2.get(node2);
	}
	
	private void unite(Node current, Node node1, Node node2, int magic) {
		Set<RuleData> commonData = new HashSet<>(node1.getRulesData());
		commonData.retainAll(node2.getRulesData());
		
		for (RuleData data : commonData) {
			Node nd1 = node1.getRule(data);
			Node nd2 = node2.getRule(data);
			Node united = getUnion(nd1, nd2);
			current.edges.put(data, united);
			
			if (united.magic == magic) {
				continue;
			}
			united.magic = magic;
			unite(united, nd1, nd2, magic);
		}
		
		Collection<RuleData> data1 = node1.getRulesData();
		Collection<RuleData> data2 = node2.getRulesData();
		data1.removeAll(commonData);
		data2.removeAll(commonData);
		
		for (RuleData data : data1) {
			current.edges.put(data, node1.getRule(data));
		}
		for (RuleData data : data2) {
			current.edges.put(data, node2.getRule(data));
		}
		
		current.isTermainal = node1.isTermainal || node2.isTermainal;
	}
}
