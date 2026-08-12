/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.sngular.api.generator.plugin.asyncapi.model;

import com.sngular.api.generator.plugin.common.tools.StringCaseUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelObject {

  private String operationId;

  private String channelName;

  public String getConstantName() {
    return StringCaseUtils.titleToSnakeCase(operationId);
  }

  public String getChannelValue() {
    return channelName.replaceAll("\\{[^}]*\\}", "(.*)");
  }
}