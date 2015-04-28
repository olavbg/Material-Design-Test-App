package materialtest.vivz.slidenerd.materialtest.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    protected List<V> mData = new ArrayList<>();

    @Override
    public abstract K onCreateViewHolder(ViewGroup viewGroup, int i);

    @Override
    public abstract void onBindViewHolder(K k, int i);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(final List<V> data) {
        // Remove all items
        for (V entity : new ArrayList<>(mData)) {
            deleteMovie(mData.indexOf(entity));
        }

        for (V entity : data) {
            addMovie(data.indexOf(entity), entity);
        }
    }

    private int getLocation(List<V> data, V entity) {
        for (int j = 0; j < data.size(); ++j) {
            V newEntity = data.get(j);
            if (entity.equals(newEntity)) {
                return j;
            }
        }

        return -1;
    }

    public void addMovie(int i, V entity) {
        mData.add(i, entity);
        notifyItemInserted(i);
    }

    public void deleteMovie(int i) {
        mData.remove(i);
        notifyItemRemoved(i);
    }

    public void deleteMovie(V v) {
        for (V v1 : mData) {
            if (v1.equals(v)){
                final int indexOf = mData.indexOf(v);
                mData.remove(v);
                notifyItemRemoved(indexOf);
                return;
            }
        }
    }

    public void moveMovie(int i, int loc) {
        move(mData, i, loc);
        notifyItemMoved(i, loc);
    }

    private void move(List<V> data, int a, int b) {
        V temp = data.remove(a);
        data.add(b, temp);
    }
}
