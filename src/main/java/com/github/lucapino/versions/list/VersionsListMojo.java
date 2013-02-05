package com.github.lucapino.versions.list;

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
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.version.Version;

/**
 * Goal which generate a version list.
 *
 * @goal list
 *
 */
public class VersionsListMojo extends AbstractMojo {

    /**
     * The entry point to Aether, i.e. the component doing all the work.
     *
     * @component
     */
    private RepositorySystem repoSystem;
    /**
     * The current repository/network configuration of Maven.
     *
     * @parameter default-value="${repositorySystemSession}"
     * @readonly
     */
    private RepositorySystemSession repoSession;
    /**
     * The project's remote repositories to use for the resolution of project dependencies.
     *
     * @parameter default-value="${project.remoteProjectRepositories}"
     * @readonly
     */
    private List<RemoteRepository> projectRepos;
    // Your code here
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
    
    @Override
    public void execute() throws MojoExecutionException {

        try {

            Artifact artifact = new DefaultArtifact(groupId, artifactId, project.getPackaging(), "[" + startingVersion + ",)");

            VersionRangeRequest rangeRequest = new VersionRangeRequest();
            rangeRequest.setArtifact(artifact);
            rangeRequest.setRepositories(projectRepos);

            
            
            VersionRangeResult rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);

            List<Version> availableVersions = rangeResult.getVersions();

            System.out.println("Available versions " + availableVersions);

            if (!includeSnapshots) {
                filterSnapshots(availableVersions);
            }
            Collections.reverse(availableVersions);
            ArrayList<String> versionList = new ArrayList<String>();
            for (Iterator versionIterator = availableVersions.iterator(); versionIterator.hasNext();) {
                Version version = (Version) versionIterator.next();
                versionList.add(version.toString());
            }
            project.getProperties().put(versionListPropertyName, versionList);
        } catch (Exception ex) {
            throw new MojoExecutionException("Error in plugin", ex.getCause());
        }
    }

    private void filterSnapshots(List<Version> versions) {
        for (Iterator<Version> versionIterator = versions.iterator(); versionIterator.hasNext();) {
            Version version = versionIterator.next();
            if (version.toString().endsWith("SNAPSHOT")) {
                versionIterator.remove();
            }
        }
    }
}
