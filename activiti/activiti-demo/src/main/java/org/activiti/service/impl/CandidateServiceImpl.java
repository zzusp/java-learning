package org.activiti.service.impl;

import org.activiti.engine.exception.WorkflowException;
import org.activiti.service.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 自定义候选人候选组服务实现类
 * @date Created in 19:16 2020/8/9
 * @modified By
 */
@Service
public class CandidateServiceImpl implements CandidateService {

	/**
	 * 获取候选人
	 *
	 * @return 候选人
	 */
	@Override
	public String getCandidateUser(List<String> paramKeys) {
//		return "123";
		for (String key : paramKeys) {
			System.out.println(key);
		}
		throw new WorkflowException("12312312");
	}
}
