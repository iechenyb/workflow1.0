package com.kiiik.pub;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivitProcessServiceImpl {
	@Autowired
	protected RepositoryService repositoryService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected ProcessEngine processEngine;
	
	@Autowired
	protected HistoryService historyService;
	
	@Autowired
	protected FormService formService;

	/**
	 * 获取流程活动的任务id
	 * 
	 * @param procId
	 *            流程号
	 * @return
	 */
	public List<String> getActivityIds(String procId) {
		// 得到流程执行对象
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(procId).list();
		// 得到正在执行的Activity的Id
		List<String> activityIds = new ArrayList<String>();
		for (Execution exe : executions) {
			List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
			activityIds.addAll(ids);
		}
		return activityIds;
	}

	/**
	 * 获取流程图的流信息
	 * 
	 * @param proDefId
	 * @param procId
	 * @return
	 */
	public InputStream getProcessImage(String proDefId, String procId) {
		/**/
		List<String> activityIds = getActivityIds(procId);
		BpmnModel bpmnModel = repositoryService.getBpmnModel(proDefId);
		InputStream in = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activityIds,
				new ArrayList<String>(0), "宋体", "宋体", "宋体", null, 1.0);
		return in;
	}

	public Deployment deploye(String bpmnName) {
		Deployment deployment = repositoryService.createDeployment().addClasspathResource(bpmnName + ".bpmn").deploy();
		return deployment;
	}

	public ProcessDefinition getProcessDefine(String deployId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		return processDefinition;
	}

	public ProcessDefinitionEntity getProcessDefineEntity(String proDefId) {
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(proDefId).singleResult();
		return pde;
	}

	public ProcessInstance startProcessInstanceById(String proDefId) {
		return runtimeService.startProcessInstanceById(proDefId);
	}

	public Task getTask(String processId) {
		return taskService.createTaskQuery().processInstanceId(processId).singleResult();
	}
	public Task getTaskById(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	public void complete(String taskId) {
		taskService.complete(taskId);
	}

	public void complete(String taskId, Map<String, Object> variables) {
		taskService.setVariables(taskId, variables);
		taskService.complete(taskId, variables);
	}
	public void completeByProcId(String processId, Map<String, Object> variables) {
		Task task = getTask(processId);
		taskService.setVariables(task.getId(), variables);
		taskService.complete(task.getId(), variables);
	}
	public void completeByProcId(String processId) {
		Task task = getTask(processId);
		taskService.complete(task.getId());
	}


	public List<Task> getTaskListByCandidateUser(String candidateUser) {// management
		List<Task> tasks =
		taskService.createTaskQuery().taskCandidateUser(candidateUser).list();
		return tasks;
	}
	
	public List<Task> getTaskListByGroup(String group) {
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group).list();
		return tasks;
	}
	public List<Task> getTaskListInGroup(List<String> candidateGroups) {
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups).list();
		return tasks;
	}
	/**
	 * 将指定某人的任务，或者指定候选人或者候选组的任务查询出来
	 * @param userIdForCandidateAndAssignee
	 * @return
	 */
	public List<Task> getTaskListByAll(String userIdForCandidateAndAssignee) {
		List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(userIdForCandidateAndAssignee).list();
		return tasks;
	}

	public List<Task> getTaskListByRole(String assigne) {// management
		/*
		 * List<Task> tasks = taskService.createTaskQuery()
		 * .taskAssignee(assigne) .processVariableValueEquals("orderId", "0815")
		 * .orderByDueDate().asc() .list();
		 */
		/*
		 * List<Task> tasks = taskService.createNativeTaskQuery() .sql(
		 * "SELECT count(*) FROM " + managementService.getTableName(Task.class)
		 * + " T WHERE T.NAME_ = #{taskName}") .parameter("taskName",
		 * "gonzoTask") .list();
		 * 
		 * long count = taskService.createNativeTaskQuery() .sql(
		 * "SELECT count(*) FROM " + managementService.getTableName(Task.class)
		 * + " T1, " +
		 * managementService.getTableName(VariableInstanceEntity.class) +
		 * " V1 WHERE V1.TASK_ID_ = T1.ID_") .count();
		 */
		return taskService.createTaskQuery().taskAssignee(assigne).list();
		// repositoryService.suspendProcessDefinitionByKey("vacationRequest");挂起流程
		// repositoryService.activateProcessDefinitionById("");激活流程
	}

	public boolean setVariableByTaskId(String taskId, boolean isLocal, HashMap<String, Object> map) {
		/*
		 * taskService.setVariable(taskId, variableName, value);
		 *
		 * 设置本执行对象的变量，该作用域只在当前的executionId中 taskService.setVariableLocal(taskId,
		 * variableName, value); 可以设置对个变量，放在map中
		 */
		if (isLocal) {
			taskService.setVariablesLocal(taskId, map);
		} else {
			taskService.setVariables(taskId, map);
		}
		return true;
	}

	public Map<String, Object> getVariableByTaskId(String taskId, boolean isLocal) {
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		if (isLocal) {
			variablesMap = taskService.getVariablesLocal(taskId);
		} else {
			variablesMap = taskService.getVariables(taskId);
		}
		return variablesMap;
	}

	// 也可以强制类型转换
	public Object getVariableByTaskId(String taskId, String key) {
		return getVariableByTaskId(taskId, key);
	}

	/**
	 * // 传递的是一个自定义的bean对象 SequenceFlow sequenceFlow = new SequenceFlow();
	 * sequenceFlow.setCreateLoginName("0003"); sequenceFlow.setDate(new
	 * Date()); sequenceFlow.setMessage("重要");
	 * activitiService.setVariableByTaskId(taskId, "sequenceFlow",
	 * sequenceFlow);
	 * 
	 * SequenceFlow sequenceFlow = activitiService.getVariableByTaskId(taskId,
	 * "sequenceFlow", SequenceFlow.class);
	 */
	public void addComment(String taskId, String processId, String message) {
		Authentication.setAuthenticatedUserId("chenyb");
		taskService.addComment(taskId, processId, message);
	}
	public void addCommentByProcId(String processId, String message,String username) {
		Authentication.setAuthenticatedUserId(username);
		taskService.addComment(getTask(processId).getId(), processId, message);
	}
	public void addComment(String taskId, String processId, String message,String curUser) {
		Authentication.setAuthenticatedUserId(curUser);
		taskService.addComment(taskId, processId, message);
	}

	public List<Map<String,String>> findCommentByProcId(String procId) {
		List<Map<String,String>> data = new ArrayList<>();
		// 获取流程实例ID
		String processInstanceId = procId;
		// 使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()// 历史任务表查询
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.list();
		List<Comment> list = new ArrayList<Comment>();
		// 遍历集合，获取每个任务ID
		if (htiList != null && htiList.size() > 0) {
			for (HistoricTaskInstance hti : htiList) {
				// 任务ID
				String htaskId = hti.getId();
				// 获取批注信息
				List<Comment> taskList = taskService.getTaskComments(htaskId);// 对用历史完成后的任务ID
				list.addAll(taskList);
			}
		}
		list = taskService.getProcessInstanceComments(processInstanceId);
		Map<String,String> map = null;
		for (Comment com : list) {
			map = new LinkedHashMap<>();
			map.put("ID" , com.getId());
			map.put("Message" , com.getFullMessage());
			map.put("TaskId" , com.getTaskId());
			map.put("ProcessInstanceId:" , com.getProcessInstanceId());
			map.put("UserId" , com.getUserId());//如何存储为用户名
			map.put("time" , com.getTime().toString());
			data.add(map);
		}
		System.out.println(list);
		return data;
	}

	public void findCommentByTaskId(String taskId) {
		//HistoryService historyService = processEngine.getHistoryService();
		//TaskService taskService = processEngine.getTaskService();
		
		// 使用当前的任务ID，查询当前流程对应的历史任务ID

		// 使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();
		findCommentByProcId(task.getProcessInstanceId());
	}
	
	public StartFormData getProcessFormData(String processDefinitionId){
		return formService.getStartFormData(processDefinitionId);
	}
	
	public TaskFormData getTaskFormData(String taskId){
		return formService.getTaskFormData(taskId);
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public ProcessDefinition getProcessDefinition(String taskId){
		Task task=taskService.createTaskQuery() // 创建任务查询
                .taskId(taskId) // 根据任务id查询
                .singleResult(); 
        String processDefinitionId=task.getProcessDefinitionId(); // 获取流程定义id
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery() // 创建流程定义查询
                .processDefinitionId(processDefinitionId) // 根据流程定义id查询
                .singleResult(); 
        return processDefinition;
	}
	
	public String getDeploymentIdByTaskId(String taskId){
		return getProcessDefinition(taskId).getDeploymentId();
	}
	public String getDeploymentIdByProcId(String processId){
		return getDeploymentIdByTaskId(getTask(processId).getId());
	}
	public ProcessInstance getProcessInstance(String processInstanceId){
		 ProcessInstance pi=runtimeService.createProcessInstanceQuery() // 根据流程实例id获取流程实例
	                .processInstanceId(processInstanceId)
	                .singleResult();
		 return pi;
	}

	public ProcessInstance startProcessInstanceByKey(String name, Map<String, Object> map) {
		return runtimeService.startProcessInstanceByKey(name, map);
	}
}
