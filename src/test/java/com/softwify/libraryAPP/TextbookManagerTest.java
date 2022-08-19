package com.softwify.libraryAPP;


import com.softwify.libraryAPP.dao.AuthorDao;
import com.softwify.libraryAPP.dao.TextbookDao;
import com.softwify.libraryAPP.model.Author;
import com.softwify.libraryAPP.model.Textbook;
import com.softwify.libraryAPP.service.TextbookManager;
import com.softwify.libraryAPP.util.OptionSelector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TextbookManagerTest {
    TextbookDao textbookDao = Mockito.mock(TextbookDao.class);
    OptionSelector optionSelector = Mockito.mock(OptionSelector.class);
    AuthorDao authorDao = Mockito.mock(AuthorDao.class);
    TextbookManager textbookManager = new TextbookManager(textbookDao, optionSelector, authorDao);

    @Test
    public void testDisplayTextbooks() {
        textbookManager.displayTextbooks();
        verify(textbookDao, times(1)).getAll();
    }

    @Test
    public void deleteTextbookShouldReturnFalseWhenDaoReturnFalse() {
        when(textbookDao.deleteTextbook(anyInt())).thenReturn(false);

        boolean deleted = textbookManager.delete(5);
        assertFalse(deleted);
        verify(textbookDao, times(1)).deleteTextbook(5);
    }

    @Test
    public void deleteTextbookShouldReturnTrueWhenDaoReturnTrue() {
        when(textbookDao.deleteTextbook(anyInt())).thenReturn(true);
        boolean deleted = textbookManager.delete(5);
        assertTrue(deleted);
        verify(textbookDao, times(1)).deleteTextbook(5);
    }

    @Test
    public void testShouldReturnTrueWhenTextbookIsDisplayed() {
        Textbook textbook = new Textbook(6, "titre propre", "delor", "tity", 5, 1234, "USA", new Date());
        when(textbookDao.getTextbookInformation(6)).thenReturn(textbook);
        assertTrue(textbookManager.readTextbook(6));
        verify(textbookDao, times(1)).getTextbookInformation(6);
    }

    @Test
    public void testShouldReturnFalseWhenTextbookIsNotDisplayed() {
        when(textbookDao.getTextbookInformation(5)).thenReturn(null);
        assertFalse(textbookManager.readTextbook(5));
        verify(textbookDao, times(1)).getTextbookInformation(5);
    }

    @Test
    public void testTextbookRegistrationIs() throws ParseException {
        when(optionSelector.readString()).thenReturn("Thione Niang");
        when(optionSelector.readInt()).thenReturn(569);
        when(optionSelector.readDate()).thenReturn("02-03-2020");
        when(authorDao.checkExistingAuthor(any())).thenReturn(true);
        when(authorDao.getByFirstNameAndLastName(any(), any())).thenReturn(new Author(1, "Thione", "Niang"));
        textbookManager.processAdd();

        verify(authorDao, times(1)).getByFirstNameAndLastName(any(), any());
    }

}