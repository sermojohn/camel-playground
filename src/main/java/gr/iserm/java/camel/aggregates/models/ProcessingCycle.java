package gr.iserm.java.camel.aggregates.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProcessingCycle {

	private String id;

	private List<Item> items;

	private Date startDate;

	private Date endDate;

	public ProcessingCycle(String id, Date startDate) {
		this.id = id;
		this.startDate = startDate;
		this.items = new ArrayList<>();
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public void addItem(Item item) {
		item.setParentId(id);
		items.add(item);
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ProcessingCycle{" +
				"items=" + items +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}
}
