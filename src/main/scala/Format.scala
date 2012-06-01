package jsonz

trait Format[T] extends Writes[T] with Reads[T]
