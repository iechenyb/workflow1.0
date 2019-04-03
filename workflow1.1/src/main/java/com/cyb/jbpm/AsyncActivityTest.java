/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.cyb.jbpm;

import java.io.InputStream;
import java.util.Set;

import org.jbpm.api.Execution;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.pvm.internal.id.PropertyImpl;
import org.jbpm.test.JbpmTestCase;
import org.slf4j.helpers.MessageFormatter;


/**
 * @author Tom Baeyens
 */
public class AsyncActivityTest extends JbpmTestCase {

  String deploymentId;
  
  protected void setUp() throws Exception {
   
    super.setUp();
    //com/cyb/jbpm/process.jpdl.xml
    //jbpm/process.jpdl.xml
    PropertyImpl y;
    MessageFormatter z;
//    z.format(messagePattern, arg)
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("process.jpdl.xml")
        .deploy(); System.out.println("setUp");
//        StaticLoggerBinder x;
  }

  protected void tearDown() throws Exception {
	
//    repositoryService.deleteDeploymentCascade(deploymentId);    
//    super.tearDown();  System.out.println("tearDown:"+deploymentId);
  }

  public void testAsyncActivity() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("AsyncActivity");
    String processInstanceId = processInstance.getId();
    System.out.println("processInstance.getid:"+processInstanceId);
//    assertEquals(Execution.STATE_ASYNC, processInstance.getState());
    /* Job job = managementService.createJobQuery().processInstanceId(processInstanceId)
      .uniqueResult();
    System.out.println("job:"+job);
    managementService.executeJob(job.getId());
     processInstance = executionService.findProcessInstanceById(processInstanceId);
    assertEquals(Execution.STATE_ASYNC, processInstance.getState());
    job = managementService.createJobQuery()
      .processInstanceId(processInstanceId)
      .uniqueResult();
    managementService.executeJob(job.getId());
    assertNull(executionService.findProcessInstanceById(processInstanceId));
  System.out.println("testAsyncActivity");*/
    Execution executionInA = processInstance.findActiveExecutionIn("a");
    assertNotNull(executionInA);

    processInstance = executionService.signalExecutionById(executionInA.getId());
    Execution executionInB = processInstance.findActiveExecutionIn("b");
//    assertNotNull(executionInB);

    /*processInstance = executionService.signalExecutionById(executionInB.getId());
    Execution executionInC = processInstance.findActiveExecutionIn("c");
    assertNotNull(executionInC);*/
  }
  public void testShowPic(){
	  ProcessInstance processInstance = executionService.startProcessInstanceByKey("AsyncActivity");
	    String processInstanceId = processInstance.getId();
	    System.out.println("processInstance.getid:"+processInstanceId);
	  Execution executionInA = processInstance.findActiveExecutionIn("a");
	    assertNotNull(executionInA);

	    processInstance = executionService.signalExecutionById(executionInA.getId());
	    Execution executionInB = processInstance.findActiveExecutionIn("b");
	    assertNotNull(executionInB);
	    processInstance = executionService.signalExecutionById(executionInB.getId());
	    Execution executionInC = processInstance.findActiveExecutionIn("c");
	    assertNotNull(executionInC);
	    String id ="AsyncActivity.7";
//		 String id=request.getParameter("id");
//		 ProcessInstance processInstance=executionService.findProcessInstanceById(id);
		 String processDefinitionId=processInstance.getProcessDefinitionId();
		 ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).uniqueResult();
		 InputStream inputStream=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), "process.png");
	     String path = "d:/jpbm/process.png";
	     System.out.println("path:"+path);
//		 FileUtils.copyFileByStream(inputStream, path);
//		 http://blog.csdn.net/clj198606061111/article/details/8507515
//		    String id=request.getParameter("id");
//		    ProcessEngine processEngine=Configuration.getProcessEngine(); 
//		    RepositoryService repositoryService = processEngine.getRepositoryService();
//		    ExecutionService executionService=processEngine.getExecutionService();
//		    ProcessInstance processInstance=executionService.findProcessInstanceById(id);
		    Set<String> activityName=processInstance.findActiveActivityNames();
		    ActivityCoordinates ac=repositoryService.getActivityCoordinates(processInstance.getProcessDefinitionId(), activityName.iterator().next());
		    System.out.println("x = "+ac.getX());
		    System.out.println("y = "+ac.getY());
		    System.out.println("x = "+ac.getWidth());
		    System.out.println("x = "+ac.getHeight());
  }
}
