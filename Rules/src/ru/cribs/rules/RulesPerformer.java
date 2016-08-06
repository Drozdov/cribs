package ru.cribs.rules;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.automaton.AutomatonData;
import ru.cribs.automaton.Rule;
import ru.cribs.automaton.RuleDataAutomaton;
import ru.cribs.rules.RulesParser.ElementLeftContext;
import ru.cribs.rules.RulesParser.ElementRightContext;
import ru.cribs.rules.RulesParser.ElementsLeftContext;
import ru.cribs.rules.RulesParser.ElementsRightContext;
import ru.cribs.rules.RulesParser.IdContext;
import ru.cribs.rules.RulesParser.SimpleElementLeftContext;
import ru.cribs.rules.RulesParser.SimpleRuleContext;
import ru.cribs.rules.RulesParser.StringLiteralContext;

public class RulesPerformer extends RulesBaseVisitor<Rule>
{
	
	private final List<Rule> rules = new ArrayList<>();

	@Override
	public Rule visitStringLiteral(StringLiteralContext ctx) {
		String name = ctx.getText();
		name = name.substring(1, name.length() - 1);
		return new Rule(new RuleDataAutomaton(new AutomatonData(true, name)));
	}

	@Override
	public Rule visitId(IdContext ctx) {
		return new Rule(new RuleDataAutomaton(new AutomatonData(false, ctx.getText())));
	}

	@Override
	public Rule visitElementsLeft(ElementsLeftContext ctx) {
		Rule rule = new Rule();
		for (ParseTree child : ctx.children){
			Rule childRule = visit(child);
			if (childRule != null) {
				rule.concat(childRule);
			}
		}
		
		return rule;
	}

	@Override
	public Rule visitElementLeft(ElementLeftContext ctx) {
		Rule rule = visit(ctx.element);
		
		if (ctx.addition != null) {
			switch (ctx.addition.getText()) {
			case "+":
				rule.cycle();
				break;
			case "*":
				rule.star();
				break;
			case "?":
				rule.allowSkip();
				break;
			}
		}
		
		if (ctx.identifier != null) {
			rule.name(ctx.identifier.getText());
		}
		
		return rule;
	}

	@Override
	public Rule visitSimpleElementLeft(SimpleElementLeftContext ctx) {
		return visit(ctx.element1 != null ? ctx.element1 : ctx.element2);
	}

	@Override
	public Rule visitSimpleRule(SimpleRuleContext ctx) {
		Rule rule = visit(ctx.left);
		Rule chain = visit(ctx.right);
		rule.setChain(chain);
		rules.add(rule);
		return rule;
	}

	@Override
	public Rule visitElementsRight(ElementsRightContext ctx) {
		Rule rule = new Rule();
		for (ParseTree child : ctx.children){
			Rule childRule = visit(child);
			if (childRule != null) {
				rule.concat(childRule);
			}
		}

		return rule;
	}

	@Override
	public Rule visitElementRight(ElementRightContext ctx) {
		Rule result = visit(ctx.element);
		if (ctx.block != null) {
			result.comment = ctx.block.getText();
		}
		return result;
	}

	public Iterable<Rule> getRules(ParseTree tree) {
		rules.clear();
		visit(tree);
		return rules;
	}
		
}