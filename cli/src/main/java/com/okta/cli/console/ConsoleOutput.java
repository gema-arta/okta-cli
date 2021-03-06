/*
 * Copyright 2020-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.cli.console;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

public interface ConsoleOutput extends Closeable {

    void write(String msg);

    default void write(Object obj) {
        write(obj + "");
    }

    void writeLine(String msg);

    default void writeLine(Object obj) {
        writeLine(obj + "");
    }

    void writeError(String message);

    void bold(String message);

    void flush();

    static ConsoleOutput create(boolean colors) {

        return new AnsiConsoleOutput(System.out, colors && !System.getProperty("os.name").startsWith("Windows"));
    }

    class AnsiConsoleOutput implements ConsoleOutput {

        private final PrintStream out;

        private final boolean colors;

        public AnsiConsoleOutput(PrintStream out, boolean colors) {
            this.out = out;
            this.colors = colors;
        }

        @Override
        public void write(String msg) {
            out.print(msg);
        }

        @Override
        public void writeLine(String msg) {
            out.println(msg);
            flush();
        }

        public void writeError(String message) {
            writeWithColor(message, ConsoleColors.RED);
        }

        public void bold(String message) {
            writeWithColor(message, ConsoleColors.BOLD);
        }

        public void writeWithColor(String message, String ansiColor) {
            if (colors) {
                out.print(ansiColor);
            }

            out.print(message);

            if (colors) {
                out.print(ConsoleColors.RESET);
            }
        }

        @Override
        public void flush() {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }
    }
}
