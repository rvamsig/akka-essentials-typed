package part2actors


import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

/**
 *
 * part 1: Defining Behaviour of the Actor.
 * part 2: Instantiating an Actor.
 * part 3: Communicate with Actor.
 * part 4: Gracefully shutdown.
 * */

object ActorsIntro {
 //part 1: Behaviour

  /* In Akka, We define an Actor in terms of it's behaviour that means what will the Actor when it receives a
    message. So, In order to define an Actor we've to define it's behaviour.

    Behaviors can be constructed with a very interesting Factory Method called Behaviors

    Behaviors is an object with bunch of methods that are able to construct instances of Behavior.
    receiveMessage method takes the argument of the form message of type String to something that returns another Behavior of type String.

    So, this return Behavior[String] which is Behavior of this Actor will take after receiving the message

    Actor(receives message) -> Change Behavior -> Receives messages -> Change Behavior

    It is very common that we keep the behavior the SAME
   */
  val simpleActorBehavior: Behavior[String] = Behaviors.receiveMessage { (message: String) =>
    //the logic of handling a message is multiline. So, here is the place we can do something with the message.

    /*Say,  Print line and add a little tag. Say, I've received the message. Own logic to handle the message and define
    new behavior for the NEXT message. Actor can receive a number of message so we've to define if and how the Actor has
    to change the behavior of the message.*/

    println(s"[simple actor] I have received: $message ")

    Behaviors.same //If we don't want to change the behavior of the message. We keep the same logic


    //The above logic only define how the Actor should work but it doesn't instantiate an actual Actor
  }


  def demoSimpleActor() : Unit ={
   /* We're going to define the Actor in terms of ActorSystem and this ActorSystem which is the Roots of an entire hierarchy
    of Actors that we are going to discuss throughout this Chapter and the roots ActorSystem is going to be my simpleActorBehavior
    and I've to give this ActorSystem a Name*/

    //part 2: Instantiating an Actor whose behavior is mentioned above.
    val actorSystem = ActorSystem(simpleActorBehavior, "FirstActorSystem")

    //part 3: communicate with above Actor.
    //the below line is read as : actorSystem tell (!) - that means send and we have to send the message of the type that the behavior supports (String) Asynchronously
    actorSystem ! "I am learning Akka" //Sending a message Asynchronously

    //part 4: Gracefully shutdown the ActorSystem and
    Thread.sleep(1000)    //(not usually advised) and sleep for some time

    actorSystem.terminate()

  }

  //Refactored Code as per best practices
  def demoSimpleActor_v1() : Unit ={

    //Parameter : SimpleActor_v2 can be replaced with SimpleActor_v1, simpleActorBehavior
    val actorSystem = ActorSystem(SimpleActor_v2(), "RefactoredActorSystem") //Referring SimpleActor object is much like we're using natural Scala code to instantiate classes
    //in this style we bring the Akka model closer to the OOP style that we are naturally used to
    //So, whenever we need to create particular behavior of a particular logic we're going to run an apply method inside an object.

    //part 3: communicate with above Actor.
    actorSystem ! "Refactored - I am learning Akka" //Sending a message Asynchronously

    //part 4: Gracefully shutdown the ActorSystem and
    Thread.sleep(1000)    //(not usually advised) and sleep for some time
    actorSystem.terminate()

  }

  //Refactored Code as per best practices
  object SimpleActor_v1 {
    def apply: Behavior[String] = Behaviors.receiveMessage{ (message: String) =>
        println(s"[simple actor - Refactored-v1] I have received: $message ")

        //New behaviour for the NEXT message
        Behaviors.same
    }
  }

  //There are many ways of creating Actors and their behaviors. so, Behavior.receiveMessage is one of a variety Factory Method useful for creating behavior.
  object SimpleActor_v2{
    /*
    Behaviors.receive is a more general takes a lamda of 2 args - context and message
    context: data structure which is bundled with message interpol. Actor context having access to variety of API inside the logic that handles messages.
    The context stays the same when the Actor is instantiated. So, the "context" is created alongside with the Actor and is passed around the underlying Akka API to the user so that
    we have access to this data structure.
    */
    def apply(): Behavior[String] = Behaviors.receive{(context, message) =>
      //One of the context that the  API offers is logging : simple example: logging
      context.log.info(s"[simple actor - Refactored-v2] I have received: $message ")
      Behaviors.same
    }
  }

/* *
General way of creating an actor behavior by apply method. And, this is just a wrapper over the First Behavior that the
 Actor would use on it's first message.But, inside the bigger lamda we can create some other code and access private data
 and methods and can define other bit that we find helpful and only then we will return to the first behavior to startwith.
 */
  object SimpleActor_V3{
  //it a lamda that takes context as an argument, the context is created alongside with the Actor and this is larger lamda which gives permission to create methods, variables etc.,
  //Behaviors.setup is most general API because, this will help you define the Behavior of your first message.
  def apply: Behavior[String] = Behaviors.setup { context =>
      //actor "private" data and methods, behavior etc., with the capability of creating our own private code first before returning the First Behavior.


      //We can nest Behaviors inside other Behaviors
     // This is the Behavior used for the FIRST message.
    Behaviors.receiveMessage{ message =>
      context.log.info(s"[simple actor - Refactored-v2] I have received: $message ")
      Behaviors.same
    }
      //Return the first behavior that the actor would get on the first message
    }
}

  def main(args: Array[String]): Unit = {
    //demoSimpleActor()
    demoSimpleActor_v1()
  }
}

