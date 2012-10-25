package no.hnilsen.android.bdayreminder.contact;

import android.content.Context;

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

    Locale locale;

    GregorianCalendar mBirthday;
    GregorianCalendar mToday;

    Context mContext;

    public GeneralContact(Context context, String birthday, String name) {
        this.birthday = birthday;
        this.name = name;

        locale = context.getResources().getConfiguration().locale;

        mBirthday = new GregorianCalendar(getYear(), getMonth(), getDay());
        mToday = new GregorianCalendar(locale);

        mContext = context;
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
}

