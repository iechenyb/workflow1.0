package com.cyb.jbpm;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.jbpm.api.DeploymentQuery;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyb.Constants;
import com.cyb.jbpm.vo.ProcessDefinitionVo;
import com.cyb.jbpm.vo.ProcessInstanceVo;
import com.cyb.jbpm.vo.TaskVo;
import com.cyb.jbpm.vo.YwInfor;

import net.sf.json.JSONArray;
//请求统一加*.html
@RequestMapping("/jbpm")
@Controller
public class JpbmController {
	@Resource(name = "taskService")
	public TaskService taskService;//节点任务服务
	
	@Resource(name = "processEngine")
	public ProcessEngine processEngine;//流程处理器引擎
	
	@Resource(name = "executionService")//流程实例服务，包括发起流程
	public ExecutionService executionService;
	
	@Resource(name = "repositoryService")
	public RepositoryService repositoryService;//查询部署实例

	@Resource
	JdbcTemplate jdbcTemplate;
	
	/**
	 * 查看基础表信息
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/tables")
	@ResponseBody
	public List<String> tables() throws SQLException {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		List<String> list = new ArrayList<String>();
		 try {
	            DatabaseMetaData dbmd=con.getMetaData();
	            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
	            while (resultSet.next()) {
	                String tableName=resultSet.getString("TABLE_NAME");
	                String remarkes = resultSet.getString("REMARKS");
	                System.out.println(tableName+"="+remarkes);
	                list.add(tableName);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		return list;
	}
	
	/**
	 * 根据文件名生成部署实例
	 * @param file
	 * @return
	 */
	@RequestMapping("/deploy")
	public ModelAndView deploy(String file) {
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		String path = Constants.WEBPATH + "jbpm/" + file;
		System.out.println("deploy file is :" + path);
		repositoryService.createDeployment()
				.addResourceFromFile(new File(path)).deploy();
		// repositoryService.createDeployment().addResourceFromClasspath(file).deploy();
		System.out.println("deploy success!");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
    //可以传递id 也可以传递key
	/**
	 * 根据模板实例发起一个流程
	 * @param id
	 * @return
	 */
	@RequestMapping("/start")
	public ModelAndView startProcess(String id) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("content", "bad");
		System.out.println("id====" + id);
		DeploymentQuery query = repositoryService.createDeploymentQuery().deploymentId(id);
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery();
		query.list().get(0).getName();
		String deployementId = processDefinitionQuery.deploymentId(id).list().get(0).getId();//draw_key-2
		ExecutionService executionService = processEngine.getExecutionService();
		/** 鏍规嵁娴佺▼鐨刱ey鏉ュ垱寤轰竴涓祦绋嬪疄渚� */
		// ProcessInstance pi =
		// executionService.startProcessInstanceByKey("draw_key",variables);
		ProcessInstance pi = executionService.startProcessInstanceById(
				deployementId, variables);
		System.out.println(pi);
		recordLog(new YwInfor("任务发起",Constants.ROLE ,null));
		System.out.println(pi.isEnded());
//		pi = executionService.signalExecutionById(pi.getId());
//		System.out.println(pi.isEnded());
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
    /**
     * 记录日志
     * @param ywInfor
     */
	private void recordLog(YwInfor ywInfor) {
		System.out.println(ywInfor);
	}
	/**
	 * 删除部署模板实例
	 * @param deploymentId
	 * @return
	 */
	@RequestMapping("/del")
	public ModelAndView deleteDeploy(String deploymentId) {
		repositoryService.deleteDeploymentCascade(deploymentId);
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
	/**
	 * 删除部署模板实例
	 * @param deploymentId
	 * @return
	 */
	@RequestMapping("/del1")
	public ModelAndView deleteDeploy1(String deploymentId) {
		repositoryService.deleteDeploymentCascade(deploymentId);
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		view.setViewName("jbpm/list-1");
		return view;
	}
	/**
	 * 获取流程信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/lst")
	public JSONArray showList() {
		Map<String, List<?>> ret = new HashMap<String, List<?>>();
		ret.put("data", getData());
		ret.put("data2", getData2());
		ret.put("data3", myTasks());
		return JSONArray.fromObject(ret);
	}
	/**
	 * 获取流程实例信息
	 * @return
	 */
	public List<ProcessInstanceVo> getData2(){
		List<ProcessInstance> instances = executionService
				.createProcessInstanceQuery().list();
		List<ProcessInstanceVo> data_proc = new ArrayList<ProcessInstanceVo>();
		System.out.println(instances);
		ProcessInstanceVo obj2 = null;
		if (instances != null && instances.size() > 0) {
			for (ProcessInstance vo : instances) {
				obj2 = new ProcessInstanceVo();
				obj2.setId(vo.getId());
				obj2.setName(vo.getName());
				obj2.setKey(vo.getKey());
				obj2.setState(vo.getState());
				obj2.setProcessDefinitionId(vo.getProcessDefinitionId());
				data_proc.add(obj2);
			}
		}
		return data_proc;
	}
	/**
	 * 获取流程定义模板信息
	 * @return
	 */
	public List<ProcessDefinitionVo> getData(){
		List<ProcessDefinitionVo> data = new ArrayList<ProcessDefinitionVo>();
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery();
		List<ProcessDefinition> processDefitionList = processDefinitionQuery
				.list();
		ProcessDefinitionVo obj = null;
		for (ProcessDefinition pd : processDefitionList) {
			obj = new ProcessDefinitionVo();
			System.out.println("进程id：" + pd.getId());
			obj.setDeploymentId(pd.getDeploymentId());
			obj.setId(pd.getId());
			obj.setPicName(pd.getImageResourceName());
			obj.setKey(pd.getKey());
			obj.setVersion(pd.getVersion() + "");
			data.add(obj);
		}
		return data;
	}
	/**
	 * 主页展示信息
	 * @return
	 */
	@RequestMapping("/lst1")
	public ModelAndView showList1() {
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		
		List<ProcessInstance> instances = executionService
				.createProcessInstanceQuery().list();
		List<ProcessInstanceVo> data_proc = new ArrayList<ProcessInstanceVo>();
		System.out.println(instances);
		ProcessInstanceVo obj2 = null;
		if (instances != null && instances.size() > 0) {
			for (ProcessInstance vo : instances) {
				obj2 = new ProcessInstanceVo();
				obj2.setId(vo.getId());
				obj2.setName(vo.getName());
				obj2.setKey(vo.getKey());
				obj2.setState(vo.getState());
				obj2.setProcessDefinitionId(vo.getProcessDefinitionId());
				data_proc.add(obj2);
			}
		}
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
	/**
	 * 图像展示
	 * @param id
	 * @return
	 */
	@RequestMapping("/image")
	public ModelAndView showPic(String id) {
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/jbpmPic");
		ProcessInstance processInstance = executionService
				.findProcessInstanceById(id);
		Set<String> activityName = processInstance.findActiveActivityNames();// [b]
																				// taskname
		ActivityCoordinates ac = repositoryService.getActivityCoordinates(
				processInstance.getProcessDefinitionId(), activityName
						.iterator().next());
		view.addObject("taskName", activityName);
		view.addObject("person", Constants.ROLE);
		System.out.println("x = " + ac.getX());
		view.addObject("x", ac.getX());
		view.addObject("y", ac.getY());
		view.addObject("w", ac.getWidth());
		view.addObject("h", ac.getHeight());
		view.addObject("id", id);
		String path = Constants.WEBPATH + "jbpm"+File.separator+"process.png";
		view.addObject("path", path);
		return view;
	}
	/**
	 * 图像展示
	 * @param id
	 * @return
	 */
	@RequestMapping("/image1")
	public ModelAndView showPic1(String id) {
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/jbpmPic");
		ProcessInstance processInstance = executionService
				.findProcessInstanceById(id);
		String processDefinitionId = processInstance.getProcessDefinitionId();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).uniqueResult();
		InputStream inputStream = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), "process.png");
		String path = Constants.WEBPATH + "jbpm//process.png";
		System.out.println("path:" + path);
		FileUtils.copyFileByStream(inputStream, path);
		Set<String> activityName = processInstance.findActiveActivityNames();// [b]
		ActivityCoordinates ac = repositoryService.getActivityCoordinates(
				processInstance.getProcessDefinitionId(), activityName
						.iterator().next());
		System.out.println("x = " + ac.getX());
		System.out.println("y = " + ac.getY());
		System.out.println("w = " + ac.getWidth());
		System.out.println("h = " + ac.getHeight());
		view.addObject("taskName", activityName);
		view.addObject("person", Constants.ROLE);
		view.addObject("x", ac.getX());
		view.addObject("y", ac.getY());
		view.addObject("w", ac.getWidth());
		view.addObject("h", ac.getHeight());
		view.addObject("id", id);
		view.addObject("path", path);
		return view;
	}
	/**
	 * 获取当前登录人员的任务信息
	 * @return
	 */
	public List<TaskVo> myTasks(){
		List<Task> taskList = taskService.findPersonalTasks(Constants.ROLE); 
		List<TaskVo> ret = new ArrayList<TaskVo>();
		TaskVo task = null;
		if(taskList!=null&&taskList.size()>0){
			for(Task vo:taskList){
			task = new TaskVo();
			task.setId(vo.getId());
			task.setActivityName(vo.getActivityName());
			task.setAssignee(vo.getAssignee());
			task.setExecutionId(vo.getExecutionId());
			task.setName(vo.getName());
			task.setProgress(vo.getProgress()+"");
			task.setCreateTime(DateUtil.date2long14(vo.getCreateTime()).toString());
			ret.add(task);
			}
		}
		return ret;
	}
	
