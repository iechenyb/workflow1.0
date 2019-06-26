package com.kiiik.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器支持以下属性：
	event（必选）：任务监听器会被调用的任务类型。 
	可能的类型为：
	create：任务创建并设置所有属性后触发。	
	assignment：任务分配给一些人时触发。 当流程到达userTask， assignment事件 会在create事件之前发生。 这样的顺序似乎不自然，但是原因很简单：当获得create时间时， 我们想获得任务的所有属性，包括执行人。
	complete：当任务完成，并尚未从运行数据中删除时触发。
	delete：只在任务删除之前发生。 注意在通过completeTask正常完成时，也会执行。
	class：必须调用的代理类。 这个类必须实现org.activiti.engine.delegate.TaskListener接口。
 * @author Administrator
 *
 */
public class MyTaskCreateListener implements TaskListener {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
	    // Custom logic goes here
	  }

	}