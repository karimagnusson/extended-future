/*
* Copyright 2021 Kári Magnússon
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

package io.github.karimagnusson

import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Either, Left, Right, Success, Failure}


package object xfuture {

  implicit class XFuture[T](fu: Future[T]) {

    def asOpt(implicit ec: ExecutionContext): Future[Option[T]] = {
      fu.transformWith {
        case Success(v) => Future.successful(Some(v))
        case Failure(_) => Future.successful(None)
      }
    }

    def asEither(implicit ec: ExecutionContext): Future[Either[Throwable, T]] = {
      fu.transformWith {
        case Success(v)  =>  Future.successful(Right(v))
        case Failure(ex) => Future.successful(Left(ex))
      }
    }

    def withDefault(default: T)(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v) => Future.successful(v)
        case Failure(_) => Future.successful(default)
      }
    }

    def tap(fn: T => Future[Any])(implicit ec: ExecutionContext): Future[T] = {
      fu.flatMap(v => fn(v).map(_ => v))
    }

    def tapError(fn: Throwable => Future[Any])(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v) => Future.successful(v)
        case Failure(ex) => fn(ex).flatMap(_ => Future.failed(ex))
      }
    }

    def mapError(fn: Throwable => Throwable)(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v)  => Future.successful(v)
        case Failure(ex) => Future.failed(fn(ex))
      }
    }

    def ensuring(fn: => Future[Any])(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v)  => fn.flatMap(_ => Future.successful(v))
        case Failure(ex) => fn.flatMap(_ => Future.failed(ex))
      }
    }

    def orElse(fn: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v)  => Future.successful(v)
        case Failure(ex) => fn
      }
    }

    def catchAll(fn: Throwable => Future[T])(implicit ec: ExecutionContext): Future[T] = {
      fu.transformWith {
        case Success(v)  => Future.successful(v)
        case Failure(ex) => fn(ex)
      }
    }
  }
}














