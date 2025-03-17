/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.noosa.audio;

import com.badlogic.gdx.Gdx;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public enum Music {
	
	INSTANCE;
	
	private com.badlogic.gdx.audio.Music player;
	
	private String lastPlayed;
	private boolean looping;
	
	private boolean enabled = true;
	private float volume = 1f;

	private float fadeTime = -1f;
	private float fadeTotal = -1f;
	private Callback onFadeOut = null;

	String[] trackList;
	float[] trackChances;
	private final ArrayList<String> trackQueue = new ArrayList<>();
	boolean shuffle = false;
	
	public synchronized void play( String assetName, boolean looping ) {

		//iOS cannot play ogg, so we use an mp3 alternative instead
		if (assetName != null && DeviceCompat.isiOS()){
			assetName = assetName.replace(".ogg", ".mp3");
		}
		
		if (isPlaying() && lastPlayed != null && lastPlayed.equals( assetName )) {
			player.setVolume(volumeWithFade());
			return;
		}
		
		stop();
		
		lastPlayed = assetName;
		trackList = null;

		this.looping = looping;
		this.shuffle = false;

		if (!enabled || assetName == null) {
			return;
		}

		play(assetName, null);
	}

	public synchronized void playTracks( String[] tracks, float[] chances, boolean shuffle){

		if (tracks == null || tracks.length == 0 || tracks.length != chances.length){
			stop();
			return;
		}

		//iOS cannot play ogg, so we use an mp3 alternative instead
		if (tracks != null && DeviceCompat.isiOS()){
			for (int i = 0; i < tracks.length; i ++){
				tracks[i] = tracks[i].replace(".ogg", ".mp3");
			}
		}

		if (isPlaying() && this.trackList != null && tracks.length == trackList.length){

			//lists are considered the same if they are identical or merely shifted
			// e.g. the regular title theme and the victory theme are considered equivalent
			boolean sameList = false;
			for (int ofs = 0; ofs < tracks.length; ofs++){
				sameList = true;
				for (int j = 0; j < tracks.length; j++){
					int i = (j+ofs)%tracks.length;
					if (!tracks[i].equals(trackList[j]) || chances[i] != trackChances[j]){
						sameList = false;
						break;
					}
				}
				if (sameList) break;
			}

			if (sameList) {
				player.setVolume(volumeWithFade());
				return;
			}
		}

		stop();

		lastPlayed = null;
		trackList = tracks;
		trackChances = chances;
		trackQueue.clear();

		for (int i = 0; i < trackList.length; i++){
			if (Random.Float() < trackChances[i]){
				trackQueue.add(trackList[i]);
			}
		}

		this.looping = false;
		this.shuffle = shuffle;

		if (!enabled || trackQueue.isEmpty()){
			return;
		}

		play(trackQueue.remove(0), trackLooper);
	}

	public synchronized void fadeOut(float duration, Callback onComplete){
		if (fadeTotal == -1f) {
			fadeTotal = duration;
			fadeTime = 0f;
		} else {
			fadeTime = (fadeTime/fadeTotal) * duration;
			fadeTotal = duration;
		}
		onFadeOut = onComplete;
	}

	public synchronized void update(){
		if (fadeTotal > 0f && !paused){
			fadeTime += Game.elapsed;

			if (player != null) {
				player.setVolume(volumeWithFade());
			}

			if (fadeTime >= fadeTotal) {
				fadeTime = fadeTotal = -1f;
				if (onFadeOut != null){
					onFadeOut.call();
				}
			}
		}
	}

	private com.badlogic.gdx.audio.Music.OnCompletionListener trackLooper = new com.badlogic.gdx.audio.Music.OnCompletionListener() {
		@Override
		public void onCompletion(com.badlogic.gdx.audio.Music music) {
			//don't play the next track if we're currently in the middle of a fade
			if (fadeTotal == -1f) {
				//we do this in a separate thread to avoid graphics hitching while the music is prepared
				if (!DeviceCompat.isDesktop()) {
					new Thread() {
						@Override
						public void run() {
							playNextTrack(music);
						}
					}.start();
				} else {
					//don't use a separate thread on desktop, causes errors and makes no performance difference
					playNextTrack(music);
				}
			}
		}
	};

	private synchronized void playNextTrack(com.badlogic.gdx.audio.Music music){
		if (trackList == null || trackList.length == 0 || music != player || player.isLooping()){
			return;
		}

		Music.this.stop();

		if (trackQueue.isEmpty()) {
			for (int i = 0; i < trackList.length; i++) {
				if (Random.Float() < trackChances[i]) {
					trackQueue.add(trackList[i]);
				}
			}
			if (shuffle) Collections.shuffle(trackQueue);
		}

		if (!enabled || trackQueue.isEmpty()) {
			return;
		}

		play(trackQueue.remove(0), trackLooper);
	};

	private synchronized void play(String track, com.badlogic.gdx.audio.Music.OnCompletionListener listener){
		try {
			fadeTime = fadeTotal = -1;

			player = Gdx.audio.newMusic(Gdx.files.internal(track));
			player.setLooping(looping);
			player.setVolume(volumeWithFade());
			if (!paused) player.play();
			if (listener != null) {
				player.setOnCompletionListener(listener);
			}
		} catch (Exception e){
			Game.reportException(e);
			player = null;
		}
	}
	
	public synchronized void end() {
		lastPlayed = null;
		trackList = null;
		stop();
	}

	private boolean paused = false;

	public synchronized boolean paused(){
		return paused;
	}
	
	public synchronized void pause() {
		paused = true;
		if (player != null) {
			player.pause();
		}
	}
	
	public synchronized void resume() {
		paused = false;
		if (player != null) {
			player.play();
			player.setLooping(looping);
		}
	}

	public synchronized void stop() {
		if (player != null) {
			player.dispose();
			player = null;
		}
	}
	
	public synchronized void volume( float value ) {
		volume = value;
		if (player != null) {
			player.setVolume( volumeWithFade() );
		}
	}

	private synchronized float volumeWithFade(){
		if (fadeTotal > 0f){
			return Math.max(0, volume * ((fadeTotal - fadeTime) / fadeTotal));
		} else {
			return volume;
		}
	}
	
	public synchronized boolean isPlaying() {
		return player != null && player.isPlaying();
	}
	
	public synchronized void enable( boolean value ) {
		enabled = value;
		if (isPlaying() && !value) {
			stop();
		} else
		if (!isPlaying() && value) {
			if (trackList != null){
				playTracks(trackList, trackChances, shuffle);
			} else if (lastPlayed != null) {
				play(lastPlayed, looping);
			}
		}
	}
	
	public synchronized boolean isEnabled() {
		return enabled;
	}
	
}
