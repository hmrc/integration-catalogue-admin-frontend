/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.integrationcatalogueadminfrontend.domain

import play.api.libs.json.Json
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.PublishResponse
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.{ErrorResponse, ErrorResponseMessage}
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.connectors.PublishRequest
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.connectors.PublishError
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.connectors.PublishDetails
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.connectors.PublishResult


object JsonFormatters {
  implicit val publishRequestFormat = Json.format[PublishRequest]
  implicit val publishErrorFormat = Json.format[PublishError]
  implicit val publishDetailsFormat = Json.format[PublishDetails]
  implicit val publishResultFormat = Json.format[PublishResult]

  implicit val publishResponseFormat = Json.format[PublishResponse]
  implicit val errorResponseMessageFormat = Json.format[ErrorResponseMessage]
  implicit val errorResponseFormat = Json.format[ErrorResponse]
}
