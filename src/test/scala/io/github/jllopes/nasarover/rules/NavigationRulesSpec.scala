package io.github.jllopes.nasarover.rules

import cats.effect.IO
import munit.CatsEffectSuite

class NavigationRulesSpec extends CatsEffectSuite {

  test("Filter invalid commands from input") {
    val inputCommands = "ASFRGEF"
    val expectedCommands = "FRF"
    val test = NavigationRules.filterInvalidCommands(inputCommands)
    assertIO(IO(test), expectedCommands)
  }
}