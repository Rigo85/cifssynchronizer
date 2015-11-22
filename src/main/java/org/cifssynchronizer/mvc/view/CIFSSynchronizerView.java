package org.cifssynchronizer.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cifssynchronizer.dao.models.Configuration;
import org.cifssynchronizer.mvc.view.download.DownloadTableViewPresenter;
import org.cifssynchronizer.mvc.view.download.DownloadsTableView;

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
public class CIFSSynchronizerView extends BorderPane {
    HBox toolBar;
    ComboBox<Configuration> configurationComboBox;
    DownloadsTableView downloadsTableView;
    DownloadTableViewPresenter downloadTableViewPresenter;
    MenuBar menuBar;
    MenuItem settings;
    MenuItem exit;
    MenuItem about;
    Button updateButton;
    Button showDownloadManager;
    TextField searchTextField;

    public CIFSSynchronizerView() {
        super();

        downloadsTableView = new DownloadsTableView();
        downloadTableViewPresenter = new DownloadTableViewPresenter(downloadsTableView);

        showDownloadManager = new Button("Show Download Manager");
        VBox bottomPanel = new VBox();
        VBox.setMargin(showDownloadManager, new Insets(0, 8, 8, 8));
        bottomPanel.getChildren().add(showDownloadManager);
        setBottom(bottomPanel);

        createMenus();
        createCenterPanel();
    }

    private void createCenterPanel() {
        VBox centerPanel = new VBox();

        createToolBar();

        VBox.setMargin(toolBar, new Insets(8, 8, 8, 8));
        VBox.setMargin(downloadsTableView, new Insets(0, 8, 8, 8));
        VBox.setVgrow(downloadsTableView, Priority.ALWAYS);

        centerPanel.getChildren().addAll(toolBar, downloadsTableView);

        setCenter(centerPanel);
    }

    private void createToolBar() {
        toolBar = new HBox(10);

        configurationComboBox = new ComboBox<>();
        configurationComboBox.setPrefWidth(250);

        searchTextField = new TextField();
        searchTextField.setPromptText("Search file");
        searchTextField.setPrefWidth(250);

        Label searchLabel = new Label();

        searchLabel.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/search.png").toExternalForm())));

        updateButton = new Button("Update");

        Label expander1 = new Label();
        expander1.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(expander1, Priority.ALWAYS);

        Label expander2 = new Label();
        expander2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(expander2, Priority.ALWAYS);

        toolBar.getChildren().addAll(configurationComboBox, expander1, searchTextField, searchLabel, expander2, updateButton);
    }

    private void createMenus() {
        menuBar = new MenuBar();

        Menu file = new Menu("_File");
        file.setMnemonicParsing(true);
        settings = new MenuItem("Settings");

        exit = new MenuItem("Exit");

        file.getItems().addAll(settings, new SeparatorMenuItem(), exit);

        Menu help = new Menu("_?");
        help.setMnemonicParsing(true);
        about = new MenuItem("About");

        help.getItems().add(about);

        menuBar.getMenus().addAll(file, help);

        setTop(menuBar);
    }
}
