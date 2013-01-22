package com.github.lucapino.versions.list;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which generate a version list.
 *
 * @goal list
 *
 */
public class VersionsListMojo extends AbstractMojo {

    /**
     * Starting version
     *
     * @parameter
     * @required
     */
    private String startingVersion;
    /**
     * Starting version
     *
     * @parameter default-value="false"
     * @required
     */
    private boolean includeSnapshots;
    /**
     * GroupId of project.
     *
     * @parameter default-value="${project.groupId}
     * @required
     */
    private String groupId;
    /**
     * ArtifactId of project.
     *
     * @parameter default-value="${project.artifactId}
     * @required
     */
    private String artifactId;
    /**
     * Name of the property that contains the ordered list of versions
     * requested.
     *
     * @parameter default-value="${project.artifactId}
     * @required
     */
    private String versionListPropertyName;
    /**
     * The Maven project
     *
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;
    /**
     * @parameter default-value="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;
    /**
     * @component
     */
    private ArtifactMetadataSource metadataSource;
    /**
     * @component
     */
    protected ArtifactFactory factory;

    public void execute() throws MojoExecutionException {

        try {
            VersionRange range = VersionRange.createFromVersionSpec("[" + startingVersion + ",)");

            Artifact previousArtifact = factory.createProjectArtifact(groupId, artifactId, "");
            List availableVersions = metadataSource.retrieveAvailableVersions(previousArtifact, localRepository,
                    project.getRemoteArtifactRepositories());
            if (!includeSnapshots) {
                filterSnapshots(availableVersions);
            }
            Collections.reverse(availableVersions);
            ArrayList<String> versionList = new ArrayList<String>();
            for (Iterator versionIterator = availableVersions.iterator(); versionIterator.hasNext();) {
                ArtifactVersion version = (ArtifactVersion) versionIterator.next();
                if (!range.containsVersion((ArtifactVersion) version)) {
                    versionIterator.remove();
                } else {
                    versionList.add(version.toString());
                }
            }
            project.getProperties().put(versionListPropertyName, versionList);
        } catch (Exception ex) {
            throw new MojoExecutionException("Error in plugin", ex.getCause());
        }
    }

    private void filterSnapshots(List versions) {
        for (Iterator versionIterator = versions.iterator(); versionIterator.hasNext();) {
            ArtifactVersion version = (ArtifactVersion) versionIterator.next();
            if ("SNAPSHOT".equals(version.getQualifier()) || version.toString().endsWith("SNAPSHOT")) {
                versionIterator.remove();
            }
        }
    }
}
