/*
 * Copyright 2022-2024 Open Text.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fileanalysissuite.restadapters.filesystem.core;

import jakarta.annotation.Nonnull;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

final class UnrestrictedPathProvider implements PathProvider
{
    private final FileSystem fileSystem;

    public UnrestrictedPathProvider()
    {
        this.fileSystem = FileSystems.getDefault();
    }

    @Nonnull
    @Override
    public Path getPath(final String path)
    {
        return fileSystem.getPath(path);
    }
}
