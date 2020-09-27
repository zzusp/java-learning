package org.activiti;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
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
//		Deployment deployment = repositoryService.createDeployment()
//				.name("请假流程")
//				.key("holiday")
//				.addClasspathResource("processes/holiday.bpmn20.xml")
//				.deploy();
//		System.out.println("部署ID：" + deployment.getId());
//		System.out.println("部署名称：" + deployment.getName());
		// 得到RuntimeService方法
//		RuntimeService runtimeService = context.getBean(RuntimeService.class);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");
//		System.out.println(processInstance.getId());
//		System.out.println(processInstance.getProcessDefinitionId());
//		System.out.println(processInstance.getProcessDefinitionKey());

//		TaskService taskService = context.getBean(TaskService.class);
//		List<Task> list = taskService.createTaskQuery().processDefinitionKey("holiday").list();
//		for (Task task : list) {
//			System.out.println(task.getId());
//			System.out.println(task.getName());
//		}
//		taskService.complete(list.get(0).getId());

		XmlConfigration xmlConfigration = new XmlConfigration();
		// 创建一个部署对象
//		BpmnModel bpmnModel = xmlConfigration.deployeFromStr();
//		Deployment deployment = repositoryService.createDeployment()
//				.name("请假流程")
//				.key("vocation")
//				.addBpmnModel("vocation.bpmn20.xml", bpmnModel)
//				.deploy();
//		System.out.println("部署ID：" + deployment.getId());
//		System.out.println("部署名称：" + deployment.getName());

		// 得到RuntimeService方法
//		RuntimeService runtimeService = context.getBean(RuntimeService.class);
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
//		System.out.println(processInstance.getId());
//		System.out.println(processInstance.getProcessDefinitionId());
//		System.out.println(processInstance.getProcessDefinitionKey());

		TaskService taskService = context.getBean(TaskService.class);
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("Process_1").list();
		for (Task task : list) {
			System.out.println(task.getId());
			System.out.println(task.getName());
		}
		taskService.setAssignee(list.get(0).getId(), "1234");
		taskService.complete(list.get(0).getId());
	}

	private BpmnModel deployeFromStr() {
		BpmnModel bpmnModel = new BpmnModel();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new ByteArrayInputStream(BPM_XML.getBytes()));
			bpmnModel = new BpmnXMLConverter().convertToBpmnModel(reader);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		// 设置目标命名空间
		bpmnModel.setTargetNamespace("http://activiti.org/test");

		//验证bpmnModel 是否是正确的bpmn xml文件
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		//验证失败信息的封装ValidationError
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);

		System.out.println(validate.size());
		return bpmnModel;
	}

	/** 测试用bpm */
	private static final String BPM_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" id=\"sid-38422fae-e03e-43a3-bef4-bd33b32041b2\" targetNamespace=\"http://bpmn.io/bpmn\" exporter=\"bpmn-js (https://demo.bpmn.io)\" exporterVersion=\"7.3.0\">\n" +
			"  <process id=\"Process_1\" isExecutable=\"true\">\n" +
			"    <startEvent id=\"StartEvent_1y45yut\" name=\"开始\" activiti:candidateUsers=\"hunger noticed\">\n" +
			"      <outgoing>Flow_0ymk4yj</outgoing>\n" +
			"    </startEvent>\n" +
			"    <sequenceFlow id=\"SequenceFlow_0wnb4ke\" sourceRef=\"Task_1hcentk\" targetRef=\"Event_0b29i69\" />\n" +
			"    <sequenceFlow id=\"Flow_0ymk4yj\" sourceRef=\"StartEvent_1y45yut\" targetRef=\"Activity_15y4tje\" />\n" +
			"    <endEvent id=\"Event_0b29i69\">\n" +
			"      <incoming>SequenceFlow_0wnb4ke</incoming>\n" +
			"    </endEvent>\n" +
			"    <userTask id=\"Task_1hcentk\" name=\"上级领导审批\" activiti:candidateUsers=\"1234\">\n" +
			"      <incoming>Flow_1ixwoc7</incoming>\n" +
			"      <outgoing>SequenceFlow_0wnb4ke</outgoing>\n" +
			"    </userTask>\n" +
			"    <userTask id=\"Activity_15y4tje\" name=\"请假申请\">\n" +
			"      <incoming>Flow_0ymk4yj</incoming>\n" +
			"      <outgoing>Flow_1ixwoc7</outgoing>\n" +
			"    </userTask>\n" +
			"    <sequenceFlow id=\"Flow_1ixwoc7\" sourceRef=\"Activity_15y4tje\" targetRef=\"Task_1hcentk\" />\n" +
			"  </process>\n" +
			"  <bpmndi:BPMNDiagram id=\"BpmnDiagram_1\">\n" +
			"    <bpmndi:BPMNPlane id=\"BpmnPlane_1\" bpmnElement=\"Process_1\">\n" +
			"      <bpmndi:BPMNEdge id=\"Flow_1ixwoc7_di\" bpmnElement=\"Flow_1ixwoc7\">\n" +
			"        <omgdi:waypoint x=\"510\" y=\"300\" />\n" +
			"        <omgdi:waypoint x=\"590\" y=\"300\" />\n" +
			"      </bpmndi:BPMNEdge>\n" +
			"      <bpmndi:BPMNEdge id=\"Flow_0ymk4yj_di\" bpmnElement=\"Flow_0ymk4yj\">\n" +
			"        <omgdi:waypoint x=\"318\" y=\"300\" />\n" +
			"        <omgdi:waypoint x=\"410\" y=\"300\" />\n" +
			"      </bpmndi:BPMNEdge>\n" +
			"      <bpmndi:BPMNEdge id=\"SequenceFlow_0wnb4ke_di\" bpmnElement=\"SequenceFlow_0wnb4ke\">\n" +
			"        <omgdi:waypoint x=\"690\" y=\"300\" />\n" +
			"        <omgdi:waypoint x=\"772\" y=\"300\" />\n" +
			"      </bpmndi:BPMNEdge>\n" +
			"      <bpmndi:BPMNShape id=\"StartEvent_1y45yut_di\" bpmnElement=\"StartEvent_1y45yut\">\n" +
			"        <omgdc:Bounds x=\"282\" y=\"282\" width=\"36\" height=\"36\" />\n" +
			"        <bpmndi:BPMNLabel>\n" +
			"          <omgdc:Bounds x=\"290\" y=\"325\" width=\"22\" height=\"14\" />\n" +
			"        </bpmndi:BPMNLabel>\n" +
			"      </bpmndi:BPMNShape>\n" +
			"      <bpmndi:BPMNShape id=\"Activity_1xu0joe_di\" bpmnElement=\"Task_1hcentk\">\n" +
			"        <omgdc:Bounds x=\"590\" y=\"260\" width=\"100\" height=\"80\" />\n" +
			"      </bpmndi:BPMNShape>\n" +
			"      <bpmndi:BPMNShape id=\"Activity_15y4tje_di\" bpmnElement=\"Activity_15y4tje\">\n" +
			"        <omgdc:Bounds x=\"410\" y=\"260\" width=\"100\" height=\"80\" />\n" +
			"      </bpmndi:BPMNShape>\n" +
			"      <bpmndi:BPMNShape id=\"Event_0b29i69_di\" bpmnElement=\"Event_0b29i69\">\n" +
			"        <omgdc:Bounds x=\"772\" y=\"282\" width=\"36\" height=\"36\" />\n" +
			"      </bpmndi:BPMNShape>\n" +
			"    </bpmndi:BPMNPlane>\n" +
			"  </bpmndi:BPMNDiagram>\n" +
			"</definitions>";

}
