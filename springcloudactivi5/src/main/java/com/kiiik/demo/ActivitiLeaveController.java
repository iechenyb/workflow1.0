package com.kiiik.demo;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kiiik.listener.TaskListenerDemo;

/**
 * 工作流学习
 * 动态指定用户
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/leave/activiti")
public class ActivitiLeaveController extends ActivitiBaseController{
	//部门领导
	@GetMapping("deptLeader")
	@ResponseBody
	public String deptLeader(String processId,boolean deptLeaderPass){
		//deptLeaderPass
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("deptLeaderPass", deptLeaderPass);//不通过 重新调整，否则到hr
		if(!deptLeaderPass){//不通过退回申请人那里
			variables.put("applyUserId",username );
			variables.put("afterModifyApplyContentProcessor", new TaskListenerDemo());
			//variables.put("afterModifyApplyContentProcessor","proxystring" );
		}
		
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.addComment(task.getId(), processId, "领导"+(deptLeaderPass?"通过":"不通过"), username);
		activitProcessServiceImpl.complete(task.getId(), variables);
		return "ok";
	}
	//hr
	@GetMapping("hr")
	@ResponseBody
	public String hr(String processId,boolean hrPass){
		//hrPass
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("hrPass", hrPass);
		variables.put("applyUserId", username);
		if(hrPass){
			variables.put("reportBackEndProcessor", new TaskListenerDemo());
		}
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.addComment(task.getId(), processId, "领导"+(hrPass?"通过":"不通过"), username);
		activitProcessServiceImpl.completeByProcId(processId, variables);
		return "ok";
	}
	//重新填写并申请
	@GetMapping("reApply")
	@ResponseBody
	public String reApply(String processId,boolean reApply){
		//reApply
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("reApply", reApply);//修改请假单重新提交或者放弃申请
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.addComment(task.getId(), processId, username+(reApply?"修改提交":"放弃申请"), username);
		activitProcessServiceImpl.completeByProcId(processId, variables);
		return "ok";
	}
	//销假
	@GetMapping("comfirmHolidays")
	@ResponseBody
	public String comfirmHolidays(String processId){
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.addComment(task.getId(), processId,username+"销假", username);
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
}