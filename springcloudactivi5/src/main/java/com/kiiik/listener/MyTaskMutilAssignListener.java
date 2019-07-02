package com.kiiik.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
//https://blog.csdn.net/yangschfly/article/details/78744577
public class MyTaskMutilAssignListener  implements TaskListener{
	 /**
	 * task创建的时候 进入会签节点的[那条线]里配置一个take的监听
	 */
	private static final long serialVersionUID = 1L;
	//工作流会签时,所有的都审批通过才可进入下一环节：
	public void notify(DelegateTask delegateTask) {
	        System.out.println("delegateTask.getEventName() = " + delegateTask.getEventName());
	         //添加会签的人员，所有的都审批通过才可进入下一环节
	        List<String> assigneeList = new ArrayList<String>();
	        assigneeList.add("wangba");
	        assigneeList.add("wangjiu");
	        delegateTask.setVariable("publicityList",assigneeList);
	    }
}