	@RequestMapping("/exeid")
	@ResponseBody
	public String  exeid() {
		Execution a = executionService.findExecutionById("draw_key.540032");
		if(a!=null){//流程为空  说明已经执行结束！
			System.out.println("执行状态："+a.getState());
		}
		return "s";
	}
	//id 当前节点id 流程走完了图片不可以在看 
	/**
	 * 节点流转通用方法
	 * @param cmd
	 * @param id
	 * @return
	 */
	@RequestMapping("/handle")
	public ModelAndView handlerTask(String cmd,String id) {
		exeid();
		Map<String, Object> variables = new HashMap<String, Object>();
		System.out.println("变量名："+taskService.getVariableNames(id));//变量名：[content]
		taskService.getVariableNames(id).iterator();//访问所有的变量信息
		Task task = taskService.getTask(id);
		task.getExecutionId() ;//draw_key.540032 当前任务所在的流程节点
		System.out.println("节点名称："+task.getActivityName()+",指派人员："+task.getAssignee()+",描述："+task.getDescription());
		ModelAndView view = new ModelAndView();
		if(cmd.equals("go")){
			taskService.completeTask(id);  //go next
			recordLog(new YwInfor(task.getActivityName(), Constants.ROLE,variables));
		}else{
			variables.put("content", cmd); 
			if("一般".equals(task.getActivityName())){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("content", cmd);
				variables.put("content", cmd); //go next with content conditions
				taskService.setVariables(id,variables); 
				taskService.completeTask(id);//handler
			}else{
				variables.put("content", cmd); //go next with content conditions
				taskService.setVariables(id,variables);  
				taskService.completeTask(id); //表达式
			}
			recordLog(new YwInfor(task.getActivityName(), Constants.ROLE,variables));
		}
		
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
}
