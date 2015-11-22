package org.cifssynchronizer.mvc.view.download;

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
class DownloadManagerPresenter {

    private final DownloadManagerView downloadManagerView;
    private int downloadCount = 0;

    public DownloadManagerPresenter(DownloadManagerView downloadManagerView) {
        this.downloadManagerView = downloadManagerView;
    }

    public void addDownload(DownloadTask downloadTask) {
        final DownloadPanelView newDownloadPanelView = new DownloadPanelView(downloadTask);
        newDownloadPanelView.finish.addListener((observable, oldValue, newValue) -> {
            if (newValue) downloadManagerView.getChildren().remove(newDownloadPanelView);
        });
        new DownloadPanelPresenter(downloadTask, newDownloadPanelView);
        downloadManagerView.add(newDownloadPanelView, 0, downloadCount++);
    }
}
