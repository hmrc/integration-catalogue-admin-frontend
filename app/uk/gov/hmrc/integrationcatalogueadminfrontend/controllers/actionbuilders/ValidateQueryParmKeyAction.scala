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

package uk.gov.hmrc.integrationcatalogueadminfrontend.controllers.actionbuilders


import javax.inject.{Inject, Singleton}
import play.api.mvc.Results._
import play.api.mvc.{ActionFilter, Request, Result}
import scala.concurrent.{ExecutionContext, Future}

import play.api.Logging
import _root_.uk.gov.hmrc.http.HttpErrorFunctions
import uk.gov.hmrc.integrationcatalogue.models._
import uk.gov.hmrc.integrationcatalogue.models.JsonFormatters._
import play.api.libs.json.Json


@Singleton
class ValidateQueryParamKeyAction @Inject()()(implicit ec: ExecutionContext)
  extends ActionFilter[Request] with HttpErrorFunctions with Logging {
  override def executionContext: ExecutionContext = ec

  override protected def filter[A](request: Request[A]): Future[Option[Result]] = {
    val validKeys = List("platformFilter", "searchTerm")
    val queryParamKeys = request.queryString.keys

    if (!queryParamKeys.forall(validKeys.contains(_))) {
        logger.info("Invalid query parameter key provided. It is case sensitive")
        Future.successful(Some(BadRequest(Json.toJson(ErrorResponse(List(ErrorResponseMessage( "Invalid query parameter key provided. It is case sensitive")))))))
    }
    else Future.successful(None)
  }
}