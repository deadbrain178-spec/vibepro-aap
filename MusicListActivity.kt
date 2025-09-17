package com.vibepro.app.ui
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.vibepro.app.R
import com.vibepro.app.media.MusicLibrary
import com.vibepro.app.media.Track
import com.vibepro.app.player.PlayerService

class MusicListActivity: AppCompatActivity() {
    private lateinit var adapter: TracksAdapter
    private val pickFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? -> uri?.let { startService(PlayerService.playIntent(this, arrayListOf(it), true)) } }
    private val requestPerm = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> if (granted) load() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_music_list)
        val rv = findViewById<RecyclerView>(R.id.recycler)
        adapter = TracksAdapter { track -> startService(PlayerService.playIntent(this, arrayListOf(track.contentUri), true)) }
        rv.adapter = adapter
        findViewById<Button>(R.id.btn_pick).setOnClickListener { pickFile.launch(arrayOf("audio/*")) }
        findViewById<EditText>(R.id.search).addTextChangedListener(object: TextWatcher { override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {} override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { adapter.filter(s?.toString().orEmpty()) } override fun afterTextChanged(s: Editable?) {} })
        if (Build.VERSION.SDK_INT >= 33) requestPerm.launch(Manifest.permission.READ_MEDIA_AUDIO) else requestPerm.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private fun load() { adapter.submit(MusicLibrary.queryDevice(this)) }
}
private class TracksAdapter(val onClick:(Track)->Unit) : RecyclerView.Adapter<TracksAdapter.VH>() {
    private val all = mutableListOf<Track>(); private val shown = mutableListOf<Track>()
    fun submit(list: List<Track>) { all.setAll(list); shown.setAll(list); notifyDataSetChanged() }
    fun filter(q:String){ shown.setAll(all.filter { it.title.contains(q,true) || it.artist.contains(q,true) }); notifyDataSetChanged() }
    override fun onCreateViewHolder(p: ViewGroup, viewType: Int) = VH(LayoutInflater.from(p.context).inflate(android.R.layout.simple_list_item_2, p, false) as android.widget.TwoLineListItem, onClick)
    override fun onBindViewHolder(h: VH, pos:Int) = h.bind(shown[pos]); override fun getItemCount() = shown.size
    inner class VH(private val row: android.widget.TwoLineListItem, val onClick:(Track)->Unit) : RecyclerView.ViewHolder(row) {
        private var item: Track? = null; init { row.setOnClickListener { item?.let(onClick) } }
        fun bind(t: Track){ item=t; row.text1.text=t.title; row.text2.text="${t.artist} • ${(t.durationMs/1000)}s" }
    }
    private fun <T> MutableList<T>.setAll(n: List<T>) { clear(); addAll(n) }
}
