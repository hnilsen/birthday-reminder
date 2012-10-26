package no.hnilsen.android.bdayreminder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import no.hnilsen.android.bdayreminder.R;
import no.hnilsen.android.bdayreminder.contact.GeneralContact;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Haakon
 * Date: 26.10.12
 * Time: 00:27
 */
public class BirthdayCardAdapter extends BaseAdapter {
    List<GeneralContact> mContacts = new ArrayList<GeneralContact>();
    Context mContext;

    public BirthdayCardAdapter(Context mContext, List<GeneralContact> mContacts) {
        this.mContacts = mContacts;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if(mContacts != null) {
            return mContacts.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mContacts != null) {
            mContacts.get(position);
        }
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            GeneralContact contact = mContacts.get(position);
            gridView = inflater.inflate(R.layout.grid_tile, null);

            // set value into name
            TextView textViewName = (TextView) gridView.findViewById(R.id.name);
            textViewName.setText(contact.getName());

            // set value into birthday
            TextView textViewBirthday = (TextView) gridView.findViewById(R.id.birthday);
            textViewBirthday.setText(contact.getLocaleBirthday());

            // set contact photo
            Bitmap photo = contact.getPhoto();
            if(photo != null) {
                ImageView imageViewPhoto = (ImageView) gridView.findViewById(R.id.photo);
                imageViewPhoto.setImageBitmap(photo);
            }
        } else {
            gridView = convertView;
        }

        return gridView;
    }


}
