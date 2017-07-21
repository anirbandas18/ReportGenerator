 package com.sss.report;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

import javax.xml.stream.XMLStreamException;

import com.sss.report.core.HibernateUtil;
import com.sss.report.model.ProfileMetadata;
import com.sss.report.model.ReportMetadata;
import com.sss.report.service.XMLProcessor;;

public class ReportApplication {
	
	/**
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 * @throws XMLStreamException
	 * 
	 * args[0] = mode
	 * args[1] = location of directory containing files to be processed
	 * args[2] = location of directory for report dump 
	 */
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, XMLStreamException {
		//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt")), true));
		System.setProperty("derby.system.home", System.getProperty("user.dir"));
		System.out.println(System.getProperty("derby.system.home"));
		String childDirName = args[1].substring(args[1].lastIndexOf(File.pathSeparatorChar) + 1);
		Preferences registry = Preferences.userNodeForPackage(ReportApplication.class);
		Boolean shouldProcess = registry.getBoolean(childDirName, true);
		HibernateUtil.start(shouldProcess);
		ProfileMetadata profileMetadata = XMLProcessor.parseProfiles(shouldProcess, args[1]);
		ReportMetadata reportMetadata = new ReportMetadata();
		reportMetadata.setMode(args[0]);
		reportMetadata.setProfileMetadata(profileMetadata);
		reportMetadata.setReportDumpLocation(args[2]);
		/*ReportService reportService = new ReportService(args[0], args[2], profileSet);
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		FutureTask<String> reportTask = new FutureTask<String>(reportService);
		threadPool.submit(reportTask);
		String reportLocationOnMode = reportTask.get();
		threadPool.shutdown();
		System.out.println(reportLocationOnMode);*/
		registry.putBoolean(args[1], false);
		HibernateUtil.shutdown();
	}
	
} 