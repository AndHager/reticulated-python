package io.github.oxisto.reticulated.ast.simple

import io.github.oxisto.reticulated.ast.EmptyContextException
import io.github.oxisto.reticulated.ast.Scope
import io.github.oxisto.reticulated.ast.expression.ExpressionVisitor
import io.github.oxisto.reticulated.grammar.Python3BaseVisitor
import io.github.oxisto.reticulated.grammar.Python3Parser

class SimpleStatementVisitor(val scope: Scope) : Python3BaseVisitor<SimpleStatement>() {

  override fun visitSimple_stmt(ctx: Python3Parser.Simple_stmtContext?): SimpleStatement {
    if (ctx == null) {
      throw EmptyContextException()
    }

    // not sure how to handle this. are there cases with more than 1 child?
    return ctx.getChild(0).accept(this)
  }

  override fun visitExpr_stmt(ctx: Python3Parser.Expr_stmtContext?): SimpleStatement {
    if (ctx == null) {
      throw EmptyContextException()
    }

    // need some kind of logic here how to decide what exactly this is
    if (ctx.childCount == 1) {
      val expression = ctx.getChild(0).accept(
        ExpressionVisitor(
          this.scope
        )
      )

      return ExpressionStatement(expression)
    } else if (ctx.childCount == 3) {
      // probably an assignment statement, but there are cases when an assignment has more than 3 children
      // for now assume that child 0 = target; child 2 = expression
      //val targetList = ctx.getChild(0).accept(TargetListVisitor(this.scope))
      val target = ctx.getChild(0).accept(TargetVisitor(this.scope))

      val expression = ctx.getChild(2).accept(ExpressionVisitor(this.scope))

      return AssignmentExpression(target, expression)
    }

    throw Exception()
  }
}