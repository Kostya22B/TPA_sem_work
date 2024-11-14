package com.example.demo;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath:myprocessapplication.bpmn")
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private ZeebeClient zeebeClient;

	public static void main (String[]args){
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run ( final String...args){
		var bpmnProcessId = "UniversalPaymentProcess";
		try {

			var event = zeebeClient.newCreateInstanceCommand()
					.bpmnProcessId(bpmnProcessId)
					.latestVersion()
					.send()
					.join();
			LOG.info(String.format("started a process: %d", event.getProcessInstanceKey()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
