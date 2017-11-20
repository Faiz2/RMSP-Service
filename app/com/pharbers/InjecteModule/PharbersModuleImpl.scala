package com.pharbers.InjecteModule

import javax.inject.Singleton

import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.tokenImpl.TokenImplTrait

@Singleton
class PharbersInjectDBManager extends dbInstanceManager

@Singleton
class PharbersInjectTokenModule extends TokenImplTrait {
	privateKey
	publicKey
}
