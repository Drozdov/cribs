package ru.cribs.automaton;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Node<T> {
	
	private static <T> Iterator<T> fromArray(T[] array) {
		return Arrays.asList(array).iterator();
	}
	
	protected Map<T, Node<T>> rules = new HashMap<>();
	protected boolean isTermainal;
	protected int magic;
	
	public static <T> Node<T> create(Iterator<T> values) {
		if (!values.hasNext()) {
			Node<T> node = new Node<>();
			node.isTermainal = true;
			return node;
		}
		
		Node<T> node = new Node<>();
		T key = values.next();
		node.rules.put(key, create(values));
		return node;
	}
	
	public Node<T> get(Iterator<T> items) {
		if (!items.hasNext()) {
			return isTermainal ? this : null;
		}
		T key = items.next();
		Node<T> node = getRule(key);
		if (node == null)
			return null;
		return node.get(items);
	}
	
	public boolean test(T[] items) {
		return get(fromArray(items)) != null;
	}
	
	public Node<T> getRule(T item) {
		if (!rules.containsKey(item))
			return null;
		return rules.get(item);
	}
	
	public Collection<T> getRulesData() {
		return rules.keySet();
	}
	
	public Collection<Node<T>> getChildren() {
		return rules.values();
	}
	
	public Iterable<Node<T>> getAllDescendents() {
		final Node<T> cur = this;
		return new Iterable<Node<T>>() {

			@Override
			public Iterator<Node<T>> iterator() {
				return new BfsNodeIterator(cur);
			}
		};
	}
	
	public void saveToDot(String file) {
		try (PrintWriter out = new PrintWriter(file)) {
			out.println("digraph g {");
			getGraphEdges(out, new LabelsGenerator(), MagicGenerator.generateMagic());
			out.println("}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void getGraphEdges(PrintWriter out, LabelsGenerator labelsGenerator, int magic)
	{
		if (this.magic == magic)
			return;
		this.magic = magic;
		
		String nodeLabel = labelsGenerator.getLabel(this);
		for (Entry<T, Node<T>> entry : getAllRules().entrySet()) {
			String nodeLabel2 = labelsGenerator.getLabel(entry.getValue());
			out.println(String.format("%s -> %s [label = %s]", nodeLabel, nodeLabel2, entry.getKey().toString()));
			entry.getValue().getGraphEdges(out, labelsGenerator, magic);
		}
	}
	
	protected Map<T, Node<T>> getAllRules() {
		return rules;
	}
	
	protected class LabelsGenerator {
		
		private int cur;
		private final HashMap<Node<T>, String> labels = new HashMap<>();
		
		public String generateLabel() {
			return (char)('A' + cur / 10) + "" + (cur++ % 10);
		}
		
		public String getLabel(Node<T> node) {
			if (!labels.containsKey(node)) {
				labels.put(node, generateLabel());
			}
			return labels.get(node);
		}
	}
	
	private class BfsNodeIterator implements Iterator<Node<T>> {

		private int magic = MagicGenerator.generateMagic();
		private Queue<Node<T>> bfsQueue = new LinkedList<>();
		
		public BfsNodeIterator(Node<T> root) {
			bfsQueue.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !bfsQueue.isEmpty();
		}

		@Override
		public Node<T> next() {
			Node<T> elem = bfsQueue.poll();
			for (Node<T> child : elem.getChildren()) {
				if (child.magic != magic) {
					bfsQueue.add(child);
					child.magic = magic;
				}
			}
			return elem;
		}
		
	}
	
}
