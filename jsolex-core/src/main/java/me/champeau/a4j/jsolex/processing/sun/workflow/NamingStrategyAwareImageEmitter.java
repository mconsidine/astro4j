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

import me.champeau.a4j.jsolex.processing.file.FileNamingStrategy;
import me.champeau.a4j.jsolex.processing.util.ImageWrapper;
import me.champeau.a4j.jsolex.processing.util.ImageWrapper32;

import java.awt.Graphics2D;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NamingStrategyAwareImageEmitter implements ImageEmitter {
    private final ImageEmitter delegate;
    private final FileNamingStrategy strategy;
    private final int sequenceNumber;
    private final String imageKind;
    private final String serFileBaseName;

    public NamingStrategyAwareImageEmitter(ImageEmitter delegate,
                                           FileNamingStrategy strategy,
                                           int sequenceNumber,
                                           String imageKind,
                                           String serFileBaseName) {
        this.delegate = delegate;
        this.strategy = strategy;
        this.sequenceNumber = sequenceNumber;
        this.imageKind = imageKind;
        this.serFileBaseName = serFileBaseName;
    }

    private String rename(String name, String category) {
        return strategy.render(sequenceNumber, category, imageKind, name, serFileBaseName);
    }

    @Override
    public void newMonoImage(GeneratedImageKind kind, String category, String title, String name, ImageWrapper32 image, Consumer<? super float[][]> bufferConsumer) {
        delegate.newMonoImage(kind, null, title, rename(name, category), image, bufferConsumer);
    }

    @Override
    public void newMonoImage(GeneratedImageKind kind, String category, String title, String name, ImageWrapper32 image) {
        delegate.newMonoImage(kind, null, title, rename(name, category), image);
    }

    @Override
    public void newColorImage(GeneratedImageKind kind, String category, String title, String name, ImageWrapper32 image, Function<ImageWrapper32, float[][][]> rgbSupplier) {
        delegate.newColorImage(kind, null, title, rename(name, category), image, rgbSupplier);
    }

    @Override
    public void newColorImage(GeneratedImageKind kind, String category, String title, String name, int width, int height, Map<Class<?>, Object> metadata, Supplier<float[][][]> rgbSupplier) {
        delegate.newColorImage(kind, null, title, rename(name, category), width, height, metadata, rgbSupplier);
    }

    @Override
    public void newColorImage(GeneratedImageKind kind, String category, String title, String name, ImageWrapper32 image, Function<ImageWrapper32, float[][][]> rgbSupplier, BiConsumer<Graphics2D, ? super ImageWrapper> painter) {
        delegate.newColorImage(kind, null, title, rename(name, category), image, rgbSupplier, painter);
    }

    @Override
    public void newGenericFile(GeneratedImageKind kind, String category, String title, String name, Path file) {
        delegate.newGenericFile(kind, null, title, rename(name, category), file);
    }
}
