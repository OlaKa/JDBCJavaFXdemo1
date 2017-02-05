package skola.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import skola.models.Klass;
import java.sql.*;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;

public class KlassController {
    private ArrayList<Klass> klassLista = new ArrayList<Klass>();

    @FXML
    private ComboBox cbKlassLista;
    @FXML
    private TextField txfKlassKod;
    @FXML
    private TextField txfNamn;
    @FXML
    private TextField txfArskurs;
    @FXML
    private Button btnDelete;

    public void initialize() {
        laddaAllaKlasser();
        reloadKlasser();
    }

    private void reloadKlasser() {
        ObservableList<Klass> lista = FXCollections.observableArrayList(klassLista);
        cbKlassLista.getItems().clear();
        cbKlassLista.setItems(lista);
        cbKlassLista.getItems().add("<< Skapa ny klass >>");
        cbKlassLista.getSelectionModel().selectFirst();
        ItemSelected(null);
    }

    private void laddaAllaKlasser() {
        Connection con = null;
        Statement stm = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Skola?useSSL=false", "root", "root");
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM Klass");
            while(rs.next())
                klassLista.add(new Klass(rs.getString(1),
                                         rs.getString(2),
                                         rs.getInt(3)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(stm != null)
                    stm.close();
                if(con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void sparaKlass(Klass klass) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Skola?useSSL=false", "root", "root");

            stm = con.prepareStatement("UPDATE Klass SET Namn=?, Arskurs=? WHERE Kod=?");
            stm.setString(1, klass.getNamn());
            stm.setInt(2, klass.getÅrskurs());
            stm.setString(3, klass.getKod());

            if( stm.executeUpdate() == 0) {
                stm = con.prepareStatement("INSERT INTO Klass VALUES(?,?,?)");
                stm.setString(1, klass.getKod());
                stm.setString(2, klass.getNamn());
                stm.setInt(3, klass.getÅrskurs());
                stm.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(stm != null)
                    stm.close();
                if(con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void raderaKlass(Klass klass) {
        Connection con = null;
        PreparedStatement stm = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Skola?useSSL=false", "root", "root");

            stm = con.prepareStatement("DELETE FROM Klass WHERE Kod=?");
            stm.setString(1, klass.getKod());
            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(stm != null)
                    stm.close();
                if(con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void ItemSelected(ActionEvent actionEvent) {
        if (cbKlassLista.getSelectionModel().getSelectedIndex() == cbKlassLista.getItems().size()-1) {
            txfKlassKod.setText("");
            txfKlassKod.setDisable(false);
            txfNamn.setText("");
            txfArskurs.setText("");
            btnDelete.setDisable(true);
            return;
        }
        Klass klass = (Klass)cbKlassLista.getSelectionModel().getSelectedItem();
        txfKlassKod.setText(klass.getKod());
        txfKlassKod.setDisable(true);
        txfNamn.setText(klass.getNamn());
        txfArskurs.setText(Integer.toString(klass.getÅrskurs()));
        btnDelete.setDisable(false);
    }

    public void SaveButtonClicked(ActionEvent actionEvent) {
        Klass klass = null;
        if (cbKlassLista.getSelectionModel().getSelectedIndex() == cbKlassLista.getItems().size()-1) {
            klass = new Klass(txfKlassKod.getText(), txfNamn.getText(), Integer.parseInt(txfArskurs.getText()));
            klassLista.add(klass);
            reloadKlasser();
        }
        else {
            klass = (Klass)cbKlassLista.getSelectionModel().getSelectedItem();
            klass.setNamn(txfNamn.getText());
            klass.setÅrskurs(Integer.parseInt(txfArskurs.getText()));
        }
        sparaKlass(klass);
    }

    public void DeleteButtonClicked(ActionEvent actionEvent) {
        Klass klass = (Klass)cbKlassLista.getSelectionModel().getSelectedItem();
        klassLista.remove(klass);
        raderaKlass(klass);
        reloadKlasser();
    }
}
