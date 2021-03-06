package org.scaladebugger.api.lowlevel.classes
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.DummyOperationException

class DummyClassUnloadManagerSpec extends FunSpec with Matchers with MockFactory
  with ParallelTestExecution with org.scalamock.matchers.Matchers
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val classUnloadManager = new DummyClassUnloadManager

  describe("DummyClassUnloadManager") {
    describe("#classUnloadRequestList") {
      it("should return an empty list") {
        classUnloadManager.classUnloadRequestList should be (empty)
      }
    }

    describe("#createClassUnloadRequestWithId") {
      it("should return a failure of dummy operation") {
        val result = classUnloadManager.createClassUnloadRequestWithId(
          TestRequestId
        )

        result.isFailure should be (true)
        result.failed.get shouldBe a [DummyOperationException]
      }
    }

    describe("#hasClassUnloadRequest") {
      it("should return false") {
        val expected = false

        val actual = classUnloadManager.hasClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }
    }

    describe("#getClassUnloadRequest") {
      it("should return None") {
        val expected = None

        val actual = classUnloadManager.getClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }
    }

    describe("#getClassUnloadRequestInfo") {
      it("should return None") {
        val expected = None

        val actual = classUnloadManager.getClassUnloadRequestInfo(
          TestRequestId
        )

        actual should be (expected)
      }
    }

    describe("#removeClassUnloadRequest") {
      it("should return false") {
        val expected = false

        val actual = classUnloadManager.removeClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }
    }
  }
}
