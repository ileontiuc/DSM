/*
 * Copyright 2011-2013, by Vladimir Kostyukov and Contributors.
 * 
 * This file is part of la4j project (http://la4j.org)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributor(s): -
 * 
 */

package org.la4j.matrix.source;

import org.la4j.matrix.Matrix;

public class LoopbackMatrixSource implements MatrixSource {

    private Matrix matrix;

    public LoopbackMatrixSource(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public double get(int i, int j) {
        return matrix.get(i, j);
    }

    @Override
    public int columns() {
        return matrix.columns();
    }

    @Override
    public int rows() {
        return matrix.rows();
    }
}
