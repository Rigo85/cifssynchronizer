package org.cifssynchronizer.mvc.model;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import jcifs.smb.NtlmPasswordAuthentication;

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
public class DownloadTask  extends Task<ObservableList<Long>> {
     NtlmPasswordAuthentication auth;
     int number;
     String name;
     String smbPath;
     long size;
     LocalDate lastModification;

    public DownloadTask(NtlmPasswordAuthentication auth, int number, String name, String smbPath, long size, LocalDate lastModification) {
        this.auth = auth;
        this.number = number;
        this.name = name;
        this.smbPath = smbPath;
        this.size = size;
        this.lastModification = lastModification;
    }

    @Override
    protected ObservableList<Long> call() throws Exception {
        return null;
    }
}
