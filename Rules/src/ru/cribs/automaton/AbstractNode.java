package ru.cribs.automaton;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

/**
 * Note: ANode must be of the same type as this
 * @author Vladimir
 *  * @param <ANode>
 */
public abstract class AbstractNode<ANode extends AbstractNode<ANode>> {

	protected Map<RuleData, ANode> edges = new HashMap<>();
	protected int magic;
	
	public Iterable<ANode> getAllDescendents() {
		@SuppressWarnings("unchecked")
		ANode cur = (ANode) this;
		return new Iterable<ANode>() {

			@Override
			public Iterator<ANode> iterator() {
				return new BfsNodeIterator(cur);
			}
		};
	}
	
	public void saveToDot(String file) {
		try (PrintWriter out = new PrintWriter(file)) {
			out.println("digraph g {");
			printGraphEdges(out, new LabelsGenerator(), MagicGenerator.generateMagic());
			out.println("}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<RuleData> getRulesData() {
		return edges.keySet();
	}
	
	public Collection<ANode> getChildren() {
		return edges.values();
	}
	
	protected void printGraphEdges(PrintWriter out, LabelsGenerator labelsGenerator, int magic)
	{
		if (this.magic == magic)
			return;
		this.magic = magic;
		
		String nodeLabel = labelsGenerator.getLabel(this);
		for (PrintingRuleStructure toPrint : getPrintingStructures()) {
			String nodeLabel2 = labelsGenerator.getLabel(toPrint.destNode);
			out.println(String.format("%s -> %s [label = \"%s\"]",
					nodeLabel, nodeLabel2, toPrint.label));
			toPrint.destNode.printGraphEdges(out, labelsGenerator, magic);
		}
	}
	
	protected List<PrintingRuleStructure> getPrintingStructures() {
		List<PrintingRuleStructure> result = new ArrayList<PrintingRuleStructure>();
		for (Entry<RuleData, ANode> entry : edges.entrySet()) {
			result.add(new PrintingRuleStructure(entry.getKey().toString(), entry.getValue()));
		}
		return result;
	}
	
	protected class PrintingRuleStructure {
		public final String label;
		public final AbstractNode<ANode> destNode;
		
		public PrintingRuleStructure(String label, AbstractNode<ANode> destNode) {
			super();
			this.label = label;
			this.destNode = destNode;
		}
		
	}
	
	protected class LabelsGenerator {
		
		private int cur;
		private final HashMap<AbstractNode<ANode>, String> labels = new HashMap<>();
		
		public String generateLabel() {
			return (char)('A' + cur / 10) + "" + (cur++ % 10);
		}
		
		public String getLabel(AbstractNode<ANode> node) {
			if (!labels.containsKey(node)) {
				labels.put(node, generateLabel());
			}
			return labels.get(node);
		}
	}
	
	private class BfsNodeIterator implements Iterator<ANode> {

		private int magic = MagicGenerator.generateMagic();
		private Queue<ANode> bfsQueue = new LinkedList<>();
		
		public BfsNodeIterator(ANode root) {
			bfsQueue.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !bfsQueue.isEmpty();
		}

		@Override
		public ANode next() {
			ANode elem = bfsQueue.poll();
			for (ANode child : elem.getChildren()) {
				if (child.magic != magic) {
					bfsQueue.add(child);
					child.magic = magic;
				}
			}
			return elem;
		}
		
	}

		
}
