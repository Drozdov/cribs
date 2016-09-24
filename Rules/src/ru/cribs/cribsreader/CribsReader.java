package ru.cribs.cribsreader;

import java.util.ArrayList;
import java.util.List;

import ru.cribs.automaton.RuleData;

public class CribsReader extends CribsReaderBaseVisitor<RuleData> {

	public final List<RuleData> ruleDatas = new ArrayList<>();

	@Override
	public RuleData visitSimpleRule(CribsReaderParser.SimpleRuleContext ctx) {
		RuleData ruleData = new RuleData(true, ctx.getText());
		ruleDatas.add(ruleData);
		return ruleData;
	}

}
