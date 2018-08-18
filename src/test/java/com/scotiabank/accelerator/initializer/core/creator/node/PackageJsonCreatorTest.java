/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.node;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PackageJsonCreatorTest {

	@Mock
	private FileProcessor fileProcessor;

	@Captor
	private ArgumentCaptor<Path> pathCaptor;

	@Captor
	private ArgumentCaptor<Map<String, Object>> mapCaptor;

	private FileCreator<ProjectCreation> creator;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		this.creator = new PackageJsonCreator(fileProcessor);
	}

	@Test
	public void assertPackageJsonIsCreated() {
		ProjectCreation request = ProjectCreation.builder().group("hopper")
				.name("test-app").rootDir(".").build();
		this.creator.create(request);
		verify(this.fileProcessor, times(1)).touch(pathCaptor.capture());
		Path packageJsonPath = pathCaptor.getValue();
		assertEquals(Paths.get("./package.json"), packageJsonPath);
	}

	@Test
	public void assertTemplateIsParsed() {
		ProjectCreation request = ProjectCreation.builder().group("hopper")
				.name("test-app").rootDir(".").build();
		this.creator.create(request);
		verify(this.fileProcessor, times(1)).processTemplate(
				eq(PackageJsonCreator.PACKAGE_JSON_TPL_PATH), mapCaptor.capture());

		Map<String, Object> args = mapCaptor.getValue();
		assertEquals("hopper-test-app", args.get("APP_NAME"));
		assertEquals("hopper", args.get("PROJECT_NAME"));
		assertEquals("test-app", args.get("REPO_NAME"));
	}

}