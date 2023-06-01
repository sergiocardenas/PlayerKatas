package com.globant.playerkatas.presenter.mapper

import com.globant.playerkatas.domain.model.MusicModel
import com.globant.playerkatas.presenter.state.MusicState

fun MusicModel.toState() = MusicState(
    id = id,
    title = title,
    artist = artist,
    preview = preview,
    picture = picture
)

fun MusicState.toModel() = MusicModel(
    id = id,
    title = title,
    artist = artist,
    preview = preview,
    picture = picture
)