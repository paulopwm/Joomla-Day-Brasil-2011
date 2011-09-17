package com.JoomlaDay;

public class Evento {
	
	private String title;
	private String description;
	private String local;
	private String date;

	public Evento(String title, String description, String local, String date) {
		setTitle(title);
		setDescription(description);
		setLocal(local);
		setDate(date);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
