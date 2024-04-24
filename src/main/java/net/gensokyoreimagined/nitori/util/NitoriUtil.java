// Nitori Copyright (C) 2024 Gensokyo Reimagined
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.gensokyoreimagined.nitori.util;

import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class NitoriUtil {
    public static final String modName;

    static {
        try {
            // if the Ignite mod config file is missing, there's a big problem
            modName = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(NitoriUtil.class.getResource("/ignite.mod.json")).openStream())).getAsJsonObject().get("id").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger getPreferredLogger() {
        return LogUtils.getClassLogger();
    }

    @Nonnull
    public static String makeLogMessage(String baseMessage) {
        return "[" + modName + "] " + baseMessage;
    }
}
