package processing;

public class DrawFactory {
	
	private int height, width;
	
	public DrawFactory(int height, int width) {
		this.height = height;
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Draw getDrawer(DrawMode mode) {
		Draw drawer = null;
		
		if(mode == DrawMode.DIRECTORY) {
			drawer = new DrawDirectoryTree(this.height, this.width);
		}
		else if(mode == DrawMode.TREE) {
			drawer = new DrawBinaryTree(this.width, this.height);
		}
		else if(mode == DrawMode.HIERARCHY) {
			drawer = new DrawHierarchyTree(this.width);
		}
		
		return drawer;
	}
}
