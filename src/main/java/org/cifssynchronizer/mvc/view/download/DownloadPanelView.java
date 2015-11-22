package org.cifssynchronizer.mvc.view.download;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
public class DownloadPanelView extends VBox {
    Label downloadNameLabel;
    ProgressBar downloadProgressBar;
    Button restartButton;
    Button stopButton;
    Service<SimpleLongProperty> service;
    SimpleBooleanProperty finish;

    public DownloadPanelView(DownloadTask downloadTask) {
        service = null;

        finish = new SimpleBooleanProperty(false);

        downloadProgressBar = new ProgressBar();
        downloadProgressBar.prefWidthProperty().bind(this.widthProperty());

        downloadNameLabel = new Label(downloadTask.getName());

        restartButton = new Button();
        restartButton.setGraphic(new ImageView(
                new Image(getClass().getClassLoader().getResource("images/restart.png").toExternalForm())));

        stopButton = new Button();
        stopButton.setGraphic(new ImageView(
                new Image(getClass().getClassLoader().getResource("images/stop.png").toExternalForm())));

        HBox hBox = new HBox(8, downloadProgressBar, restartButton, stopButton);

        getChildren().addAll(downloadNameLabel, hBox);
        setAlignment(Pos.BASELINE_CENTER);
        setSpacing(8);
    }
}
