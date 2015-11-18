package org.cifssynchronizer.mvc.view.download;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2015 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class DownloadTableViewPresenter {
    DownloadsTableView tableView;
    Dialog dialog;
    DownloadManager manager;
    ScrollPane scrollPane;

    public DownloadTableViewPresenter(DownloadsTableView tableView) {
        this.tableView = tableView;
        manager = new DownloadManager();
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(manager);

        attachEvents();
    }

    private void attachEvents() {
        initDialog();

        tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                manager.addDownload(tableView.getSelectionModel().getSelectedItem());
                dialog.show();
            }
        });
    }

    private void initDialog() {
        dialog = new Dialog();
        dialog.setTitle("Download Manager");
        dialog.initModality(Modality.NONE);
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefWidth(400);
        dialog.getDialogPane().setPrefHeight(300);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        dialog.getDialogPane().setContent(scrollPane);

        //todo add close event to de dialog.
        dialog.setOnCloseRequest(e -> {
            System.out.println("cerrando!");
        });
    }
}
