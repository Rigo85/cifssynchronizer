package org.cifssynchronizer.mvc.view.download;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cifssynchronizer.core.Utils;
import org.cifssynchronizer.mvc.model.DownloadTask;

import java.time.LocalDate;

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
public class DownloadsTableView extends TableView<DownloadTask> {

    public DownloadsTableView() {
        super();

        getColumns().add(getNumberColumn());
        getColumns().add(getNameColumn());
        getColumns().add(getPathColumn());
        getColumns().add(getSizeColumn());
        getColumns().add(getLastModificationColumn());
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<DownloadTask, Integer> getNumberColumn() {
        TableColumn<DownloadTask, Integer> numberCol = new TableColumn<>("#");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        return numberCol;
    }

    private TableColumn<DownloadTask, String> getNameColumn() {
        TableColumn<DownloadTask, String> numberCol = new TableColumn<>("Name");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        return numberCol;
    }

    private TableColumn<DownloadTask, String> getPathColumn() {
        TableColumn<DownloadTask, String> pathCol = new TableColumn<>("Path");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("smbPath"));
        return pathCol;
    }

    private TableColumn<DownloadTask, Long> getSizeColumn() {
        TableColumn<DownloadTask, Long> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setCellFactory(col -> new TableCell<DownloadTask, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    this.setText(Utils.humanReadableByteCount(item.longValue(), false));
                }
            }
        });
        return sizeCol;
    }

    private TableColumn<DownloadTask, LocalDate> getLastModificationColumn() {
        TableColumn<DownloadTask, LocalDate> lastMCol = new TableColumn<>("Date");
        lastMCol.setCellValueFactory(new PropertyValueFactory<>("lastModification"));
        return lastMCol;
    }
}
