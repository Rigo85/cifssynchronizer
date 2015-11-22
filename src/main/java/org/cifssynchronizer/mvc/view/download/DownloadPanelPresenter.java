package org.cifssynchronizer.mvc.view.download;

import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import org.cifssynchronizer.mvc.model.DownloadTask;


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
public class DownloadPanelPresenter {
    private final DownloadPanelView downloadPanelView;
    private final DownloadTask downloadTask;

    public DownloadPanelPresenter(DownloadTask downloadTask, DownloadPanelView downloadPanelView) {
        this.downloadPanelView = downloadPanelView;
        this.downloadTask = downloadTask;

        attachEvents();
    }

    private void attachEvents() {
        downloadPanelView.service = new Service<SimpleLongProperty>() {
            @Override
            protected Task<SimpleLongProperty> createTask() {
                return downloadTask.copy();
            }
        };

        downloadPanelView.finish.bind(downloadPanelView.service.stateProperty().isEqualTo(Worker.State.SUCCEEDED));

        downloadPanelView.service.start();

        downloadPanelView.restartButton.setOnAction(e -> downloadPanelView.service.restart());
        downloadPanelView.stopButton.setOnAction(e -> downloadPanelView.service.cancel());
        downloadPanelView.stopButton.disableProperty().bind(downloadPanelView.service.stateProperty().isNotEqualTo(Worker.State.RUNNING));

        downloadPanelView.downloadProgressBar.progressProperty().bind(downloadPanelView.service.progressProperty());
        downloadPanelView.downloadProgressBar.prefHeightProperty().bind(downloadPanelView.restartButton.heightProperty());

        downloadPanelView.service.setOnFailed(e ->
                new AudioClip(getClass().getClassLoader().getResource("sounds/error.wav").toExternalForm()).play());

        downloadPanelView.service.setOnSucceeded(e ->
                new AudioClip(getClass().getClassLoader().getResource("sounds/done.wav").toExternalForm()).play());

        downloadPanelView.service.exceptionProperty().addListener((prop, oldValue, newValue) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(String.format("The following errors was found when \ndownloading %s", downloadTask.getSmbPath()));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                    new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            Label label = new Label("The errors was:");

            TextArea textArea = new TextArea(newValue != null && newValue.getMessage() != null ? newValue.getMessage() : "No error message!");
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        });
    }
}
