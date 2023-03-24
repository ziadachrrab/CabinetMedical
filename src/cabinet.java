import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class cabinet extends JFrame {

    private JTextField TxtFullName, TxtCin ,TxtAdress ,TxtMaladie ,TxtMedicament;
    private JLabel lblFullname,lblCin , lblSexe ,lblAdress,lblMaladie ,lblMedicament;
    private JRadioButton homme, femme;
    private ButtonGroup group;
    private JButton ajouter, supprimer, modifier;

    private List<Patient> patients;

    public cabinet(){

        patients = new ArrayList<>();

        setTitle("Cabinet Medicale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(410,450);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);



        // Creation
        lblFullname = new JLabel("Nom Complet :");
        TxtFullName = new JTextField();
        lblAdress = new JLabel("Adresse :");
        TxtAdress = new JTextField();
        lblSexe = new JLabel("sexe :");
        homme = new JRadioButton("Homme");
        femme = new JRadioButton("Femme");
        group = new ButtonGroup();
        lblCin = new JLabel("CIN :");
        TxtCin = new JTextField();
        lblMaladie = new JLabel("Maladie :");
        TxtMaladie = new JTextField();
        lblMedicament = new JLabel("Médicament :");
        TxtMedicament = new JTextField();
        ajouter = new JButton("Ajouter");
        modifier = new JButton("Modifier");
        supprimer = new JButton("Supprimer");




        // Placement
        lblFullname.setBounds(30,20,335,20);
        TxtFullName.setBounds(30,40,335,20);
        lblAdress.setBounds(30,70,335,20);
        TxtAdress.setBounds(30,90,335,20);
        lblCin.setBounds(30,120,335,20);
        TxtCin.setBounds(30,140,335,20);
        lblSexe.setBounds(30,170,335,20);
        homme.setBounds(60,190,335,20);
        femme.setBounds(60,210,335,20);
        lblMaladie.setBounds(30,240,335,20);
        TxtMaladie.setBounds(30,260,335,20);
        lblMedicament.setBounds(30,290,335,20);
        TxtMedicament.setBounds(30,310,335,20);
        ajouter.setBounds(30,350,95,25);
        modifier.setBounds(145,350,95,25);
        supprimer.setBounds(265,350,100,25);

        //Add
        add(lblFullname);
        add(TxtFullName);
        add(lblAdress);
        add(TxtAdress);
        add(lblCin);
        add(TxtCin);
        add(lblSexe);
        add(homme);
        add(femme);
        group.add(homme);
        group.add(femme);
        add(lblMaladie);
        add(TxtMaladie);
        add(lblMedicament);
        add(TxtMedicament);
        add(ajouter);
        add(modifier);
        add(supprimer);

        ajouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterPatient();
            }
        });
        supprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerPatient();
            }
        });
        modifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierPatient();
            }
        });

    }


    private void ajouterPatient() {
        String fullName = TxtFullName.getText();
        String addresse = TxtAdress.getText();
        String cin = TxtCin.getText();
        String sexe = homme.isSelected() ? "Homme" : "Femme";
        String maladie = TxtMaladie.getText();
        String medicament = TxtMedicament.getText();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            String url = "jdbc:oracle:thin:@//localhost:1521/pdb_ziad";
            String user = "ahmed";
            String password = "ziad";
            connection = DriverManager.getConnection(url, user, password);

            String query = "SELECT COUNT(*) FROM patients WHERE cin = ?";
            PreparedStatement checkStatement = connection.prepareStatement(query);
            checkStatement.setString(1, cin);
            result = checkStatement.executeQuery();

            if (result.next() && result.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Le patient existe déjà.");
            } else {
                String insert = "INSERT INTO patients (fullname, addresse, cin, sexe, maladie, medicament) VALUES (?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(insert);
                statement.setString(1, fullName);
                statement.setString(2, addresse);
                statement.setString(3, cin);
                statement.setString(4, sexe);
                statement.setString(5, maladie);
                statement.setString(6, medicament);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Patient ajouté avec succès.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion à la base de données.");
            e.printStackTrace();
        }
    }



    private void supprimerPatient() {
        JTextField cinField = new JTextField(10);
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        inputPanel.add(new JLabel("CIN:"));
        inputPanel.add(cinField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Supprimer patient", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String cin = cinField.getText();
        Patient patient = null;

        String url = "jdbc:oracle:thin:@//localhost:1521/pdb_ziad";
        String user = "ahmed";
        String password = "ziad";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM patients WHERE cin = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cin);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        query = "DELETE FROM patients WHERE cin = ?";
                        try (PreparedStatement statement1 = connection.prepareStatement(query)) {
                            statement1.setString(1, cin);
                            statement1.executeUpdate();
                        }
                        patient = new Patient(resultSet.getString("fullname"), resultSet.getString("addresse"), resultSet.getString("cin"), resultSet.getString("sexe"), resultSet.getString("maladie"), resultSet.getString("medicament"));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du patient.");
            return;
        }

        if (patient != null) {
            patients.remove(patient);
            JOptionPane.showMessageDialog(this, "Patient supprimé avec succès.");
        } else {
            JOptionPane.showMessageDialog(this, "Aucun patient avec ce CIN n'a été trouvé.");
        }
    }




    private void modifierPatient() {
        String cin = TxtCin.getText();
        Patient patient = null;

        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/pdb_ziad", "ahmed", "ziad")) {
            String query = "SELECT * FROM patients WHERE cin = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                patient = new Patient(rs.getString("cin"), rs.getString("fullname"), rs.getString("addresse"), rs.getString("sexe"), rs.getString("maladie"), rs.getString("medicament"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (patient != null) {
            String fullName = TxtFullName.getText();
            String addresse = TxtAdress.getText();
            String sexe = homme.isSelected() ? "Homme" : "Femme";
            String maladie = TxtMaladie.getText();
            String medicament = TxtMedicament.getText();

            try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/pdb_ziad", "ahmed", "ziad")) {
                String query = "UPDATE patients SET fullname = ?, addresse = ?, sexe = ?, maladie = ?, medicament = ? WHERE cin = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, fullName);
                statement.setString(2, addresse);
                statement.setString(3, sexe);
                statement.setString(4, maladie);
                statement.setString(5, medicament);
                statement.setString(6, cin);
                int updatedRow = statement.executeUpdate();

                if (updatedRow > 0) {
                    JOptionPane.showMessageDialog(this, "Le patient a été modifié avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur: Aucune modification n'a été effectuée.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Patient non trouvé.");
        }
    }

}
