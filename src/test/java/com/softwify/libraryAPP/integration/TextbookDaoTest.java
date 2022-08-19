package com.softwify.libraryAPP.integration;

import com.softwify.libraryAPP.configuration.DataBaseConfig;
import com.softwify.libraryAPP.dao.TextbookDao;
import com.softwify.libraryAPP.integration.config.DataBaseConfigTest;
import com.softwify.libraryAPP.integration.service.DataBasePrepareServiceTextbook;
import com.softwify.libraryAPP.model.Textbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TextbookDaoTest {
    DataBaseConfig dataBaseConfig = new DataBaseConfigTest();
    TextbookDao textbookDao = new TextbookDao(dataBaseConfig);
    DataBasePrepareServiceTextbook dataBasePrepareServiceTextbook = new DataBasePrepareServiceTextbook();

    @BeforeEach
    public void setUp() {
        dataBasePrepareServiceTextbook.clearDataBaseEntries();
    }

    @Test
    public void getTextbookReturnsExpectedSIzeAndTextbookOrderByTitle() {
        List<Textbook> textbooks = textbookDao.getAll();
        assertEquals("Demain tu gouvernes le monde", textbooks.get(0).getTitle());
        assertEquals("En as-tu vraiment besoin ?", textbooks.get(1).getTitle());
        assertEquals("L'effet papillon", textbooks.get(2).getTitle());
        assertEquals("Liberté 45", textbooks.get(3).getTitle());
        assertEquals("Social Entrepreneurship", textbooks.get(4).getTitle());
        assertEquals("Think and grow rich", textbooks.get(5).getTitle());
        assertEquals(6, textbooks.size());
    }

    @Test
    public void givenIdDeleteTextbookRemovesCorrespondingTextbook() {
        assertEquals(6, textbookDao.getAll().size());
        boolean deleted = textbookDao.deleteTextbook(3);
        assertTrue(deleted);

        List<Textbook> textbooks = textbookDao.getAll();
        assertEquals(5, textbooks.size());

        for (Textbook textbook : textbooks) {
            assertNotEquals(3, textbook.getId());
        }
    }

    @Test
    public void testShouldReturnNullWhenIdNotExists() {
        Textbook textbook = textbookDao.getTextbookInformation(30);
        assertNull(textbook);
    }

    @Test
    public void testShouldNotReturnFalseWhenIdExists() {
        Textbook textbook = textbookDao.getTextbookInformation(16);
        assertNotNull(textbook);

        assertEquals("Liberté 45", textbook.getTitle());
        assertEquals("Pierre-Yves Mcsween", textbook.getFullName());
        assertEquals(1234567890, textbook.getIsbn());
        assertEquals("canada", textbook.getEditor());

        Date publicationDate = textbook.getPublicationDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(publicationDate);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(7, calendar.get(Calendar.MONTH));
        assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testSaveTextbook() throws ParseException {
        String title = "Game of peace";
        int isbn = 230;
        int authorId = 16;
        String editor = "USA";
        String date = "16-08-2022";
        SimpleDateFormat time = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date dates = time.parse(date);

        Textbook textbook = new Textbook(1,title, authorId, isbn, editor, dates);
        textbookDao.save(textbook);
    }


}