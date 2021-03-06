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

package io.github.oxisto.reticulated.ast.expression.argument

import io.github.oxisto.reticulated.ast.CouldNotParseException
import io.github.oxisto.reticulated.ast.Scope
import io.github.oxisto.reticulated.ast.expression.primary.call.CallTrailer
import io.github.oxisto.reticulated.ast.expression.primary.call.EmptyCallTrailer
import io.github.oxisto.reticulated.ast.expression.Expression
import io.github.oxisto.reticulated.ast.expression.ExpressionVisitor
import io.github.oxisto.reticulated.ast.expression.comprehension.CompFor
import io.github.oxisto.reticulated.ast.expression.comprehension.Comprehension
import io.github.oxisto.reticulated.ast.expression.comprehension.ComprehensionVisitor
import io.github.oxisto.reticulated.grammar.Python3BaseVisitor
import io.github.oxisto.reticulated.grammar.Python3Parser

class CallTrailerVisitor(val scope: Scope) : Python3BaseVisitor<CallTrailer>() {

  /**
   * It is the trailer form the call in the EBNF form:
   *      call_trailer ::= "(" [ argument_list [","] | comprehension ] ")"
   * [see: {@linktourl https://docs.python.org/3/reference/expressions.html#calls }]
   */
  override fun visitTrailer(ctx: Python3Parser.TrailerContext): CallTrailer {
      if(ctx.childCount < 2 || ctx.childCount > 3){
          throw CouldNotParseException("The childCount of the ctx=$ctx was not expected.")
      }
    return if(ctx.childCount == 3) {
        val trailer = ctx.getChild(1)
        if (trailer.childCount == 1) {
            val argumentContext = trailer.getChild(0)
            if (argumentContext.getChild(1) is Python3Parser.Comp_forContext) {
                // The trailer is a comprehension
                val expression: Expression = argumentContext
                        .getChild(0)
                        .accept(
                                ExpressionVisitor(
                                        this.scope
                                )
                        )
                val compFor: CompFor = argumentContext
                        .getChild(1)
                        .accept(
                                ComprehensionVisitor(
                                        this.scope
                                )
                        ) as CompFor
                return Comprehension(expression, compFor)
            }
        }
        // The trailer is a argument_list
        trailer.accept(
                ArgumentListVisitor(
                        this.scope
                )
        )
    } else {
        EmptyCallTrailer()
    }
  }
}