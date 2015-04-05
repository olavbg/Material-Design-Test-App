package materialtest.vivz.slidenerd.materialtest.addmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.VolleyRequest;

public class FragmentClasses {

    public static class ManualAddFragment extends Fragment {
        private EditText txtTitle;
        private Button btnAdd;
        private Spinner formats;

        private RecyclerView recyclerView;
        private MovieSearchCardAdapter adapter;
        private ArrayList<SimpleSearchedMovie> suggestions = new ArrayList<>();

        public ManualAddFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View v = inflater.inflate(R.layout.fragment_manual_add, container, false);
            txtTitle = (EditText) v.findViewById(R.id.txtNewTitle);
            btnAdd = (Button) v.findViewById(R.id.btnAdd);
            formats = (Spinner) v.findViewById(R.id.movieFormats);
            recyclerView = (RecyclerView) v.findViewById(R.id.searchResults);

            GlobalVars.init(getActivity());
            Helper.init(getActivity());

            adapter = new MovieSearchCardAdapter(suggestions);
            final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(adapter);

            txtTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    GlobalVars.requestQueue.stop();
                    GlobalVars.requestQueue.start();
                    suggestions.clear();
                    adapter.notifyDataSetChanged();
                    if (txtTitle.getText().length() > 1) {
                        if (Helper.isConnected()) {
                            GlobalVars.requestQueue.add(VolleyRequest.getMovieSearchRequest(txtTitle.getText().toString().trim(), formats.getSelectedItem().toString(), suggestions, adapter, getActivity()));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (txtTitle.getText().toString().trim().isEmpty()) {
                        GlobalVars.requestQueue.stop();
                        GlobalVars.requestQueue.start();
                    }
                }
            });
            txtTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                        addNewMovie();
                        return true;
                    }
                    return false;
                }
            });
            formats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    for (SimpleSearchedMovie searchedMovie : suggestions) {
                        searchedMovie.setFormat(formats.getSelectedItem().toString());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewMovie();
                }
            });

            return v;
        }

        public void addNewMovie(View view) {
            addNewMovie();
        }

        public void addNewMovie() {
            Toast.makeText(getActivity(), "Adding movie", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ScanBarcodeFragment extends Fragment {

        public ScanBarcodeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_scan_barcode, container, false);
        }
    }

    public static class QuicAddFragment extends Fragment {

        public QuicAddFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_quick_add, container, false);
        }
    }
}
