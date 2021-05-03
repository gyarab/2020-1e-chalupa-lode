package com.mycompany.lode_rp;

import java.io.IOException;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;

public class PlayController {

    private boolean p1 = false;
    private boolean p2 = false;
    private String p1Name, p2Name;
    private static String[] p1Ships, p2Ships;
    @FXML
    private VBox vbox;
    @FXML
    private Label label_header;
    @FXML
    private TextField input_name;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label label_selected;
    @FXML
    private Button btn_submitSelection;

    @FXML
    private void initialize() {
        input_name.setText("P1");
        generateButtons();
    }

    private void showAlert(AlertType type, String title, String text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(2), e -> {
            alert.close();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void submitSelection() throws IOException {
        if (label_selected.getText().equals("10/10")) {
            if (input_name.getText().isBlank() == false) {
                if (p1 == false) {
                    p1Name = input_name.getText();
                    input_name.setText("P2");
                    label_selected.setText("0/10");
                    getButtons();
                    generateButtons();
                    p1 = true;
                } else if (p2 == false) {
                    if (input_name.getText().equals(p1Name) == false) {
                        p2Name = input_name.getText();
                        label_header.setText("Hraje " + p1Name);
                        vbox.getChildren().remove(input_name);
                        label_selected.setText("0/1");
                        getButtons();
                        generateButtons();
                        p2 = true;
                        startPlay();
                    } else {
                        showAlert(AlertType.WARNING, "Varování", "Jména obou hráčů se nesmí shodovat!");
                    }
                }
            } else {
                showAlert(AlertType.WARNING, "Varování", "Nějaké jméno mít musíte!");
            }
        } else {
            showAlert(AlertType.WARNING, "Varování", "Musíte mít vybráno všech 10 polí!");
        }
    }

    private void generateButtons() {
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                ObservableList<Node> childrens = gridPane.getChildren();
                for (Node node : childrens) {
                    if (node instanceof Button && gridPane.getColumnIndex(node) == j && gridPane.getRowIndex(node) == i) {
                        gridPane.getChildren().remove(node);
                        break;
                    }
                }

                Button btn = new Button();
                btn.setId(j + ";" + i); // x;y coordinates
                gridPane.add(btn, j, i);

                btn.setOnAction(e -> {
                    int selectedCount = Integer.parseInt(label_selected.getText().split("/")[0]);
                    int selectedCountMax = Integer.parseInt(label_selected.getText().split("/")[1]);
                    if (btn.getText().isBlank()) {
                        if (selectedCount < selectedCountMax) {
                            selectedCount++;
                            label_selected.setText(selectedCount + "/" + selectedCountMax);
                            btn.setText("X");
                        }
                    } else {
                        selectedCount--;
                        label_selected.setText(selectedCount + "/" + selectedCountMax);
                        btn.setText("");
                    }
                });
            }
        }
    }

    private void getButtons() {
        String[] ships = new String[10];
        int selectedCount = 0;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (node instanceof Button && ((Button) node).getText().isBlank() == false) {
                ships[selectedCount] = node.getId();
                selectedCount++;
                if (selectedCount == 10) {
                    break;
                }
            }
        }

        if (p1 == false) {
            p1Ships = ships;
        } else if (p2 == false) {
            p2Ships = ships;
        }
    }

    private void startPlay() {
        btn_submitSelection.setOnMouseClicked(null);
        btn_submitSelection.setOnMouseClicked((e1) -> {
            if (label_selected.getText().equals("1/1")) {
                calculateShot("P2", p2Ships);
                if (p2Ships.length > 0) { //P2 can still play
                    label_header.setText("Hraje " + p2Name);
                    label_selected.setText("0/1");
                    generateButtons();

                    btn_submitSelection.setOnMouseClicked(null);
                    btn_submitSelection.setOnMouseClicked((e2) -> {
                        if (label_selected.getText().equals("1/1")) {
                            calculateShot("P1", p1Ships);
                            if (p1Ships.length > 0) { //P1 can still play -> end of round
                                label_header.setText("Hraje " + p1Name);
                                label_selected.setText("0/1");
                                generateButtons();
                                startPlay();
                            } else {
                                try {
                                    App.setRoot("results", p2Name); //P2 wins -> end of game
                                } catch (IOException ex) {
                                    System.out.println(ex);
                                }
                            }
                        } else {
                            showAlert(AlertType.WARNING, "Varování", "Musíte vybrat pole!");
                        }
                    });
                } else {
                    try {
                        App.setRoot("results", p1Name); //P1 wins -> end of game
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            } else {
                showAlert(AlertType.WARNING, "Varování", "Musíte vybrat pole!");
            }
        });
    }

    private void calculateShot(String player, String[] ships) {
        String shot = "";
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (node instanceof Button && ((Button) node).getText().isBlank() == false) {
                shot = node.getId();
            }
        }
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].equals(shot)) {
                String[] newArrayOfShips = new String[ships.length - 1];
                System.arraycopy(ships, 0, newArrayOfShips, 0, i);
                System.arraycopy(ships, i + 1, newArrayOfShips, i, ships.length - i - 1);
                if (player.equals("P1")) {
                    p1Ships = newArrayOfShips;
                    showAlert(AlertType.INFORMATION, "Informace", "Zásah na " + (char) (64 + Integer.parseInt(shot.split(";")[1])) + ";" + shot.split(";")[0] + "!\nHráči " + p1Name + " zbývá " + p1Ships.length + " lodí.");
                } else {
                    p2Ships = newArrayOfShips;
                    showAlert(AlertType.INFORMATION, "Informace", "Zásah na " + (char) (64 + Integer.parseInt(shot.split(";")[1])) + ";" + shot.split(";")[0] + "!\nHráči " + p2Name + " zbývá " + p2Ships.length + " lodí.");
                }
            }
        }
    }
}
