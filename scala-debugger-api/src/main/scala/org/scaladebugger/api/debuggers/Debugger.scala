package org.scaladebugger.api.debuggers
import acyclic.file

import java.util.concurrent.ConcurrentHashMap

import org.scaladebugger.api.utils.{Logging, JDILoader}
import org.scaladebugger.api.virtualmachines.{DummyScalaVirtualMachine, ScalaVirtualMachine}

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise, Future}

/**
 * Represents the generic interface that all debugger instances implement.
 */
trait Debugger extends Logging {
  protected val jdiLoader = new JDILoader(this.getClass.getClassLoader)
  private val pendingScalaVirtualMachines =
    new ConcurrentHashMap[String, ScalaVirtualMachine]().asScala

  /**
   * Determines whether or not the debugger is available for use.
   *
   * @return True if the debugger is available, otherwise false
   */
  def isAvailable: Boolean = jdiLoader.isJdiAvailable()

  /**
   * Attempts to load the JDI, asserting that it can be and is loaded.
   *
   * @throws AssertionError If failed to load the JDI
   */
  @throws(classOf[AssertionError])
  protected def assertJdiLoaded(): Unit =
    assert(jdiLoader.tryLoadJdi(),
      """
        |Unable to load Java Debugger Interface! This is part of tools.jar
        |provided by OpenJDK/Oracle JDK and is the core of the debugger! Please
        |make sure that JAVA_HOME has been set and that tools.jar is available
        |on the classpath!
      """.stripMargin.replace("\n", " "))

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @param newVirtualMachineFunc The function that will be called when a new
   *                              virtual machine connection is created as a
   *                              result of this debugger
   * @tparam T The return type of the callback function
   */
  def start[T](newVirtualMachineFunc: ScalaVirtualMachine => T): Unit = {
    start(startProcessingEvents = true, newVirtualMachineFunc)
  }

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @note Returned future represents next connected Scala virtual machine. All
   *       other Scala virtual machines connected after the first one will be
   *       ignored.
   *
   * @param startProcessingEvents If true, events are immediately processed by
   *                              the VM as soon as it is connected
   *
   * @return The future representing the connected Scala virtual machine
   */
  def start(startProcessingEvents: Boolean): Future[ScalaVirtualMachine] = {
    val promise = Promise[ScalaVirtualMachine]()

    start(startProcessingEvents, s => if (!promise.trySuccess(s)) logger.warn(
      s"Unable to accept JVM ${s.uniqueId} as future already completed!"
    ))

    promise.future
  }

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @note Returned future represents next connected Scala virtual machine. All
   *       other Scala virtual machines connected after the first one will be
   *       ignored.
   *
   * @return The future representing the connected Scala virtual machine
   */
  def start(): Future[ScalaVirtualMachine] = start(startProcessingEvents = true)

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @note Returned Scala virtual machine represents next connected Scala
   *       virtual machine. All other Scala virtual machines connected after
   *       the first one will be ignored.
   *
   * @param timeout The maximum time to wait for the JVM to connect
   * @param startProcessingEvents If true, events are immediately processed by
   *                              the VM as soon as it is connected
   *
   * @return The connected Scala virtual machine
   */
  def start(
    timeout: Duration,
    startProcessingEvents: Boolean
  ): ScalaVirtualMachine = Await.result(start(startProcessingEvents), timeout)

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @note Returned Scala virtual machine represents next connected Scala
   *       virtual machine. All other Scala virtual machines connected after
   *       the first one will be ignored.
   *
   * @param timeout The maximum time to wait for the JVM to connect
   *
   * @return The connected Scala virtual machine
   */
  def start(timeout: Duration): ScalaVirtualMachine =
    start(timeout, startProcessingEvents = true)

  /**
   * Starts the debugger, performing any necessary setup and ending with
   * an initialized debugger that is or will be capable of connecting to one or
   * more virtual machine instances.
   *
   * @param startProcessingEvents If true, events are immediately processed by
   *                              the VM as soon as it is connected
   * @param newVirtualMachineFunc The function that will be called when a new
   *                              virtual machine connection is created as a
   *                              result of this debugger
   * @tparam T The return type of the callback function
   */
  def start[T](
    startProcessingEvents: Boolean,
    newVirtualMachineFunc: ScalaVirtualMachine => T
  ): Unit

  /**
   * Shuts down the debugger, releasing any connected virtual machines.
   */
  def stop(): Unit

  /**
   * Indicates whether or not the debugger is running.
   *
   * @return True if it is running, otherwise false
   */
  def isRunning: Boolean

  /**
   * Retrieves the connected virtual machines for the debugger.
   *
   * @return The collection of connected virtual machines
   */
  def connectedScalaVirtualMachines: Seq[ScalaVirtualMachine]

  /**
   * Creates a new dummy Scala virtual machine instance that can be used to
   * prepare pending requests to apply to the Scala virtual machines generated
   * by the debugger once it starts.
   *
   * @return The new dummy (no-op) Scala virtual machine instance
   */
  def newDummyScalaVirtualMachine(): ScalaVirtualMachine =
    DummyScalaVirtualMachine.newInstance()

  /**
   * Adds a new Scala virtual machine to use for pending operations. Essentially
   * a wrapper around [[Debugger.addPendingScalaVirtualMachine)]].
   *
   * @param scalaVirtualMachine The Scala virtual machine to add
   * @return The debugger instance updated with the new pending operations
   */
  def withPending(scalaVirtualMachine: ScalaVirtualMachine): Debugger = {
    addPendingScalaVirtualMachine(scalaVirtualMachine)
    this
  }

  /**
   * Removes a Scala virtual machine used for pending operations. Essentially
   * a wrapper around [[Debugger.removePendingScalaVirtualMachine]].
   *
   * @param scalaVirtualMachineId The id of the Scala virtual machine to remove
   * @return The updated debugger instance
   */
  def withoutPending(scalaVirtualMachineId: String): Debugger = {
    removePendingScalaVirtualMachine(scalaVirtualMachineId)
    this
  }

  /**
   * Adds a new Scala virtual machine whose pending operations will be applied
   * to any new Scala virtual machine resulting from this debugger.
   *
   * @param scalaVirtualMachine The Scala virtual machine to add
   * @return Some Scala virtual machine if added, otherwise None
   */
  def addPendingScalaVirtualMachine(
    scalaVirtualMachine: ScalaVirtualMachine
  ): Option[ScalaVirtualMachine] = {
    val key = scalaVirtualMachine.uniqueId
    val hasKey = pendingScalaVirtualMachines.contains(key)

    pendingScalaVirtualMachines.putIfAbsent(
      scalaVirtualMachine.uniqueId,
      scalaVirtualMachine
    )

    if (!hasKey) Some(scalaVirtualMachine) else None
  }

  /**
   * Removes a Scala virtual machine from the list whose pending operations
   * would be applied to any new Scala virtual machine resulting from this
   * debugger.
   *
   * @param scalaVirtualMachineId The id of the Scala virtual machine to remove
   * @return Some Scala virtual machine if removed, otherwise None
   */
  def removePendingScalaVirtualMachine(
    scalaVirtualMachineId: String
  ): Option[ScalaVirtualMachine] = pendingScalaVirtualMachines.remove(
    scalaVirtualMachineId
  )

  /**
   * Retrieves the collection of Scala virtual machines whose pending operations
   * will be applied to any new Scala virtual machine resulting from this
   * debugger.
   *
   * @return The collection of Scala virtual machines
   */
  def getPendingScalaVirtualMachines: Seq[ScalaVirtualMachine] =
    pendingScalaVirtualMachines.values.toSeq
}
