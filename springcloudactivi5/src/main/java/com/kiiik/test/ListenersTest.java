package com.kiiik.test;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import com.kiiik.listener.ExectuionListenerDemo;
import com.kiiik.listener.TaskListenerDemo;

public class ListenersTest  extends PluggableActivitiTestCase {
	@Test
	@Deployment(resources = "listener.bpmn")
	public void test() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myprocessListener", new ExectuionListenerDemo());
		map.put("mytaskListener", new TaskListenerDemo());
		map.put("user", "mpc");
		runtimeService.startProcessInstanceByKey("myProcess", map);
		taskService.complete(taskService.createTaskQuery().singleResult()
				.getId());
		;
	}
}
