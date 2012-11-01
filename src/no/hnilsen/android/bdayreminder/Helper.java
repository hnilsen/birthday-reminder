package no.hnilsen.android.bdayreminder;

import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import no.hnilsen.android.bdayreminder.contact.GeneralContact;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Haakon
 * Date: 01.11.12
 * Time: 17:02
 */
public class Helper {
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context mContext;

    public static String PREFS_NAME = "birthday-reminder-store";
    public static String TAG = "BdayReminder";

    public Helper(Context context) {
        mContext = context;

        settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    public boolean isContactSelected(long id) {
        return settings.contains(String.valueOf(id));
    }

    public void removeContactFromStorage(long id) {
        editor.remove(String.valueOf(id));
        editor.commit();
    }

    public void selectContactInStorage(long id) {
        editor.putString(String.valueOf(id), "true");
        editor.commit();
    }

    public List<GeneralContact> populateContacts() {
        List<GeneralContact> contacts = new ArrayList<GeneralContact>();

        Cursor cursor = getContactsBirthdays();
        int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME);
        int contactIdColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int photoIdColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_ID);

        while (cursor.moveToNext()) {
            String bDay = cursor.getString(bDayColumn);
            String name = cursor.getString(nameColumn);
            long contactId = cursor.getLong(contactIdColumn);
            long photoId = cursor.getLong(photoIdColumn);

            GeneralContact gc = new GeneralContact(mContext, contactId, bDay, name, photoId);
            gc.setSelected(this.isContactSelected(contactId));
            contacts.add(gc);
        }

        return contacts;
    }

    public Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Event.START_DATE,
                ContactsContract.Contacts.PHOTO_ID
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;


        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };

        String sortOrder = null;

        CursorLoader cl = new CursorLoader(mContext, uri, projection, where, selectionArgs, sortOrder);

        return cl.loadInBackground();
    }
}
