package par1recap

object ScalaRecap {

  //value
  val aBoolean: Boolean = false
  var aVariable: Int = 56
  aVariable += 1

  //expressions
  val anIfExpression = if (2>3) "bigger" else "smaller"

  //instructions Vs Expressions
  val theUnit = println("Hello, Scala")

  //OOP
  class Animal
  class Cat extends Animal
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  //inheritance: extends <= 1 class, but inherit from >= 0 traits
  class Crocodile extends Animal with Carnivore{
    override def eat(animal: Animal): Unit = println("Eating this poor animal")
  }


  //singleton
  object MySingleton

  //companion object
  object Carnivore

  //case class
  case class Person(name: String, age: Int)

  //generics
  class MyList[A] // instances of MyList can be created of A particular type

  //method notation
  //croc.eat(animal) is same as cros eat animal

  val three =  1 + 2
  val three_2 = 1. + (2)

  //Functional Programming
  val incrementer : Int => Int = x => x + 1
  val incremented = incrementer(4)  //5 this intern is equal to incrementer.apply(4)

  //map filter flatmap -> Higher Order functions
  val processedList = List(1,2,3).map(incrementer)
  val aLongerList = List(1,2,3)



  def main(args: Array[String]): Unit = {

  }

}
