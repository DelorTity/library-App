package com.softwify.libraryapp.model;

import java.util.Date;

public class Textbook {
    private int id;
    private String title;
    private int authorId;
    private int isbn;
    private String editor;
    private Date publicationDate;
    private String authorFirstName;
    private String authorFastName;

    public Textbook(int id, String title, int authorId, int isbn, String editor, Date publicationDate) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.isbn = isbn;
        this.editor = editor;
        this.publicationDate = publicationDate;
    }

    public Textbook(int id, int authorId, String title, int isbn, String editor, Date publicationDate) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.editor = editor;
        this.publicationDate = publicationDate;
        this.authorId = authorId;
    }

    public Textbook(int id, String title, String firstName, String lastName, int authorId, int isbn, String editor, Date publicationDate) {
        this.title = title;
        this.id = id;
        this.authorFirstName = firstName;
        this.authorFastName = lastName;
        this.authorId = authorId;
        this.isbn = isbn;
        this.editor = editor;
        this.publicationDate = publicationDate;
    }

    public Textbook(int id, String title, String firstName, String lastName) {
        this.id = id;
        this.title = title;
        this.authorFirstName = firstName;
        this.authorFastName = lastName;
    }

    public Textbook(String title, String authorFirstname, String authorLastname, int isbn, String editor, java.sql.Date publicationDate) {
        this.title = title;
        this.authorFirstName = authorFirstname;
        this.authorFastName = authorLastname;
        this.isbn = isbn;
        this.editor = editor;
        this.publicationDate = publicationDate;
    }

    public Textbook(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorFastName;
    }

    public void setAuthorFastName(String authorFastName) {
        this.authorFastName = authorFastName;
    }

    public String getFullName() {
        return authorFirstName + " " + authorFastName;
    }
}
