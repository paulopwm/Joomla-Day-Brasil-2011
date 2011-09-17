package com.JoomlaDay;

public class Noticias {
	
	private String title;
	private String description;
	private String author;
	private String link;
	private String image;
	private String date;

	public Noticias(String title, String description, String author,
			String link, String image, String date) {
		setTitle(title);
		setDescription(description);
		setAuthor(author);
		setLink(link);
		setImage(image);
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
