package com.sss.report;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.xml.stream.XMLStreamException;

import com.sss.report.core.HibernateUtil;
import com.sss.report.entity.ProfileEntity;
import com.sss.report.service.ReportService;
import com.sss.report.service.XMLServices;

public class ReportApplication {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, XMLStreamException {
		System.setProperty("derby.system.home", System.getProperty("user.dir"));
		System.out.println(System.getProperty("derby.system.home"));
		HibernateUtil.create();
		XMLServices xmlService = new XMLServices();
		ReportService reportService = new ReportService(args[0], args[2]);
		Set<ProfileEntity> profiles = xmlService.parseProfiles(args[1]);
		System.out.println("Number of profiles parsed : " + profiles.size());
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		FutureTask<String> reportTask = new FutureTask<String>(reportService);
		threadPool.submit(reportTask);
		String reportLocationOnMode = reportTask.get();
		threadPool.shutdown();
		System.out.println(reportLocationOnMode);
		HibernateUtil.shutdown();
	}
	
} 