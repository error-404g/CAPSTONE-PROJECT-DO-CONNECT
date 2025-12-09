package com.contactmanager.util;

import java.util.ArrayList;
import java.util.List;
import com.contactmanager.model.Contact;

public class ContactRepository {

    private static List<Contact> contactList = new ArrayList<>();

    public static void addContact(Contact c) {
        contactList.add(c);
    }

    public static List<Contact> getAllContacts() {
        return contactList;
    }

    public static boolean updateContact(Contact updated) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getId() == updated.getId()) {
                contactList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteContact(int id) {
        return contactList.removeIf(c -> c.getId() == id);
    }
}
