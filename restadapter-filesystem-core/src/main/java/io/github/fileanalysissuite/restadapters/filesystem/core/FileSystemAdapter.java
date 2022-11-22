/*
 * Copyright 2022 Micro Focus or one of its affiliates.
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

import io.github.fileanalysissuite.adaptersdk.convenience.ConvenientAdapterDescriptor;
import io.github.fileanalysissuite.adaptersdk.convenience.ConvenientFailureDetails;
import io.github.fileanalysissuite.adaptersdk.convenience.ConvenientFileMetadata;
import io.github.fileanalysissuite.adaptersdk.interfaces.extensibility.AdapterDescriptor;
import io.github.fileanalysissuite.adaptersdk.interfaces.extensibility.FileMetadata;
import io.github.fileanalysissuite.adaptersdk.interfaces.extensibility.OpenStreamFunction;
import io.github.fileanalysissuite.adaptersdk.interfaces.extensibility.RepositoryAdapter;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.CancellationToken;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.FileDataResultsHandler;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.FileListResultsHandler;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.OptionsProvider;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.RepositoryFile;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.RetrieveFileListRequest;
import io.github.fileanalysissuite.adaptersdk.interfaces.framework.RepositoryFilesRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileSystemAdapter implements RepositoryAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemAdapter.class);

    private final PathProvider pathProvider;

    public FileSystemAdapter()
    {
        this(null);
    }

    public FileSystemAdapter(final Path basePath)
    {
        this.pathProvider = createPathProvider(basePath);
    }

    @Nonnull
    private static PathProvider createPathProvider(final Path basePath)
    {
        if (basePath == null) {
            LOGGER.info("No base path restriction has been specified.");
            return new UnrestrictedPathProvider();
        } else {
            LOGGER.info("Base path: {}", basePath);
            return new RestrictedPathProvider(basePath);
        }
    }

    @Nonnull
    @Override
    public AdapterDescriptor createDescriptor()
    {
        return ConvenientAdapterDescriptor.create("RestFileSystem");
    }

    @Override
    public void retrieveFileList(
        final RetrieveFileListRequest request,
        final FileListResultsHandler handler,
        final CancellationToken cancellationToken
    )
    {
        final OptionsProvider repositoryOptions = request.getRepositoryProperties().getRepositoryOptions();
        final Path rootPath = getPath(repositoryOptions);

        try {
            queueAllFiles("-", rootPath, handler, cancellationToken);
        } catch (final IOException ex) {
            handler.registerFailure(ConvenientFailureDetails.create("Failed to queue file hierarchy", ex));
        }
    }

    private static void queueAllFiles(
        final String parentGroupId,
        final Path path,
        final FileListResultsHandler handler,
        final CancellationToken cancellationToken
    ) throws IOException
    {
        final String pathString = path.toString();
        final Iterable<Path> subpaths = Files.list(path)::iterator;

        for (final Path subpath : subpaths) {
            final BasicFileAttributes subpathAttributes
                = Files.readAttributes(subpath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

            if (subpathAttributes.isDirectory()) {
                queueAllFiles(pathString, subpath, handler, cancellationToken);
            } else {
                final FileMetadata fileMetadata = ConvenientFileMetadata.create(
                    subpath.toString(),
                    subpath.getFileName().toString(),
                    subpathAttributes.size(),
                    subpathAttributes.lastModifiedTime().toInstant());

                handler.queueFile(fileMetadata, parentGroupId, cancellationToken);
            }
        }
    }

    @Override
    public void retrieveFilesData(
        final RepositoryFilesRequest request,
        final FileDataResultsHandler handler,
        final CancellationToken cancellationToken
    )
    {
        for (final RepositoryFile file : request.getFiles()) {
            final String fileId = file.getFileId();
            final FileMetadata fileMetadata = file.getMetadata();

            final String name = fileMetadata.getName();
            final String fileLocation = fileMetadata.getFileLocation();

            final Path fileLocationPath = pathProvider.getPath(fileLocation);
            final BasicFileAttributes fileLocationAttributes;
            try {
                fileLocationAttributes
                    = Files.readAttributes(fileLocationPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            } catch (final IOException ex) {
                handler.registerFailure(fileLocation, ConvenientFailureDetails.create("Failed to read file attributes", ex));
                continue;
            }

            final OpenStreamFunction contentStream = () -> new BufferedInputStream(Files.newInputStream(fileLocationPath));

            final FileMetadata newFileMetadata = ConvenientFileMetadata.builder()
                .name(name)
                .fileLocation(fileLocation)
                .size(fileLocationAttributes.size())
                .createdTime(fileLocationAttributes.creationTime().toInstant())
                .accessedTime(fileLocationAttributes.lastAccessTime().toInstant())
                .modifiedTime(fileLocationAttributes.lastModifiedTime().toInstant())
                .additionalMetadata("IS_SYMBOLIC_LINK", Boolean.toString(fileLocationAttributes.isSymbolicLink()))
                .build();

            handler.queueFile(fileId, contentStream, newFileMetadata, cancellationToken);
        }
    }

    @Nonnull
    private Path getPath(final OptionsProvider repositoryOptions) throws FileSystemRuntimeException
    {
        return pathProvider.getPath(
            repositoryOptions.getOption("Path").orElseThrow(() -> new FileSystemRuntimeException("Path not supplied!")));
    }
}
