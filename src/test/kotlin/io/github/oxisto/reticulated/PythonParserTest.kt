/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.github.oxisto.reticulated

import io.github.oxisto.reticulated.ast.expression.Call
import io.github.oxisto.reticulated.ast.expression.Name
import io.github.oxisto.reticulated.ast.expression.argument.ArgumentList
import io.github.oxisto.reticulated.ast.simple.ExpressionStatement
import io.github.oxisto.reticulated.ast.statement.FunctionDefinition
import java.io.File
import kotlin.test.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PythonParserTest {
  @Test
  fun testMain() {
    val classUnderTest = PythonParser()
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource("main.py").file)

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
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource("hint.py").file)

    val input = classUnderTest.parse(file.path)

    assertNotNull(input)
  }

  @Test
  fun testTypeSolve() {
    val classUnderTest = PythonParser()
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource("solve.py").file)

    val input = classUnderTest.parse(file.path).root

    val func = input.statements[0]
    assertTrue(func is FunctionDefinition)

    // get the first statement of the suite
    val stmt = func.suite.statements[0].asStatementList().statements[0]
    assertTrue(stmt is ExpressionStatement)

    val call = stmt.expression
    assertTrue(call is Call)

    // get first argument
    val arg = call.callTrailer as ArgumentList
    val arg0 = arg[0]
    assertNotNull(arg0)

    val name = arg0.expression
    assertTrue(name is Name)
    assertEquals("i", name.name)

    assertNotNull(input)
  }
}
