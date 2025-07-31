package com.sweatnote.app

import android.app.Application
import com.sweatnote.app.data.SweatNoteDatabase

class SweatNoteApplication : Application() {
    val database: SweatNoteDatabase by lazy { SweatNoteDatabase.getDatabase(this) }
}