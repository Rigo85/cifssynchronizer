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

package org.cifssynchronizer.dao.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "smbpath", nullable = false)
    private String smbPath;

    @Column(name = "lastSynchronization", nullable = false)
    private long lastSynchronization;

    @Column(name = "downloadPath", nullable = false)
    private String downloadPath;

    @ManyToOne(fetch = FetchType.EAGER)
    private Credential credential;

    public Configuration() {
    }

    public Configuration(String smbPath, long lastSynchronization, String downloadPath, Credential credential) throws Exception {
        if (smbPath == null || smbPath.trim().isEmpty() || lastSynchronization < 0l || downloadPath.trim().isEmpty())
            throw new Exception("Problems in the creation of Configuration");

        this.smbPath = smbPath;
        this.lastSynchronization = lastSynchronization;
        this.downloadPath = downloadPath;
        this.credential = credential;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmbPath() {
        return smbPath;
    }

    public void setSmbPath(String smbPath) {
        this.smbPath = smbPath;
    }

    public long getLastSynchronization() {
        return lastSynchronization;
    }

    public void setLastSynchronization(long lastSynchronization) {
        this.lastSynchronization = lastSynchronization;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.smbPath);
        hash = 47 * hash + (int) (this.lastSynchronization ^ (this.lastSynchronization >>> 32));
        hash = 47 * hash + Objects.hashCode(this.downloadPath);
        hash = 47 * hash + Objects.hashCode(this.credential);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Configuration other = (Configuration) obj;
        return Objects.equals(this.id, other.id) &&
                Objects.equals(this.smbPath, other.smbPath) &&
                this.lastSynchronization == other.lastSynchronization &&
                Objects.equals(this.downloadPath, other.downloadPath) &&
                Objects.equals(this.credential, other.credential);
    }

    @Override
    public String toString() {
        return String.format("%s with %s", smbPath, credential != null ? credential : "Unknown");
    }

}
