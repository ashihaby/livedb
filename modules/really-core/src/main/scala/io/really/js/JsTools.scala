/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.js

import javax.script.{ ScriptEngine, ScriptContext, Invocable, Bindings }

import _root_.io.really.jshooks.API
import jdk.nashorn.api.scripting.NashornScriptEngineFactory

object JsTools {
  val factory = new NashornScriptEngineFactory

  def injectSDK(b: Bindings): Unit = {
    b.put("cancel", API.cancel)
    b.put("print", API.print)
  }

  def getBindings(engine: ScriptEngine): Bindings = engine.getContext.getBindings(ScriptContext.ENGINE_SCOPE)
  def newEngine(): ScriptEngine = factory.getScriptEngine("-strict", "--no-java", "--no-syntax-extensions")

  def newEngineWithSDK(): ScriptEngine = {
    val engine = newEngine()
    injectSDK(getBindings(engine))
    engine
  }
}