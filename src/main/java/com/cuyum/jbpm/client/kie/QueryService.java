package com.cuyum.jbpm.client.kie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.kie.api.task.model.Status;

public class QueryService {

	//private HashMap<String, List<String>> parameter;
	
	public boolean isEmpty(HashMap<String, List<String>> parameter) {
		
		boolean empty=true;
		if(parameter!=null){
			for (List<String> value : parameter.values()) {
				if(value!=null){
					if(!value.isEmpty())
						empty=false;
				}
			}		
		}
		return empty;
	}

	public List<String> getProcessInstanceId(HashMap<String, List<String>> parameters) {
		List<String> instanceIds = parameters.get("processInstanceId");
		return instanceIds;
	}

	public HashMap<String, List<String>> createMap(List<Long> workItemId,
			List<Long> taskId, List<String> businessAdministrator,
			List<String> potentialOwner, List<Status> status,
			List<String> taskOwner, List<Long> processInstanceId) {
		
		HashMap<String, List<String>> parameter = new HashMap<String, List<String>>();
		List<String> newList;
		
		//*********************************************************
		
		if(!workItemId.isEmpty()){
			newList=new ArrayList<String>();
			for (Long newString : workItemId) { 
				  newList.add(newString.toString()); 
			}
			parameter.put("workItemId", newList);
		}	
		else
			parameter.put("workItemId", null);
		
		//*********************************************************
		
		if(!taskId.isEmpty()){
			newList=new ArrayList<String>();
			for (Long newString : taskId) { 
				  newList.add(newString.toString()); 
			}
			parameter.put("taskId", newList);
		}	
		else
			parameter.put("taskId", null);
		
		//*********************************************************
		
		if(businessAdministrator!=null)
			parameter.put("businessAdministrator", businessAdministrator);
		else
			parameter.put("businessAdministrator", null);
		
		//*********************************************************
		
		if(potentialOwner!=null)
			parameter.put("potentialOwner", potentialOwner);
		else
			parameter.put("potentialOwner", null);
		
		//*********************************************************
		
		if(!status.isEmpty()){
			newList=new ArrayList<String>();
			for (Status newString : status) { 
				  newList.add(newString.toString()); 
			}
			parameter.put("status", newList);
		}
		else
			parameter.put("status", null);
		
		//*********************************************************
		
		if(taskOwner!=null)
			parameter.put("taskOwner", taskOwner);
		else
			parameter.put("taskOwner", null);
		
		//*********************************************************
		
		if(!processInstanceId.isEmpty()){
			newList=new ArrayList<String>();
			for (Long newString : processInstanceId) { 
				  newList.add(newString.toString()); 
			}
			parameter.put("processInstanceId", newList);
		}
		else
			parameter.put("processInstanceId", null);
		
		//*********************************************************
		
		return parameter;
	}

	public HashMap<String, Object> createTaskParam(String query) {
		
		String[] params = null;
		if(query!=null && !query.isEmpty()){
			params = query.split("&");
		}else{
			return null;
		}
		return getTaskParam(params);
	}
 	
	public HashMap<String, Object> getTaskParam(String[] params){
		HashMap<String, Object> mapParam = new HashMap<String, Object>();
		
		String key;
		Object value;

		for (String string : params) {
			key= new String(string.substring(0, string.indexOf('=')));
			value = (Object) new String (string.substring(string.indexOf('=')+1));
			mapParam.put(key, value);
		}
		return mapParam;
	}
	
}
