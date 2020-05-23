package com.example.note;

import java.io.Serializable;

class Note implements Serializable {
	private String title;
	private String preview;

	Note(String title, String preview) {
		this.title = title;
		this.preview = preview;
	}

	String getTitle() {
		return title;
	}

	String getPreview() {
		return preview;
	}
}