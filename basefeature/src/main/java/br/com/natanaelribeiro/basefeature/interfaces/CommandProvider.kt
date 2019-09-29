package br.com.natanaelribeiro.basefeature.interfaces

import br.com.natanaelribeiro.basefeature.models.GenericCommand
import br.com.natanaelribeiro.basefeature.viewmodels.SingleLiveEvent

interface CommandProvider {

    fun getCommand(): SingleLiveEvent<GenericCommand>
}