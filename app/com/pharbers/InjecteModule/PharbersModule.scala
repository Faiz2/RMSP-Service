package com.pharbers.InjecteModule

import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.tokenImpl.TokenImplTrait
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class PharbersModule extends Module {
	override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
		Seq(
			bind[dbInstanceManager].to[PharbersInjectDBManager],
			bind[TokenImplTrait].to[PharbersInjectTokenModule]
		)
	}
}
