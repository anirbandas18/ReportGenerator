 package com.sss.report;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

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
		Utility.configure();
		String childDirName = args[1].substring(args[1].lastIndexOf(File.pathSeparatorChar) + 1);
		Preferences registry = Preferences.userNodeForPackage(ReportApplication.class);
		System.out.println(Arrays.toString(registry.keys()));
		Boolean shouldProcess = registry.getBoolean(childDirName, true);
		HibernateUtil.start(shouldProcess);
		Set<String> filter = new TreeSet<>();
		if(args.length == 4) {
			filter.addAll(Arrays.asList(args[3].split(",")).stream().map(x -> x.toLowerCase()).collect(Collectors.toSet()));
		}
		ReportMetadata reportMetadata = new ReportMetadata();
		reportMetadata.setMode(Mode.valueOf(args[0]));
		reportMetadata.setReportDumpLocation(args[2]);
		reportMetadata.setFilter(filter);
		XMLService xmlService = new XMLService(args[1]);
		ProfileMetadata profileMetadata = xmlService.parseProfiles(shouldProcess, filter);
		reportMetadata.setProfileMetadata(profileMetadata);
		System.out.println(reportMetadata);
		ReportDumpService reportDump = new ReportDumpService(reportMetadata);
		reportDump.dump(reportMetadata);
		registry.putBoolean(args[1], false);
		HibernateUtil.shutdown();
	}
} 