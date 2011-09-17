package com.JoomlaDay;

public class Tweet {
	
	private String text;
	private String author;
	private String image;
	private String date;

	public Tweet(String text, String author, String image, String date) {
		setText(text);
		setAuthor(author);
		setImage(image);
		setDate(date);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
