/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.node;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.Node;
import com.scotiabank.accelerator.initializer.core.creator.common.TestFolderCreator;
import com.scotiabank.accelerator.initializer.core.model.ProjectCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Slf4j
@Node
class IndexSpecJsCreator implements FileCreator<ProjectCreation> {

	private static final String INDEX_SPEC_JS_TPL_PATH = "templates/projectCreation/node/index_spec.js.tpl";

	private final FileProcessor fileProcessor;

	public IndexSpecJsCreator(FileProcessor fileProcessor) {
		this.fileProcessor = checkNotNull(fileProcessor);
	}

	@Override
	public void create(ProjectCreation request) {
		log.info("Creating index_spec.js file");
		InputStream inputStream = this.fileProcessor
				.loadResourceFromClassPath(INDEX_SPEC_JS_TPL_PATH);
		Path testPath = Paths.get(request.getRootDir(), TestFolderCreator.TEST_PATH);
		Path indexSpecJsPath = Paths.get("index_spec.js");
		File indexSpecJs = fileProcessor.touch(testPath.resolve(indexSpecJsPath));
		this.fileProcessor.copy(inputStream, indexSpecJs);
	}

	@Override
	public int order() {
		return FileCreationOrder.INDEX_SPEC_JS.order();
	}

}