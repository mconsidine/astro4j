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
package me.champeau.a4j.jsolex.processing.params;

import java.io.File;
import java.util.Optional;

public record ProcessParams(
        SpectrumParams spectrumParams,
        ObservationDetails observationDetails,
        DebugParams debugParams,
        VideoParams videoParams,
        GeometryParams geometryParams,
        BandingCorrectionParams bandingCorrectionParams
) {
    public static ProcessParams loadDefaults() {
        return ProcessParamsIO.loadDefaults();
    }

    public static void saveDefaults(ProcessParams params) {
        ProcessParamsIO.saveDefaults(params);
    }

    public static Optional<ProcessParams>  readFrom(File configFile) {
        return Optional.ofNullable(ProcessParamsIO.readFrom(configFile.toPath()));
    }

    public void saveTo(File destination) {
        ProcessParamsIO.saveTo(this, destination);
    }

    public ProcessParams withGeometry(double tilt, double xyRatio) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                new GeometryParams(tilt, xyRatio, geometryParams().isHorizontalMirror(), geometryParams().isVerticalMirror()),
                bandingCorrectionParams
        );
    }

    public ProcessParams withSpectrumParams(SpectrumParams spectrumParams) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }

    public ProcessParams withObservationDetails(ObservationDetails observationDetails) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }

    public ProcessParams withDebugParams(DebugParams debugParams) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }

    public ProcessParams withVideoParams(VideoParams videoParams) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }

    public ProcessParams withGeometryParams(GeometryParams geometryParams) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }

    public ProcessParams withBandingCorrectionParams(BandingCorrectionParams bandingCorrectionParams) {
        return new ProcessParams(
                spectrumParams,
                observationDetails,
                debugParams,
                videoParams,
                geometryParams,
                bandingCorrectionParams
        );
    }
}
