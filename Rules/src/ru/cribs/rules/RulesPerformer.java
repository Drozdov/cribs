package ru.cribs.rules;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ru.cribs.automaton.RuleData;
import ru.cribs.automaton.Chain;
import ru.cribs.rules.RulesParser.ElementLeftContext;
import ru.cribs.rules.RulesParser.ElementRightContext;
import ru.cribs.rules.RulesParser.ElementsLeftContext;
import ru.cribs.rules.RulesParser.ElementsRightContext;
import ru.cribs.rules.RulesParser.IdContext;
import ru.cribs.rules.RulesParser.SimpleElementLeftContext;
import ru.cribs.rules.RulesParser.SimpleRuleContext;
import ru.cribs.rules.RulesParser.StringLiteralContext;

public class RulesPerformer extends RulesBaseVisitor<Chain>
{
	
	private final List<Chain> rules = new ArrayList<>();

	@Override
	public Chain visitStringLiteral(StringLiteralContext ctx) {
		String name = ctx.getText();
		name = name.substring(1, name.length() - 1);
		return new Chain(new RuleData(true, name));
	}

	@Override
	public Chain visitId(IdContext ctx) {
		return new Chain(new RuleData(false, ctx.getText()));
	}

	@Override
	public Chain visitElementsLeft(ElementsLeftContext ctx) {
		Chain rule = new Chain();
		for (ParseTree child : ctx.children){
			Chain childRule = visit(child);
			if (childRule != null) {
				rule.concat(childRule);
			}
		}
		
		return rule;
	}

	@Override
	public Chain visitElementLeft(ElementLeftContext ctx) {
		Chain rule = visit(ctx.element);
		
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
	public Chain visitSimpleElementLeft(SimpleElementLeftContext ctx) {
		return visit(ctx.element1 != null ? ctx.element1 : ctx.element2);
	}

	@Override
	public Chain visitSimpleRule(SimpleRuleContext ctx) {
		Chain rule = visit(ctx.left);
		Chain chain = visit(ctx.right);
		rule.setChain(chain);
		rules.add(rule);
		return rule;
	}

	@Override
	public Chain visitElementsRight(ElementsRightContext ctx) {
		Chain rule = new Chain();
		for (ParseTree child : ctx.children){
			Chain childRule = visit(child);
			if (childRule != null) {
				rule.concat(childRule);
			}
		}

		return rule;
	}

	@Override
	public Chain visitElementRight(ElementRightContext ctx) {
		Chain result = visit(ctx.element);
		if (ctx.block != null) {
			result.comment = ctx.block.getText();
		}
		return result;
	}

	public Iterable<Chain> getRules(ParseTree tree) {
		rules.clear();
		visit(tree);
		return rules;
	}
		
}