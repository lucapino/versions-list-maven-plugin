/*
 * Copyright 2013 Luca Tagliani
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
package com.github.lucapino.versions.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

/**
 * Goal which generate a version list.
 *
 */
@Mojo(name = "list")
public class VersionsListMojo extends AbstractMojo {

    /**
     * The entry point to Aether, i.e. the component doing all the work.
     */
    @Component
    private RepositorySystem repoSystem;
    /**
     * The current repository/network configuration of Maven.
     */
    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repoSession;
    /**
     * The project's remote repositories to use for the resolution of project
     * dependencies.
     */
    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
    private List<RemoteRepository> projectRepos;
    /**
     * Starting version
     */
    @Parameter(required = true)
    private String startingVersion;
    /**
     * Starting version
     */
    @Parameter(defaultValue = "false", required = true)
    private boolean includeSnapshots;
    /**
     * GroupId of project.
     */
    @Parameter(defaultValue = "${project.groupId}", required = true)
    private String groupId;
    /**
     * ArtifactId of project.
     */
    @Parameter(defaultValue = "${project.artifactId}", required = true)
    private String artifactId;
    /**
     * Name of the property that contains the ordered list of versions
     * requested.
     */
    @Parameter(defaultValue = "${project.artifactId}", required = true)
    private String versionListPropertyName;
    /**
     * The Maven project
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            // create the artifact to search for
            Artifact artifact = new DefaultArtifact(groupId, artifactId, project.getPackaging(), "[" + startingVersion + ",)");
            // create the version request object
            VersionRangeRequest rangeRequest = new VersionRangeRequest();
            rangeRequest.setArtifact(artifact);
            rangeRequest.setRepositories(projectRepos);
            // search for the versions
            VersionRangeResult rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);
            getLog().info("Retrieving version of " + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getExtension());
            List<Version> availableVersions = rangeResult.getVersions();
            getLog().info("Available versions " + availableVersions);
            // if we don't want the snapshots, filter them
            if (!includeSnapshots) {
                filterSnapshots(availableVersions);
            }
            // order the version from the newer to the older
            Collections.reverse(availableVersions);
            ArrayList<String> versionList = new ArrayList<>();
            availableVersions.forEach((version) -> {
                versionList.add(version.toString());
            });
            // set the poject property
            project.getProperties().put(versionListPropertyName, versionList);
        } catch (VersionRangeResolutionException ex) {
            throw new MojoExecutionException("Error in plugin", ex.getCause());
        }
    }

    private void filterSnapshots(List<Version> versions) {
        for (Iterator<Version> versionIterator = versions.iterator(); versionIterator.hasNext();) {
            Version version = versionIterator.next();
            // if the version is a snapshot, get rid of it
            if (version.toString().endsWith("SNAPSHOT")) {
                versionIterator.remove();
            }
        }
    }
}
