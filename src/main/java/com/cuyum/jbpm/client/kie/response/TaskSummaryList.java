/**
 * 
 */
package com.cuyum.jbpm.client.kie.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Jorge Morando
 *
 */

@XmlRootElement(name = "task-summary-list")
@XmlAccessorType (XmlAccessType.FIELD)
public class TaskSummaryList{
	
	
	@XmlElement (name="task-summary")
	private List<TaskSummary>	taskSummaryList;
	
	@SuppressWarnings("rawtypes")
	public List getTaskSummary(){
		return taskSummaryList;
	}
	
	public void setEmployees(List<TaskSummary> taskSummaryList) {
        this.taskSummaryList = taskSummaryList;
    }
}
