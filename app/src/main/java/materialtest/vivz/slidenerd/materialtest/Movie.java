package materialtest.vivz.slidenerd.materialtest;

import materialtest.vivz.slidenerd.materialtest.utils.GlobalVars;

public class Movie {
    private int filmID = -1;
    private String tittel = "";
    private String format = "";
    private String lagtTil = "";
    private int brukerID = -1;
    private String type = "";
    private String year = "";
    private String rated = "";
    private String released = "";
    private String runtime = "";
    private String genre = "";
    private String director = "";
    private String writer = "";
    private String actor = "";
    private String tagline = "";
    private String plot = "";
    private String trailer = "";
    private String imdb_id = "";
    private String trakt_id = "";
    private String poster = "";

    private String ut = "";
    private String dato = "";
    private String utlanID = "";
    private String navn = "";

    private String err_msg = "";

    public Movie(){
        this.setBrukerID(GlobalVars.loggedInUser.getBrukerID());
    }

    public Movie(String tittel, String format) {
        this.setBrukerID(GlobalVars.loggedInUser.getBrukerID());
        this.tittel = tittel;
        this.format = format;
    }

    public Movie(int filmID, String tittel, String format, String lagtTil, int brukerID, String type, String year, String rated, String released, String runtime, String genre, String director, String writer, String actor, String tagline, String plot, String trailer, String imdb_id, String trakt_id, String poster, String ut, String dato, String utlanID, String navn, String err_msg) {
        setFilmID(filmID);
        setTittel(tittel);
        setFormat(format);
        setLagtTil(lagtTil);
        setBrukerID(brukerID);
        setType(type);
        setYear(year);
        setRated(rated);
        setReleased(released);
        setRuntime(runtime);
        setGenre(genre);
        setDirector(director);
        setWriter(writer);
        setActor(actor);
        setTagline(tagline);
        setPlot(plot);
        setTrailer(trailer);
        setImdb_id(imdb_id);
        setTrakt_id(trakt_id);
        setPoster(poster);
        setUt(ut);
        setDato(dato);
        setUtlanID(utlanID);
        setNavn(navn);
        setErr_msg(err_msg);
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        if (filmID > 0) {
            this.filmID = filmID;
        }
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(String tittel) {
            this.tittel = tittel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
            this.format = format;
        }

    public String getLagtTil() {
        return lagtTil;
    }

    public void setLagtTil(String lagtTil) {
            this.lagtTil = lagtTil;
    }

    public int getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(int brukerID) {
        if (brukerID > 0) {
            this.brukerID = brukerID;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
            this.type = type;
    }

    public String getYear() {
        return year != null && !year.equalsIgnoreCase("0000") ? year : "";
    }

    public void setYear(String year) {
            this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
            this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
            this.released = released;
    }

    public String getRuntime() {
        return runtime.contains("min") ? runtime : runtime + " mins";
    }

    public void setRuntime(String runtime) {
            this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
            this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
            this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
            this.writer = writer;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
            this.actor = actor;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
            this.tagline = tagline;
    }

    public String getPlot() {
        return plot.equalsIgnoreCase("N/A") ? "" : plot;
    }

    public void setPlot(String plot) {
            this.plot = plot;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
            this.trailer = trailer;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
            this.imdb_id = imdb_id;
    }

    public String getTrakt_id() {
        return trakt_id;
    }

    public void setTrakt_id(String trakt_id) {
            this.trakt_id = trakt_id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
            this.poster = poster;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
            this.ut = ut;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
            this.dato = dato;
    }

    public String getUtlanID() {
        return utlanID;
    }

    public void setUtlanID(String utlanID) {
            this.utlanID = utlanID;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setErr_msg(String err_msg) {
            this.err_msg = err_msg;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;
        
        makeMovieSecure(this);
        makeMovieSecure(movie);
        
        if (brukerID != movie.brukerID) return false;
        if (filmID != movie.filmID) return false;
        if (!actor.equalsIgnoreCase(movie.actor)) return false;
        if (!dato.equalsIgnoreCase(movie.dato)) return false;
        if (!director.equalsIgnoreCase(movie.director)) return false;
        if (err_msg != null ? !err_msg.equalsIgnoreCase(movie.err_msg) : movie.err_msg != null) return false;
        if (!format.equalsIgnoreCase(movie.format)) return false;
        if (!genre.equalsIgnoreCase(movie.genre)) return false;
        if (!imdb_id.equalsIgnoreCase(movie.imdb_id)) return false;
        if (!lagtTil.equalsIgnoreCase(movie.lagtTil)) return false;
        if (!plot.equalsIgnoreCase(movie.plot)) return false;
        if (!poster.equalsIgnoreCase(movie.poster)) return false;
        if (!rated.equalsIgnoreCase(movie.rated)) return false;
        if (!released.equalsIgnoreCase(movie.released)) return false;
        if (!runtime.equalsIgnoreCase(movie.runtime)) return false;
        if (!tagline.equalsIgnoreCase(movie.tagline)) return false;
        if (!tittel.equalsIgnoreCase(movie.tittel)) return false;
        if (!trailer.equalsIgnoreCase(movie.trailer)) return false;
        if (!trakt_id.equalsIgnoreCase(movie.trakt_id)) return false;
        if (!type.equalsIgnoreCase(movie.type)) return false;
        if (!ut.equalsIgnoreCase(movie.ut)) return false;
        if (!utlanID.equalsIgnoreCase(movie.utlanID)) return false;
        if (!writer.equalsIgnoreCase(movie.writer)) return false;
        if (!year.equalsIgnoreCase(movie.year)) return false;
        if (!navn.equalsIgnoreCase(movie.navn)) return false;

        return true;
    }

    private void makeMovieSecure(Movie movie) {
        if (movie.getActor() == null) movie.setActor("");
        if (movie.getDato() == null) movie.setDato("");
        if (movie.getDirector() == null) movie.setDirector("");
        if (movie.getFormat() == null) movie.setFormat("");
        if (movie.getGenre() == null) movie.setGenre("");
        if (movie.getImdb_id() == null) movie.setImdb_id("");
        if (movie.getLagtTil() == null) movie.setLagtTil("");
        if (movie.getPlot() == null) movie.setPlot("");
        if (movie.getPoster() == null) movie.setPoster("");
        if (movie.getRated() == null) movie.setRated("");
        if (movie.getReleased() == null) movie.setReleased("");
        if (movie.getRuntime() == null) movie.setRuntime("");
        if (movie.getTagline() == null) movie.setTagline("");
        if (movie.getTittel() == null) movie.setTittel("");
        if (movie.getTrailer() == null) movie.setTrailer("");
        if (movie.getTrakt_id() == null) movie.setTrakt_id("");
        if (movie.getType() == null) movie.setType("");
        if (movie.getUt() == null) movie.setUt("");
        if (movie.getUtlanID() == null) movie.setUtlanID("");
        if (movie.getWriter() == null) movie.setWriter("");
        if (movie.getYear() == null) movie.setYear("");
        if (movie.getNavn() == null) movie.setNavn("");
    }

    @Override
    public int hashCode() {
        int result = filmID;
        result = 31 * result + tittel.hashCode();
        result = 31 * result + format.hashCode();
        result = 31 * result + lagtTil.hashCode();
        result = 31 * result + brukerID;
        result = 31 * result + type.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + rated.hashCode();
        result = 31 * result + released.hashCode();
        result = 31 * result + runtime.hashCode();
        result = 31 * result + genre.hashCode();
        result = 31 * result + director.hashCode();
        result = 31 * result + writer.hashCode();
        result = 31 * result + actor.hashCode();
        result = 31 * result + tagline.hashCode();
        result = 31 * result + plot.hashCode();
        result = 31 * result + trailer.hashCode();
        result = 31 * result + imdb_id.hashCode();
        result = 31 * result + trakt_id.hashCode();
        result = 31 * result + poster.hashCode();
        result = 31 * result + ut.hashCode();
        result = 31 * result + dato.hashCode();
        result = 31 * result + utlanID.hashCode();
        result = 31 * result + navn.hashCode();
        result = 31 * result + (err_msg != null ? err_msg.hashCode() : 0);
        return result;
    }

    public void setIsBorrowed(){
        ut = "0";
    }

    public boolean isBorrowed() {
        return ut.equals("0");
    }

    public void setIsLent(){
        ut = "1";
    }

    public boolean isLent() {
        return ut.equals("1");
    }
}
