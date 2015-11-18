package org.cifssynchronizer.mvc.view.download;

import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.cifssynchronizer.mvc.model.DownloadTask;

import java.util.List;

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
public class DownloadManager extends GridPane {
    private List<DownloadPanel> downloadPanels;
    private int downloadCount = 0;

    public DownloadManager() {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);
        cc.setHalignment(HPos.CENTER);
        this.getColumnConstraints().add(cc);
    }

    public void addDownload(DownloadTask download) {
        //add(new DownloadPanel(download), 0, downloadCount++);
    }
}

