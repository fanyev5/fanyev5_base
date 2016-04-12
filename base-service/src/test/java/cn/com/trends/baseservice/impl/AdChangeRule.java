package cn.com.trends.baseservice.impl;



public class AdChangeRule {

    private AdChangeRuleEnum ruleEnum;

    private String content;

    public AdChangeRule(AdChangeRuleEnum ruleEnum, String content) {
        this.ruleEnum = ruleEnum;
        this.content = content;
    }

    public AdChangeRuleEnum getRuleEnum() {
        return ruleEnum;
    }

    public void setRuleEnum(AdChangeRuleEnum ruleEnum) {
        this.ruleEnum = ruleEnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

