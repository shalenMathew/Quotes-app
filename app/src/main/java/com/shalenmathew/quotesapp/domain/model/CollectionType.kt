package com.shalenmathew.quotesapp.domain.model

sealed class CollectionType {
    abstract val id: Int

    object Favorites : CollectionType() {
        override val id: Int = ID_FAV
    }

    object Custom : CollectionType() {
        override val id: Int = ID_CUSTOM
    }

    data class UserDefined(override val id: Int) : CollectionType()

    companion object {
        const val ID_FAV = -1
        const val ID_CUSTOM = -2

        fun fromId(id: Int): CollectionType {
            return when (id) {
                ID_FAV -> Favorites
                ID_CUSTOM -> Custom
                else -> UserDefined(id)
            }
        }
    }
}
