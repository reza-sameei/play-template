package xyz.sigmalab.template.play.json

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object PrintInfo {

    def typeOfSymbol(c: Context)(sym: c.Symbol): String = sym match {
        case sym if sym.isConstructor => "Constrcutor"
        case sym if sym.isMethod => "Method"
        case sym if sym.isModule => "Module"
        case sym if sym.isClass => "Class"
        case sym if sym.isType => "Type"
        case sym => s"{ ?, Sym: ${sym.getClass.getName} / Type: ${sym.info.getClass.getName} }"
    }

    def of[T <: AnyVal]: play.api.libs.json.Writes[T] = macro old_impl[T]

    val debug = false

    def old_impl[T <: AnyVal : c.WeakTypeTag](c: Context)  = {
        import c.universe._

        val tag = implicitly[c.WeakTypeTag[T]]
        val tpe = tag.tpe
        val sym = tpe.typeSymbol

        if (debug) println(
            s"""Info :
                 | Tag              : ${tag}
                 | Type             : ${tag.tpe}
                 | Symbol           : ${sym}
                 | Is term          : ${sym.isTerm}
                 | Is type          : ${sym.isType}
                 | Is class         : ${sym.isClass}
                 | Is implicit      : ${sym.isImplicit}
                 | Is abstract      : ${sym.isAbstract}
                 | Is public        : ${sym.isPublic}
                 | Is constructor   : ${sym.isConstructor}
                 | Is java          : ${sym.isJava}
                 | Is static        : ${sym.isStatic}
                 | Is method        : ${sym.isMethod}
                 | Is enum          : ${sym.isJavaEnum}
                 | Is macro         : ${sym.isMacro}
                 | Is module        : ${sym.isModule}
                 | Is module class  : ${sym.isModuleClass}
                 |
                 | ?                : ${tag.getClass.getName}
                 | ?                : ${tag.tpe.getClass.getName}
                 | ?                : ${sym.getClass.getName}
             """.stripMargin
        )

        if (debug) {
            println(f"${"Member"}%20s, ${"Is Public"}%20s, ${"Full Name"}%9s")
            tpe.members.foreach { m =>
                println(f"${m.name}%20s, ${m.isPublic}%20s, ${m.fullName}, ${typeOfSymbol(c)(m)}")
            }
        }

        val klassSym = tpe.baseClasses.head.asClass
        val klassTpe = klassSym.info


        if (debug) println(s"Class: \n${klassSym}, \n${klassTpe}")

        if (!klassSym.isCaseClass) c.abort(c.enclosingPosition, "Should be a case-class")
        if (klassTpe.typeParams.size != 0) c.abort(c.enclosingPosition, "No type-constructor")

        val constructor = klassSym.primaryConstructor

        if (constructor.info.paramLists.size > 1) c.abort(c.enclosingPosition, "No curried constructor!")

        val isAnyVal = klassSym.baseClasses.find( _.fullName == "scala.AnyVal" ).isDefined
        if (!isAnyVal) c.abort(c.enclosingPosition, "Should extends scala.AnyVal")


        val list = constructor.info.paramLists.head

        if (list.size != 1) c.abort(c.enclosingPosition, "Should have just one parameter")

        val only = list.head
        val term = only.asTerm

        if (term.isByNameParam) c.abort(c.enclosingPosition, "The parameter should be by value! no by name!")

        if (debug) println(
            s"""
                 |=== ${term.name} ===
                 | - isByNameParam          : ${term.isByNameParam}
                 | - typeSignature          : ${term.typeSignature}
                 | - fullName               : ${term.fullName}
                 """.stripMargin
        )

        val method = klassTpe.member(term.name)

        if (!method.isMethod) c.abort(c.enclosingPosition, "?")
        if (!method.isPublic) c.abort(c.enclosingPosition, "?")

        if (debug) {
            println("Base Classes:")
            tag.tpe.baseClasses.foreach { i =>
                val klass = i.asClass
                println(f"    | ${klass.name}, ${klass.fullName}, ${klass.isClass}, ${klass.isFinal}, ${klass.isPublic}")
            }
        }

        val writerName = TermName("LongWrites")
        val getterName = TermName(method.name.toString)

        val x = q"""
            new play.api.libs.json.Writes[${tpe}] {
                import play.api.libs.json._
                override def writes(data : ${tpe}) : JsValue =
                    Writes.${writerName}.writes(data.${getterName})
            }"""

        println(showCode(x))

        // reify{ () }

        x

        /*// q"""play.api.libs.json.Writes[${}]"""

        term.typeSignature match {
            case t if t =:= typeOf[Long] =>
                reify {
                    import play.api.libs.json._
                    new Writes[T] {
                        override def writes(o : T) : JsValue = Writes.LongWrites.writes(o.)
                    }
                    ()
                }
        }*/

        /*if (klass.typeParams.size > 0)
            c.abort(c.enclosingPosition, "Needs be a type! not a type-constructor")

        val tpe = klass.toType

        val members =
            tpe.members.filter { m => m.isPublic }.foreach { i =>
                println(f"    | ${i.name}, ${i.fullName}, ${i.isClass}, ${i.isFinal}, ${i.isPublic}")
            }*/





        // println(showRaw())



    }

}
