package part2actors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

/**
 * Exercises:
 * 1. Define two "person" actor behaviors, which receive Strings:
 *  - "happy" , which logs your message. e.g. "I've received ____. That's great!"
 *  - "sad" - which logs your message. e.g. "I've received ____. That sucks!"
 *  Test both
 *
 *  2. Change the actor behavior:
 *    - the happy behavior will turn to sad() if it receives "Akka is bad"
 *    - the sad behavior will turn to happy() if it receives "Akka is awesome!"
 *  Test both.
 *
 */

object ActorExercise {

  //Defining Person Behavior
  object HappyBehavior {
    def apply: Behavior[String] = Behaviors.receive{(context, message) =>
      context.log.info(s"I've received '$message'. That's great!")
      Behaviors.same
      }
    }

  object SadBehavior{
    def apply: Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"I've received '$message'. That sucks")
      Behaviors.same
    }
  }

  //Refactored code

  object Person {

    def happy: Behavior[String] = Behaviors.receive{(context, message) =>
      context.log.info(s"I've received '$message'. That's great!")
      Behaviors.same
    }
    def sad : Behavior[String]= Behaviors.receive{(context, message) =>
      context.log.info(s"I've received '$message'. That sucks")
      Behaviors.same
    }

  }

  // Exercise 2
  object ChangingPersonBehavior{

    def happy: Behavior[String] = Behaviors.receive{(context, message) =>
      message match {
        case "Akka is bad." =>
          context.log.info(s"I've received '$message'. Don't say anything bad about Akka")
          sad
        case _  => context.log.info(s"I've received '$message'. That's great!")
        Behaviors.same
      }
    }

    def sad : Behavior[String]= Behaviors.receive{(context, message) =>
      message match {
        case "Akka is awesome!" =>
          context.log.info(s"I've received '$message'. That's Fantastic, Happy Now!")
          happy
        case _ => context.log.info(s"I've received '$message'. That sucks")
          Behaviors.same
      }
    }

    def apply: Behavior[String] = happy

  }

  def testPerson(): Unit ={
    val testPersonActor = ActorSystem(ChangingPersonBehavior.happy, "ChangingPersonTest" )

    testPersonActor ! "I love the color blue."
    testPersonActor ! "Akka is bad."
    testPersonActor ! "I also love color green"
    testPersonActor ! "Akka is awesome!"
    testPersonActor ! "I've love Akka."

    Thread.sleep(1000)
    testPersonActor.terminate()
  }

  //Initiating Person Actor
  def personActor(): Unit = {

    //val actorSystem = ActorSystem(HappyBehavior.apply, "PersonActorSystem" )

    //Refactored
    val actorSystem = ActorSystem(Person.happy, "PersonActorSystem" )
    actorSystem ! "I'm learning Akka"

    Thread.sleep(1000)
    actorSystem.terminate()
  }


  def main(args: Array[String]): Unit = {
    //personActor()
    testPerson()
  }

}
