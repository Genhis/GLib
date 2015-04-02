package sk.genhis.glib.mysql;

public enum MySQLFieldType {
	INT(0, "int"),
	VARCHAR(1, "varchar"),
	FLOAT(2, "float"),
	DOUBLE(3, "double");
	
	private final int id;
	private final String name;
	
	private MySQLFieldType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}