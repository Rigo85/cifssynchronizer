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

package org.cifssynchronizer.core;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;

public class Utils {
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static Keys loadKeys() {
        Keys keys = null;

        try (FileReader fr = new FileReader(new File(Utils.class.getClassLoader().getResource("security/keys").toExternalForm()))) {
            Gson g = new Gson();
            keys = g.fromJson(fr, Keys.class);
        } catch (Exception ignored) {
        } finally {
            if (keys == null) keys = new Keys("Guantanamera.951", "!@##$%85135.7875");
        }

        return keys;
    }
}
