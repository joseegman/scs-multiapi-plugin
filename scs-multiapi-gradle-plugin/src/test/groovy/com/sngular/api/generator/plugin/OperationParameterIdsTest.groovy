/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.sngular.api.generator.plugin

import com.sngular.api.generator.plugin.model.OperationParameter
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class OperationParameterIdsTest {

  @Test
  void idsConfiguredAsSingleStringIsPreserved() {
    def parameter = new OperationParameter()
    parameter.with {
      ids = "orderCreated"
    }

    def result = AsyncApiTask.toOperationParameterObject(parameter)

    assertEquals("orderCreated", result.ids)
    assertEquals(["orderCreated"], result.operationIds)
  }

  @Test
  void idsConfiguredAsListAreJoined() {
    def parameter = new OperationParameter()
    parameter.with {
      ids = ["orderCreated", "orderConfirmed"]
    }

    def result = AsyncApiTask.toOperationParameterObject(parameter)

    assertEquals("orderCreated,orderConfirmed", result.ids)
    assertEquals(["orderCreated", "orderConfirmed"], result.operationIds)
  }

  @Test
  void idsConfiguredAsSingleElementListResolvesMatchingOperationId() {
    def parameter = new OperationParameter()
    parameter.with {
      ids = ["orderCreated"]
    }

    def result = AsyncApiTask.toOperationParameterObject(parameter)

    assertTrue(result.operationIds.contains("orderCreated"))
  }
}