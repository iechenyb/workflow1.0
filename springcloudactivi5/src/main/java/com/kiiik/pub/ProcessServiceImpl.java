package com.kiiik.pub;

import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.springframework.stereotype.Service;

@Service
public class ProcessServiceImpl  extends ActivitProcessServiceImpl{
	
	
	public void addComment(String taskId,String processInstanceId,String message){
		taskService.addComment(taskId, processInstanceId, message);
		//设置当前任务的办理人
		//Authentication.setAuthenticatedUserId("SessionContext.get().getName()");
	}
	
	public void completeTask(String taskId,Map<String,Object> variables){
		taskService.complete(taskId, variables);
	}
	
	public void testProcessDeploy(){
		//activiti支持链式编程
		Deployment deploy = processEngine.getRepositoryService()
											.createDeployment()
											.addClasspathResource("diagrams/groupTaskDelegate.bpmn")
											.addClasspathResource("diagrams/groupTaskDelegate.png")
											.name("分配角色任务")
											.deploy();
		System.out.println("流程部署ID:"+deploy.getId());
		System.out.println("流程部署的名称:"+deploy.getName());
		
		IdentityService   identityService =  processEngine.getIdentityService();
		//创建角色 
		identityService.saveGroup(new GroupEntity("部门主管"));
		identityService.saveGroup(new GroupEntity("部门经理"));
		identityService.saveGroup(new GroupEntity("CTO"));	
		//创建用户
		identityService.saveUser(new UserEntity("张三"));
		identityService.saveUser(new UserEntity("李四"));
		identityService.saveUser(new UserEntity("王五"));
		identityService.saveUser(new UserEntity("赵六"));
		identityService.saveUser(new UserEntity("田七"));
		identityService.saveUser(new UserEntity("胡八"));
		
		//创建角色和用户的对应关系
		identityService.createMembership("张三", "部门主管");
		identityService.createMembership("李四", "部门主管");
		identityService.createMembership("王五", "部门经理");
		identityService.createMembership("赵六", "部门经理");
		identityService.createMembership("田七", "CTO");
		identityService.createMembership("胡八", "CTO");	
	}
}
