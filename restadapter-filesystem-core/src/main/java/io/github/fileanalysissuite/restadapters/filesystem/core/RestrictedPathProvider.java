/*
 * Copyright 2022-2022 Open Text.
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

import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nonnull;

final class RestrictedPathProvider implements PathProvider
{
    private final Path basePath;

    public RestrictedPathProvider(final Path basePath)
    {
        this.basePath = Objects.requireNonNull(basePath);
    }

    @Nonnull
    @Override
    public Path getPath(final String path) throws FileSystemRuntimeException
    {
        final Path suppliedPath = basePath.resolve(path).normalize();
        if (!suppliedPath.startsWith(basePath)) {
            throw new FileSystemRuntimeException("Adapter not does allows browsing outside " + basePath);
        }

        return suppliedPath;
    }
}
