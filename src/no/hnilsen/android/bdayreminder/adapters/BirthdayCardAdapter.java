package no.hnilsen.android.bdayreminder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
            return mContacts.get(position);
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
        GeneralContact contact = mContacts.get(position);

        gridView = convertView;

        if (gridView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.grid_tile, null);

            // set animation properties - only animate newly drawn layouts
            Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.tile_fade_in);
            gridView.setAnimation(anim);
        }

        // set value into name
        TextView textViewName = (TextView) gridView.findViewById(R.id.name);
        textViewName.setText(contact.getName());

        // set value into birthday
        TextView textViewBirthday = (TextView) gridView.findViewById(R.id.birthday);
        textViewBirthday.setText(contact.getLocaleBirthday());

        // set contact photo
        Bitmap photo = contact.getPhoto();
        if(photo == null) {
            photo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_contact_picture);
        }
        ImageView imageViewPhoto = (ImageView) gridView.findViewById(R.id.photo);
        imageViewPhoto.setImageBitmap(photo);

        // set checkbox
        CheckBox checkBox = (CheckBox) gridView.findViewById(R.id.star);
        checkBox.setChecked(contact.isSelected());

        return gridView;
    }
}
