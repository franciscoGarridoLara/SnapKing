package com.example.snapking.BaseDatos
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

class LiveDataSala<WraperSala> private constructor(
    private val reference: DatabaseReference,
    private val type: Class<WraperSala>?,
    private val typeIndicator: GenericTypeIndicator<WraperSala>?,
): LiveData<WraperSala>() {
    constructor(reference: DatabaseReference, type: Class<WraperSala>): this(reference, type, null)
    constructor(reference: DatabaseReference, type: GenericTypeIndicator<WraperSala>): this(reference, null, type)

    private val listener = object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = if (type != null)
                snapshot.getValue(type)
            else
                snapshot.getValue(typeIndicator!!)
        }
        override fun onCancelled(error: DatabaseError) {
            value = null
        }
    }

    override fun onActive() {
        super.onActive()
        reference.addValueEventListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        reference.removeEventListener(listener)
    }
}