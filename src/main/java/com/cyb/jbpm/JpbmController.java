package com.cyb.jbpm;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskService;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Task;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyb.Constants;
import com.cyb.jbpm.vo.ProcessDefinitionVo;
import com.cyb.jbpm.vo.ProcessInstanceVo;
import com.cyb.jbpm.vo.TaskVo;

@RequestMapping("/jbpm")
@Controller
public class JpbmController {
	@Resource(name = "taskService")
	public TaskService taskService;
	@Resource(name = "processEngine")
	public ProcessEngine processEngine;
	@Resource(name = "executionService")
	public ExecutionService executionService;
	@Resource(name = "repositoryService")
	public org.jbpm.api.RepositoryService repositoryService;

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

	@RequestMapping("/start")
	public ModelAndView startProcess(String id) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("content", "bad");
		System.out.println("id====" + id);
		ExecutionService executionService = processEngine.getExecutionService();
		/** 鏍规嵁娴佺▼鐨刱ey鏉ュ垱寤轰竴涓祦绋嬪疄渚� */
		// ProcessInstance pi =
		// executionService.startProcessInstanceByKey("draw_key",variables);
		ProcessInstance pi = executionService.startProcessInstanceById(
				"draw_key-1", variables);
		System.out.println(pi);
		// 妫�楠屾祦绋嬪疄渚媝i锛屾槸鍚︾粨鏉熶簡
		System.out.println(pi.isEnded());
		// 褰撴祦绋嬪鐞嗙瓑寰呮椂锛屾垜浠彲浠ユ墜鍔ㄨ鍏跺悜涓嬭繍琛�
		// 鍙傛暟鏄牴鎹祦绋嬪疄渚嬬殑ID
//		pi = executionService.signalExecutionById(pi.getId());
//		System.out.println(pi.isEnded());
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}

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
	@RequestMapping("/del1")
	public ModelAndView deleteDeploy1(String deploymentId) {
		repositoryService.deleteDeploymentCascade(deploymentId);
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		AbstractAutowireCapableBeanFactory x;
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		view.setViewName("jbpm/list-1");
		return view;
	}
	@ResponseBody
	@RequestMapping("/lst")
	public JSONArray showList() {
		Map ret = new HashMap();
		ret.put("data", getData());
		ret.put("data2", getData2());
		ret.put("data3", myTasks());
		return JSONArray.fromObject(ret);
	}
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
	public List<ProcessDefinitionVo> getData(){
		List<ProcessDefinitionVo> data = new ArrayList<ProcessDefinitionVo>();
		// 2.1鍒涘缓涓�涓祦绋嬫悳绱㈢被
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery();
		// 2.2鎼滅储娴佺▼瀹氫箟
		List<ProcessDefinition> processDefitionList = processDefinitionQuery
				.list();
		// 2.3寰幆杈撳嚭娴佺▼瀹氫箟ID锛堢湅鐪嬪拰娴佺▼瀹氫箟鏂囦欢锛歫pdl.xml鐨刵ame鍝︼級
		ProcessDefinitionVo obj = null;
		for (ProcessDefinition pd : processDefitionList) {
			obj = new ProcessDefinitionVo();
			System.out.println("娴佺▼ID锛�" + pd.getId());
			obj.setDeploymentId(pd.getDeploymentId());
			obj.setId(pd.getId());
			obj.setPicName(pd.getImageResourceName());
			obj.setKey(pd.getKey());
			obj.setVersion(pd.getVersion() + "");
			data.add(obj);
		}
		return data;
	}
	@RequestMapping("/lst1")
	public ModelAndView showList1() {
		ModelAndView view = new ModelAndView();
		view.setViewName("jbpm/list-1");
		AbstractAutowireCapableBeanFactory x;
		
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
		Map ret = new HashMap();
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
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
		view.addObject("person", "chenyb");
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
		view.addObject("person", "chenyb");
		view.addObject("x", ac.getX());
		view.addObject("y", ac.getY());
		view.addObject("w", ac.getWidth());
		view.addObject("h", ac.getHeight());
		view.addObject("id", id);
		view.addObject("path", path);
		return view;
	}
	public List<TaskVo> myTasks(){
		List<Task> taskList = taskService.findPersonalTasks("chenyb"); 
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
	
	@RequestMapping("/handle")
	public ModelAndView handlerTask(String cmd,String id) {
		Map<String, Object> variables = new HashMap<String, Object>();
		System.out.println("变量名："+taskService.getVariableNames(id));
		ModelAndView view = new ModelAndView();
		if(cmd.equals("go")){
			taskService.completeTask(id);  //go next
		}else{
			variables.put("content", cmd); //go next with content conditions
			taskService.setVariables(id,variables);  
			taskService.completeTask(id);  
		}
		view.setViewName("jbpm/list-1");
		view.addObject("data", getData());
		view.addObject("data2", getData2());
		view.addObject("data3", myTasks());
		return view;
	}
}
