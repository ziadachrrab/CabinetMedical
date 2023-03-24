public class Patient {
    private String fullName;
    private String cin;
    private String addresse;
    private String sexe;
    private String maladie;
    private String medicament;

    public Patient(String fullName, String cin, String addresse, String sexe, String maladie, String medicament) {
        this.fullName = fullName;
        this.cin = cin;
        this.addresse = addresse;
        this.sexe = sexe;
        this.maladie = maladie;
        this.medicament = medicament;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getMaladie() {
        return maladie;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }
}
