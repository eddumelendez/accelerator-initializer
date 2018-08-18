/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.gradle;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.scotiabank.accelerator.initializer.core.creator.annotation.JavaLibrary;
import com.scotiabank.accelerator.initializer.core.creator.annotation.SpringBoot;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import org.springframework.stereotype.Component;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SpringBoot
@JavaLibrary
class BuildDotGradleCreator implements FileCreator<ProjectCreation> {

	@VisibleForTesting
	static final String BUILD_DOT_GRADLE_FILE_NAME = "build.gradle";

	private final FileProcessor fileProcessor;

	public BuildDotGradleCreator(FileProcessor fileProcessor) {
		this.fileProcessor = checkNotNull(fileProcessor);
	}

	@Override
	public void create(ProjectCreation request) {
		log.info("Creating build.gradle");
		File buildDotGradle = fileProcessor
				.touch(Paths.get(request.getRootDir(), BUILD_DOT_GRADLE_FILE_NAME));
		processTemplate(request, buildDotGradle);
	}

	private void processTemplate(ProjectCreation request, File buildDotGradle) {
		log.info("Parsing build.gradle template");
		Map<String, Object> args = createArgs(request);
		new BuildGradleTemplateParser(fileProcessor, args, buildDotGradle)
				.addBuildScript().addApplyPlugin().addProjectMetadata().addRepositories()
				.addDependencies().addJar().addIntegrationTest();
	}

	private Map<String, Object> createArgs(ProjectCreation request) {
		return ImmutableMap.<String, Object>builder().put("ACCP_VERSION", "1.2.1")
				.put("SPRING_BOOT_VERSION", request.discoverySpringBootVersion())
				.put("IS_SPRING_BOOT", request.isSpringBootApp())
				.put("IS_SPRING_BOOT2",
						request.getType() == ApplicationType.JAVA_SPRING_BOOT_2)
				.put("REPOSITORY_NAME", request.getName())
				.put("GROUP_ID", request.getGroup()).build();
	}

}