package gr.iserm.java.camel.aggregates.models;

public class Item {

	private String id;

	private String parentId;

	private String name;

	public Item(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", parentId=" + parentId +
				", name='" + name + '\'' +
				'}';
	}
}
