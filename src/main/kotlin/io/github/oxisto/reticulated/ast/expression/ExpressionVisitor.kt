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

import io.github.oxisto.reticulated.ast.Scope
import io.github.oxisto.reticulated.ast.expression.argument.CallTrailerVisitor
import io.github.oxisto.reticulated.ast.expression.literal.Integer
import io.github.oxisto.reticulated.ast.expression.literal.StringLiteral
import io.github.oxisto.reticulated.grammar.Python3BaseVisitor
import io.github.oxisto.reticulated.grammar.Python3Parser
import org.antlr.v4.runtime.tree.TerminalNode

/**
 * Think of splitting the class
 */
class ExpressionVisitor(val scope: Scope) : Python3BaseVisitor<Expression>() {

  /**
   * It is probably a primary
   *
   */
  override fun visitAtom_expr(ctx: Python3Parser.Atom_exprContext): Expression {

    // TODO: can be different things: atom | attributeref | subscription | slicing | call
    // TODO: check if it is a expression no cond
    // return super.visitAtom_expr(ctx)

    return if (ctx.childCount == 1) {
      // check if an atom really have in every case a childCount from 1
      // It is an atom
      ctx.getChild(0).accept(this)
    } else {
      // first child is the primary
      val primary = ctx.getChild(0).accept(this) as Primary

      // The other children are one out of: attributeref | subscription | slicing | call
      // All of them have a primary as the first child

      // check the trailer and decide what it is
      val trailer = ctx.getChild(Python3Parser.TrailerContext::class.java, 0)

      if ( trailer.childCount == 2 ) {
        if (trailer.getChild(TerminalNode::class.java, 0).text == "." ) {
          // it is an attribute ref, parse the identifier
          val id = trailer.getChild(1).accept(IdentifierVisitor(this.scope))

          val attributeRef = AttributeRef(primary, id)

          attributeRef
        } else if(trailer.getChild(0).text == "await") {
          // It is an await expression
          AwaitExpr(trailer.getChild(1).accept(this) as Primary)
        }else {
          throw Exception("could not parse")
        }
      } else if (
              trailer.childCount == 3 &&
              trailer.getChild(TerminalNode::class.java, 0).text == "("
      ) {
        // it is a call

        // parse the trailer
        val argumentList =
                trailer.accept(
                        CallTrailerVisitor(
                                this.scope
                        )
                )
        // create a call
        val call = Call(primary, argumentList)

        call
      } else {
        throw Exception("could not parse")
      }
    }
  }

  override fun visitAtom(ctx: Python3Parser.AtomContext): Atom {

    val expression = ctx.getChild(0).accept(this)

    if (expression is Identifier) {
      // convert identifier to name
      return Name.fromIdentifier(expression)
    }

    if (expression is Atom) {
      return expression
    }

    throw Exception("Invalid type")
  }

  /**
   * See visitTerminal in the IdentifierVisitor
   *
   */
  override fun visitTerminal(node: TerminalNode): Expression {

    // check for some literals now
    val text = node.text

    if (text.startsWith("\"")) {
      return StringLiteral(text.replace("\"", ""))
    }

    // check if it is a number
    val i = text.toIntOrNull()
    if (i != null) {
      return Integer(i)
    }

    // lets just return an identifier for now
    val id = Identifier(node.text)

    return id
  }
}