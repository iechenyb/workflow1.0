package com.kiiik.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kiiik.listener.ExectuionListenerDemo;
import com.kiiik.listener.TaskListenerDemo;
import com.kiiik.pub.ActivitProcessServiceImpl;
import com.kiiik.utils.FileUtils;
import com.mysql.jdbc.StringUtils;

import io.swagger.annotations.ApiOperation;

public class ActivitiBaseController {
	@Autowired
	ActivitProcessServiceImpl activitProcessServiceImpl;
	public static String username = "chenyuanbao";// 当前登录人员

	@GetMapping("login/{name}")
	@ResponseBody
	public String login(@PathVariable String name) {
		username = name;
		return username;
	}

	@GetMapping("process/bpmns")
	@ResponseBody
	public String[] getBpmn() throws FileNotFoundException {
		if (ResourceUtils.isUrl("classpath:")) {
			return ResourceUtils.getFile("classpath:").list();
		}
		return null;
	}

	@GetMapping("/picview/{deployId}/{processId}")
	@ResponseBody
	// 这种方法比上面的简单，但是没有上面的灵活
	public String showImage(
			@PathVariable(value = "deployId") String deployId,
			@PathVariable(value = "processId") String processId) throws Exception {
		System.out.println("**************");
		String dir = "d:/data/tmp/watch.png";
		try {
			// 获取流程定义
			ProcessDefinition processDefinition = activitProcessServiceImpl.getProcessDefine(deployId);
			InputStream in = activitProcessServiceImpl.getProcessImage(processDefinition.getId(), processId);
			FileOutputStream out = new FileOutputStream("d:/data/tmp/watch.png");
			FileCopyUtils.copy(in, out);
			return "流程图路径：" + dir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "图像生成失败！";
	}

	@GetMapping("/picview2/procId/{processId}")
	@ResponseBody
	@ApiOperation(value = "下载信息", httpMethod = "GET", notes = "下载符合条件的Excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	// 这种方法比上面的简单，但是没有上面的灵活
	public String showImage2(@PathVariable(value = "processId") String processId) throws Exception {
		System.out.println("**************");
		String deployId = activitProcessServiceImpl.getDeploymentIdByProcId(processId);
		String dir = "d:/data/tmp/watch.png";
		try {
			// 获取流程定义
			ProcessDefinition processDefinition = activitProcessServiceImpl.getProcessDefine(deployId);
			InputStream in = activitProcessServiceImpl.getProcessImage(processDefinition.getId(), processId);
			FileOutputStream out = new FileOutputStream(dir);
			FileCopyUtils.copy(in, out);
			downloadFile(new File(dir));
			return "流程图路径：" + dir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "图像生成失败！";
	}

	protected void downloadFile(String filePath) throws FileNotFoundException, IOException, Exception {
		downloadFile(new File(filePath));
	}

	protected HttpServletResponse getCurrentResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	protected void downloadFile(File file) throws FileNotFoundException, IOException, Exception {
		downFile(getCurrentResponse(), file);
	}

	public static void downFile(HttpServletResponse response, File file)
			throws FileNotFoundException, IOException, Exception {
		// response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
		response.setCharacterEncoding("utf-8");
		FileUtils.fileUpload(new FileInputStream(file), response.getOutputStream());
	}

	@GetMapping("start/process")
	@ResponseBody
	public String startProcess(String bpmnName) {
		// 根据bpmn文件部署流程
		Deployment deployment = activitProcessServiceImpl.deploye(bpmnName);
		// 获取流程定义
		ProcessDefinition processDefinition = activitProcessServiceImpl.getProcessDefine(deployment.getId());
		// 启动流程定义，返回流程实例
		ProcessInstance pi = activitProcessServiceImpl.startProcessInstanceById(processDefinition.getId());
		String processId = pi.getId();
		activitProcessServiceImpl.addCommentByProcId(processId, "发起流程",username);
		String result = "流程创建成功，当前流程实例ID：" + processId + ",deploymentid=" + deployment.getId();
		System.out.println(result);
		return result + "[" + deployment.getId() + "/" + processId + "]";
	}

	@GetMapping("taskVars/{taskId}")
	@ResponseBody
	public Map<String, Object> taskVars(@PathVariable(value = "taskId") String taskId) {
		return activitProcessServiceImpl.getVariableByTaskId(taskId, true);
	}

	@GetMapping("procVars/{procId}")
	@ResponseBody
	public Map<String, Object> procVars(@PathVariable(value = "procId") String procId) {
		return activitProcessServiceImpl.getVariableByTaskId(procId, true);
	}

	@GetMapping("process/{processId}")
	@ResponseBody
	public String procTaskLastly(@PathVariable(value = "processId") String processId) {
		Task task = activitProcessServiceImpl.getTask(processId);
		return task == null ? "" : task.getId();
	}

	/**
	 * 流程当前的任务id
	 * 
	 * @param processId
	 * @return
	 */
	@GetMapping("taskId/{processId}")
	@ResponseBody
	public String getTaskIdByProcessId(@PathVariable String processId) {
		Task task = activitProcessServiceImpl.getTask(processId);
		return task == null ? "" : task.getId();
	}

	/**
	 * 查询某个任务的表单数据
	 * 
	 * @param taskId
	 * @return
	 */
	@GetMapping("task/form/{taskId}")
	@ResponseBody
	public String taskForm(@PathVariable String taskId) {
		TaskFormData tfd = activitProcessServiceImpl.getTaskFormData(taskId);
		if (tfd == null)
			return null;
		List<FormProperty> a = tfd.getFormProperties();
		StringBuilder sb = new StringBuilder();
		sb.append(tfd.getDeploymentId() + ",").append(tfd.getFormKey() + ",").append(tfd.getTask().getName() + ",");
		StringBuilder ps = new StringBuilder();
		for (FormProperty p : a) {
			ps.append(
					p.getId() + "/" + p.getName() + "/" + p.getValue() + "/" + p.getClass() + "/" + p.getType() + ",");
		}
		sb.append(ps.toString());
		return sb.toString();
	}

	/**
	 * 获取指派个单个人员的任务
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("tasks/user/{user}")
	@ResponseBody
	public List<String> tasksByRole(@PathVariable String user) {
		List<Task> tasks = activitProcessServiceImpl.getTaskListByRole(user);
		return commonWrap(tasks);
	}

	/**
	 * 获取指派给候选人执行的任务
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("tasks/candidateuser/{user}")
	@ResponseBody
	public List<String> tasksByCandiUser(@PathVariable String user) {
		List<Task> tasks = activitProcessServiceImpl.getTaskListByCandidateUser(user);
		return commonWrap(tasks);
	}

	public List<String> commonWrap(List<Task> tasks) {
		List<String> tasksStr = new ArrayList<String>();
		for (Task task : tasks) {
			tasksStr.add(task.getId() + "," + task.getName() + "," + task.getProcessInstanceId() + ","
					+ task.getDescription());
		}
		return tasksStr;
	}

	// 获取流程意见
	@GetMapping("comments/{processId}")
	@ResponseBody
	public List<Map<String, String>> comments(@PathVariable(value = "processId") String processId) {
		return activitProcessServiceImpl.findCommentByProcId(processId);
	}

	/**
	 * 获取指派给候选人执行的任务，同时查询多个不行！
	 * 
	 * @param role
	 * @return
	 */
	@GetMapping("tasks/candidategroups/{groups}")
	@ResponseBody
	public List<String> tasksByCandiGroups(@PathVariable String groups) {
		List<String> candidateGroups = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(groups)) {
			candidateGroups = Arrays.asList(groups);
		} else {
			return candidateGroups;
		}
		List<Task> tasks = activitProcessServiceImpl.getTaskListInGroup(candidateGroups);
		return commonWrap(tasks);
	}

	/**
	 * 单个grop查询
	 * 
	 * @param group
	 * @return
	 */
	@GetMapping("tasks/candidategroup/{group}")
	@ResponseBody
	public List<String> tasksByCandiGroup(@PathVariable String group) {
		List<Task> tasks = activitProcessServiceImpl.getTaskListByGroup(group);
		return commonWrap(tasks);
	}

	@GetMapping("/showCurrentView")
	public ModelAndView showCurrentView(String taskId, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		Task task = activitProcessServiceImpl.getTaskById(taskId);
		String processDefinitionId = task.getProcessDefinitionId(); // 获取流程定义id
		ProcessDefinition processDefinition = activitProcessServiceImpl.getProcessDefinition(taskId);
		mav.addObject("deploymentId", processDefinition.getDeploymentId()); // 部署id
		mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称
		ProcessDefinitionEntity processDefinitionEntity = activitProcessServiceImpl.getProcessDefineEntity(processDefinitionId);
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		ProcessInstance pi = activitProcessServiceImpl.getProcessInstance(processInstanceId);
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId()); // 根据活动id获取活动实例
		mav.addObject("x", activityImpl.getX()); // x坐标
		mav.addObject("y", activityImpl.getY()); // y坐标
		mav.addObject("width", activityImpl.getWidth()); // 宽度
		mav.addObject("height", activityImpl.getHeight()); // 高度
		mav.setViewName("page/currentView");
		return mav;
	}
	//https://blog.csdn.net/u012613903/article/details/43732941
	@GetMapping("listener/study")
	@ResponseBody
	public String listeners(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("myprocessListener", new ExectuionListenerDemo());
		map.put("mytaskListener", new TaskListenerDemo());
		map.put("user", "mpc");
		ProcessInstance pi = activitProcessServiceImpl.startProcessInstanceByKey("myProcess", map);
		activitProcessServiceImpl.complete(activitProcessServiceImpl.getTask(pi.getId()).getId());
		return "ok";
	}
}
