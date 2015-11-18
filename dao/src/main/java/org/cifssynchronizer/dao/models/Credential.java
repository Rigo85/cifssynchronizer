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

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"domainname", "username"})
)
@Entity
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "domainname", nullable = false)
    private String domainName;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    public Credential() {
    }

    public Credential(String domainName, String userName, String password) throws Exception {
        if (domainName == null || domainName.trim().isEmpty() || userName == null || userName.trim().isEmpty() || password == null)
            throw new Exception("Problems in the creation of the Credential");
        this.domainName = domainName;
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credential)) return false;

        Credential that = (Credential) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null) &&
                !(getDomainName() != null ? !getDomainName().equals(that.getDomainName()) : that.getDomainName() != null) &&
                !(getUserName() != null ? !getUserName().equals(that.getUserName()) : that.getUserName() != null) &&
                !(getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null);

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDomainName() != null ? getDomainName().hashCode() : 0);
        result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s\\%s", domainName, userName);
    }
}
