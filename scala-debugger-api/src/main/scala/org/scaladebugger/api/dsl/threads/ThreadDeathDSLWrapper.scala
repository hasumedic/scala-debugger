package org.scaladebugger.api.dsl.threads

import com.sun.jdi.event.ThreadDeathEvent
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.traits.threads.ThreadDeathProfile

import scala.util.Try

/**
 * Wraps a profile, providing DSL-like syntax.
 *
 * @param threadDeathProfile The profile to wrap
 */
class ThreadDeathDSLWrapper private[dsl] (
  private val threadDeathProfile: ThreadDeathProfile
) {
  /** Represents a ThreadDeath event and any associated data. */
  type ThreadDeathEventAndData = (ThreadDeathEvent, Seq[JDIEventDataResult])

  /** @see ThreadDeathProfile#tryGetOrCreateThreadDeathRequest(JDIArgument*) */
  def onThreadDeath(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadDeathEvent]] =
    threadDeathProfile.tryGetOrCreateThreadDeathRequest(extraArguments: _*)

  /** @see ThreadDeathProfile#getOrCreateThreadDeathRequest(JDIArgument*) */
  def onUnsafeThreadDeath(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ThreadDeathEvent] =
    threadDeathProfile.getOrCreateThreadDeathRequest(extraArguments: _*)

  /** @see ThreadDeathProfile#getOrCreateThreadDeathRequestWithData(JDIArgument*) */
  def onUnsafeThreadDeathWithData(
    extraArguments: JDIArgument*
  ): IdentityPipeline[ThreadDeathEventAndData] =
    threadDeathProfile.getOrCreateThreadDeathRequestWithData(
      extraArguments: _*
    )

  /** @see ThreadDeathProfile#tryGetOrCreateThreadDeathRequestWithData(JDIArgument*) */
  def onThreadDeathWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ThreadDeathEventAndData]] =
    threadDeathProfile.tryGetOrCreateThreadDeathRequestWithData(
      extraArguments: _*
    )
}
