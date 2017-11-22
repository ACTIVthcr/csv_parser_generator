package csv;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVReader;

public class ReadCSVFileTest {

	public static DefaultTableModel start(String csvFile) throws IOException {
		Object[] columnnames = null;
		// final CSVParser parser = new
		// CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
		// final CSVReader reader = new CSVReaderBuilder(new
		// StringReader(csvFile)).withSkipLines(1).withCSVParser(parser)
		// .build();
		// List<String[]> myEntries = reader.readAll();
		CSVReader CSVFileReader = new CSVReader(new FileReader(csvFile), ';');
		List<String[]> myEntries = CSVFileReader.readAll();
		columnnames = (String[]) myEntries.get(0);
		DefaultTableModel tableModel = new DefaultTableModel(columnnames, myEntries.size() - 1);
		int rowCount = tableModel.getRowCount();
		for (int x = 0; x < rowCount + 1; x++) {
			int columnnumber = 0;
			// if x = 0 this is the first row...skip it... data used for columnnames
			if (x > 0) {
				for (String thiscellvalue : (String[]) myEntries.get(x)) {
					tableModel.setValueAt(thiscellvalue, x - 1, columnnumber);
					columnnumber++;
				}
			}
		}
		int columnCount = tableModel.getColumnCount();
		if (LocalDebug.DEBUG) {
			for (int i = 0; i < columnCount; i++) {
				System.out.print(tableModel.getColumnName(i) + "\t\t");
			}
			System.out.println();
			for (int j = 0; j < rowCount; j++) {
				for (int i = 0; i < columnCount; i++) {
					System.out.print(tableModel.getValueAt(j, i) + "\t\t\t");
				}
				System.out.println();
			}
		}
		CSVFileReader.close();
		return tableModel;
	}

}
