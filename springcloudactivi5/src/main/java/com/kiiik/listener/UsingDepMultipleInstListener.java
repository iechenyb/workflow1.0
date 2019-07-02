package com.kiiik.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
public class UsingDepMultipleInstListener implements ExecutionListener {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		//模拟在数据库里查出会签的用户名把用户放到Variable
		List<String> assigneeList=new ArrayList<String>();
		assigneeList.add("wangba");
		assigneeList.add("wangjiu");
		execution.setVariable("publicityList", assigneeList);
	}
}