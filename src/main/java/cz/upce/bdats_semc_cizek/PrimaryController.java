package cz.upce.bdats_semc_cizek;

import enums.eTypKey;
import enums.eTypProhl;
import enums.eTypStruktura;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import pamatky.Zamek;
import sprava.SpravaZamku;

public class PrimaryController implements Initializable {

    @FXML
    private ListView<Zamek> listViewZamky;
    @FXML
    private ComboBox<eTypKey> comboBoxTypKlice;
    @FXML
    private TextField textFieldNazev;
    @FXML
    private Button buttonVlozit;
    @FXML
    private Button buttonGenerovat;
    @FXML
    private Label labelPocet;
    @FXML
    private Button buttonImport;
    @FXML
    private Button buttonVyhledat;
    @FXML
    private TextField textFieldKlicVyhledat;
    @FXML
    private Button buttonOdebrat;
    @FXML
    private TextField textFieldKlicOdebrat;
    @FXML
    private Button buttonPrebudovat;
    @FXML
    private Button buttonZrusit;
    @FXML
    private Button buttonUlozit;
    @FXML
    private Button buttonNacist;
    @FXML
    private Button buttonUkoncit;
    @FXML
    private Button buttonZobrazit;
    @FXML
    private Spinner<Integer> spinnerPocet;
    @FXML
    private ComboBox<eTypProhl> comboBoxTypProhlidky;
    @FXML
    private TextField textFieldVyhledatSN;
    @FXML
    private TextField textFieldVyhledatMN;
    @FXML
    private TextField textFieldVyhledatSE;
    @FXML
    private TextField textFieldVyhledatME;
    @FXML
    private Button buttonVyhledatNej;

    @FXML
    private Button buttonVlozitFronta;
    @FXML
    private Button buttonOdebratFronta;
    @FXML
    private ComboBox<eTypStruktura> comboBoxStruktura;
    @FXML
    private Button buttonVybudovat;
    @FXML
    private Button buttonPrebudovatFrontu;
    @FXML
    private Button buttonZrusitFrontu;
    @FXML
    private TextField textFieldStupneNFronta;
    @FXML
    private TextField textFieldMinutyNFronta;
    @FXML
    private TextField textFieldStupneEFronta;
    @FXML
    private TextField textFieldMinutyEFronta;

    private final String PAMATKY_TXT = "pamatky.txt";

    private final String FRONTA_TXT = "fronta.txt";

    private String aktualniPoloha;
    @FXML
    private TextField textFieldStupneNVlozit;
    @FXML
    private TextField textFieldMinutyNVlozit;
    @FXML
    private TextField textFieldStupneEVlozit;
    @FXML
    private TextField textFieldMinutyEVlozit;

    private SpravaZamku spravaZamku;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spravaZamku = new SpravaZamku();
        
        comboBoxTypKlice.setItems(FXCollections.observableList(Arrays.asList(eTypKey.values())));
        comboBoxTypProhlidky.setItems(FXCollections.observableList(Arrays.asList(eTypProhl.values())));
        comboBoxStruktura.setItems(FXCollections.observableList(Arrays.asList(eTypStruktura.values())));

