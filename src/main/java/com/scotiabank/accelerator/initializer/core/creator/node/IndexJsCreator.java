/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.scotiabank.accelerator.initializer.core.creator.node;

import com.scotiabank.accelerator.initializer.core.FileProcessor;
import com.scotiabank.accelerator.initializer.core.creator.FileCreationOrder;
import com.scotiabank.accelerator.initializer.core.creator.FileCreator;
import com.scotiabank.accelerator.initializer.core.creator.annotation.Node;
import com.scotiabank.accelerator.initializer.core.creator.common.SrcFolderCreator;
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
class IndexJsCreator implements FileCreator<ProjectCreation> {

	private static final String INDEX_JS_TPL_PATH = "templates/projectCreation/node/index.js.tpl";

	private final FileProcessor fileProcessor;

	public IndexJsCreator(FileProcessor fileProcessor) {
		this.fileProcessor = checkNotNull(fileProcessor);
	}

	@Override
	public void create(ProjectCreation request) {
		log.info("Creating index.js file");
		InputStream inputStream = this.fileProcessor
				.loadResourceFromClassPath(INDEX_JS_TPL_PATH);
		Path appPath = Paths.get(request.getRootDir(), SrcFolderCreator.SRC_PATH);
		Path indexJsPath = Paths.get("index.js");
		File indexJs = fileProcessor.touch(appPath.resolve(indexJsPath));
		this.fileProcessor.copy(inputStream, indexJs);
	}

	@Override
	public int order() {
		return FileCreationOrder.INDEX_JS.order();
	}

}