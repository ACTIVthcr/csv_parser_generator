package csv;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class CompareTable {

	public static final String DESCRIPTION = "Description";

	public static final String TITLE = "Title";

	public static final String ID = "Id";

	private DefaultTableModel originTable;

	private DefaultTableModel targetTable;

	private boolean isSuppressed;

	private List<String> idSupressFromOrigin;

	private List<String> idAddedFromTarget;

	private List<String> idWithDescrChange;

	private List<String> idWithTitleChange;

	private List<String> descrFromOrigin;

	private List<String> descrFromTarget;

	private List<String> titleFromOrigin;

	private List<String> titleFromTarget;
	
	public DefaultTableModel getOriginTable() {
		return originTable;
	}

	public DefaultTableModel getTargetTable() {
		return targetTable;
	}

	public boolean isSuppressed() {
		return isSuppressed;
	}

	public List<String> getIdSupressFromOrigin() {
		return idSupressFromOrigin;
	}

	public List<String> getIdAddedFromTarget() {
		return idAddedFromTarget;
	}

	public List<String> getIdWithDescrChange() {
		return idWithDescrChange;
	}

	public List<String> getIdWithTitleChange() {
		return idWithTitleChange;
	}

	public List<String> getDescrFromOrigin() {
		return descrFromOrigin;
	}

	public List<String> getDescrFromTarget() {
		return descrFromTarget;
	}

	public List<String> getTitleFromOrigin() {
		return titleFromOrigin;
	}

	public List<String> getTitleFromTarget() {
		return titleFromTarget;
	}

	public CompareTable(DefaultTableModel originTable, DefaultTableModel targetTable) {
		this.originTable = originTable;
		this.targetTable = targetTable;
		this.idSupressFromOrigin = new ArrayList<String>();
		this.idAddedFromTarget = new ArrayList<String>();
		this.descrFromOrigin = new ArrayList<String>();
		this.descrFromTarget = new ArrayList<String>();
		this.titleFromOrigin = new ArrayList<String>();
		this.titleFromTarget = new ArrayList<String>();
		this.idWithDescrChange = new ArrayList<String>();
		this.idWithTitleChange = new ArrayList<String>();
	}

	public void Compare() {

		int originTitleColumnNumber = this.originTable.findColumn(TITLE);
		int targetTitleColumnNumber = this.targetTable.findColumn(TITLE);
		int titleColumnNumber = -1;

		if (originTitleColumnNumber == targetTitleColumnNumber) {
			titleColumnNumber = originTitleColumnNumber;
		} else if (originTitleColumnNumber != targetTitleColumnNumber) {
			throw new IllegalArgumentException("Not the same format between originTable and targetTable");
		}

		int originDescrColumnNumber = this.originTable.findColumn(DESCRIPTION);
		int targetDescrColumnNumber = this.targetTable.findColumn(DESCRIPTION);
		int descrColumnNumber = -1;

		if (originDescrColumnNumber == targetDescrColumnNumber) {
			descrColumnNumber = originDescrColumnNumber;
		} else if (originDescrColumnNumber != targetDescrColumnNumber) {
			throw new IllegalArgumentException("Not the same format between originTable and targetTable");
		}

		int idFromOriginTable = this.originTable.findColumn(ID);
		int idFromTargetTable = this.targetTable.findColumn(ID);
		int idColumnNumber = -1;

		if (idFromOriginTable == idFromTargetTable) {
			idColumnNumber = idFromOriginTable;
		} else if (idFromOriginTable != idFromTargetTable) {
			throw new IllegalArgumentException("Not the same format between originTable and targetTable");
		}

		for (int j = 0; j < this.originTable.getRowCount(); j++) {
			Object idOriginTable = this.originTable.getValueAt(j, idColumnNumber);
			for (int i = 0; i < this.targetTable.getRowCount(); i++) {
				this.isSuppressed = true;
				Object idTargetTable = this.targetTable.getValueAt(i, idColumnNumber);
				if (compareIdTable(idOriginTable, idTargetTable, descrColumnNumber, titleColumnNumber, j, i)) {
					break;
				}
			}
			if (this.isSuppressed && idOriginTable instanceof String) {
				if (LocalDebug.DEBUG) {
					System.out.println(idOriginTable + " isSupressed from originTable");
				}
				this.idSupressFromOrigin.add((String) idOriginTable);
			}
			if (LocalDebug.DEBUG) {
				System.out.println("----------------------");
			}
		}
		for (int j = 0; j < this.targetTable.getRowCount(); j++) {
			Object idTargetTable = this.targetTable.getValueAt(j, idColumnNumber);
			for (int i = 0; i < this.originTable.getRowCount(); i++) {
				this.isSuppressed = true;
				Object idOriginTable = this.originTable.getValueAt(i, idColumnNumber);
				if (compareIdTable(idOriginTable, idTargetTable, descrColumnNumber, titleColumnNumber, i, j)) {
					break;
				}
			}
			if (this.isSuppressed && idTargetTable instanceof String) {
				if (LocalDebug.DEBUG) {
					System.out.println(idTargetTable + " is new from targetTable");
				}
				this.idAddedFromTarget.add((String) idTargetTable);
			}
			if (LocalDebug.DEBUG) {
				System.out.println("----------------------");
			}
		}

	}

	private boolean compareIdTable(Object idOriginTable, Object idTargetTable, int descrColumnNumber,
			int titleColumnNumber, int originIndex, int targetIndex) {

		if (idOriginTable instanceof String && idTargetTable instanceof String) {
			String idOriginTableString = (String) idOriginTable;
			String idTargetTableString = (String) idTargetTable;
			if (idOriginTableString.equals(idTargetTableString)) {
				String id = idOriginTableString;
				if (LocalDebug.DEBUG) {
					System.out.println(
							"they are the same: \torigin :" + idOriginTableString + " target: " + idTargetTableString);
				}
				compareDescr(id, descrColumnNumber, originIndex, targetIndex);
				compareTitle(id, titleColumnNumber, originIndex, targetIndex);
				isSuppressed = false;
			}
		}
		return !isSuppressed;
	}

	private void compareDescr(String id, int descrColumnNumber, int originIndex, int targetIndex) {
		String descrFromOrigin = (String) this.originTable.getValueAt(originIndex, descrColumnNumber);
		String descrFromTarget = (String) this.targetTable.getValueAt(targetIndex, descrColumnNumber);
		if (LocalDebug.DEBUG) {
			System.out.println("descrFromOrigin: " + descrFromOrigin);
			System.out.println("descrFromTarget: " + descrFromTarget);
		}
		if (descrFromOrigin.compareTo(descrFromTarget) != 0) {
			this.idWithDescrChange.add(id);
			this.descrFromOrigin.add(descrFromOrigin);
			this.descrFromTarget.add(descrFromTarget);
			if (LocalDebug.DEBUG) {
				System.out.println("id with descr change: " + id);
			}
		}
	}

	private void compareTitle(String id, int titleColumnNumber, int originIndex, int targetIndex) {
		String titleFromOrigin = (String) this.originTable.getValueAt(originIndex, titleColumnNumber);
		String titleFromTarget = (String) this.targetTable.getValueAt(targetIndex, titleColumnNumber);
		if (LocalDebug.DEBUG) {
			System.out.println("titleFromOrigin: " + titleFromOrigin);
			System.out.println("titleFromTarget: " + titleFromTarget);
		}
		if (titleFromOrigin.compareTo(titleFromTarget) != 0) {
			this.idWithTitleChange.add(id);
			this.titleFromOrigin.add(titleFromOrigin);
			this.titleFromTarget.add(titleFromTarget);
			if (LocalDebug.DEBUG) {
				System.out.println("id with title change: " + id);
			}
		}
	}

}
