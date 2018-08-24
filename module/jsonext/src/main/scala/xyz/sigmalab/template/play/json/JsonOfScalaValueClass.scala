package xyz.sigmalab.template.play.json

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context
import play.api.libs.json._

object JsonOfScalaValueClass {

    private lazy val debugEnabled: Boolean =
        Option(
            System.getProperty("xyz.sigmalab.play_jsonext_macro.debug")
        ).map{_.toLowerCase()} match {
            case None | Some("false") | Some("no") => false
            case Some("true") | Some("yes") => true
        }

    @deprecated("Use format[T] instead")
    def writer[T <: AnyVal]: Writes[T] = macro writer_impl_v1[T]

    def writer_impl_v1[T <: AnyVal : c.WeakTypeTag](c: Context) = {

        def debug(msg: String) = if (debugEnabled) c.info(c.enclosingPosition, msg, false)

        import c.universe._
        val tpe = implicitly[WeakTypeTag[T]].tpe
        val typeSym = tpe.typeSymbol // Class & Type
        val klassSym = typeSym.asClass

        if (!klassSym.isCaseClass)
            c.abort(c.enclosingPosition, s"Needs to be a case class: ${tpe}")

        if (klassSym.typeParams.size > 0)
            c.abort(c.enclosingPosition, s"Needs to be regular type; not a type-constructor: ${tpe}")

        if (klassSym.baseClasses.find(_.fullName == "scala.AnyVal").isEmpty)
            c.abort(c.enclosingPosition, s"Needs to be a sub-type of scala.AnyVal: ${tpe}")

        val constructor = klassSym.primaryConstructor

        if (
            constructor.info.paramLists.size != 1
                || constructor.info.paramLists.head.size != 1
        )
            c.abort(c.enclosingPosition, s"Needs to have a single params list, with a single value: ${tpe}")

        val param =
            constructor.info.paramLists.head.head

        val relatedMethod =
            tpe.member(param.name)

        if (
            relatedMethod == NoSymbol
                || !relatedMethod.isMethod
                || !relatedMethod.isPublic
                || relatedMethod.asMethod.paramLists.size != 0
        ) c.abort(c.enclosingPosition, s"There is something wrong about your case-class-anyval type: ${tpe}!")


        // val writer = q"play.api.libs.json.Writes.LongWrites"

        val writer = param.typeSignature match {
            case t if t =:= typeOf[Byte] => q"play.api.libs.json.Writes.ByteWrites"
            case t if t =:= typeOf[Short] => q"play.api.libs.json.Writes.ShortWrites"
            case t if t =:= typeOf[Int] => q"play.api.libs.json.Writes.IntWrites"
            case t if t =:= typeOf[Long] => q"play.api.libs.json.Writes.LongWrites"
            case t if t =:= typeOf[Float] => q"play.api.libs.json.Writes.FloatWrites"
            case t if t =:= typeOf[Double] => q"play.api.libs.json.Writes.DoubleWrites"
            case t if t =:= typeOf[BigDecimal] => q"play.api.libs.json.Writes.BigDecimalWrites"
            case t if t =:= typeOf[Boolean] => q"play.api.libs.json.Writes.BooleanWrites"
            case t if t =:= typeOf[String] => q"play.api.libs.json.Writes.StringWrites"
            case t => c.abort(c.enclosingPosition, s"Unsupported inner-value-type of: ${param} / ${param.typeSignature} / ${tpe}")
        }

        val getter = TermName(relatedMethod.name.toString)

        q"""
            new play.api.libs.json.Writes[${tpe}] {
                import play.api.libs.json._
                override def writes(data : ${tpe}) : JsValue =
                    ${writer}.writes(data.${getter})
            }"""
    }

    @deprecated("Use format[T] instead")
    def reader[T <: AnyVal]: Reads[T] = macro reader_impl_v1[T]

