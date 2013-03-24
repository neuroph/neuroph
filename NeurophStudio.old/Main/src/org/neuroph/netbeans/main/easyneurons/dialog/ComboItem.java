package org.neuroph.netbeans.main.easyneurons.dialog;

public class ComboItem {

	private String string;
	private Object value;

	public ComboItem(String string, Object value) {
		this.string = string;
		this.value = value;
	}

	@Override
	public String toString() {
		return this.string;
	}

	public Object getValue() {
		return this.value;
	}

}