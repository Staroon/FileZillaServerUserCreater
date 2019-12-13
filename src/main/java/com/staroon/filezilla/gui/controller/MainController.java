package com.staroon.filezilla.gui.controller;

import com.staroon.filezilla.GetUserConfigXml;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Staroon
 * Date: 2019/2/21
 * Time: 12:09
 */
public class MainController implements Initializable {
    @FXML
    private AnchorPane rootItem;

    @FXML
    private Label openFilezillaDir;

    @FXML
    private Label splitLine;

    @FXML
    private Label openFtpDir;

    @FXML
    private TextField ftpHomeItem;

    @FXML
    private TextField configFilePathItem;

    @FXML
    private Button getFtpHome;

    @FXML
    private Button getConfigFile;

    @FXML
    private Label signItem;

    @FXML
    private Button getXml;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        splitLine.setVisible(false);
        openFtpDir.setVisible(false);
        signItem.setVisible(false);
    }

    public void toFilezillaDir(MouseEvent event) throws Exception {
        System.out.println("打开FileZilla Server安装目录->");
        File setupDir = new File("C:\\Program Files (x86)\\FileZilla Server 中文版");
        if (!setupDir.exists()) {
            Parent config = FXMLLoader.load(getClass().getResource("/fxml/OpenDirFailed.fxml"));
            Scene configScene = new Scene(config);
            Stage configStage = new Stage();
            configStage.setTitle("打开目录失败");
            configStage.setScene(configScene);
            configStage.setResizable(false);
            configStage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/img/flogo.png")));
            configStage.show();
        } else if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(setupDir);
        }
    }

    public void toFtpDir(MouseEvent event) throws Exception {
        System.out.println("打开FTP根目录->");
        File ftpDir = new File(ftpHomeItem.getText());
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(ftpDir);
        }
    }

    public void toGetFtpHome(ActionEvent action) {
        signItem.setVisible(false);
        System.out.println("选择FTP根目录...");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择FTP根目录");
        File dir = directoryChooser.showDialog(rootItem.getScene().getWindow());
        if (dir != null) {
            String dirPath = dir.getPath();
            if (dirPath.endsWith("\\")) {
                showSign("FF0000", "请不要将FTP根目录设置为磁盘根目录！");
            } else {
                ftpHomeItem.setText(dirPath);
                System.out.println("已选择FTP根目录：" + dirPath);
                splitLine.setVisible(true);
                openFtpDir.setVisible(true);
            }
        }
    }

    public void toGetConfigFile(ActionEvent action) {
        signItem.setVisible(false);
        System.out.println("选择配置文件...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择FTP配置表");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel文件", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(rootItem.getScene().getWindow());
        if (file != null) {
            String filePath = file.getPath();
            configFilePathItem.setText(filePath);
            System.out.println("已选择配置文件：" + filePath);
        }
    }

    public void toGetXml(ActionEvent action) {
        signItem.setVisible(false);
        String ftpHome = ftpHomeItem.getText();
        String configFilePath = configFilePathItem.getText();
        if (ftpHome.equals("") || configFilePath.equals("")) {
            showSign("FF0000", "请选择FTP根目录以及配置文件！");
            return;
        }
        System.out.println("开始生成Filezilla Server XML配置文件...");

        try {
            int unitNum = new GetUserConfigXml().getXmlAndCreateDir(configFilePath, ftpHome.replace("\\", "\\\\").concat("\\\\"));
            showSign("4169E1", unitNum + "个账号处理完毕，FileZilla配置文件已生成至桌面。");
        } catch (Exception e) {
            showSign("FF0000", "批量创建用户失败，请进入Debug模式查看工具运行日志！");
            e.printStackTrace();
        }
    }

    public void showSign(String color, String str) {
        signItem.setTextFill(Color.web(color));
        signItem.setText(str);
        signItem.setVisible(true);
    }

    public void setFilezillaInColor(MouseEvent event) {
        openFilezillaDir.setTextFill(Color.web("#4169E1"));
    }

    public void setFilezillaOutColor(MouseEvent event) {
        openFilezillaDir.setTextFill(Color.web("#000000"));
    }

    public void setFtpDirInColor(MouseEvent event) {
        openFtpDir.setTextFill(Color.web("#4169E1"));
    }

    public void setFtpDirOutColor(MouseEvent event) {
        openFtpDir.setTextFill(Color.web("#000000"));
    }
}
