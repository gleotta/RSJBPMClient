ClienteREST de JBPM 5
======================

1) El Cliente
-------------
El actual proyecto contiene un componente de desarrollo construido en Java que permite a las aplicaciones que lo utilicen interactuar con procesoso jBPM5 remotos desplegados y ejecutados en BRMS. Este componente contiene una interface POJO con servicios cuya implementación utiliza las API Rest de jBPM5 disponibilizadas en http://SERVER_BRMS:PORT/business-central-server

La interface donde se definene los servicios es: 
	* com.cuyum.jbpm.client.BRMSClient
La implementación con las API Rest es: 
	* com.cuyum.jbpm.client.BRMSClientImpl

2) El Ejemplo
-------------
Este proyecto además contiene un ejemplo que utiliza el componente para interactuar con un BRMS que debe tener desplegado el proceso "policyquoteprocess" construido y desarrollado en los labs del Workshop cuyo paquete se facilita en esta distribución para ser importado en caso de ser necesario.

La clase ejemplo que utiliza el componente es:
	* com.cuyum.jbpm.test.PolicyQuoteTest
	
3) Requisitos
-------------
Para poder abrir el ejemplo en JBDS y correr el ejemplo ser necesario contar con:

	* JDK 1.6.x
	* Jboss Development Studio 5.x (JBDS)
	* RedHat BRMS 5.3.0 para ejecutar el ejemplo
	
4) Instalación
---------------
	1) Descomprimir directorio RSJBPMClient.zip 
	2) Abrir JBDS y crear un nuevo workspace
	3) Importar proyecto Maven seleccionando el directorio donde se descomprimió el proyecto (File->Import->Maven->Existing Maven Project)
	4) Realizar un maven install desde JBDS para corroborar que esté ok (Click Derecho Sobre el Proyecto -> Run As -> maven install)
	
5) Configuración BRMS Server para ejemplo
-----------------------------------------
Se configurará BRMS para poder ejecutar los ejemplos desarrollados en Lab, para ellos antes de inciar BRMS se recomienda verificar que tenga la siguiente configuración, en caso contrario realizarla:
	1) Copiar o reemplazar el archivo RSJBMClient/src/test/resources/brms-roles.properties en brms-standalone-5.3.0\jboss-as\server\default\conf\props. Esto habilita al usuario admin ser parte del grupo reviewer.
	2) Copiar o reemplazar el jar RSJBMClient/src/test/resources/policyquotes-jbpm.jar en brms-standalone-5.3.0\jboss-as\server\default\deploy\business-central-server.war\WEB-INF\lib. Este jar es el modeo de hechos y lo necesita la consola jbpm para monitorear la ejecución del proceso.
	3) Copiar y reemplazar el archivo RSJBMClient/src/test/resources/jbpm.usergroup.callback.properties al directorio (crearlo si es necesario) brms-standalone-5.3.0\jboss-as\server\default\deploy\jbpm-human-task.war\WEB-INF\classes\org\jbpm\task\service. 
	4) Iniciar BRMS en localhost puerto 8080.
	5) Loguearse a http://localhost:8080/jboss-brms con user admin y password admin. Luego dentro de BRMS en Administration -> Import Export, seleccionar el paquete /RSJBMClient/src/test/resources/repository_export.zip subirlo e importarlo dentro del BRMS.
	
6) Ejecutar ejemplo
-------------------
Se puede correr desde el JBDS el ejemplo que muestra como se incia el proceso "org.acme.insurance.pricing.policyquoteprocess", se obtienen las tareas asignables, se reclaman las tareas y se completan.
Click derecho sobre la clase *com.cuyum.jbpm.test.PolicyQuoteTest* y elegir Run As -> Java Application
Se deberia ver por consola la siguiente salida:
	*Inicio proceso con los siguientes valores: {vehicleYear=2009, age=22, numberOfAccidents=1, driverName=German, numberOfTickets=0}*
	*Se creó instancia POSTCreateInstanceResponse [status=success, message=]*
	*Cantidad de tareas asignables: 1*
	*Reclamando tarea reviewQuote.1 con estado OPEN*
	*BRMSClientWSResponse [status=success, message=]*
	*Cantidad de tareas asignadas: 1*
	*Completo tarea reviewQuote.1 con estado ASSIGNED*
	*BRMSClientWSResponse [status=success, message=]*
	*Cantidad de instancias del proceso:0*
	
7) Soporte
----------
Por cualquier error, duda o problema, por favor contactarse con:
German Leotta
gleotta@cuym.com

