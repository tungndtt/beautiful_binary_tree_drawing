package processing;

public class Coordination {
	
	private int id;
	
	private Coordination left, right;
	
	private String value;
	
	private int coordination;
	
	private int stage;
	
	public Coordination(int id, int stage, Coordination left, Coordination right, String value) {
		this.id = id;
		this.left = left;
		this.right = right;
		this.value = value;
		this.stage = stage;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getStage() {
		return this.stage;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Coordination getLeft() {
		return this.left;
	}
	
	public Coordination getRight() {
		return this.right;
	}
	
	public void setCoordination(int coordination) {
		this.coordination = coordination;
	}
	
	public int getCoordination() {
		return this.coordination;
	}
}
