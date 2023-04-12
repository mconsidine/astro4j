/*
 * Copyright 2023 the original author or authors.
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
package me.champeau.a4j.ser.bayer;

import me.champeau.a4j.ser.ColorMode;
import me.champeau.a4j.ser.ImageGeometry;

/**
 * An implementation of demosaicing using bilinear interpolation, which works
 * on a byte[] containing (R,G,B) byte tuples.
 */
public class BilinearDemosaicingStrategy extends AbstractDemosaicingStrategy {

    @Override
    public void demosaic(byte[] rgb, ColorMode mode, ImageGeometry geometry) {
        setup(mode, geometry);
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int kind = colorKindAt(x, y);
                int k = indexOf(x, y);
                int west = x - 1;
                int north = y - 1;
                int east = x + 1;
                int south = y + 1;
                int row = y % 2;
                switch (kind) {
                    case RED -> {
                        rgb[k + GREEN] = avg(
                                rgb[indexOf(x, north)],
                                rgb[indexOf(west, y)],
                                rgb[indexOf(east, y)],
                                rgb[indexOf(x, south)]
                        );
                        rgb[k + BLUE] = avg(
                                rgb[indexOf(west, north)],
                                rgb[indexOf(east, north)],
                                rgb[indexOf(west, south)],
                                rgb[indexOf(east, south)]
                        );
                    }
                    case GREEN -> {
                        switch (colorMode) {
                            case BAYER_RGGB -> {
                                if (row == 0) {
                                    rgb[k + RED] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                } else {
                                    rgb[k + RED] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                }
                            }
                            case BAYER_BGGR -> {
                                if (row == 0) {
                                    rgb[k + RED] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                } else {
                                    rgb[k + RED] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                }
                            }
                            case BAYER_GBRG -> {
                                if (row == 0) {
                                    rgb[k + RED] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                } else {
                                    rgb[k + RED] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                }
                            }
                            case BAYER_GRBG -> {
                                if (row == 0) {
                                    rgb[k + RED] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                } else {
                                    rgb[k + RED] = avg(rgb[indexOf(x, north)], rgb[indexOf(x, south)]);
                                    rgb[k + BLUE] = avg(rgb[indexOf(west, y)], rgb[indexOf(east, y)]);
                                }
                            }
                        }
                    }
                    case BLUE -> {
                        rgb[k + GREEN] = avg(
                                rgb[indexOf(x, north)],
                                rgb[indexOf(west, y)],
                                rgb[indexOf(east, y)],
                                rgb[indexOf(x, south)]
                        );
                        rgb[k + RED] = avg(
                                rgb[indexOf(west, north)],
                                rgb[indexOf(east, north)],
                                rgb[indexOf(west, south)],
                                rgb[indexOf(east, south)]
                        );
                    }
                }
            }
        }
    }
}
