package materialtest.vivz.slidenerd.materialtest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import materialtest.vivz.slidenerd.materialtest.utils.API_CONST;
import materialtest.vivz.slidenerd.materialtest.utils.Callback;
import materialtest.vivz.slidenerd.materialtest.utils.Helper;
import materialtest.vivz.slidenerd.materialtest.utils.MovieList;

import static android.text.TextUtils.isEmpty;
import static materialtest.vivz.slidenerd.materialtest.utils.Helper.getFriendlyTime;


public class MovieDetailsActivity extends ActionBarActivity {
    private Movie movie;
    private ImageView backgroundImage;
    private TextView title;
    private TextView year;
    private TextView format;
    private TextView dateAdded;
    private TextView rated;
    private TextView runtime;
    private TextView genre;
    private TextView director;
    private TextView writer;
    private TextView actor;
    private TextView tagline;
    private TextView plot;
    private TextView utlaan;

    private TextView lblDirector;
    private TextView lblWriter;
    private TextView lblActor;
    private TextView lblPlot;
    private TextView lblDateAdded;
    private TextView lblUtlaan;

    private Button btnTrailer;
    private Button btnAddFromDetails;
    private Button btnReturned;
    private Button btnThisIsBorrowed;
    private Button btnThisIsLent;
    private Button btnEdit;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setUpLayout();
        movie = MovieList.getMovieById(getIntent().getIntExtra("movieId", -1));
        displayDetails();
    }

    private void setUpLayout() {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        title = (TextView) findViewById(R.id.txtTitle);
        year = (TextView) findViewById(R.id.txtYear);
        format = (TextView) findViewById(R.id.txtFormat);
        dateAdded = (TextView) findViewById(R.id.txtDateAdded);
        rated = (TextView) findViewById(R.id.txtRated);
        runtime = (TextView) findViewById(R.id.txtRuntime);
        genre = (TextView) findViewById(R.id.txtGenre);
        director = (TextView) findViewById(R.id.txtDirector);
        writer = (TextView) findViewById(R.id.txtWriter);
        actor = (TextView) findViewById(R.id.txtActor);
        tagline = (TextView) findViewById(R.id.txtTagline);
        plot = (TextView) findViewById(R.id.txtPlot);
        utlaan = (TextView) findViewById(R.id.txtUtlaan);

        lblDirector = (TextView) findViewById(R.id.lblDirector);
        lblWriter = (TextView) findViewById(R.id.lblWriter);
        lblActor = (TextView) findViewById(R.id.lblActor);
        lblPlot = (TextView) findViewById(R.id.lblPlot);
        lblDateAdded = (TextView) findViewById(R.id.lblDateAdded);
        lblUtlaan = (TextView) findViewById(R.id.lblUtlaan);

        btnTrailer = (Button) findViewById(R.id.btnTrailer);
        btnAddFromDetails = (Button) findViewById(R.id.btnAddFromDetails);
        btnReturned = (Button) findViewById(R.id.btnReturned);
        btnThisIsBorrowed = (Button) findViewById(R.id.btnThisIsBorrowed);
        btnThisIsLent = (Button) findViewById(R.id.btnThisIsLent);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }

    private void displayDetails() {
        if (movie != null) {
            if (!isEmpty(movie.getImdb_id())) {
                Picasso.with(this)
                        .load(API_CONST.omdbAPIPosterURL(movie.getImdb_id()))
                        .into(backgroundImage);
                backgroundImage.setColorFilter(Color.argb(180, 0, 0, 0));
            }
            setValueInField(title, movie.getTittel());
            //subHeader
            setValueInField(format, movie.getFormat() + "  |  ");
            setValueInField(rated, movie.getRated());
            if (!rated.getText().toString().isEmpty()) rated.append("  |  ");
            setValueInField(year, movie.getYear());
            if (!year.getText().toString().isEmpty()) year.append("  |  ");
            setValueInField(runtime, movie.getRuntime());

            setValueInField(dateAdded, movie.getLagtTil());
            setValueInField(genre, movie.getGenre());
            setValueInField(director, movie.getDirector());
            setValueInField(writer, movie.getWriter());
            setValueInField(actor, movie.getActor());
            setValueInField(tagline, !isEmpty(movie.getTagline()) ? "\"" + movie.getTagline() + "\"" : "");
            setValueInField(plot, movie.getPlot());
            if (movie.isBorrowed()) {
                setValueInField(lblUtlaan, "Borrowed from ");
                setValueInField(utlaan, movie.getNavn() + " " + getFriendlyTime(movie.getDato()));
            }
            if (movie.isLent()) {
                setValueInField(lblUtlaan, "Lent to ");
                setValueInField(utlaan, movie.getNavn() + " " + getFriendlyTime(movie.getDato()));
            }
            setupBottomButtons();
            hideEmptyFields();
        }
    }

    private void setupBottomButtons() {
        if (movie == null) {
            btnReturned.setVisibility(View.GONE);
            btnThisIsBorrowed.setVisibility(View.GONE);
            btnThisIsLent.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            return;
        }
        btnAddFromDetails.setVisibility(View.GONE);
//        btnTrailer.setVisibility(movie.getTrailer().isEmpty() ? View.GONE : View.VISIBLE);
        if (movie.isBorrowed() || movie.isLent()) {
            btnThisIsBorrowed.setVisibility(View.GONE);
            btnThisIsLent.setVisibility(View.GONE);
        } else {
            btnReturned.setVisibility(View.GONE);
        }
        setupBottomButtonEvents();
    }

    private void setupBottomButtonEvents() {
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movie.getTrailer().isEmpty()){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailer())));
                }else{
                    final Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.setPackage("com.google.android.youtube");
                    intent.putExtra("query", movie.getTittel() + " official trailer");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showProgressDialog("Deleting movie...");
                Helper.deleteMovie(movie, null, new Callback<Boolean>() {
                    @Override
                    public void call(Boolean success) {
                        if (success){
                            finish();
                        }
                    }
                });
            }
        });

        btnThisIsBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void hideEmptyFields() {
        hideEmptyField(rated);
        hideEmptyField(year);
        hideEmptyField(runtime);
        hideEmptyField(genre);
        hideEmptyField(tagline);
        hideEmptyField(director, lblDirector);
        hideEmptyField(writer, lblWriter);
        hideEmptyField(actor, lblActor);
        hideEmptyField(plot, lblPlot);
        hideEmptyField(utlaan, lblUtlaan);
    }

    private void hideEmptyField(TextView textView) {
        hideEmptyField(textView, null);
    }

    private void hideEmptyField(TextView textView, TextView label) {
        if (textView.getText().toString().isEmpty()) {
            textView.setVisibility(View.GONE);
            if (label != null) {
                label.setVisibility(View.GONE);
            }
        }
    }

    private void setValueInField(final TextView field, final String value) {
        if (field != null && !isEmpty(value)
                && !value.toLowerCase().equalsIgnoreCase("N/A")
                && !value.toLowerCase().equalsIgnoreCase("0")
                && !value.toLowerCase().equalsIgnoreCase("0 mins")
                && !value.toLowerCase().equalsIgnoreCase("0000")) {
            field.setText(value);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.init(this);
    }

    @Override
    protected void onStop() {
        Helper.dismissProgressDialog();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
