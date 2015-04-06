package materialtest.vivz.slidenerd.materialtest.addmovie;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;

public class EANSearchedMovie {
    private String title;
    private String format;
    private String eanNr;
    private String api;
    private String err_msg;

    public EANSearchedMovie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEanNr() {
        return eanNr;
    }

    public void setEanNr(String eanNr) {
        this.eanNr = eanNr;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public Movie convertFromSearch() {
        final Movie movie = new Movie();
        movie.setTittel(getTitle());
        movie.setFormat(getFormat());
        movie.setBrukerID(GlobalVars.loggedInUser.getBrukerID());
        return movie;
    }
}
