package com.staroon.filezilla.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Staroon
 * Date: 2019/2/21
 * Time: 13:55
 */
public class OpenDirFailedController implements Initializable {
    @FXML
    private Button exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void toExit(ActionEvent action) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
