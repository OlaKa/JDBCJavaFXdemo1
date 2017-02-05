package skola.models;

public class Klass {
    private String kod;
    private String namn;
    private int årskurs;

    public Klass(String kod, String namn, int årskurs) {
        this.kod = kod;
        this.namn = namn;
        this.årskurs = årskurs;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public int getÅrskurs() {
        return årskurs;
    }

    public void setÅrskurs(int årskurs) {
        this.årskurs = årskurs;
    }

    @Override
    public String toString() {
        return this.namn;
    }
}
