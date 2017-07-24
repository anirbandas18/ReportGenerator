 package com.sss.report;

import java.io.File;
import java.util.prefs.Preferences;

import com.sss.report.core.tags.Mode;
import com.sss.report.metadata.model.ProfileMetadata;
import com.sss.report.metadata.model.ReportMetadata;
import com.sss.report.service.ReportDumpService;
import com.sss.report.service.XMLService;
import com.sss.report.util.HibernateUtil;
import com.sss.report.util.Utility;;

public class ReportApplication {
	
	/**
	 * 
	 * @param args
	 * args[0] = mode
	 * args[1] = location of directory containing files to be processed
	 * args[2] = location of directory for report dump 
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
		//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt")), true));
		Utility.configure();
		String childDirName = args[1].substring(args[1].lastIndexOf(File.pathSeparatorChar) + 1);
		Preferences registry = Preferences.userNodeForPackage(ReportApplication.class);
		Boolean shouldProcess = registry.getBoolean(childDirName, true);
		HibernateUtil.start(shouldProcess);
		XMLService xmlService = new XMLService(args[1]);
		ProfileMetadata profileMetadata = xmlService.parseProfiles(shouldProcess);
		ReportMetadata reportMetadata = new ReportMetadata();
		reportMetadata.setMode(Mode.valueOf(args[0]));
		reportMetadata.setProfileMetadata(profileMetadata);
		reportMetadata.setReportDumpLocation(args[2]);
		 System.out.println(reportMetadata);
		ReportDumpService reportDump = new ReportDumpService(reportMetadata);
		reportDump.generate(reportMetadata);
		registry.putBoolean(args[1], false);
		HibernateUtil.shutdown();
	}
} 