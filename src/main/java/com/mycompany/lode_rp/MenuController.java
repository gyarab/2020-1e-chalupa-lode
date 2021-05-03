package com.mycompany.lode_rp;

import java.io.IOException;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    private void startSelecting() throws IOException {
        App.setRoot("play", "");
    }
}
