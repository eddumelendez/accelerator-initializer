/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.gradle;

import com.google.common.collect.ImmutableMap;
import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import com.scotiabank.accelerator.initializer.model.ApplicationType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GradleBinaryCreatorTest {

	@Mock
	private FileProcessor fileProcessor;

	@Mock
	private InputStream inputStream;

	private FileCreator<ProjectCreation> creator;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		this.creator = new GradleBinaryCreator(fileProcessor);
	}

	@Test
	public void asssertGradleVersionIsFourForSpringBoot2() {
		when(this.fileProcessor.loadResourceFromClassPath(anyString()))
				.thenReturn(inputStream);
		ProjectCreation request = ProjectCreation.builder()
				.type(ApplicationType.JAVA_SPRING_BOOT_2).rootDir(".").build();
		this.creator.create(request);
		verify(this.fileProcessor, times(1)).processTemplate(
				eq(GradleBinaryCreator.GRADLE_WRAPPER_PROPERTIES_TEMPLATE_PATH),
				eq(ImmutableMap.of("GRADLE_VERSION", "4.5.1")));
	}

}