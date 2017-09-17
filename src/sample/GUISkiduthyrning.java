/**
 * IK1004
 * Labb 2 och 3
 * @author: Ida Funk
 */

package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class GUISkiduthyrning extends BorderPane {

    private final String[] utsrustnig = {"Längdskidor", "Alpina skidor", "Snowboard"};
    private final String[] tillagg = {"Pjäxor", "Skidor", "Stavar", "Liftkort", "Boots", "Snowboard", "Hjälm"};

    private final ObservableList<String> betalMedel = FXCollections.observableArrayList("Kontant", "Kort", "Swish");
    private final ObservableList<String> langd = FXCollections.observableArrayList();
    private final ObservableList<String> storlek = FXCollections.observableArrayList();
    private final ObservableList<String> vikt = FXCollections.observableArrayList();
    private final ObservableList<String> tid = FXCollections.observableArrayList("1 dag", "2 dagar", "3 dagar",
            "4 dagar", "5 dagar", "6 dagar", "7 dagar", "8 dagar", "9 dagar", "10 dagar", "11 dagar", "12 dagar",
            "13 dagar", "14 dagar", "3 veckor", "4 veckor", "Halvår");

    private StringBuilder builder = new StringBuilder();

    private String valdUtrustning;
    private String valdaTillagg;
    private String valdStorlek;
    private String valdLangd;
    private String valdVikt;
    private String valdUtTid;
    private String valtBetalsatt;

    private TextField tf_fnamn;
    private TextField tf_enamn;
    private TextField tf_adress;
    private TextField tf_postnr;
    private TextField tf_stad;
    private TextField tf_telefon;
    private TextField tf_mail;

    private Label text;

    private CheckBox[] cb_tillagg;

    /* * * * * * * * * * * * * * *
     *      KONSTRUKTOR          *
     * * * * * * * * * * * * * * */
    public GUISkiduthyrning() {
        setTop(getBorderTop());
        setLeft(getVBoxLeft());
        setCenter(getGridCenter());
        setRight(getLabelRight());
        setBottom(getLabelBottom());
    }

    /* * * * * * * * * * * * * * *
     *          TOP              *
     * * * * * * * * * * * * * * */
    private BorderPane getBorderTop() {

        BorderPane borderPane = new BorderPane();

        //Label
        Label label_header = new Label("Datadalens Skiduthyrning AB");
        label_header.setFont(new Font("Arial Rounded MT Bold", 35));
        label_header.setPadding(new Insets(15, 5, 15, 15));


        //Meny
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Meny");
        MenuItem[] menuItems = new MenuItem[1];
        String[] menuItemText = {"Om oss"};

        MenyLyssnare menylyssnare = new MenyLyssnare();

        //Skapar en undermeny, namnger undermenyvalen samt sätter en lyssnare på dessa
        for (int i = 0; i < menuItemText.length; i++) {
            menuItems[i] = new MenuItem(menuItemText[i]);
            menuItems[i].setOnAction(menylyssnare);
        }

        menu.getItems().addAll(menuItems);
        menuBar.getMenus().addAll(menu);

        //Placeringen av Label och MenuBar
        borderPane.setTop(label_header);
        borderPane.setBottom(menuBar);

        return borderPane;
    }


    /* * * * * * * * * * * * * * *
     *         LEFT              *
     * * * * * * * * * * * * * * */
    private VBox getVBoxLeft() {

        //ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();

        //VBox
        VBox vbox_radioButtons = new VBox();

        //Label
        Label labelUtrusting = new Label();
        labelUtrusting.setText("Jag vill åka: ");
        labelUtrusting.setFont(new Font("Arial Rounded MT Bold", 15));
        labelUtrusting.setPadding(new Insets(5, 5, 10, 0));

        vbox_radioButtons.getChildren().add(labelUtrusting);


        //Lägger till radiobuttons med namn strån arrayen utrustning samt lägger dessa i en vbox
        for (int i = 0; i < utsrustnig.length; i++) {
            RadioButton rb = new RadioButton(utsrustnig[i]);
            rb.setToggleGroup(toggleGroup);

            //Lägg till komponenter i VBoxar
            vbox_radioButtons.getChildren().addAll(rb);

            rb.setOnAction(new RadiobuttonLyssnare());
        }

        setMargin(vbox_radioButtons, new Insets(5, 100, 5, 15));

        return vbox_radioButtons;
    }


    /* * * * * * * * * * * * * * *
     *         CENTER            *
     * * * * * * * * * * * * * * */
    private GridPane getGridCenter() {

        GridPane grid = new GridPane();

        grid.add(getGridCheckBoxes(), 0, 0);
        grid.add(getGridComboBoxes(), 0, 1);
        grid.add(getGridTexFields(), 0, 2);
        grid.add(getGridComboBoxBetala(), 0, 3);
        grid.add(getGridButton(), 2, 4);

        grid.setHgap(5);
        grid.setVgap(15);

        return grid;
    }

    //Metoder till getGridCenter()...
    private GridPane getGridCheckBoxes() {

        //GridPane till center-"delen"
        GridPane grid = new GridPane();

        //Label
        Label label_tillagg = new Label();
        label_tillagg.setText("Följande utrustning vill jag hyra: ");
        label_tillagg.setFont(new Font("Arial Rounded MT Bold", 15));
        label_tillagg.setPadding(new Insets(5, 5, 10, 0));

        cb_tillagg = new CheckBox[tillagg.length];

        //Lägger till radiobuttons med namn strån arrayen utrustning samt lägger dessa i en vbox
        for (int i = 0; i < tillagg.length; i++) {
            CheckBox cb = new CheckBox(tillagg[i]);

            text = new Label();
            text.wrapTextProperty();

            cb.setOnAction(new CheckboxLyssnare());

            cb_tillagg[i] = cb;

            //Placerar checkBoxarna:
            //jämna i värden läggs i col. 0 och rad i's värde plus 1.
            if (i % 2 == 0) {
                grid.add(cb, 0, i + 1);
            } else {
                //udda i värden läggs i col 1 och rad på i's värde just nu,
                grid.add(cb, 1, i);
            }
        }

        grid.add(label_tillagg, 0, 0);

        return grid;
    }

    private GridPane getGridComboBoxes() {

        GridPane grid = new GridPane();

        //ComboBox/Menu
        /*
        Kod inspo:
        https://www.programcreek.com/java-api-examples/index.php?api=javafx.util.converter.IntegerStringConverter

        Lägga till storlek, längd och vikt genom en for-loop
        */

        //Combobox och label för val av storlek på skor/boots, hämtade från ObservableList med namnet storlek

        Label labelStorl = new Label("Storlek: (skor/boots)");
        ComboBox<String> comboBox_storl = new ComboBox<>(storlek);

        for (int i = 25; i < 50; i++) {
            storlek.add(String.valueOf(i));
        }

        comboBox_storl.getSelectionModel().selectedItemProperty().addListener(new ComboboxStorlekLyssnare());


        //Combobox och label för val av din längd, hämtade från ObservableList med namnet langd
        Label labelLangd = new Label("Din längd: (cm)");
        ComboBox<String> comboBox_langd = new ComboBox<>(langd);

        for (int i = 80; i < 220; i++) {
            langd.add(String.valueOf(i));
        }

        comboBox_langd.getSelectionModel().selectedItemProperty().addListener(new ComboboxLangdLyssnare());


        //Combobox och label för val av din vikt, hämtade från ObservableList med namnet vikt
        Label labelVikt = new Label("Din vikt: (kg)");
        ComboBox<String> comboBox_vikt = new ComboBox<>(vikt);

        for (int i = 30; i < 170; i++) {
            vikt.add(String.valueOf(i));
        }

        comboBox_vikt.getSelectionModel().selectedItemProperty().addListener(new ComboboxViktLyssnare());


        //Combobox och label för val av hur länge du vill hyra, hämtade från ObservableList med namnet tid
        Label labelTid = new Label("Hur länge vill du hyra?");
        ComboBox<String> comboBox_uthyrningstid = new ComboBox<>(tid);

        comboBox_uthyrningstid.getSelectionModel().selectedItemProperty().addListener(new ComboboxUthyrningsTidLyssnare());


        //lägger ut labels och comboboxar i "gridden"
        grid.add(labelStorl, 0, 0);
        grid.add(comboBox_storl, 0, 1);
        grid.add(labelLangd, 1, 0);
        grid.add(comboBox_langd, 1, 1);
        grid.add(labelVikt, 0, 2);
        grid.add(comboBox_vikt, 0, 3);
        grid.add(labelTid, 1, 2);
        grid.add(comboBox_uthyrningstid, 1, 3);

        grid.setHgap(5);
        grid.setVgap(5);

        return grid;
    }

    private GridPane getGridTexFields() {

        GridPane grid = new GridPane();

        /*
        Kod inspo:
        https://stackoverflow.com/questions/34069030/how-to-add-hint-text-in-a-textfield-in-javafx

        tf_ för att få en hinttext som försvinner när man trycker på textrutan.
        */

        //TextField
        tf_fnamn = new TextField();
        tf_fnamn.setPromptText("Förnamn");
        tf_fnamn.setFocusTraversable(false);

        tf_enamn = new TextField();
        tf_enamn.setPromptText("Efternamn");
        tf_enamn.setFocusTraversable(false);

        tf_adress = new TextField();
        tf_adress.setPromptText("Adress");
        tf_adress.setFocusTraversable(false);

        tf_postnr = new TextField();
        tf_postnr.setPromptText("Post nummer");
        tf_postnr.setFocusTraversable(false);

        tf_stad = new TextField();
        tf_stad.setPromptText("Stad");
        tf_stad.setFocusTraversable(false);

        TextField tf_persnr = new TextField();
        tf_persnr.setPromptText("Personnummer");
        tf_persnr.setFocusTraversable(false);

        tf_telefon = new TextField();
        tf_telefon.setPromptText("Telefonnummer");
        tf_telefon.setFocusTraversable(false);

        tf_mail = new TextField();
        tf_mail.setPromptText("Mail");
        tf_mail.setFocusTraversable(false);


        grid.add(tf_fnamn, 0, 0);
        grid.add(tf_enamn, 1, 0);

        grid.add(tf_adress, 0, 1, 2, 1);

        grid.add(tf_postnr, 0, 2);
        grid.add(tf_stad, 1, 2);

        grid.add(tf_persnr, 0, 3);
        grid.add(tf_telefon, 1, 3);

        grid.add(tf_mail, 0, 4, 2, 1);

        grid.setHgap(10);
        grid.setVgap(5);

        return grid;
    }

    private GridPane getGridComboBoxBetala() {

        GridPane grid = new GridPane();

        //Skapar upp en combobox med betalsätt, hämtade från ObservableList med namnet betalmedel
        Label label_Bet = new Label("Jag vill betala med:");
        ComboBox<String> comboBox_Bet = new ComboBox<>(betalMedel);
        comboBox_Bet.getSelectionModel().selectedItemProperty().addListener(new ComboboxBetalmedelLyssnare());

        grid.add(label_Bet, 0, 0, 2, 1);
        grid.add(comboBox_Bet, 0, 1);

        return grid;
    }

    private GridPane getGridButton() {

        GridPane grid = new GridPane();

        //Buttons
        Button btn_Boka = new Button("Boka!");

        btn_Boka.setOnAction(new KnappLyssnare());


        //Lägger till komponenter i GridPane
        //add(komponent, column, row, sträck över columner, sträck över rader);
        grid.add(btn_Boka, 0, 0);

        return grid;
    }


    /* * * * * * * * * * * * * * *
     *         RIGHT             *
     * * * * * * * * * * * * * * */
    private VBox getLabelRight() {

        VBox vBox = new VBox();

        //Label
        Label label_right = new Label("\t");

        vBox.getChildren().add(label_right);

        return vBox;
    }


    /* * * * * * * * * * * * * * *
     *         BOTTOM            *
     * * * * * * * * * * * * * * */
    private Label getLabelBottom() {
        //Label
        Label label_footer = new Label("\t" + " Datadalens Skiduthyrning AB " + "\t" + "telefon: 021-123 456 " + "\t" + " mail: uthyrning@datadalen.se ");


        label_footer.setPadding(new Insets(5, 5, 5, 5));

        return label_footer;
    }


    /* * * * * * * * * * * * * * *
     *         LYSSNARE          *
     * * * * * * * * * * * * * * */
    private class MenyLyssnare implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

            MenuItem item = (MenuItem) actionEvent.getSource();
            switch (item.getText()) {

                case "Om oss":
                    //Hyperlink
                    Hyperlink link = new Hyperlink();
                    link.setText("Ottsjö");

                    try {
                        java.awt.Desktop.getDesktop().browse(new URI("http://www.ottsjofjallhotell.se/om-oss/"));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }


    private class RadiobuttonLyssnare implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            RadioButton temp = (RadioButton) actionEvent.getSource();

            valdUtrustning = temp.getText();
        }
    }


    private class CheckboxLyssnare implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

            text.setText(null);
            valdaTillagg = "";

            for (CheckBox cb : cb_tillagg) {
                if (cb.isSelected()) {

                    valdaTillagg += cb.getText() + ", "; //fyller på valda tillägg
                }
            }
            text.setText(valdaTillagg);
        }
    }


    private class ComboboxStorlekLyssnare implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

            valdStorlek = newValue;
        }
    }

    private class ComboboxLangdLyssnare implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

            valdLangd = newValue;
        }
    }

    private class ComboboxViktLyssnare implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

            valdVikt = newValue;
        }
    }

    private class ComboboxUthyrningsTidLyssnare implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

            valdUtTid = newValue;
        }
    }


    private class ComboboxBetalmedelLyssnare implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

            valtBetalsatt = newValue;
        }
    }


    private class KnappLyssnare implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Din Bokning");
            alert.setHeaderText(null);

            builder.setLength(0); //Tömmer buildern

            builder.append("------Din Bokning-------------------------------");
            builder.append("\n");
            builder.append("Du vill åka: ");
            builder.append(valdUtrustning);
            builder.append("\n");
            builder.append("Du vill boka följande utrustning: ");
            builder.append(valdaTillagg);
            builder.append("\n");
            builder.append("Du vill boka utrustningen i: ");
            builder.append(valdUtTid);
            builder.append("\n");
            builder.append("\n");
            builder.append("Dina kontaktuppgifter ");
            builder.append("\n");
            builder.append("Namn: ");
            builder.append(tf_fnamn.getText() + " " + tf_enamn.getText());
            builder.append("\n");
            builder.append("Adress: ");
            builder.append(tf_adress.getText());
            builder.append("\n");
            builder.append("Postnummer och stad: ");
            builder.append(tf_postnr.getText() + " " + tf_stad.getText());
            builder.append("\n");
            builder.append("Telefonnummer: ");
            builder.append(tf_telefon.getText());
            builder.append("\n");
            builder.append("Mail: ");
            builder.append(tf_mail.getText());
            builder.append("\n");
            builder.append("\n");
            builder.append("\n");
            builder.append("Du har valt att betala med: " + valtBetalsatt);
            builder.append("\n");

            if (Objects.equals(valtBetalsatt, "Swish")) {
                builder.append("Swishnummer: 123 45 67 89 10, Datadalens Skiduthyrning");
            } else if (Objects.equals(valtBetalsatt, "Kort")) {
                builder.append("Du skickas nu vidare till betalsidan");
            }

            builder.append("\nKom ihåg att ta med din bokningsbekräftelse när du hämtar din bokning.");
            builder.append("\n");

            builder.append("-------------------------------------------------------");

            alert.setContentText(builder.toString());

            alert.showAndWait();
        }
    }
}
