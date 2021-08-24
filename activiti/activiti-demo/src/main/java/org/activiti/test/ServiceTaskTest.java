package org.activiti.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("serviceTaskTest")
public class ServiceTaskTest implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(execution.getCurrentActivityId());
        System.out.println(execution.getVariables());
    }
}
