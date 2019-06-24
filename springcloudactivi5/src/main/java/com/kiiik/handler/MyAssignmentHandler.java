package com.kiiik.handler;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
public class MyAssignmentHandler implements TaskListener {

	  /**
	 * <userTask id="task1" name="My task" >
		  <extensionElements>
		    <activiti:taskListener event="create" class="org.activiti.MyAssignmentHandler" />
		  </extensionElements>
		</userTask>
	 */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
	    delegateTask.setAssignee("kermit");
	    delegateTask.addCandidateUser("fozzie");
	    delegateTask.addCandidateGroup("management");
	  }

	}
