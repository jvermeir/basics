package nl.vermeir.kotlin.repository

class CustomerAlreadyExistsException(message: String?) : RuntimeException(message) {
    override val message: String
        get() = super.message!!
}