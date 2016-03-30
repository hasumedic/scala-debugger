package org.scaladebugger.api.profiles.swappable.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import com.sun.jdi.event.StepEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.steps.StepRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.steps.StepProfile

import scala.concurrent.Future
import scala.util.Try

/**
 * Represents a swappable profile for step events that redirects the
 * invocation to another profile.
 */
trait SwappableStepProfile extends StepProfile {
  this: SwappableDebugProfileManagement =>

  override def stepIntoLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepIntoLineWithData(threadReference, extraArguments: _*)
  }

  override def stepOverLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepOverLineWithData(threadReference, extraArguments: _*)
  }

  override def stepOutLineWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepOutLineWithData(threadReference, extraArguments: _*)
  }

  override def stepIntoMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepIntoMinWithData(threadReference, extraArguments: _*)
  }

  override def stepOverMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepOverMinWithData(threadReference, extraArguments: _*)
  }

  override def stepOutMinWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Future[StepEventAndData] = {
    withCurrentProfile.stepOutMinWithData(threadReference, extraArguments: _*)
  }

  override def tryCreateStepListenerWithData(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = {
    withCurrentProfile.tryCreateStepListenerWithData(threadReference, extraArguments: _*)
  }

  override def isStepRequestPending(
    threadReference: ThreadReference
  ): Boolean = {
    withCurrentProfile.isStepRequestPending(threadReference)
  }

  override def isStepRequestWithArgsPending(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Boolean = {
    withCurrentProfile.isStepRequestWithArgsPending(
      threadReference,
      extraArguments: _*
    )
  }

  override def removeStepRequests(
    threadReference: ThreadReference
  ): Seq[StepRequestInfo] = {
    withCurrentProfile.removeStepRequests(threadReference)
  }

  override def removeStepRequestWithArgs(
    threadReference: ThreadReference,
    extraArguments: JDIArgument*
  ): Option[StepRequestInfo] = {
    withCurrentProfile.removeStepRequestWithArgs(
      threadReference,
      extraArguments: _*
    )
  }

  override def removeAllStepRequests(): Seq[StepRequestInfo] = {
    withCurrentProfile.removeAllStepRequests()
  }

  override def stepRequests: Seq[StepRequestInfo] = {
    withCurrentProfile.stepRequests
  }
}
