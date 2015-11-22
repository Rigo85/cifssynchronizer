package org.cifssynchronizer.mvc.view.download;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
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
import org.cifssynchronizer.mvc.model.DownloadTask;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private final DownloadsTableView tableView;
    private final DownloadManagerPresenter managerPresenter;
    private final ScrollPane scrollPane;
    private Dialog dialog;
    public SimpleBooleanProperty managerIsVisible;

    public DownloadTableViewPresenter(DownloadsTableView tableView) {
        this.tableView = tableView;
        DownloadManagerView managerView = new DownloadManagerView();
        managerPresenter = new DownloadManagerPresenter(managerView);
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(managerView);
        managerIsVisible = new SimpleBooleanProperty(false);

        attachEvents();
    }

    private void attachEvents() {
        tableView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    final SimpleBooleanProperty addDownload = new SimpleBooleanProperty(true);
                    if (fileExist(tableView.getSelectionModel().getSelectedItem())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Destination file exist, do you want to overwrite it?", ButtonType.YES, ButtonType.NO);
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alert.setResizable(true);
                        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
                        alert.setTitle("Warning");
                        alert.showAndWait().filter(b -> b != ButtonType.YES).ifPresent(b -> addDownload.set(false));
                    }

                    if (addDownload.get()) {
                        managerPresenter.addDownload(tableView.getSelectionModel().getSelectedItem());
                        initDialog();
                        dialog.show();
                        managerIsVisible.set(true);
                    }
                }
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
        dialog.setOnCloseRequest(e -> managerIsVisible.set(false));
    }

    public void showManager(){
        initDialog();
        dialog.show();
        managerIsVisible.set(true);
    }

    private boolean fileExist(DownloadTask selectedItem) {
        final Path path = Paths.get(selectedItem.getDownloadPath(), selectedItem.getName());
        return Files.exists(path);
    }
}