        comboBoxTypKlice.getSelectionModel().selectFirst();
        comboBoxTypProhlidky.getSelectionModel().selectFirst();
        comboBoxStruktura.getSelectionModel().selectFirst();
        SpinnerValueFactory<Integer> pocetValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500);
        spinnerPocet.setValueFactory(pocetValueFactory);
    }

    @FXML
    private void handleComboBoxTypKliceOnAction(ActionEvent event) {
        if (comboBoxTypKlice.getValue() != null)
            spravaZamku.zmenKlic(comboBoxTypKlice.getValue());
    }

    @FXML
    private void handleButtonVlozitOnAction(ActionEvent event) {
        String nazev = textFieldNazev.getText();
        String gps = vytvorGps(
                textFieldStupneNVlozit.getText(),
                textFieldStupneEVlozit.getText(),
                textFieldMinutyNVlozit.getText(),
                textFieldMinutyEVlozit.getText()
        );
        if (nazev == null || nazev.length() == 0 || gps.length() < 6) {
            zobrazZpravu("Byly zadány neplatné údaje");
            return;
        }
        String id = vytvorId(nazev);
        Zamek novy = new Zamek(id, nazev, gps);
        if (!spravaZamku.vlozZamek(novy)) {
            zobrazZpravu("Zámek se nepodařilo vložit");
            return;
        }
        if (comboBoxStruktura.getValue() == eTypStruktura.BINARNI_STROM) {
            listViewZamky.getItems().add(novy);
        } else {
            zobrazZpravu("Struktura musí být binární strom");
        }
    }

    @FXML
    private void handleButtonGenerovatOnAction(ActionEvent event) {
        int pocet = spinnerPocet.getValue();
        spravaZamku.generujZamky(pocet);
    }

    @FXML
    private void handleButtonImportOnAction(ActionEvent event) {
        int pocet = spravaZamku.importujData();
        if (pocet == 0) 
            zobrazZpravu("Nepodařilo se naimportovat památky");
        labelPocet.setText(String.valueOf(pocet));
    }

    @FXML
    private void handleButtonVyhledatOnAction(ActionEvent event) {
        Zamek zamek = spravaZamku.najdiZamek(textFieldKlicVyhledat.getText());
        if (zamek != null) {
            zobrazZpravu(zamek.toString());
        } else {
            zobrazZpravu("Zámek nebyl nalezen");
        }
        listViewZamky.getSelectionModel().select(zamek);
        listViewZamky.requestFocus();
    }

    @FXML
    private void handleButtonOdebratOnAction(ActionEvent event) {
        Zamek odebrany = spravaZamku.odeberZamek(textFieldKlicOdebrat.getText());
        if (odebrany == null) {
            zobrazZpravu("Zámek nebyl nalezen");
        } else if (comboBoxStruktura.getValue() == eTypStruktura.BINARNI_STROM) {
            listViewZamky.getItems().remove(odebrany);
        }
    }

    @FXML
    private void handleButtonPrebudovatOnAction(ActionEvent event) {
        spravaZamku.prebuduj();
    }

    @FXML
    private void handleButtonZrusitOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrzení");
        alert.setHeaderText(null);
        alert.setContentText("Přejete si vymazat všechny památky?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            spravaZamku.zrusPamatky();
        }
    }

    @FXML
    private void handleButtonUlozitOnAction(ActionEvent event) {
        spravaZamku.uloz(PAMATKY_TXT, FRONTA_TXT);
    }

    @FXML
    private void handleButtonNacistOnAction(ActionEvent event) {
        spravaZamku.nacti(PAMATKY_TXT, FRONTA_TXT);
    }

    @FXML
    private void handleButtonUkoncitOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleButtonZobrazitOnAction(ActionEvent event) {
        listViewZamky.getItems().clear();
        eTypProhl typP = comboBoxTypProhlidky.getValue();
        eTypStruktura typS = comboBoxStruktura.getValue();
        Iterator<Zamek> it = spravaZamku.vytvorIterator(typS, typP);
        if(it != null){
            while (it.hasNext()) {
                listViewZamky.getItems().add(it.next());
            }
        }
    }

    private void zobrazZpravu(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upozornění");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @FXML
    private void handleButtonVyhledatNejOnAction(ActionEvent event) {
        String gps = vytvorGps(
                textFieldVyhledatSN.getText(),
                textFieldVyhledatSE.getText(),
                textFieldVyhledatMN.getText(),
                textFieldVyhledatME.getText()
        );
        Zamek nejblizsi = spravaZamku.najdiNejblizsi(gps);
        if (nejblizsi == null) {
            zobrazZpravu("Zvolený typ klíče musí být GPS");
            return;
        }
        zobrazZpravu(nejblizsi.toString());
        listViewZamky.getSelectionModel().select(nejblizsi);
        listViewZamky.requestFocus();
    }

    @FXML
    private void handleButtonVlozitFrontaOnAction(ActionEvent event) {
        String nazev = textFieldNazev.getText();
        String gps = vytvorGps(
                textFieldStupneNFronta.getText(),
                textFieldStupneEFronta.getText(),
                textFieldMinutyNFronta.getText(),
                textFieldMinutyEFronta.getText()
        );
        if (nazev != null && nazev.length() > 0 && gps.length() > 5) {
            String id = vytvorId(nazev);
            Zamek novy = new Zamek(id, nazev, gps);
            if (aktualniPoloha != null) 
                novy.setVzdalenost(aktualniPoloha);
            spravaZamku.vlozZamek(novy);
        } else {
            zobrazZpravu("Byly zadány neplatné údaje");
        }
    }

    @FXML
    private void handleButtonOdebratFrontaOnAction(ActionEvent event) {
        Zamek odebrany = spravaZamku.odeberZFronty();
        if (odebrany == null) {
            zobrazZpravu("Prioritní fronta je prázdná");
        }
    }

    @FXML
    private void handleButtonVybudovatOnAction(ActionEvent event) {
        String gps = vytvorGps(
                textFieldStupneNFronta.getText(),
                textFieldStupneEFronta.getText(),
                textFieldMinutyNFronta.getText(),
                textFieldMinutyEFronta.getText()
        );
        if (gps.length() <= 5) {
            zobrazZpravu("Neplatné GPS");
            return;
        }
        spravaZamku.vybuduj(gps);
    }

    @FXML
    private void handleButtonPrebudovatFrontuOnAction(ActionEvent event) {
        String gps = vytvorGps(
                textFieldStupneNFronta.getText(),
                textFieldStupneEFronta.getText(),
                textFieldMinutyNFronta.getText(),
                textFieldMinutyEFronta.getText()
        );
        if (gps.length() <= 5) {
            zobrazZpravu("Neplatný údaj GPS");
            return;
        }
        spravaZamku.prebudujFrontu(gps);
    }

    @FXML
    private void handleButtonZrusitFrontuOnAction(ActionEvent event) {
        spravaZamku.zrusFrontu();
    }

    private String vytvorId(String nazev) {
        if (nazev.length() > 5) {
            nazev = nazev.substring(0, 5);
        }
        return nazev;
    }

    private String vytvorGps(String stupneN, String stupneE, String minutyN, String minutyE) {
        return "N" + stupneN + " " + minutyN + " E" + stupneE + " " + minutyE;
    }

}
