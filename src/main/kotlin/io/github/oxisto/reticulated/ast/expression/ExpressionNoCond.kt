/*
 * Copyright (c) 2020, Christian Banse and Andreas Hager. All rights reserved.
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

import io.github.oxisto.reticulated.ast.CouldNotParseException
import io.github.oxisto.reticulated.ast.expression.booleanops.OrTest
import io.github.oxisto.reticulated.ast.expression.lambda.LambdaNoCond

/**
 * This class represents an expression_nocond
 * It´s EBNF representation is:
 *      expression_nocond ::= or_test | lambda_expr_nocond
 * [see: https://docs.python.org/3/reference/expressions.html#grammar-token-expression-nocond]
 */
class ExpressionNoCond(val orTest: OrTest?, val lambdaNoCond: LambdaNoCond?): Expression(){
    init {
        if ( orTest == null ) {
            if ( lambdaNoCond == null ) {
                throw CouldNotParseException()
            }
        } else {
            if ( lambdaNoCond != null) {
                throw CouldNotParseException()
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