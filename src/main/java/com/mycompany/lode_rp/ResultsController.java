/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lode_rp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author chalu
 */
public class ResultsController {

    @FXML
    private Label label_header;

    public void passName(String name) {
        label_header.setText("Vyhrává " + name + "!");
    }

    @FXML
    private void resultsSubmit() throws IOException {
        App.setRoot("menu", "");
    }

}
