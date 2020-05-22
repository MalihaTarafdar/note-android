package com.example.note;

import java.io.Serializable;

class NoteItem implements Serializable {
	private String title;
	private String preview;

	NoteItem(String title, String preview) {
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