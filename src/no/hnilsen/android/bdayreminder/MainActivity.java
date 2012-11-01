package no.hnilsen.android.bdayreminder;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
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
    List<GeneralContact> mContacts = new ArrayList<GeneralContact>();
    static Helper helper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.tile_fade_in);
        GridLayoutAnimationController animationController = new GridLayoutAnimationController(animation, 0.2f, 0.2f);
        helper = new Helper(this);

        mContacts = helper.populateContacts();

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

                if(contact.isSelected()) {
                    if(!helper.isContactSelected(contact.getId())) {
                        helper.selectContactInStorage(contact.getId());
                    }
                } else {
                    helper.removeContactFromStorage(contact.getId());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((GridView) findViewById(R.id.gridView1)).getLayoutAnimation().start();
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
