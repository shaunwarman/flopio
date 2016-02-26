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

@Mojo(name = "deploy")
public class DeployMojo extends AbstractMojo {

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
						element(name("executable"), "${samza.deploy.path}/bin/run-job.sh"),
						element(name("arguments"),
								"--config-factory=org.apache.samza.config.factories.PropertiesConfigFactory," +
								"--config-path=file://${samza.deploy.path}/config/${project.artifactId}.properties"
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
