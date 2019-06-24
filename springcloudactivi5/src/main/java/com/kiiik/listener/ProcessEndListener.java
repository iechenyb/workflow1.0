package com.kiiik.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.kiiik.pub.ProcessServiceImpl;
import com.kiiik.utils.MyApplicationContextHandler;

public class ProcessEndListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		/*String paramName = (String)execution.getVariable("anyname");
		ServletContext sc = ServletActionContext.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);*/
		execution.getVariableInstancesLocal();
		System.out.println("流程结束了！"+MyApplicationContextHandler.getBean(ProcessServiceImpl.class));
	}

}
