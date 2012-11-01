package no.hnilsen.android.bdayreminder.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import no.hnilsen.android.bdayreminder.Helper;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Haakon
 * Date: 20.10.12
 * Time: 00:36
 */
public class GeneralContact {
    String birthday;
    String name;
    Bitmap photo;

    Locale locale;

    GregorianCalendar mBirthday;
    GregorianCalendar mToday;

    Context mContext;

    Uri uri;
    long id;
    boolean selected = false;

    public GeneralContact(Context context, long id, String birthday, String name, long photoId) {
        this.birthday = birthday;
        this.name = name;
        this.id = id;

        mContext = context;

        uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);

        locale = context.getResources().getConfiguration().locale;
        // TODO: Add selected = true when stored as true

        photo = loadContactPhoto(mContext.getContentResolver(), id, photoId);

        mBirthday = new GregorianCalendar(getYear(), getMonth(), getDay());
        mToday = new GregorianCalendar(locale);
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthdayAge() {
        return getAge() + 1;
    }

    public int getAge() {
        return mToday.get(GregorianCalendar.YEAR) - getYear();
    }

    public int getDaysToBirthday() {
        return 0;
    }

    public String getLocaleDayOfBirth() {
        return getLocaleDate(mBirthday);
    }

    public GregorianCalendar getNextBirthday() {
        GregorianCalendar bday = mBirthday;

        int birthdayDOY = mBirthday.get(GregorianCalendar.DAY_OF_YEAR);
        int currentDOY = mToday.get(GregorianCalendar.DAY_OF_YEAR);
        int currentYear = mToday.get(GregorianCalendar.YEAR);

        if(birthdayDOY < currentDOY) {
            currentYear += 1;
        }

        bday.set(Calendar.YEAR, currentYear);

        return bday;
    }

    public String getLocaleBirthday() {
        return getLocaleDate(getNextBirthday());
    }

    public int getYear() {
        return Integer.parseInt(birthday.substring(0, 4));
    }

    public int getMonth() {
        return Integer.parseInt(birthday.substring(5, 7));
    }

    public int getDay() {
        return Integer.parseInt(birthday.substring(8, 10));
    }

    public String getLocaleDate(GregorianCalendar calendar) {
        GregorianCalendar gcal = new GregorianCalendar(calendar.get(GregorianCalendar.YEAR),
                                                       calendar.get(GregorianCalendar.MONTH)-1,    // ffffuuuuuuu java calendar
                                                       calendar.get(GregorianCalendar.DAY_OF_MONTH));

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        return dateFormat.format(gcal.getTime());
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null) {
            return BitmapFactory.decodeStream(input);
        }

        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);

        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);

        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            Log.d(Helper.TAG, e.getMessage());
        } finally {
            c.close();
        }

        if (photoBytes != null)
            return BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);

        return null;
    }

    public long getId() {
        return id;
    }
}

