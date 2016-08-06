package ru.cribs.automaton;

import java.util.HashMap;
import ru.cribs.automaton.RuleDataDeclaration.DeclarationType;

public class Rule {

	public RuleNode nodeStart, nodeEnd;
	private Rule chain;
	public String comment;
	
	public Rule() {
		nodeStart = nodeEnd = new RuleNode();
	}
	
	public Rule(RuleData data) {
		nodeStart = new RuleNode();
		nodeEnd = new RuleNode();
		nodeStart.rules.put(data, nodeEnd);
	}
	
	public void concat(Rule chain) {
		nodeEnd.eps.put(new RuleDataEps(), chain.nodeStart);
		nodeEnd = chain.nodeEnd;
	}
	
	public void parallel(Rule chain) {
		nodeStart.eps.put(new RuleDataEps(), chain.nodeStart);
		chain.nodeEnd.eps.put(new RuleDataEps(), nodeEnd);
	}
	
	public void cycle() {
		nodeEnd.eps.put(new RuleDataEps(), nodeStart);
	}
	
	public void allowSkip() {
		nodeStart.eps.put(new RuleDataEps(), nodeEnd);
	}
	
	public void star() {
		cycle();
		allowSkip();
	}
	
	public void name(String name) {
		RuleNode start = new RuleNode();
		RuleNode end = new RuleNode();

		start.eps.put(new RuleDataDeclaration(name, DeclarationType.BEGIN), nodeStart);
		nodeEnd.eps.put(new RuleDataDeclaration(name, DeclarationType.END), end);
		
		nodeStart = start;
		nodeEnd = end;

	}
	
	public Rule getChain() {
		return chain;
	}
	
	public void setChain(Rule chain) {
		this.chain = chain;
		nodeEnd.isTermainal = true;
	}

	public Node<AutomatonData> toAutomaton() {
		nodeEnd.isTermainal = true;
		Node<AutomatonData> data = new Node<>();
		NodesMap map = new NodesMap();
		map.put(nodeStart, data);
		
		for (Node<RuleData> node : nodeStart.getAllDescendents()) {
			Node<AutomatonData> automatonNode = map.getSafe(node);
			automatonNode.isTermainal = node.isTermainal;
			
			for (RuleData curData : node.getRulesData()) {
				if (curData.isEps()) // TODO: this must be not necessary, verify and remove later
					continue;
				Node<RuleData> rule = node.getRule(curData);
				Node<AutomatonData> automatonNode2 = map.getSafe(rule);
				automatonNode.rules.put(curData.createAutomatonData(), automatonNode2);
			}
		}
		return data;
	}
	
	private class NodesMap extends HashMap<Node<RuleData>, Node<AutomatonData>> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Node<AutomatonData> getSafe(Node<RuleData> origNode) {
			if (containsKey(origNode))
				return get(origNode);
			
			Node<AutomatonData> node = new Node<>();
			put(origNode, node);
			return node;
		}
	}
	
}
