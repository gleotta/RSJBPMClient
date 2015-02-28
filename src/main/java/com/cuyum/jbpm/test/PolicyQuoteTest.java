package com.cuyum.jbpm.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cuyum.jbpm.client.BRMSClient;
import com.cuyum.jbpm.client.artifacts.HumanTask;
import com.cuyum.jbpm.client.artifacts.responses.GETAssignedTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETParticipationsTasksResponse;
import com.cuyum.jbpm.client.artifacts.responses.GETProcessInstancesResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTClaimTaskResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTCreateInstanceResponse;
import com.cuyum.jbpm.client.artifacts.responses.POSTUpdateTaskResponse;
import com.cuyum.jbpm.client.kie.KieRestClient;

public class PolicyQuoteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//creo una conexión al BRMS
		BRMSClient client = new KieRestClient("localhost", "8080");
		
		try {
			
			final String user = "admin";
			final String pass = "admin";
			//logeo con mis credenciales de Jbpm
			client.login(user, pass);
			
			//creo una lista con los parametros de entrada al proceso
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("driverName", "German");
			vars.put("age", "22");
			vars.put("numberOfAccidents", "1");
			vars.put("numberOfTickets", "0");
			vars.put("vehicleYear", "2009");
			
			//inicio el proceso
			System.out.println("Incio proceso con los siguientes valores: "+vars);
			POSTCreateInstanceResponse instanceResponse = client.createInstance("org.acme.insurance.pricing.policyquoteprocess", vars);
			System.out.println("Se creó instancia "+instanceResponse);
			
			//me fijo que tareas puedo yo como usuario tomar
			GETParticipationsTasksResponse tasksPart =  client.getParticipationsTasks(user);
			
			
			//por cada tarea en la list que no tenga asignada la reclamo 
			List<HumanTask> tasks = tasksPart.getTasks();
			System.out.println("Cantidad de tareas asignables: "+tasks.size());
			
			for (HumanTask ht : tasks) {
				System.out.println("Reclamando tarea "+ht.getName()+"."+ht.getId()+" con estado "+ ht.getCurrentState());
				
				if (ht.getCurrentState().equals("OPEN")) {
					POSTClaimTaskResponse claimResponse = client.claimTask(ht.getId()+"", user);
					System.out.println(claimResponse);
				}

			}
			
			//completo cada tarea que tengo asignada
			GETAssignedTasksResponse tasksPartA =  client.getAssignedTasks(user);
			tasks = tasksPartA.getTasks();
			System.out.println("Cantidad de tareas asignadas: "+tasks.size());
			
			for (HumanTask ht : tasks) {
				System.out.println("Completo tarea "+ht.getName()+"."+ht.getId()+" con estado "+ ht.getCurrentState());
				Map<String, Object> valReview = new HashMap<String, Object>();
				valReview.put("taskPrice", "700");
				POSTUpdateTaskResponse upt =client.updateTask(ht.getId()+"", valReview);
				System.out.println(upt);

			}
			
			//reviso que hayan finalizado las instancias
			GETProcessInstancesResponse resp = client.getProcessIntances("org.acme.insurance.pricing.policyquoteprocess");
			System.out.println("Catidad de instancias del proceso:" + resp.getTotalCount());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}