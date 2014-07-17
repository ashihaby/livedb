package io.really.js

import javax.script.{ScriptEngine, ScriptContext, Invocable, Bindings}

import io.really.jshooks.API
import jdk.nashorn.api.scripting.NashornScriptEngineFactory

object JsTools {
  val factory = new NashornScriptEngineFactory

  def injectSDK(b: Bindings) = {
    b.put("cancel", API.cancel)
    b.put("print", API.print)
  }

  def getBindings(engine: ScriptEngine) = engine.getContext.getBindings(ScriptContext.ENGINE_SCOPE)
  def newEngine() = factory.getScriptEngine(Array("-strict", "--no-java", "--no-syntax-extensions"))

  def newEngineWithSDK() = {
    val engine = newEngine()
    injectSDK(getBindings(engine))
    engine
  }
}