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

package uk.gov.hmrc.integrationcatalogueadminfrontend.controllers


import play.api.Logging
import play.api.libs.Files
import play.api.mvc._
import uk.gov.hmrc.integrationcatalogueadminfrontend.config.AppConfig
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain._
import uk.gov.hmrc.integrationcatalogueadminfrontend.controllers.actionbuilders._
import uk.gov.hmrc.integrationcatalogueadminfrontend.domain.connectors.{PublishRequest, PublishResult}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import uk.gov.hmrc.integrationcatalogueadminfrontend.services.PublishService

@Singleton
class PublishController @Inject()(
  appConfig: AppConfig,
  mcc: MessagesControllerComponents,
  publishService: PublishService,
  validatePlatformHeaderAction: ValidatePlatformHeaderAction,
  validateSpecificationTypeHeaderAction: ValidateSpecificationTypeHeaderAction,
  validatePublisherRefHeaderAction: ValidatePublisherRefHeaderAction,
  playBodyParsers: PlayBodyParsers)
  (implicit ec: ExecutionContext)
    extends FrontendController(mcc) with Logging{

  implicit val config: AppConfig = appConfig


    def publishApi(): Action[MultipartFormData[Files.TemporaryFile]] = 
    
    (Action andThen 
    validatePlatformHeaderAction andThen
    validatePublisherRefHeaderAction andThen
    validateSpecificationTypeHeaderAction).async(playBodyParsers.multipartFormData) { implicit request =>

      (request.headers.get(HeaderKeys.platformKey), 
      request.headers.get(HeaderKeys.specificationTypeKey),
       request.headers.get(HeaderKeys.publisherRefKey)) match {
        case (Some(platformType), Some(specificationType), Some(publisherRef)) => {request.body.file("selectedFile").map { selectedFile =>
                    val convertedPlatformType = PlatformType.withNameInsensitive(platformType)
                    val convertedSpecType = SpecificationType.withNameInsensitive(specificationType)
                    val fileContents = Source.fromFile(selectedFile.ref.path.toFile).getLines.mkString("\r\n")

                    for {
                      response <- publishService.publishApi(publisherRef, convertedPlatformType, selectedFile.filename, convertedSpecType, fileContents)
                      result <- handlePublishResult(response)
                    } yield result
                  }.getOrElse {
                    Future.successful(BadRequest("SOME ERROR"))
                  }
       }            
        case _ => Future.successful(BadRequest("SOME ERROR"))
      }
      
    }

  def handlePublishResult(result: PublishResult) ={
    if(result.isSuccess){
      Future.successful(Ok("Publish Successful"))
    }else{
      logger.info(result.errors.head.message)
      Future.successful(Ok("Publish Failed"))
    }
  }

}
