package com.kiiik.demo;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 工作流学习
 * 多人会签，第二个节点
 * @author Administrator
 *  问题：因为存在多个任务，后台报错！
 *  无法查看图片和在多人会签节点执行流程
 *  已解决！single-result 改成 list.get(0)
 *  流程：
 *  发起流程-根据流程号执行【上级领导方法】
 *  ->根据流程号执行会签【公司领导】
 *  ->根据流程号执行会签【公司领导】
 *  -结束！
 *  查看日志 会有执行人员的信息提示！
 */
@Controller
@RequestMapping("/mutil/activiti")
public class ActivitiMutilController extends ActivitiBaseController{
	//上级领导
	@GetMapping("upperLeader")
	@ResponseBody
	public String deptLeader(String processId,boolean deptLeaderPass){
		Task task = activitProcessServiceImpl.getTask(processId);
		System.out.println("执行人员信息："+task.getAssignee());
		activitProcessServiceImpl.addComment(task.getId(), processId, "上级领导"+(deptLeaderPass?"通过":"不通过"), username);
		activitProcessServiceImpl.complete(task.getId());
		return "ok";
	}
	//companyLeader
	@GetMapping("companyLeader")
	@ResponseBody
	public String companyLeader(String processId,String userId){
		/*long total = taskService.createTaskQuery()
				.taskAssignee(userId)
				.taskNameLike("%" + userId + "%").count();
		List<Task> taskList = taskService.createTaskQuery()// 根据用户id查询
				.taskAssignee(userId)
				// 根据任务名称查询
				.taskNameLike("%" + userId + "%")
				// 返回带分页的结果集合
				.listPage(0, 10);*/
		/*total = taskService.createTaskQuery()
				.taskCandidateUser(userId)
				.taskNameLike("%" + userId + "%").count(); 
		taskList = taskService.createTaskQuery()
				// 根据用户id查询
				.taskCandidateUser(userId)// 根据任务名称查询
				.taskNameLike("%" + userId + "%")// 返回带分页的结果集合
				.listPage(0, 10);*/
		Task task = activitProcessServiceImpl.getTask(processId);
		System.out.println("多人会签-执行人员信息："+task.getAssignee());
		//activitProcessServiceImpl.addComment(task.getId(), processId, "公司领导"+(hrPass?"通过":"不通过"), username);
		activitProcessServiceImpl.completeByProcId(processId);
		return "ok";
	}
	
	
	
}