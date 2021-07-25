package io.github.jllopes.nasarover.rules

object NavigationRules {
  def filterInvalidCommands(commands: String): String = {
    commands.filter(isValidCharacter)
  }

  private def isValidCharacter(character: Char): Boolean = {
    character match {
      case 'F' | 'B' | 'L' | 'R' => true
      case _ => false
    }
  }
}
