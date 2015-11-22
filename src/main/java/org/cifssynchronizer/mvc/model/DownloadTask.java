package org.cifssynchronizer.mvc.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class DownloadTask extends Task<SimpleLongProperty> {
    final private SimpleLongProperty result;
    private final NtlmPasswordAuthentication auth;
    private final int number;
    private final String name;
    private final String smbPath;
    private final long size;
    private final LocalDate lastModification;
    private final String downloadPath;

    public DownloadTask(NtlmPasswordAuthentication auth, int number, String name, String smbPath,
                        long size, LocalDate lastModification, String downloadPath) {
        this.auth = auth;
        this.number = number;
        this.name = name;
        this.smbPath = smbPath;
        this.size = size;
        this.lastModification = lastModification;
        this.downloadPath = downloadPath;
        result = new SimpleLongProperty(0l);
    }

    @Override
    protected SimpleLongProperty call() throws Exception {
        long counter = 0l;
        SmbFile remoteFile = new SmbFile(smbPath, auth);
        Path output = Paths.get(downloadPath, name);
        InputStream is;

        try (OutputStream os = new FileOutputStream(output.toString())) {
            is = remoteFile.getInputStream();
            int bufferSize = 8192;
            byte[] b = new byte[bufferSize];
            int noOfBytes;
            while ((noOfBytes = is.read(b)) != -1) {
                if (this.isCancelled()) {
                    break;
                }
                counter += noOfBytes;
                os.write(b, 0, noOfBytes);
                result.setValue(noOfBytes);
                updateValue(result);
                updateProgress(counter, size);
            }
        }
        is.close();

        return result;
    }

    public DownloadTask copy() {
        return new DownloadTask(auth, number, name, smbPath, size, lastModification, downloadPath);
    }

    private NtlmPasswordAuthentication getAuth() {
        return auth;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getSmbPath() {
        return smbPath;
    }

    public long getSize() {
        return size;
    }

    public LocalDate getLastModification() {
        return lastModification;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadTask)) return false;

        DownloadTask that = (DownloadTask) o;

        return getNumber() == that.getNumber() && getSize() == that.getSize() &&
                !(getAuth() != null ? !getAuth().equals(that.getAuth()) : that.getAuth() != null) &&
                !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null) &&
                !(getSmbPath() != null ? !getSmbPath().equals(that.getSmbPath()) : that.getSmbPath() != null) &&
                !(getLastModification() != null ? !getLastModification().equals(that.getLastModification()) :
                        that.getLastModification() != null);
    }

    @Override
    public int hashCode() {
        int result = getAuth() != null ? getAuth().hashCode() : 0;
        result = 31 * result + getNumber();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSmbPath() != null ? getSmbPath().hashCode() : 0);
        result = 31 * result + (int) (getSize() ^ (getSize() >>> 32));
        result = 31 * result + (getLastModification() != null ? getLastModification().hashCode() : 0);
        return result;
    }
}
