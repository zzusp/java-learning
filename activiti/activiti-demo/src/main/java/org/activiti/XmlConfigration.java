package org.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description activiti spring容器启动
 * @date Created in 19:16 2020/8/9
 * @modified By
 */
public class XmlConfigration {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		// 通过name获取bean
		RepositoryService repositoryService = context.getBean(RepositoryService.class);
		// 创建一个部署对象
		Deployment deployment = repositoryService.createDeployment()
				.name("请假流程")
				.key("holiday")
				.addClasspathResource("processes/holiday.bpmn20.xml")
				.deploy();
		System.out.println("部署ID：" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
		// 得到RuntimeService方法
		RuntimeService runtimeService = context.getBean(RuntimeService.class);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
		System.out.println(processInstance.getId());
		System.out.println(processInstance.getProcessDefinitionId());
		System.out.println(processInstance.getProcessDefinitionKey());

		TaskService taskService = context.getBean(TaskService.class);
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("holiday").list();
		for (Task task : list) {
			System.out.println(task.getId());
			System.out.println(task.getName());
		}
		taskService.complete(list.get(0).getId());
	}

}
