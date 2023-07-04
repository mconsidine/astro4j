/*
 * Copyright 2023-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.champeau.a4j.jsolex.processing.sun.workflow;

import me.champeau.a4j.jsolex.processing.sun.Broadcaster;
import me.champeau.a4j.jsolex.processing.sun.tasks.WriteColorizedImageTask;
import me.champeau.a4j.jsolex.processing.sun.tasks.WriteMonoImageTask;
import me.champeau.a4j.jsolex.processing.sun.tasks.WriteRGBImageTask;
import me.champeau.a4j.jsolex.processing.util.ForkJoinContext;
import me.champeau.a4j.jsolex.processing.util.ImageWrapper32;
import me.champeau.a4j.jsolex.processing.util.ProcessingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An image emitter is a utility tool to generate
 * mono or color images with transformations.
 */
public class DefaultImageEmitter implements ImageEmitter {
    private final Broadcaster broadcaster;
    private final ForkJoinContext executor;
    private final File outputDir;

    public DefaultImageEmitter(Broadcaster broadcaster,
                               ForkJoinContext executor,
                               File outputDir) {
        this.broadcaster = broadcaster;
        this.executor = executor;
        this.outputDir = outputDir;
    }

    @Override
    public Supplier<Void> newMonoImage(GeneratedImageKind kind, String title, String name, ImageWrapper32 image, Consumer<? super float[]> bufferConsumer) {
        prepareOutput(name);
        return executor.submit(new WriteMonoImageTask(broadcaster,
                image,
                outputDir,
                title,
                name
        ) {
            @Override
            public void transform() {
                bufferConsumer.accept(getBuffer());
            }
        });
    }

    private void prepareOutput(String name) {
        var file = outputDir.toPath().resolve(name);
        try {
            Files.createDirectories(file.getParent());
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }

    @Override
    public Supplier<Void> newMonoImage(GeneratedImageKind kind, String title, String name, ImageWrapper32 image) {
        prepareOutput(name);
        return executor.submit(new WriteMonoImageTask(broadcaster,
                image,
                outputDir,
                title,
                name
        ));
    }

    @Override
    public Supplier<Void> newColorImage(GeneratedImageKind kind, String title, String name, ImageWrapper32 image, Function<float[], float[][]> rgbSupplier) {
        prepareOutput(name);
        return executor.submit(new WriteColorizedImageTask(broadcaster,
                image,
                outputDir,
                title,
                name,
                rgbSupplier)
        );
    }

    @Override
    public Supplier<Void> newColorImage(GeneratedImageKind kind, String title, String name, int width, int height, Supplier<float[][]> rgbSupplier) {
        prepareOutput(name);
        return executor.submit(new WriteRGBImageTask(broadcaster,
                new ImageWrapper32(width, height, new float[0]),
                outputDir,
                title,
                name,
                rgbSupplier)
        );
    }
}
