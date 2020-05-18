package com.example.note;

class NoteItem {
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