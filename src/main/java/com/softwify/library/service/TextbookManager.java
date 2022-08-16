package com.softwify.library.service;

import com.softwify.library.dao.TextbookDao;
import com.softwify.library.model.Author;
import com.softwify.library.model.Textbook;
import com.softwify.library.util.OptionSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TextbookManager {

        private static final Logger logger = LogManager.getLogger(TextbookManager.class.getSimpleName());
        private final TextbookDao textbookDao;
        private final OptionSelector optionSelector;

        public TextbookManager(TextbookDao textbookDao, OptionSelector optionSelector) {
            this.textbookDao = textbookDao;
            this.optionSelector = optionSelector;
        }

        public void manage(){
            displayTextbooks();

            boolean continueSection = true;
            while (continueSection){

                System.out.println("\nPour ajouter un nouveau livre, veuillez saisir : \"add\"\n" +
                        "Pour faire une action sur un livre, veuillez saisir l'action suivit, de l'espace, puis de l'identifiant du livre.\n" +
                        "Actions possible : Affichage (read) et Suppression (delete) \n" +
                        "\n" +
                        "Ou saisissez \"back\" pour retourner au menu principal \n" +
                        "----------------------------------------------");
                String input = optionSelector.readString();
                input = input.trim().replaceAll("\\s+", " ");
                String substring[] = input.split(" ");
                String option = substring[0];
                switch (option){
                    case "back": {
                        LibraryMenu.loadApp();
                        continueSection = false;
                        break;
                    }
                    case "delete": {
                        try {
                            processDelete(substring[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            logger.error("Veuillez entrer un  identifiant s'il vous plait");
                        }
                        break;
                    }
                    case "read": {
                        try {
                            processRead(substring[1]);
                        } catch (ArrayIndexOutOfBoundsException e){
                            logger.error("Veuillez entrer un  identifiant s'il vous plait");
                        }
                        break;
                    }
                    default:
                        logger.error("L'action que vous avez effectuer n'est pas correcte, veuillez entrer la valeur requise");
                        break;
                     }

                }
             }

        public void displayTextbooks() {
            List<Textbook> textbooks = textbookDao.getAll();

            System.out.println("Liste des livres");
            for (Textbook textbook : textbooks) {
                System.out.println(textbook.getId() + " - " + textbook.getTitle() + " - " + textbook.getFullName());
            }
        }

    public boolean delete(int id) {
        boolean result = textbookDao.deleteTextbook(id);
        if (result) {
            System.out.println("Le livre a été supprimé avec succès.");
        } else {
            System.out.println("Le livre que vous avez choisi, n'existe pas.");
        }

        return result;
    }

    public boolean readTextbook(int id) {
        Textbook textbook = textbookDao.getTextbookInformation(id);
        if (textbook == null) {
            return false;
        }
        System.out.println("Titre : " + textbook.getTitle());
        System.out.println("Auteur : " + textbook.getFullName());
        System.out.println("ISBN : " + textbook.getIsbn());
        System.out.println("Editeur : " + textbook.getEditor());
        System.out.println("Année de publication : " + textbook.getPublicationDate());
        return true;
    }

    public void processRead(String idInString){

        try {
            int id = Integer.parseInt(idInString);
            boolean read = readTextbook(id);
            if (read) {
                returnToList();
            }
        } catch (NumberFormatException e) {
            logger.error(idInString
                    + "n'est pas un nombre, entrer un nombre représentant l'identifiant du livre !!!");
        }
    }

    public void processDelete(String idInString){
        try {
            int id = Integer.parseInt(idInString);
            boolean deleted = delete(id);
            if (deleted) {
                returnToList();
            }
        } catch (NumberFormatException e) {
            logger.error(idInString
                    + "n'est pas un nombre, entrer un nombre représentant l'identifiant de l'auteur !!!");
        }
    }

    private void returnToList() {
        System.out.println("\nTapez \"ENTER\" pour retourner\r\n" + "-------------------------");
        optionSelector.readString();
        displayTextbooks();
    }


}
