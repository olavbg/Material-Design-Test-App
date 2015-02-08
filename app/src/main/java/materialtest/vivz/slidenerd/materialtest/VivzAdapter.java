package materialtest.vivz.slidenerd.materialtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class VivzAdapter extends ArrayAdapter<Information> {

    public VivzAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public VivzAdapter(Context context, int resource, List<Information> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_row, null);
        }

        Information p = getItem(position);

        if (p != null) {

            ImageView tt = (ImageView) v.findViewById(R.id.listIcon);
            TextView tt1 = (TextView) v.findViewById(R.id.listText);

            if (tt != null) {
                tt.setImageResource(p.iconId);
            }
            if (tt1 != null) {
                tt1.setText(p.title);
            }
        }

        return v;

    }
}
