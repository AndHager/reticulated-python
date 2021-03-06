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

package io.github.oxisto.reticulated.ast.expression.comparison

import io.github.oxisto.reticulated.ast.CouldNotParseException
import io.github.oxisto.reticulated.ast.expression.booleanexpr.BooleanExprVisitor
import io.github.oxisto.reticulated.ast.expression.booleanexpr.OrExpr
import io.github.oxisto.reticulated.grammar.Python3BaseVisitor
import io.github.oxisto.reticulated.grammar.Python3Parser
import io.github.oxisto.reticulated.Pair

/**
 * This visitor is called for a comparison
 * It´s EBNF representations is:
 *      comparison ::= or_expr ( comp_operator or_expr )*
 * [see: {@linktourl https://docs.python.org/3/reference/expressions.html#comparisons}]
 */
class ComparisonVisitor(val scope: io.github.oxisto.reticulated.ast.Scope): Python3BaseVisitor<Comparison>() {

    override fun visitComparison(ctx: Python3Parser.ComparisonContext): Comparison {
        if(ctx.childCount < 1 || ctx.childCount%2 == 0){
            throw CouldNotParseException(
                    "The ctx=$ctx in a ComparisonContext should have one or a even childCount."
            )
        }
        val getOrExprByPosition = {
            position:Int -> ctx
                .getChild(position)
                .accept(
                    BooleanExprVisitor(
                        this.scope
                    )
                ) as OrExpr
        }
        val orExpr:OrExpr = getOrExprByPosition(0)
        val comparisons:ArrayList<Pair<CompOperator, OrExpr>> = ArrayList()
        var index = 1
        while(index < ctx.childCount){
            val compSymbol: String = ctx
                    .getChild(index)
                    .text
            val compOperator: CompOperator = CompOperator
                    .getCompOperatorBySymbol(compSymbol)
                    ?: throw CouldNotParseException(
                            "The compOperator should be an element of the enum CompOperator."
                    )
            val pair = Pair(
                    compOperator,
                    getOrExprByPosition(index+1)
            )
            comparisons.add(pair)
            index += 2
        }
        return Comparison(orExpr, comparisons)
    }
}