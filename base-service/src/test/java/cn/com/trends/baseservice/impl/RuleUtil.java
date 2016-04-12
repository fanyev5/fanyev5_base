package cn.com.trends.baseservice.impl;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 自定义规则工具
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-25
 */
public final class RuleUtil {

	private RuleUtil() {
	}

	/**
	 * 生成广告变更规则串
	 * 
	 * @return
	 */
	public static String genAdChangeRule(AdChangeRule changeRule) {
		Preconditions.checkNotNull(changeRule);
		String rule;
		switch (changeRule.getRuleEnum()) {
		case RELOAD:
			rule = String.valueOf(changeRule.getRuleEnum().getIndex());
			break;
		default:
			throw new IllegalArgumentException();
		}
		return rule;
	}

	/**
	 * 解析广告变更规则
	 * 
	 * @return
	 */
	public static AdChangeRule parseAdChangeRule(String ruleStr) {
		try {
			String[] rules = StringUtils.split(ruleStr, ',');
			AdChangeRuleEnum ruleEnum = AdChangeRuleEnum.indexOf(Integer
					.parseInt(rules[0]));
			Preconditions.checkNotNull(ruleEnum);
			String content = "";
			if (rules.length >= 2) {
				content = rules[2];
			}
			return new AdChangeRule(ruleEnum, content);
		} catch (Exception e) {
			throw new RuntimeException(String.format(
					"Parse ad change rule exception. rule: %s", ruleStr), e);
		}
	}
}
