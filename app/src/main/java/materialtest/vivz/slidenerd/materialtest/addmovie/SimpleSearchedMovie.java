package materialtest.vivz.slidenerd.materialtest.addmovie;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;

public class SimpleSearchedMovie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String format;

    public SimpleSearchedMovie(String title, String year, String imdbID, String type) {
        this.Title = title;
        this.Year = year;
        this.imdbID = imdbID;
        this.Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Movie convertFromSearch() {
        final Movie movie = new Movie();
        movie.setTittel(getTitle());
        movie.setFormat(getFormat());
        movie.setBrukerID(GlobalVars.loggedInUser.getBrukerID());
        movie.setImdb_id(getImdbID());
        movie.setType(getType());
        movie.setYear(getYear());
        return movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleSearchedMovie)) return false;

        SimpleSearchedMovie that = (SimpleSearchedMovie) o;

        if (imdbID != null ? !imdbID.equals(that.imdbID) : that.imdbID != null) return false;
        if (Title != null ? !Title.equals(that.Title) : that.Title != null) return false;
        if (Type != null ? !Type.equals(that.Type) : that.Type != null) return false;
        if (Year != null ? !Year.equals(that.Year) : that.Year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Title != null ? Title.hashCode() : 0;
        result = 31 * result + (Year != null ? Year.hashCode() : 0);
        result = 31 * result + (imdbID != null ? imdbID.hashCode() : 0);
        result = 31 * result + (Type != null ? Type.hashCode() : 0);
        return result;
    }
}
