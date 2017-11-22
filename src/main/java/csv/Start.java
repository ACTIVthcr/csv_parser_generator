package csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class Start {
	
	private static final String PATH = "C:\\Users\\Master Two T\\eclipse-workspace\\csv\\src\\main\\resources\\";

	public static void main(String[] args) throws IOException {
		LocalDebug.DEBUG = true;
		String csvOrigin = PATH + "test1.csv";
		String csvTarget = PATH + "test2.csv";
		DefaultTableModel originTable = ReadCSVFileTest.start(csvOrigin);
		DefaultTableModel targetTable = ReadCSVFileTest.start(csvTarget);
		CompareTable compareTable = new CompareTable(originTable, targetTable);
		compareTable.Compare();
		
		String csvOutputNewId = PATH + "csvOutputNewId.csv";
		String csvOutputSuppressId = PATH + "csvOutputSuppressId.csv";
		String csvOutputDescrChange = PATH + "csvOutputDescrChange.csv";
		String csvOutputTitleChange = PATH + "csvOutputTitleChange.csv";
		writeCSVId(compareTable.getIdAddedFromTarget(), csvOutputNewId);
		writeCSVId(compareTable.getIdSupressFromOrigin(), csvOutputSuppressId);
		writeCSVChange(compareTable.getIdWithTitleChange(), compareTable.getTitleFromOrigin(), compareTable.getTitleFromTarget(), csvOutputDescrChange);
		writeCSVChange(compareTable.getIdWithDescrChange(), compareTable.getDescrFromOrigin(), compareTable.getDescrFromTarget(), csvOutputTitleChange);
	}
	
	private static void writeCSVId(List<String> idList, String fileOutput) throws IOException {
        FileWriter writer = new FileWriter(fileOutput);
        for (int i = 0; i < idList.size(); i++) {
			CSVUtils.writeLine(writer, Arrays.asList(idList.get(i)), ';');
		}
		writer.flush();
        writer.close();
	}
	
	private static void writeCSVChange(List<String> idList, List<String> originList, List<String> targetList, String fileOutput) throws IOException {
        FileWriter writer = new FileWriter(fileOutput);
        CSVUtils.writeLine(writer, Arrays.asList("Id", "origin", "target"), ';');
        for (int i = 0; i < idList.size(); i++) {
			CSVUtils.writeLine(writer, Arrays.asList(idList.get(i), originList.get(i), targetList.get(i)), ';');
		}

        writer.flush();
        writer.close();
	}

}
