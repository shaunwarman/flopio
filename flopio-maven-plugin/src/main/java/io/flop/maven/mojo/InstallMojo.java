package io.flop.maven.mojo;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "install")
public class InstallMojo extends AbstractMojo {

	@Component
	private MavenProject mavenProject;

	@Component
	private MavenSession mavenSession;

	@Component
	private BuildPluginManager pluginManager;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		executeMojo(
				plugin(
						groupId("org.codehaus.mojo"),
						artifactId("exec-maven-plugin"),
						version("1.4.0")
				),
				goal("exec"),
				configuration(
						element(name("executable"), "tar"),
						element(name("arguments"),
								"xf," +
								"${project.build.directory}/${project.artifactId}-${project.version}-dist.tar.gz," +
								"-C," +
								"${samza.deploy.path}"
						)
				),
				executionEnvironment(
						mavenProject,
						mavenSession,
						pluginManager
				)
		);
	}
}
