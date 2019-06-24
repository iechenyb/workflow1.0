package com.kiiik.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kiiik.pub.ProcessServiceImpl;
import com.mysql.jdbc.StringUtils;

/**
 * 工作流学习
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/demo1/activiti")
public class ActivitiTestController {
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	ProcessServiceImpl process;

	public static String username = "chenyb";// 当前登录人员
	// http://localhost:9999/demo/activiti/test

	@RequestMapping("/login/{name}")
	@ResponseBody
	public String login(@PathVariable String name) {
		username = name;
		return username;
	}

	@RequestMapping("/test")
	@ResponseBody
	public void firstDemo() {
		// 根据bpmn文件部署流程
		Deployment deployment = repositoryService.createDeployment().addClasspathResource("demo2.bpmn").deploy();
		// 获取流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		// 启动流程定义，返回流程实例
		ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
		String processId = pi.getId();
		System.out.println("流程创建成功，当前流程实例ID：" + processId);

		Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		System.out.println("第一次执行前，任务名称：" + task.getName());
		taskService.complete(task.getId());

		task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		System.out.println("第二次执行前，任务名称：" + task.getName());
		taskService.complete(task.getId());

		task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		System.out.println("task为null，任务执行完毕：" + task);
	}

	@RequestMapping("/test/{name}")
	@ResponseBody
	public void firstDemo(@PathVariable String name) {
		if (StringUtils.isNullOrEmpty(name))
			return;
		// 根据bpmn文件部署流程
		Deployment deployment = repositoryService.createDeployment().addClasspathResource(name + ".bpmn").deploy();

		// 获取流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		System.out.println("deployment id:" + deployment.getId());

		// 启动流程定义，返回流程实例
		ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
		String processId = pi.getId();
		System.out.println("流程创建成功，当前流程实例ID：" + processId);
		boolean exe = true;
		while (exe) {
			Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
			if (task == null)
				exe = false;
			else {
				System.out.println("任务名称：" + task.getName());
				taskService.complete(task.getId());
			}
		}
	}

	/**
	 * 测试用例查看流程图
	 * 
	 * @param deployId
	 * @throws Exception
	 */
	@RequestMapping("/test/picview1/{deployId}")
	@ResponseBody
	public void viewImage(String deployId) throws Exception {
		// 创建仓库服务对对象
		RepositoryService repositoryService = processEngine.getRepositoryService();
		// 从仓库中找需要展示的文件
		String deploymentId = "701";
		List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
		String imageName = null;
		for (String name : names) {
			if (name.indexOf(".png") >= 0) {
				imageName = name;
			}
		}
		if (imageName != null) {
			System.out.println(imageName);
			File f = new File("d:/data/tmp" + imageName);
			// 通过部署ID和文件名称得到文件的输入流
			InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
			FileUtils.copyInputStreamToFile(in, f);
		}
	}

	@SuppressWarnings("unused")
	public String viewCurrentImage(String taskId) {
		
		ProcessDefinition pd = getProcessDefinitionByTaskId(taskId);
		// 1. 获取流程部署ID
		//putContext("deploymentId", pd.getDeploymentId());
		// 2. 获取流程图片的名称
		//putContext("imageName", pd.getDiagramResourceName());

		// 3.获取当前活动的坐标
		Map<String, Object> currentActivityCoordinates = getCurrentActivityCoordinates(taskId);
		//putContext("acs", currentActivityCoordinates);
		return "image";
	}

	public Map<String, Object> getCurrentActivityCoordinates(String taskId) {
		Map<String, Object> coordinates = new HashMap<String, Object>();
		// 1. 获取到当前活动的ID
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
				.singleResult();
		String currentActivitiId = pi.getActivityId();
		// 2. 获取到流程定义
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		// 3. 使用流程定义通过currentActivitiId得到活动对象
		ActivityImpl activity = pd.findActivity(currentActivitiId);
		// 4. 获取活动的坐标
		coordinates.put("x", activity.getX());
		coordinates.put("y", activity.getY());
		coordinates.put("width", activity.getWidth());
		coordinates.put("height", activity.getHeight());

		// 如果有多个流程活动节点（并发流程一般有多个活动节点）该方法应该返回一个list，代码应该使用下面的方法
		// 得到流程执行对象
		/*
		 * List<Execution> executions = runtimeService.createExecutionQuery()
		 * .processInstanceId(pi.getId()).list(); // 得到正在执行的Activity的Id
		 * List<String> activityIds = new ArrayList<String>(); for (Execution
		 * exe : executions) { List<String> ids =
		 * runtimeService.getActiveActivityIds(exe.getId());
		 * activityIds.addAll(ids); } List<Map<String, Integer>> list = new
		 * ArrayList<Map<String, Integer>>(); for (String id : activityIds) {
		 * ActivityImpl activity1 = pd.findActivity(id); Map<String, Integer>
		 * map = new HashMap<String, Integer>(); map.put("x", activity1.getX());
		 * map.put("y", activity1.getY()); map.put("width",
		 * activity1.getWidth()); map.put("height", activity1.getHeight());
		 * list.add(map); }
		 */

		return coordinates;
	}

	public ProcessDefinition getProcessDefinitionByTaskId(String taskId) {
		// 1. 得到task
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 2. 通过task对象的pdid获取流程定义对象
		ProcessDefinition pd = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		return pd;
	}

	@RequestMapping("/test/picview2/{deployId}")
	@ResponseBody
	public String viewImage2(HttpServletResponse resp) {
		String deploymentId = "";
		String imageName = "";
		InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
		// .getImageStream(deploymentId,imageName);//此处方法实际项目应该放在service里面
		try {
			OutputStream out = resp.getOutputStream();
			// 把图片的输入流程写入resp的输出流中
			byte[] b = new byte[1024];
			for (int len = -1; (len = in.read(b)) != -1;) {
				out.write(b, 0, len);
			}
			// 关闭流
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/test/allTask")
	@ResponseBody
	public List<Task> getCurUserTask() {
		List<Task> list = taskService.createTaskQuery().taskAssignee(username)// 指定个人任务查询
				.orderByTaskCreateTime().asc().list();
		if (CollectionUtils.isEmpty(list)) {
			list = new ArrayList<>();
		}
		return list;
	}

	// 这种方法比上面的简单，但是没有上面的灵活
	@SuppressWarnings("unused")
	public void showImage() throws Exception {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1");
		taskService.complete(taskService.createTaskQuery().singleResult().getId());

		// 得到流程定义实体类
		// ProcessDefinitionEntity pde = (ProcessDefinitionEntity)
		// ((RepositoryServiceImpl) repositoryService)
		// .getDeployedProcessDefinition(processInstance
		// .getProcessDefinitionId());
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();

		// 得到流程执行对象
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.list();
		// 得到正在执行的Activity的Id
		List<String> activityIds = new ArrayList<String>();
		for (Execution exe : executions) {
			List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
			activityIds.addAll(ids);
		}
		InputStream in = null;/* ProcessDiagramGenerator
				.generateDiagram(pde, "png", activityIds);*/
		FileOutputStream out = new FileOutputStream("f:\\watch.png");
		FileCopyUtils.copy(in, out);
	}
}