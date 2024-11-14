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
@Deployment(resources = "classpath:Test_process_with_user_task.bpmn")
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private ZeebeClient zeebeClient;

	public static void main (String[]args){
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run ( final String...args){
		var bpmnProcessId = "TestProcessWithUserTask"; // or whatever your process ID in the Modeler is
		var event = zeebeClient.newCreateInstanceCommand()
				.bpmnProcessId(bpmnProcessId)
				.latestVersion()
				.variables(Map.of("total", 100))	//here I can put some variables if I need
				.send()
				.join();

		LOG.info(String.format("started a process: %d", event.getProcessInstanceKey()));
	}
}
