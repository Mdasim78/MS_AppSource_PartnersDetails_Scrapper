package com.appsource.partnerDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class DataExporter {
	
	public static CSVPrinter createCSVprinter(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		String[] header = new String[]{"Partner Name","Profile URL","Locations","Partner Description","Designations","Expertise","Contact Informations"};
		return new CSVPrinter(writer, CSVFormat.EXCEL.builder().setHeader(header).build());
	}
	
}
