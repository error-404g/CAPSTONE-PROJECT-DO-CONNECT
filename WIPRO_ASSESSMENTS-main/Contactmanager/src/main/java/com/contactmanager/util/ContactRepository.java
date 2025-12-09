package com.contactmanager.util;

import java.util.ArrayList;
import java.util.List;
import com.contactmanager.model.Contact;

public class ContactRepository {
    private static List<Contact> contacts = new ArrayList<>();

    public static void addContact(Contact contact) {
        contacts.add(contact);
    }

    public static List<Contact> getAllContacts() {
        return contacts;
    }
}
