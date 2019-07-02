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
 * 并行网关使用  or关系
 * @author Administrator
 *  财务或者人事都成立时，执行完成后，才能到办理离职！
 */
@Controller
@RequestMapping("/parallel/activiti")
public class ActivitiParallelGatewayController extends ActivitiBaseController{
	//申请离职
	@GetMapping("applay")
	@ResponseBody
	public String deptLeader(String processId,
			boolean isPayment,
			boolean isReturn){
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("isPayment",isPayment );
		variables.put("isReturn", isReturn);
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.complete(task.getId(), variables);
		return "ok";
	}
	//hr
	@GetMapping("hr")
	@ResponseBody
	public String hr(String processId,boolean hrPass){
		//hrPass
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
	//cw
	@GetMapping("cw")
	@ResponseBody
	public String cw(String processId,boolean hrPass){
		//cwPass
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
	//办理离职
	@GetMapping("doLeave")
	@ResponseBody
	public String reApply(String processId,boolean reApply){
		//doLeave
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
	
}