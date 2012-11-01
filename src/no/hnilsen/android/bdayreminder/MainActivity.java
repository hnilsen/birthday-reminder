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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import no.hnilsen.android.bdayreminder.adapters.BirthdayCardAdapter;
import no.hnilsen.android.bdayreminder.contact.GeneralContact;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks {
    public static String TAG = "BdayReminder";
    List<GeneralContact> mContacts = new ArrayList<GeneralContact>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);

        mContacts = populateContacts();

        for(GeneralContact contact : mContacts) {
            Log.d(TAG, contact.getLocaleBirthday() + " " + contact.getUri());
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.tile_fade_in);
        GridLayoutAnimationController animationController = new GridLayoutAnimationController(animation, 0.2f, 0.2f);

        GridView gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new BirthdayCardAdapter(this, mContacts));
        gridView.setLayoutAnimation(animationController);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BirthdayCardAdapter birthdayCardAdapter = (BirthdayCardAdapter) parent.getAdapter();
                GeneralContact contact = (GeneralContact) birthdayCardAdapter.getItem(position);
                contact.setSelected(!contact.isSelected());

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.star);
                checkBox.setChecked(contact.isSelected());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((GridView) findViewById(R.id.gridView1)).getLayoutAnimation().start();
    }

    private List<GeneralContact> populateContacts() {
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

            GeneralContact gc = new GeneralContact(this, contactId, bDay, name, photoId);
            contacts.add(gc);
        }

        return contacts;
    }

    private Cursor getContactsBirthdays() {
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

        CursorLoader cl = new CursorLoader(this, uri, projection, where, selectionArgs, sortOrder);

        return cl.loadInBackground();
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
