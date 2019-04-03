package com.cyb.jbpm;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;
/**
 * 每次修改完成后  需要重新部署。
 * @author Administrator
 *
 */
public class FirstHandler implements DecisionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String decide(OpenExecution params) {
		if(params!=null){
			System.out.println("handler:"+params.getVariables());
			return (String) params.getVariable("content");
		}
		return "tob";
	}

}
