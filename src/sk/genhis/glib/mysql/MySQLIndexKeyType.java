package sk.genhis.glib.mysql;

public enum MySQLIndexKeyType {
	PRIMARY(0, "PRIMARY KEY"),
	UNIQUE(1, "UNIQUE KEY"),
	INDEX(2, "INDEX");
	
	private final int id;
	private final String name;
	
	private MySQLIndexKeyType(int id, String name) {
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