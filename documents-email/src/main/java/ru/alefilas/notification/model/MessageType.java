package ru.alefilas.notification.model;

public enum MessageType {

    DELETE("Hello %s! Your document with id = %d was deleted", "Document deleted"),
    CHANGE("Hello %s! Your document with id = %d was updated", "Document updated");

    private final String text;
    private final String subject;

    MessageType(String text, String subject) {
        this.text = text;
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public String getSubject() {
        return subject;
    }
}
