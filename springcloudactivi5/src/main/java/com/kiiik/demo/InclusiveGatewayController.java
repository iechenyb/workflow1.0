package com.kiiik.demo;

import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 工作流学习
 * 并行网关使用  or关系
 * @author Administrator
 *  财务或者人事都成立时，执行完成后，才能到办理离职！
 */
@Controller
@RequestMapping("/inclusive/activiti")
public class InclusiveGatewayController extends ActivitiBaseController{
	//生成订单
	@GetMapping("order")
	@ResponseBody
	public String deptLeader(String processId){
		Task task = activitProcessServiceImpl.getTask(processId);
		activitProcessServiceImpl.complete(task.getId());
		return "ok";
	}
	//付款
	@GetMapping("pay")
	@ResponseBody
	public String hr(String processId){
		//hrPass
		List<Task> tasks = activitProcessServiceImpl.getTaskList(processId);
		String taskId ="";
		if(!CollectionUtils.isEmpty(tasks)){
			for(Task t:tasks){
				if("付款".equals(t.getName())){
					taskId=t.getId();
					break;
				}
			}
		}
		activitProcessServiceImpl.complete(taskId);
		return "ok";
	}
	//收款
	@GetMapping("settleMoney")
	@ResponseBody
	public String cw(String processId){
		//settleMoney
		String taskId ="";
		List<Task> tasks = activitProcessServiceImpl.getTaskList(processId);
		if(!CollectionUtils.isEmpty(tasks)){
			for(Task t:tasks){
				if("收款".equals(t.getName())){
					taskId=t.getId();
					break;
				}
			}
		}
		activitProcessServiceImpl.complete(taskId);
		return "ok";
	}
	//发货
	@GetMapping("sendGoods")
	@ResponseBody
	public String sendGoods(String processId){
		//sendGoods
		String taskId ="";
		List<Task> tasks = activitProcessServiceImpl.getTaskList(processId);
		if(!CollectionUtils.isEmpty(tasks)){
			for(Task t:tasks){
				if("发货".equals(t.getName())){
					taskId=t.getId();
					break;
				}
			}
		}
		activitProcessServiceImpl.complete(taskId);
		return "ok";
	}
	@GetMapping("recGoods")
	@ResponseBody
	public String recGoods(String processId){
		//recGoods
		String taskId ="";
		List<Task> tasks = activitProcessServiceImpl.getTaskList(processId);
		if(!CollectionUtils.isEmpty(tasks)){
			for(Task t:tasks){
				if("收货".equals(t.getName())){
					taskId=t.getId();
					break;
				}
			}
		}
		activitProcessServiceImpl.complete(taskId);
		return "ok";
	}
	@GetMapping("orderOver")
	@ResponseBody
	public String orderOver(String processId){
		//recGoods
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
	
}