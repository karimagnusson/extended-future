# Extended future

##### Import:
```scala
import io.github.karimagnusson.xfuture._
``` 

##### Methods:
```scala
def asOpt(implicit ec: ExecutionContext): Future[Option[T]]
def asEither(implicit ec: ExecutionContext): Future[Either[Throwable, T]]
def withDefault(default: T)(implicit ec: ExecutionContext): Future[T]
def tap(fn: T => Future[Any])(implicit ec: ExecutionContext): Future[T]
def tapError(fn: Throwable => Future[Any])(implicit ec: ExecutionContext): Future[T]
def mapError(fn: Throwable => Throwable)(implicit ec: ExecutionContext): Future[T]
def ensuring(fn: => Future[Any])(implicit ec: ExecutionContext): Future[T]
def orElse(fn: => Future[T])(implicit ec: ExecutionContext): Future[T]
def catchAll(fn: Throwable => Future[T])(implicit ec: ExecutionContext): Future[T]
``` 