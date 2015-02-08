package materialtest.vivz.slidenerd.materialtest;

public class Bruker {
    private int brukerID = -1;
    private String brukernavn = "";
    private String epost = "";
    private String passord = "";
    private String regDato = null;
    private int brukerType = -1;
    private String sistAktiv = null;
    private String fornavn = "";
    private String mellomnavn = "";
    private String etternavn = "";
    private String fodt = null;
    private String kjonn = "";
    private String visFelt = "";
    private int fb_id = -1;
    private String google_id = "";
    private boolean success = false;
    private String err_msg = "";

    public Bruker(int brukerID, String brukernavn, String epost, String passord, String regDato, int brukerType, String sistAktiv, String fornavn, String mellomnavn, String etternavn, String fodt, String kjonn, String visFelt, int fb_id, String google_id, boolean success, String err_msg) {
        this.brukerID = brukerID;
        this.brukernavn = brukernavn;
        this.epost = epost;
        this.passord = passord;
        this.regDato = regDato;
        this.brukerType = brukerType;
        this.sistAktiv = sistAktiv;
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.fodt = fodt;
        this.kjonn = kjonn;
        this.visFelt = visFelt;
        this.fb_id = fb_id;
        this.google_id = google_id;
        this.success = success;
        this.err_msg = "";
    }

    public int getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(int brukerID) {
        this.brukerID = brukerID;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getRegDato() {
        return regDato;
    }

    public void setRegDato(String regDato) {
        this.regDato = regDato;
    }

    public int getBrukerType() {
        return brukerType;
    }

    public void setBrukerType(int brukerType) {
        this.brukerType = brukerType;
    }

    public String getSistAktiv() {
        return sistAktiv;
    }

    public void setSistAktiv(String sistAktiv) {
        this.sistAktiv = sistAktiv;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public void setMellomnavn(String mellomnavn) {
        this.mellomnavn = mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getFodt() {
        return fodt;
    }

    public void setFodt(String fodt) {
        this.fodt = fodt;
    }

    public String getKjonn() {
        return kjonn;
    }

    public void setKjonn(String kjonn) {
        this.kjonn = kjonn;
    }

    public String getVisFelt() {
        return visFelt;
    }

    public void setVisFelt(String visFelt) {
        this.visFelt = visFelt;
    }

    public long getFb_id() {
        return fb_id;
    }

    public void setFb_id(int fb_id) {
        this.fb_id = fb_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bruker)) return false;

        Bruker bruker = (Bruker) o;

        if (brukerType != bruker.brukerType) return false;
        if (fb_id != bruker.fb_id) return false;
        if (brukerID != bruker.brukerID) return false;
        if (!brukernavn.equals(bruker.brukernavn)) return false;
        if (!epost.equals(bruker.epost)) return false;
        if (etternavn != null ? !etternavn.equals(bruker.etternavn) : bruker.etternavn != null)
            return false;
        if (fodt != null ? !fodt.equals(bruker.fodt) : bruker.fodt != null) return false;
        if (fornavn != null ? !fornavn.equals(bruker.fornavn) : bruker.fornavn != null)
            return false;
        if (google_id != null ? !google_id.equals(bruker.google_id) : bruker.google_id != null)
            return false;
        if (kjonn != null ? !kjonn.equals(bruker.kjonn) : bruker.kjonn != null) return false;
        if (mellomnavn != null ? !mellomnavn.equals(bruker.mellomnavn) : bruker.mellomnavn != null)
            return false;
        if (!passord.equals(bruker.passord)) return false;
        if (!regDato.equals(bruker.regDato)) return false;
        if (sistAktiv != null ? !sistAktiv.equals(bruker.sistAktiv) : bruker.sistAktiv != null)
            return false;
        if (visFelt != null ? !visFelt.equals(bruker.visFelt) : bruker.visFelt != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = brukerID;
        result = 31 * result + brukernavn.hashCode();
        result = 31 * result + epost.hashCode();
        result = 31 * result + passord.hashCode();
        result = 31 * result + regDato.hashCode();
        result = 31 * result + brukerType;
        result = 31 * result + (sistAktiv != null ? sistAktiv.hashCode() : 0);
        result = 31 * result + (fornavn != null ? fornavn.hashCode() : 0);
        result = 31 * result + (mellomnavn != null ? mellomnavn.hashCode() : 0);
        result = 31 * result + (etternavn != null ? etternavn.hashCode() : 0);
        result = 31 * result + (fodt != null ? fodt.hashCode() : 0);
        result = 31 * result + (kjonn != null ? kjonn.hashCode() : 0);
        result = 31 * result + (visFelt != null ? visFelt.hashCode() : 0);
        result = 31 * result + fb_id;
        result = 31 * result + (google_id != null ? google_id.hashCode() : 0);
        return result;
    }
}