    def reader_impl_v1[T <: AnyVal : c.WeakTypeTag](c: Context) = {

        def debug(msg: String) = if (debugEnabled) c.info(c.enclosingPosition, msg, false)

        import c.universe._
        val tpe = implicitly[WeakTypeTag[T]].tpe
        val typeSym = tpe.typeSymbol // Class & Type
        val klassSym = typeSym.asClass

        if (!klassSym.isCaseClass)
            c.abort(c.enclosingPosition, s"Needs to be a case class: ${tpe}")

        if (klassSym.typeParams.size > 0)
            c.abort(c.enclosingPosition, s"Needs to be regular type; not a type-constructor: ${tpe}")

        if (klassSym.baseClasses.find(_.fullName == "scala.AnyVal").isEmpty)
            c.abort(c.enclosingPosition, s"Needs to be a sub-type of scala.AnyVal: ${tpe}")

        val constructor = klassSym.primaryConstructor

        if (
            constructor.info.paramLists.size != 1
                || constructor.info.paramLists.head.size != 1
        ) c.abort(c.enclosingPosition, s"Needs to have a single params list, with a single value: ${tpe}")

        val param =
            constructor.info.paramLists.head.head

        val relatedMethod =
            tpe.member(param.name)

        if (
            relatedMethod == NoSymbol
                || !relatedMethod.isMethod
                || !relatedMethod.isPublic
                || relatedMethod.asMethod.paramLists.size != 0
        ) c.abort(c.enclosingPosition, s"There is something wrong about your case-class-anyval type: ${tpe}!")

        // val reader = q"play.api.libs.json.Reads.LongReads"

        val reader = param.typeSignature match {
            case t if t =:= typeOf[Byte] => q"play.api.libs.json.Reads.ByteReads"
            case t if t =:= typeOf[Short] => q"play.api.libs.json.Reads.ShortReads"
            case t if t =:= typeOf[Int] => q"play.api.libs.json.Reads.IntReads"
            case t if t =:= typeOf[Long] => q"play.api.libs.json.Reads.LongReads"
            case t if t =:= typeOf[Float] => q"play.api.libs.json.Reads.FloatReads"
            case t if t =:= typeOf[Double] => q"play.api.libs.json.Reads.DoubleReads"
            case t if t =:= typeOf[BigDecimal] => q"play.api.libs.json.Reads.BigDecimalReads"
            case t if t =:= typeOf[Boolean] => q"play.api.libs.json.Reads.BooleanReads"
            case t if t =:= typeOf[String] => q"play.api.libs.json.Reads.StringReads"
            case t => c.abort(c.enclosingPosition, s"Unsupported inner-value-type of: ${param} / ${param.typeSignature} / ${tpe}")
        }

        // override def reads(json : JsValue) : JsResult[String] = ???

        q"""
            new play.api.libs.json.Reads[${tpe}] {
                import play.api.libs.json._
                override def reads(json: JsValue) : JsResult[${tpe}] =
                    ${reader}.reads(json) match {
                        case JsSuccess(value, path) => JsSuccess(new ${tpe}(value))
                        case JsError(error) => JsError(error)
                    }
            }"""

    }

    def format[T <: AnyVal] : Format[T] = macro format_impl_v1[T]

    def format_impl_v1[T <: AnyVal : c.WeakTypeTag](c: Context) = {

        def debug(msg: String) = if (debugEnabled) c.info(c.enclosingPosition, msg, false)

        import c.universe._
        val libs = q"_root_.play.api.libs"
        val json = q"$libs.json"
        val atpe = implicitly[WeakTypeTag[T]].tpe.dealias
        val ctor = atpe.decl(c.universe.termNames.CONSTRUCTOR).asMethod

        ctor.paramLists match {

            case List(term: TermSymbol) :: Nil =>

                val reader = q"""implicitly[$json.Reads[${term.info}]].map { v => new ${atpe}(v) }"""

                val writer = q"""{
                        val fn = implicitly[_root_.play.api.libs.functional.ContravariantFunctor[$json.Writes]]
                        val w = implicitly[$json.Writes[${term.info}]]
                        fn.contramap[${term.info}, ${atpe}](w, _.${term.name.toTermName})
                    }"""

                val result = q"""$json.Format[$atpe]($reader, $writer)"""

                debug(showCode(result))

                result

            case _ =>
                c.abort(
                    c.enclosingPosition,
                    s"Invalid ValueClass '${atpe}': single value expected"
                )
        }

    }

