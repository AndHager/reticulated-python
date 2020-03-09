/*
 * Copyright (c) 2019, Fraunhofer AISEC. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.oxisto.reticulated.ast.expression

import io.github.oxisto.reticulated.ast.expression.boolean_ops.OrTest
import io.github.oxisto.reticulated.ast.expression.lambda.LambdaNoCond
import java.lang.IllegalArgumentException

class ExpressionNoCond(val orTest: OrTest?, val lambdaNoCond: LambdaNoCond?): Expression(){
    init {
        if ( orTest == null ) {
            if ( lambdaNoCond == null ) {
                throw IllegalArgumentException()
            }
        } else {
            if ( lambdaNoCond != null) {
                throw IllegalArgumentException()
            }
        }
    }

    override fun isCall(): Boolean {
        return false
    }

    override fun toString(): String {
        val result:String
        if(orTest == null){
            result = "lambdaNoCond=$lambdaNoCond"
        } else {
            result = "ortest=$orTest"
        }
        return "ExpressionNoCond(" + System.lineSeparator() +
                "\t$result" + System.lineSeparator() +
                ")"
    }

}