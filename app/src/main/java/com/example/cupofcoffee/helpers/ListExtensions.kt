package com.example.cupofcoffee.helpers

import com.example.cupofcoffee.base.ViewState


/**
 * Returns element at index 1, this is useful for testing list of viewstates since
 * view state at index 0 will be always an InitState and is irrelevant
 */
fun <E : ViewState> List<E>.start(): E {
    return this[1]
}
