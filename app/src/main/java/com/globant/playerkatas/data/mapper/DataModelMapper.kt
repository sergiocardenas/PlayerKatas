package com.globant.playerkatas.data.mapper

import com.globant.playerkatas.data.entity.MusicEntity
import com.globant.playerkatas.data.responde.MusicResponse
import com.globant.playerkatas.domain.model.MusicModel

fun MusicResponse.toModel() = MusicModel(
    id = id,
    title = title,
    artist = artist.name,
    preview = preview,
    picture = artist.picture
)

fun MusicEntity.toModel() = MusicModel(
    id = id,
    title = title,
    artist = artist,
    preview = preview,
    picture = picture
)

fun MusicModel.toEntity() = MusicEntity(
    id = id,
    title = title,
    artist = artist,
    preview = preview,
    picture = picture
)