package br.com.natanaelribeiro.basefeature.di

import br.com.natanaelribeiro.basefeature.interfaces.CommandProvider
import br.com.natanaelribeiro.basefeature.objects.CommandInjector
import org.koin.core.module.Module
import org.koin.dsl.module

val commandInjectorModule: Module = module(override = true) {

    single<CommandProvider> { CommandInjector }
}