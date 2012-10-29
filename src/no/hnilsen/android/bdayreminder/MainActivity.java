package no.hnilsen.android.bdayreminder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import no.hnilsen.android.bdayreminder.adapters.BirthdayCardAdapter;
import no.hnilsen.android.bdayreminder.contact.GeneralContact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks {
    static String TAG = "BdayReminder";
    List<GeneralContact> mContacts = new ArrayList<GeneralContact>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);

        mContacts = populatecontacts();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.tile_fade_in);
        GridLayoutAnimationController animationController = new GridLayoutAnimationController(animation,  0.2f, 0.2f);

        GridView gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new BirthdayCardAdapter(this, mContacts));
        gridView.setLayoutAnimation(animationController);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Not fireing");

                Toast.makeText(MainActivity.this, "Item " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((GridView)findViewById(R.id.gridView1)).getLayoutAnimation().start();
    }

    private List<GeneralContact> populatecontacts() {
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

            GeneralContact gc = new GeneralContact(this, bDay, name);
            gc.setPhoto(loadContactPhoto(getContentResolver(), contactId, photoId));

            contacts.add(gc);
        }

        return contacts;
    }

    private Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Event.START_DATE,
                ContactsContract.Contacts.PHOTO_ID
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
    }

    public static Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id)
    {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null) {
            return BitmapFactory.decodeStream(input);
        }

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);

        Cursor c = cr.query(photoUri, new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);

        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            c.close();
        }

        if (photoBytes != null)
            return BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.length);

        return null;
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
