package br.com.natanaelribeiro.basefeature.objects

import br.com.natanaelribeiro.basefeature.interfaces.CommandProvider
import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.basefeature.viewmodels.SingleLiveEvent

object CommandInjector : CommandProvider {

    override fun getCommand(): SingleLiveEvent<GenericCommand> = SingleLiveEvent()
}