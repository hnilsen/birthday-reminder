package no.hnilsen.android.bdayreminder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import no.hnilsen.android.bdayreminder.contact.GeneralContact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks {
    String TAG = "BdayReminder";
    List<GeneralContact> mContacts = new ArrayList<GeneralContact>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContacts = populatecontacts();

        for(GeneralContact person : mContacts) {
            Log.d(TAG, person.getName() + ": " + person.getLocaleBirthday());

        }
    }

    private List<GeneralContact> populatecontacts() {
        List<GeneralContact> contacts = new ArrayList<GeneralContact>();

        Cursor cursor = getContactsBirthdays();
        int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME);
        while (cursor.moveToNext()) {
            String bDay = cursor.getString(bDayColumn);
            String name = cursor.getString(nameColumn);
//            Log.d(TAG, name + ": " + bDay);

            contacts.add(new GeneralContact(this, bDay, name));
        }

        return contacts;
    }

    // method to get name, contact id, and birthday
    private Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
        String[] selectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String sortOrder = null;
        CursorLoader cl = new CursorLoader(this, uri, projection, where, selectionArgs, sortOrder);
        return cl.loadInBackground();
        //return managedQuery(uri, projection, where, selectionArgs, sortOrder);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
