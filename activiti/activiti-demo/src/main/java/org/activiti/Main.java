package org.activiti;

import org.springframework.util.CollectionUtils;

import java.util.*;

public class Main {

	private Map<String, Set<String>> map;

	public void setMap(Map<String, Set<String>> map) {
		this.map = map;
	}

	public static void main(String[] args) {
		Main main = new Main();
		Map<String, Set<String>> map = new HashMap<>();
		map.put("1", Collections.singleton("2"));
		map.put("2", Collections.singleton("3"));
		map.put("3", Collections.singleton("4"));
		map.put("5", Collections.singleton("4"));
		map.put("4", Collections.singleton("5"));
		main.setMap(map);
		Set<String> checked = new HashSet<>();
		checked.add("1");
		List<String> result = main.checkRelation("1", "5", checked, new LinkedList<>());
		if (!CollectionUtils.isEmpty(result)) {
			result.add("1");
		}
		Collections.reverse(result);
		Iterator<String> itr = result.iterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	/**
	 * 检查是否有关联
	 *
	 * @param code code
	 * @param otherCode 表名
	 * @param checked 检查过的code集合
	 * @return 是否有关联
	 */
	private LinkedList<String> checkRelation(String code, String otherCode, Set<String> checked, LinkedList<String> result) {
		if (CollectionUtils.isEmpty(map)) {
			return new LinkedList<>();
		}
		Set<String> relations = map.get(code);
		// 判断是否关联
		if (relations.contains(otherCode)) {
			result.add(otherCode);
			return result;
		}
		for (String relation : relations) {
			// 跳过已检查过的数据
			if (checked.contains(relation)) {
				continue;
			}
			checked.add(relation);
			// 判断是否找到（递归调用）
			if (!CollectionUtils.isEmpty(checkRelation(relation, otherCode, checked, result))) {
				// 如果找到，设置返回结果
				result.add(relation);
				return result;
			}
		}
		return new LinkedList<>();
	}

}
