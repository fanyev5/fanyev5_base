package cn.com.trends.baseservice.impl;

import java.util.List;

import cn.com.fanyev5.basecommons.enums.IndexedEnum;


public enum AdChangeRuleEnum implements IndexedEnum {

	/**
	 * 重载
	 */
	RELOAD(0);

	private static final List<AdChangeRuleEnum> INDEXS = IndexedEnumUtil
			.toIndexes(AdChangeRuleEnum.values());

	/**
	 * 索引
	 */
	private final int index;

	AdChangeRuleEnum(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * 根据index取得相应的Enum
	 * 
	 * @param index
	 * @return
	 */
	public static AdChangeRuleEnum indexOf(final int index) {
		return IndexedEnumUtil.valueOf(INDEXS, index);
	}
}
