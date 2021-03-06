package org.scaladebugger.api.dsl.threads

import com.sun.jdi.event.ThreadStartEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.threads.ThreadStartProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param threadStartProfile The profile to wrap
 */
class ThreadStartDSLWrapper private[dsl] (
  private val threadStartProfile: ThreadStartProfile
) {
  /** Represents a ThreadStart event and any associated data. */
  type ThreadStartEventAndData = (ThreadStartEvent, Seq[JDIEventDataResult])

  /** @see ThreadStartProfile#tryGetOrCreateThreadStartRequest(JDIArgument*) */
  def onThreadStart(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadStartEvent]] =
    threadStartProfile.tryGetOrCreateThreadStartRequest(extraArguments: _*)

  /** @see ThreadStartProfile#getOrCreateThreadStartRequest(JDIArgument*) */
  def onUnsafeThreadStart(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ThreadStartEvent] =
    threadStartProfile.getOrCreateThreadStartRequest(extraArguments: _*)

  /** @see ThreadStartProfile#getOrCreateThreadStartRequestWithData(JDIArgument*) */
  def onUnsafeThreadStartWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ThreadStartEventAndData] =
    threadStartProfile.getOrCreateThreadStartRequestWithData(
      extraArguments: _*
    )

  /** @see ThreadStartProfile#tryGetOrCreateThreadStartRequestWithData(JDIArgument*) */
  def onThreadStartWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadStartEventAndData]] =
    threadStartProfile.tryGetOrCreateThreadStartRequestWithData(
      extraArguments: _*
    )
}
