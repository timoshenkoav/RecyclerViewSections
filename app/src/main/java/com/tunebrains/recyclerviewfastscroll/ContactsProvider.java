package com.tunebrains.recyclerviewfastscroll;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 11/13/15.
 */
public class ContactsProvider {
    public static List<Contact> load(Context pContext) {
        List<Contact> lContacts = new ArrayList<>();
        Cursor c = null;
        try {
            c = pContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                String lName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                lContacts.add(new Contact(lName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(lContacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        return lContacts;
    }
}
