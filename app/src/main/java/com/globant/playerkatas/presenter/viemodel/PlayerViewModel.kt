package com.globant.playerkatas.presenter.viemodel

import androidx.lifecycle.*
import com.globant.playerkatas.domain.usecase.MusicUseCase
import com.globant.playerkatas.presenter.mapper.toModel
import com.globant.playerkatas.presenter.mapper.toState
import com.globant.playerkatas.presenter.state.MusicState
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel  @Inject constructor(
    private val useCase: MusicUseCase
) : ViewModel()  {

    private val _player = MutableLiveData<ExoPlayer?> (null)
    val currentPlayer: LiveData<ExoPlayer?> = _player

    private val _list = MutableStateFlow<MutableList<MusicState>>(mutableListOf())
    private val _listFav = MutableStateFlow<MutableList<MusicState>>(mutableListOf())
    private val _isPlaying = MutableStateFlow(false)
    val list: StateFlow<List<MusicState>> = _list.asStateFlow()
    val listFav: StateFlow<List<MusicState>> = _listFav.asStateFlow()
    val isPlaying : StateFlow<Boolean> = _isPlaying.asStateFlow()

    init {
        getFavMusic()
        getMusicList()
    }

    fun setPlayer(player: ExoPlayer){
        _player.value = player
        _isPlaying.value = player.isPlaying

    }

    fun setNewMusic(song: MusicState) {
        _player.value?.let {
            val mediaItem = MediaItem.Builder()
                .setUri(song.preview)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .build()
                )
                .build()
            if(it.isPlaying){
                it.pause()
            }

            it.addMediaItem(mediaItem)
            it.seekToDefaultPosition(it.mediaItemCount -1)
            it.prepare()
            it.play()
            _isPlaying.value = true
        }
    }

    fun moveSong(next: Boolean){
        _player.value?.let {
            val nextIndex = if(next){
                it.nextMediaItemIndex
            }else {
                it.previousMediaItemIndex
            }

            if(nextIndex != C.INDEX_UNSET){
                if(next)
                    it.seekToNextMediaItem()
                else
                    it.seekToPreviousMediaItem()
                it.prepare()
                it.play()
                _isPlaying.value = true
            }
        }
    }

    fun playControl(play: Boolean){
        _player.value?.let { player ->
            if(play){
                player.play()
            }else{
                player.pause()
            }
            _isPlaying.value = play
        }
    }

    fun manageFav(index: Int) {
        _list.value.apply {
            this[index] = get(index).copy(
                fav = !get(index).fav
            )
            editFav(this[index], this[index].fav)
        }
    }

    private fun editFav(song: MusicState, add: Boolean){
        viewModelScope.launch {
            if(add){
                useCase.saveFavMusic(song.toModel())
            }else{
                useCase.removeFavMusic(song.toModel())
            }

            useCase.getAllFav().collect{ listModel ->
                if (listModel.isNotEmpty())
                    _listFav.value = listModel.map { it.toState() }.toMutableList()
                else
                    _listFav.value = mutableListOf()
            }
        }
    }

    private fun getMusicList(){
        viewModelScope.launch {
            useCase.getMusicList().collect(){ listModel ->
                if (listModel.isNotEmpty())
                    _list.value = listModel.map {
                        it.toState().copy(
                            fav = isSongFav(it.id)
                        )
                    }.toMutableList()
            }

        }
    }

    private fun getFavMusic(){
        viewModelScope.launch {
            useCase.getAllFav().collect(){ listModel ->
                if (listModel.isNotEmpty())
                    _listFav.value = listModel.map { it.toState() }.toMutableList()
            }
        }
    }

    private fun isSongFav(id: String): Boolean{
        return _listFav.value.flatMap { listOf(it.id) }.contains(id)
    }
}