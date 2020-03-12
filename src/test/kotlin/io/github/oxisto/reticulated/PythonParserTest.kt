/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
/*
 * Copyright (c) 2020, Fraunhofer AISEC. All rights reserved.
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
package io.github.oxisto.reticulated

import io.github.oxisto.reticulated.ast.expression.Identifier
import io.github.oxisto.reticulated.ast.expression.call.Call
import io.github.oxisto.reticulated.ast.expression.Name
import io.github.oxisto.reticulated.ast.expression.argument.ArgumentList
import io.github.oxisto.reticulated.ast.expression.boolean_ops.OrTest
import io.github.oxisto.reticulated.ast.expression.comparison.Comparison
import io.github.oxisto.reticulated.ast.expression.operator.PowerExpr
import io.github.oxisto.reticulated.ast.simple.ExpressionStatement
import io.github.oxisto.reticulated.ast.statement.FunctionDefinition
import java.io.File
import kotlin.test.*


class PythonParserTest {
  @Test
  fun testMain() {
    val classUnderTest = PythonParser()
    val file = File(
        javaClass
            .classLoader
            .getResource("main.py")!!
            .file
    )

    val input = classUnderTest.parse(file.path).root
    assertNotNull(input)

    // first function without arguments
    var func = input.statements[0]
    assertTrue(func is FunctionDefinition)
    assertEquals("func_no_arguments", func.id.name)
    assertEquals(0, func.parameterList.count)

    // first function without arguments
    func = input.statements[1]
    assertTrue(func is FunctionDefinition)
    assertEquals("func_one_argument", func.id.name)
    assertEquals(1, func.parameterList.count)

    // first function without arguments
    func = input.statements[2]
    assertTrue(func is FunctionDefinition)
    assertEquals("func_two_arguments", func.id.name)
    assertEquals(2, func.parameterList.count)
  }

  @Test
  @Ignore
  fun testTypeHintFunction() {
    val classUnderTest = PythonParser()
    val file = File(
        javaClass
        .classLoader
        .getResource("hint.py")!!
        .file
    )

    val input = classUnderTest.parse(file.path)

    assertNotNull(input)
  }

  @Test
  fun testTypeSolve() {
    val classUnderTest = PythonParser()
    val file = File(
        javaClass
        .classLoader
        .getResource("solve.py")!!
        .file
    )

    val input = classUnderTest.parse(file.path).root

    val func = input.statements[0]
    assertTrue(func is FunctionDefinition)

    // get the first statement of the suite
    val stmt = func.suite.statements[0].asStatementList().statements[0]
    assertTrue(stmt is ExpressionStatement)

    val orTestCall = stmt.expression as OrTest
    assertNotNull(orTestCall)
    val subOrTestCall = orTestCall.orTest
    assertNull(subOrTestCall)
    val andTestCall = orTestCall.andTest
    assertNotNull(andTestCall)
    val subAndTestCall = andTestCall.andTest
    assertNull(subAndTestCall)
    val notTestCall = andTestCall.notTest
    assertNotNull(notTestCall)
    val subNotTestCall = notTestCall.notTest
    assertNull(subNotTestCall)
    val comparisonCall = notTestCall.comparison
    assertNotNull(comparisonCall)
    val comparisonsCall = comparisonCall.comparisons
    assertNotNull(comparisonsCall)
    assertEquals(comparisonsCall.size, 0)
    val orExprCall = comparisonCall.orExpr
    assertNotNull(orExprCall)
    val subOrExprCall = orExprCall.orExpr
    assertNull(subOrExprCall)
    val xorExprCall = orExprCall.xorExpr
    assertNotNull(xorExprCall)
    val subXorExprCall = xorExprCall.xorExpr
    assertNull(subXorExprCall)
    val andExprCall = xorExprCall.andExpr
    assertNotNull(andExprCall)
    val subAndExprCall = andExprCall.andExpr
    assertNull(subAndExprCall)
    val shiftExprCall = andExprCall.shiftExpr
    assertNotNull(shiftExprCall)
    val subShiftExprCall = shiftExprCall.shiftExpr
    assertNull(subShiftExprCall)
    val binaryOperatorCall = shiftExprCall.binaryOperator
    assertNull(binaryOperatorCall)
    val baseOperatorCall = shiftExprCall.baseOperator as PowerExpr
    assertNotNull(baseOperatorCall)
    val awaitExprCall = baseOperatorCall.awaitExpr
    assertNull(awaitExprCall)
    val subBaseOperatorCall = baseOperatorCall.baseOperator
    assertNull(subBaseOperatorCall)
    val call = baseOperatorCall.primary
    assertTrue(call is Call)

    // get first argument
    val arg = call.callTrailer as ArgumentList
    val arg0 = arg[0]
    assertNotNull(arg0)

    val name = (
        (
            (
                arg0.expression as OrTest
                ).andTest
                .notTest
                .comparison as Comparison
            ).orExpr
            .xorExpr
            .andExpr
            .shiftExpr
            .baseOperator as PowerExpr
        ).primary
    assertTrue(name is Identifier)
    assertEquals("i", name.name)

    assertNotNull(input)
  }
}
