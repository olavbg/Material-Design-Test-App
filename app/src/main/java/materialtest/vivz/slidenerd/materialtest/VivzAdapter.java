package materialtest.vivz.slidenerd.materialtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;

import java.util.List;

public class VivzAdapter extends ArrayAdapter<NavDrawerItem> {

    public VivzAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public VivzAdapter(Context context, int resource, List<NavDrawerItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NavDrawerItem p = getItem(position);
        if (p != null) {
            final LayoutInflater vi = LayoutInflater.from(getContext());
            View rowView;

            if (!p.isGroupHeader) {
                rowView = vi.inflate(R.layout.nav_drawer_row, parent, false);

                // 3. Get icon,title & counter views from the rowView
                TextView imgView = (TextView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);


                // 4. Set the text for textView 
                imgView.setText(p.iconString);
                Iconify.addIcons(imgView);

                titleView.setText(p.title);
                if (p.numMovies >= 0) {
                    counterView.setText(String.valueOf(p.numMovies));
                } else {
                    counterView.setVisibility(View.GONE);
                }
            } else {
                rowView = vi.inflate(R.layout.group_header_item, parent, false);
//                TextView titleView = (TextView) rowView.findViewById(R.id.header);
//                titleView.setText(p.title);
            }
            // 5. retrn rowView
            return rowView;
        }
        return convertView;
    }
}
