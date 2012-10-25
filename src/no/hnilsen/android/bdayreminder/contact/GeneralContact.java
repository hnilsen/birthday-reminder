package no.hnilsen.android.bdayreminder.contact;

import android.content.Context;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
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
 //   DateTime dtBirthday;
 //   DateTime dtToday;

    Locale locale;

    Calendar cal;
    Calendar today;

    public GeneralContact(Context context, String birthday, String name) {
        this.birthday = birthday;
        this.name = name;

        locale = context.getResources().getConfiguration().locale;

//        dtToday = new DateTime(context.getResources().getConfiguration().locale);
//        dtBirthday = new DateTime(context.getResources().getConfiguration().locale);

        today = Calendar.getInstance(context.getResources().getConfiguration().locale);

        cal = Calendar.getInstance(context.getResources().getConfiguration().locale);
        cal.set(getYear(), getMonth(), getDay());
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

    public int getAge() {
        return today.get(Calendar.YEAR) - getYear();
    }

    public int getDaysToBirthday() {
        return 0;
    }

    public String getLocaleDayOfBirth() {
        return getLocaleDate(cal);
    }

    public String getLocaleBirthday() {
        Calendar bday = cal;
        bday.set(Calendar.YEAR, 2012);

        return getLocaleDate(bday);
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

    public String getLocaleDate(Calendar calendar) {
        GregorianCalendar gcal = new GregorianCalendar(calendar.get(Calendar.YEAR),
                                                       calendar.get(Calendar.MONTH),
                                                       calendar.get(Calendar.DAY_OF_MONTH));

        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);

        return df.format(gcal);
    }
}

