package ru.cribs.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Uniter<T> {
	
	// Yeah, map inside another map seems to be rather complicated, but what to do?
	// Node<T> -> (Node<T> -> Node<T>)
	private Map<Node<T>, Map<Node<T>, Node<T>>> map;
	
	public Node<T> unite(Node<T> node1, Node<T> node2) {
		map = new HashMap<>();
		Node<T> result = new Node<T>();
		unite(result, node1, node2, MagicGenerator.generateMagic());
		return result;
	}
	
	private Node<T> getUnion(Node<T> node1, Node<T> node2) {
		if (!map.containsKey(node1)) {
			map.put(node1, new HashMap<>());
		}
		Map<Node<T>, Node<T>> map2 = map.get(node1);
		if (!map2.containsKey(node2)) {
			Node<T> node = new Node<>();
			map2.put(node2, node);
		}
		return map2.get(node2);
	}
	
	private void unite(Node<T> current, Node<T> node1, Node<T> node2, int magic) {
		Set<T> commonData = new HashSet<>(node1.getRulesData());
		commonData.retainAll(node2.getRulesData());
		
		for (T data : commonData) {
			Node<T> nd1 = node1.getRule(data);
			Node<T> nd2 = node2.getRule(data);
			Node<T> united = getUnion(nd1, nd2);
			current.rules.put(data, united);
			
			if (united.magic == magic) {
				continue;
			}
			united.magic = magic;
			unite(united, nd1, nd2, magic);
		}
		
		Collection<T> data1 = node1.getRulesData();
		Collection<T> data2 = node2.getRulesData();
		data1.removeAll(commonData);
		data2.removeAll(commonData);
		
		for (T data : data1) {
			current.rules.put(data, node1.getRule(data));
		}
		for (T data : data2) {
			current.rules.put(data, node2.getRule(data));
		}
		
		current.isTermainal = node1.isTermainal || node2.isTermainal;
	}
}
