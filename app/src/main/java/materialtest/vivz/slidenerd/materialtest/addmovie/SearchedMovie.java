package materialtest.vivz.slidenerd.materialtest.addmovie;

import materialtest.vivz.slidenerd.materialtest.Movie;
import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;

public class SearchedMovie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String format;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private int Metascore;
    private double ImdbRating;
    private String imdbVotes;
    private boolean Response;
    private String Error;

    public SearchedMovie(String title, String year, String imdbID, String type) {
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

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public int getMetascore() {
        return Metascore;
    }

    public void setMetascore(int metascore) {
        Metascore = metascore;
    }

    public double getImdbRating() {
        return ImdbRating;
    }

    public void setImdbRating(double imdbRating) {
        ImdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public boolean isResponse() {
        return Response;
    }

    public void setResponse(boolean response) {
        Response = response;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public Movie convertFromSearch() {
        final Movie movie = new Movie();
        movie.setTittel(getTitle());
        movie.setFormat(getFormat());
        movie.setBrukerID(GlobalVars.loggedInUser.getBrukerID());
        movie.setImdb_id(getImdbID());
        movie.setType(getType());
        movie.setYear(getYear());

        movie.setActor(getActors());
        movie.setDirector(getDirector());
        movie.setErr_msg(getError());
        movie.setGenre(getGenre());
        movie.setPlot(getPlot());
        movie.setPoster(getPoster());
        movie.setRated(getRated());
        movie.setReleased(getReleased());
        movie.setRuntime(getRuntime());
        movie.setWriter(getWriter());
        return movie;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchedMovie)) return false;

        SearchedMovie that = (SearchedMovie) o;

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
