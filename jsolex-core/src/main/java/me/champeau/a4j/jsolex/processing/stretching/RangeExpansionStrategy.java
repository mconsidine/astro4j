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
package me.champeau.a4j.jsolex.processing.stretching;

public final class RangeExpansionStrategy implements StretchingStrategy {
    private static final int MAX_VALUE = 65535;

    public static final RangeExpansionStrategy DEFAULT = new RangeExpansionStrategy();

    private RangeExpansionStrategy() {

    }

    @Override
    public void stretch(float[] data) {
        double max = Double.MIN_VALUE;
        for (float v : data) {
            if (v > max) {
                max = v;
            }
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) ((MAX_VALUE / max) * data[i]);
        }
    }

    @Override
    public void stretch(float[][] rgb) {
        double max = Double.MIN_VALUE;
        for (float[] channel : rgb) {
            for (float v : channel) {
                if (v > max) {
                    max = v;
                }
            }
        }
        for (float[] channel : rgb) {
            for (int i = 0; i < channel.length; i++) {
                channel[i] = (float) ((MAX_VALUE / max) * channel[i]);
            }
        }
    }
}
