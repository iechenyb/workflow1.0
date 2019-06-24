package com.kiiik.demo;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kiiik.bean.SequenceFlow;
import com.kiiik.pub.ActivitProcessServiceImpl;

/**
 * 工作流学习
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/simple/activiti")
public class ActivitiSimpleController {

	public static String username = "chenyb";// 当前登录人员
	// http://localhost:9999/demo/activiti/test/login/iechenyb
	@Autowired
	ActivitProcessServiceImpl activitProcessServiceImpl;

	@RequestMapping("test/login/{name}")
	@ResponseBody
	public String login(@PathVariable String name) {
		username = name;
		return username;
	}

	/**
	 * http://localhost:9999/demo/activiti/test/start
	 * 
	 * @return
	 */
	@RequestMapping("test/start")
	@ResponseBody
	public String startProcess(String bpmnName) {
		// 根据bpmn文件部署流程
		Deployment deployment = activitProcessServiceImpl.deploye(bpmnName);
		// 获取流程定义
		ProcessDefinition processDefinition = activitProcessServiceImpl.getProcessDefine(deployment.getId());
		// 启动流程定义，返回流程实例
		ProcessInstance pi = activitProcessServiceImpl.startProcessInstanceById(processDefinition.getId());
		String processId = pi.getId();
		String result = "流程创建成功，当前流程实例ID：" + processId + ",deploymentid=" + deployment.getId();
		System.out.println(result);
		return result;
	}

	//获取流程意见
	@GetMapping("comments/{processId}")
	@ResponseBody
	public String comments(@PathVariable(value = "processId") String processId){
		 activitProcessServiceImpl.findCommentByProcId(processId);
		 return "";
	}
	
	/**
	 * 提交流程 http://localhost:9999/demo/activiti/submit/123
	 * 
	 * @return
	 */
	@Autowired
	protected TaskService taskService;

	@GetMapping("sumbit/{processId}")
	@ResponseBody
	public String submitTask(@PathVariable(value = "processId") String processId) {
		System.out.println("**************");
		try {
			Task task = activitProcessServiceImpl.getTask(processId);
			if(task==null) return "结束！";
			String taskId = task.getId();
			try{
				/** 1.基本数据类型获取流程变量 */
		        Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		        Date date = (Date) taskService.getVariable(taskId, "请假日期");
		        String comment = (String) taskService.getVariable(taskId, "请假原因");
		        System.out.println("请假天数:" + days + "\n 请假日期:" + date + "\n 请假原因:" + comment);
		        /** 2.Javabean类型获取流程变量 */
		        SequenceFlow u = (SequenceFlow) taskService.getVariable(taskId, "用户信息");
		        System.out.println("loginname:" + u.getCreateLoginName() + " message:" + u.getMessage());
			}catch(Exception e){
				//第一次提交没有参数，所以不能正常展示，忽略！
			}
			System.out.println("任务名称：" + task.getName()+"vars:"+activitProcessServiceImpl.getVariableByTaskId(task.getId(), true));
			String lastTaskName = task.getName();
			int idx = new Random().nextInt(10);
			Map<String, Object> variables = new HashMap<String,Object>();
			variables.put("paramname", "aaaa"+idx);//获取不到
			 /** 1.使用基本数据类型设置流程变量 **/
	        taskService.setVariableLocal(taskId, "请假天数", 3); // 与任务ID绑定
	        taskService.setVariable(taskId, "请假日期", new Date());
	        taskService.setVariable(taskId, "请假原因", "回家养病，大概一周把！");
	        /** 2.使用Javabean类型设置流程变量 **/
	        SequenceFlow flow = new SequenceFlow();
	        flow.setCreateLoginName("cyb-login");
	        flow.setMessage("登录参数描述！");
	        taskService.setVariable(taskId, "用户信息", flow);
			activitProcessServiceImpl.addComment(task.getId(), processId, idx+"审核通过！");
			activitProcessServiceImpl.complete(task.getId(),variables);
			task = activitProcessServiceImpl.getTask(processId);
			if(task==null) return "over!";
			taskId= task.getId();
			 
			String nextTaskName = (task == null ? "" : task.getName() + "taskid=" + task.getId());
			System.out.println("下一个任务名称：" + nextTaskName);
			return "当前任务:" + lastTaskName + ",下一个任务：" + nextTaskName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processId;
	}

	@GetMapping("sumbit1")
	@ResponseBody
	public String submitTask1(String processId) {
		System.out.println("**************");
		try {
			Task task = activitProcessServiceImpl.getTask(processId);
			System.out.println("任务名称：" + task.getName());
			activitProcessServiceImpl.complete(task.getId());
			task = activitProcessServiceImpl.getTask(processId);
			String nextTaskName = (task == null ? "" : task.getName() + ",taskid=" + task.getId());
			System.out.println("下一个任务名称：" + nextTaskName);
			return "当前任务:" + task.getName() + ",下一个任务：" + nextTaskName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processId;
	}

	@RequestMapping("/test/picview1/{deployId}/{procId}")
	@ResponseBody
	// 这种方法比上面的简单，但是没有上面的灵活
	public String showImage(@PathVariable(value = "deployId") String deployId,
			@PathVariable(value = "procId") String procId) throws Exception {
		System.out.println("**************");
		String dir = "d:/data/tmp/watch.png";
		try {
			// 获取流程定义
			ProcessDefinition processDefinition =activitProcessServiceImpl.getProcessDefine(deployId);
			InputStream in = activitProcessServiceImpl.getProcessImage(processDefinition.getId(), procId);
			FileOutputStream out = new FileOutputStream("d:/data/tmp/watch.png");
			FileCopyUtils.copy(in, out);
			return "流程图路径：" + dir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "图像生成失败！";
	}
	
}