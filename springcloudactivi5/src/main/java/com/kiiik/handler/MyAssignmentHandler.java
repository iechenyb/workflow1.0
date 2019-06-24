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
		
		
		当分配不复杂时，用户和组的设置非常麻烦。 为避免复杂性，可以使用用户任务的自定义扩展。

		assignee属性：这个自定义扩展可以直接把用户任务分配给指定用户。
		
		<userTask id="theTask" name="my task" activiti:assignee="kermit" />
		它和使用上面定义的humanPerformer 效果完全一样。
		
		candidateUsers属性：这个自定义扩展可以为任务设置候选人。
		
		<userTask id="theTask" name="my task" activiti:candidateUsers="kermit, gonzo" />
		它和使用上面定义的potentialOwner 效果完全一样。 注意它不需要像使用potentialOwner通过user(kermit)声明， 因为这个属性只能用于人员。
		
		candidateGroups属性：这个自定义扩展可以为任务设置候选组。
		
		<userTask id="theTask" name="my task" activiti:candidateGroups="management, accountancy" />
		它和使用上面定义的potentialOwner 效果完全一样。 注意它不需要像使用potentialOwner通过group(management)声明， 因为这个属性只能用于群组。
		
		candidateUsers 和 candidateGroups 可以同时设置在同一个用户任务中。
		
		注意：虽然activiti提供了一个账号管理组件， 也提供了IdentityService， 但是账号组件不会检测设置的用户是否村爱。 它嵌入到应用中，也允许activiti与其他已存的账户管理方案集成。
		
		如果上面的方式还不满足需求，还可以使用创建事件的任务监听器 来实现自定义的分配逻辑：
	 */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
	    delegateTask.setAssignee("kermit");
	    delegateTask.addCandidateUser("fozzie");
	    delegateTask.addCandidateGroup("management");
	  }

	}