    implicit def implicits[T <: AnyVal] : Format[T] = macro implicits_impl_v1[T]

    def implicits_impl_v1[T <: AnyVal : c.WeakTypeTag](c: Context) = {

        def debug(msg: String) = if (debugEnabled) c.info(c.enclosingPosition, msg, false)

        import c.universe._
        val libs = q"_root_.play.api.libs"
        val json = q"$libs.json"
        val tpe = implicitly[WeakTypeTag[T]].tpe.dealias
        val ctor = tpe.decl(c.universe.termNames.CONSTRUCTOR).asMethod

        ctor.paramLists match {

            case List(term: TermSymbol) :: Nil =>

                val reader = term.typeSignature match {
                    case t if t =:= typeOf[Byte] => q"play.api.libs.json.Reads.ByteReads"
                    case t if t =:= typeOf[Short] => q"play.api.libs.json.Reads.ShortReads"
                    case t if t =:= typeOf[Int] => q"play.api.libs.json.Reads.IntReads"
                    case t if t =:= typeOf[Long] => q"play.api.libs.json.Reads.LongReads"
                    case t if t =:= typeOf[Float] => q"play.api.libs.json.Reads.FloatReads"
                    case t if t =:= typeOf[Double] => q"play.api.libs.json.Reads.DoubleReads"
                    case t if t =:= typeOf[BigDecimal] => q"play.api.libs.json.Reads.BigDecimalReads"
                    case t if t =:= typeOf[Boolean] => q"play.api.libs.json.Reads.BooleanReads"
                    case t if t =:= typeOf[String] => q"play.api.libs.json.Reads.StringReads"
                    case t => c.abort(c.enclosingPosition, s"Unsupported inner-value-type of: ${term} / ${term.typeSignature} / ${tpe}")
                }

                val writer = term.typeSignature match {
                    case t if t =:= typeOf[Byte] => q"play.api.libs.json.Writes.ByteWrites"
                    case t if t =:= typeOf[Short] => q"play.api.libs.json.Writes.ShortWrites"
                    case t if t =:= typeOf[Int] => q"play.api.libs.json.Writes.IntWrites"
                    case t if t =:= typeOf[Long] => q"play.api.libs.json.Writes.LongWrites"
                    case t if t =:= typeOf[Float] => q"play.api.libs.json.Writes.FloatWrites"
                    case t if t =:= typeOf[Double] => q"play.api.libs.json.Writes.DoubleWrites"
                    case t if t =:= typeOf[BigDecimal] => q"play.api.libs.json.Writes.BigDecimalWrites"
                    case t if t =:= typeOf[Boolean] => q"play.api.libs.json.Writes.BooleanWrites"
                    case t if t =:= typeOf[String] => q"play.api.libs.json.Writes.StringWrites"
                    case t => c.abort(c.enclosingPosition, s"Unsupported inner-value-type of: ${term} / ${term.typeSignature} / ${tpe}")
                }

                val getter = term.name.toTermName

                q"""
                    new play.api.libs.json.Format[${tpe}] {
                        import play.api.libs.json._

                        override def reads(json: JsValue) : JsResult[${tpe}] =
                            ${reader}.reads(json) match {
                                case JsSuccess(value, path) => JsSuccess(new ${tpe}(value))
                                case JsError(error) => JsError(error)
                            }

                        override def writes(data : ${tpe}) : JsValue =
                            ${writer}.writes(data.${getter})

                    }"""

            case _ =>
                c.abort(
                    c.enclosingPosition,
                    s"Invalid ValueClass '${tpe}': single value expected"
                )
        }

    }
}
