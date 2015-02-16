/**
 * 
 */
package com.cuyum.jbpm.client.kie.response;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jbpm.services.task.impl.model.UserImpl;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.User;


/**
 * @author Jorge Morando
 *
 */

@XmlRootElement(name = "task-summary")
@XmlAccessorType (XmlAccessType.FIELD)
public class TaskSummary implements  org.kie.api.task.model.TaskSummary{
	
	@XmlElement(name = "id")
	private Long id;
	
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "subject")
	private String subject;
	
	@XmlElement(name = "description")
	private String description;
	
	@XmlElement(name = "status")
	private String status;
	
	@XmlElement(name = "priority")
	private Integer priority;
	
	@XmlElement(name = "skipable")
	private String skipable;
	
	@XmlElement(name = "actual-owner")
	private String actualOwner;
	
	@XmlElement(name = "created-on")
	private Date createdOn;
	
	@XmlElement(name = "activation-time")
	private Date activationTime;
	
	@XmlElement(name = "process-instance-id")
	private Long processInstanceId;
	
	@XmlElement(name = "process-id")
	private String processId;
	
	@XmlElement(name = "process-session-id")
	private Integer processSessionId;
	
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return Status.valueOf(status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getSkipable() {
		return skipable;
	}

	public void setSkipable(String skipable) {
		this.skipable = skipable;
	}

	public User getActualOwner() {
		return new UserImpl(actualOwner);
	}

	public void setActualOwner(String actualOwner) {
		this.actualOwner = actualOwner;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public int getProcessSessionId() {
		return processSessionId;
	}

	public void setProcessSessionId(Integer processSessionId) {
		this.processSessionId = processSessionId;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSkipable() {
		
		return skipable.equals("true");
	}

	@Override
	public User getCreatedBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getExpirationTime() {
	
		return null;
	}

	@Override
	public List<String> getPotentialOwners() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
