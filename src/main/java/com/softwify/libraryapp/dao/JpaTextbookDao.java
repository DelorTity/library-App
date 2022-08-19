package com.softwify.libraryapp.dao;

import com.softwify.libraryapp.model.Textbook;
import jakarta.persistence.EntityManager;

public class JpaTextbookDao extends JpaDao<Textbook> {

    public JpaTextbookDao(EntityManager entityManager) {
        super(Textbook.class, entityManager);
    }
}
