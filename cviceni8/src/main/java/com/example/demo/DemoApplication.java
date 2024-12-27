package com.example.demo;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@Deployment(resources = "classpath:sem_work_bondarchuk.bpmn")
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private ZeebeClient zeebeClient;

	public static void main (String[]args){
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run ( final String...args){
		var bpmnProcessId = "translator_bondakos";
		try {

			var event = zeebeClient.newCreateInstanceCommand()
					.bpmnProcessId(bpmnProcessId)
					.latestVersion()
					.variables(Map.of("total", 100))
					.send()
					.join();
			LOG.info(String.format("started a process: %d", event.getProcessInstanceKey()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
