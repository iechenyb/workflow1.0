package com.kiiik.demo;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kiiik.pub.ActivitProcessServiceImpl;

/**
 * 工作流学习
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/simpleAssign/activiti")
public class VacationRequestController {

	public static String username = "chenyb";// 当前登录人员
	
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
		return result+"["+deployment.getId()+"/"+processId+"]";
	}
	
	@GetMapping("taskVars/{taskId}")
	@ResponseBody
	public Map<String,Object> taskVars(@PathVariable(value = "taskId") String taskId){
		return activitProcessServiceImpl.getVariableByTaskId(taskId, true);
	}
	
	@GetMapping("procVars/{procId}")
	@ResponseBody
	public Map<String,Object> procVars(@PathVariable(value = "procId") String procId){
		return activitProcessServiceImpl.getVariableByTaskId(procId, true);
	}
	
	@GetMapping("process/{processId}")
	@ResponseBody
	public String procTaskLastly(@PathVariable(value = "processId") String processId){
		Task task = activitProcessServiceImpl.getTask(processId);
		return task==null?"":task.getId();
	}

	/**
	 * 提交流程 http://localhost:9999/demo/activiti/submit/123
	 * 
	 * @return
	 */
	@GetMapping("step1/{processId}")
	@ResponseBody
	public String step1(@PathVariable(value = "processId") String processId
			,boolean approved) {
		System.out.println("**************");
		try {
			Task task = activitProcessServiceImpl.getTask(processId);
			System.out.println("任务名称：" + task.getName());
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", approved);//直接通过么？通过则发邮件
			taskVariables.put("managerMotivation", "We have a tight deadline!");//严格的截止日期
			activitProcessServiceImpl.complete(task.getId(),taskVariables);
			task = activitProcessServiceImpl.getTask(processId);
			String nextTaskName = (task == null ? "" : task.getName() + "taskid=" + task.getId());
			System.out.println("下一个任务名称：" + nextTaskName);
			return "当前任务:" + task.getName() + ",下一个任务：" + nextTaskName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processId;
	}
    /**
     * 待分支的提交！
     * @param processId
     * @return
     */
	@GetMapping("step2/{processId}")
	@ResponseBody
	public String step2(@PathVariable(value = "processId") String processId
			,boolean approved) {
		System.out.println("**************");
		try {
			Task task = activitProcessServiceImpl.getTask(processId);
			System.out.println("任务名称：" + task.getName());
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("vacationApproved", approved);//直接通过么？通过则发邮件
			taskVariables.put("managerMotivation", "We have a tight deadline!");//严格的截止日期
			activitProcessServiceImpl.complete(task.getId(), taskVariables);
			task = activitProcessServiceImpl.getTask(processId);
			String nextTaskName = (task == null ? "" : task.getName() + ",taskid=" + task.getId());
			System.out.println("下一个任务名称：" + nextTaskName);
			return "当前任务:" + task.getName() + ",下一个任务：" + nextTaskName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processId;
	}
	
	@GetMapping("step3/{processId}")
	@ResponseBody
	public String step3(@PathVariable(value = "processId") String processId
			,boolean approved) {
		System.out.println("**************");
		try {
			Task task = activitProcessServiceImpl.getTask(processId);
			System.out.println("任务名称：" + task.getName());
			String curTaskName = task.getName();
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			taskVariables.put("employeeName", "iechenyb");//指派给某人处理
			taskVariables.put("numberOfDays", 20);//休假天数
			taskVariables.put("startDate", new Date());//起始日期
			taskVariables.put("vacationMotivation", "I'm really tired!");//实在是太累了
			taskVariables.put("resendRequest", approved);//重新发起么？
			activitProcessServiceImpl.complete(task.getId(), taskVariables);
			task = activitProcessServiceImpl.getTask(processId);
			String nextTaskName = (task == null ? "" : task.getName() + ",taskid=" + task.getId());
			System.out.println("下一个任务名称：" + nextTaskName);
			return "当前任务:" + curTaskName + ",下一个任务：" + nextTaskName;
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