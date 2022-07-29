package com.sk.shotsapp


class BackNavElement private constructor(
    private var child: BackNavElement? = null,
    private val handler: () -> Result
) {

    enum class Result {
        CANNOT_GO_BACK,
        CAN_GO_BACK
    }

    /**
     * Adds element to the END of the chain.
     */
//    fun add(element: BackNavElement?) {
//        this.child?.let {
//            it.add(element)
//        } ?: run {
//            this.child = element
//        }
//    }

    fun tryGoBack(): Result {
        if (child?.tryGoBack() == Result.CANNOT_GO_BACK) {
            return Result.CANNOT_GO_BACK
        }
        return handler()
    }

    companion object {

        fun default(child: BackNavElement? = null, handler: () -> Unit) =
            BackNavElement(
                child = child,
                handler = {
                    handler()
                    BackNavElement.Result.CAN_GO_BACK
                })

        fun needsProcessing(child: BackNavElement? = null, handler: () -> Result) =
            BackNavElement(child = child, handler = handler)
    }
}
