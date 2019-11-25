package com.github.oxisto.reticulated.ast.statement

import com.github.oxisto.reticulated.ast.expression.Identifier
import com.github.oxisto.reticulated.ast.expression.Expression

/**
 * A parameter with a default value.
 *
 * Reference: https://docs.python.org/3/reference/compound_stmts.html#grammar-token-defparameter
 */
class DefaultParameter(id: Identifier, val default: Expression, expression: Expression?) : Parameter(id, expression) {

  init {
    default.parent = this
  }
}