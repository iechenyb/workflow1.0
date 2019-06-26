package com.kiiik.listener;

import java.io.Serializable;
 
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
 
/**
 * 
 * 任务监听器，实现TaskListener接口
 * 
 * */
public class TaskListenerDemo implements Serializable, TaskListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Expression arg;//listener 中的fields名称必须为arg
 
	public Expression getArg() {
		return arg;
	}
 
	public void setArg(Expression arg) {
		this.arg = arg;
	}
    /**
     * Task[id=160017, name=调整申请] 任务指派人等信息
     */
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("任务监听器:" + delegateTask+",arg="+arg);
	}
 
}
