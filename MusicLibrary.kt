package com.vibepro.app.media
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

data class Track(val id: Long, val title: String, val artist: String, val album: String, val durationMs: Long, val contentUri: Uri)

object MusicLibrary {
    fun queryDevice(context: Context): List<Track> {
        val audio = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val proj = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION)
        val sel = "${MediaStore.Audio.Media.IS_MUSIC}=1"
        val sort = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        val res = mutableListOf<Track>()
        context.contentResolver.query(audio, proj, sel, null, sort)?.use { c ->
            val idxId = c.getColumnIndexOrThrow(proj[0])
            val idxTitle = c.getColumnIndexOrThrow(proj[1])
            val idxArtist = c.getColumnIndexOrThrow(proj[2])
            val idxAlbum = c.getColumnIndexOrThrow(proj[3])
            val idxDur = c.getColumnIndexOrThrow(proj[4])
            while (c.moveToNext()) {
                val id = c.getLong(idxId)
                res += Track(id, c.getString(idxTitle) ?: "Nieznany", c.getString(idxArtist) ?: "—", c.getString(idxAlbum) ?: "—", c.getLong(idxDur), ContentUris.withAppendedId(audio, id))
            }
        }
        return res
    }
}
