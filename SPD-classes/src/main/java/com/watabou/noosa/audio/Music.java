/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.watabou.noosa.Game;

public enum Music {
	
	INSTANCE;
	
	private MediaPlayer player;
	
	private String lastPlayed;
	private boolean looping;
	
	private boolean enabled = true;
	private float volume = 1f;
	
	public void play( String assetName, boolean looping ) {
		
		if (isPlaying() && lastPlayed != null && lastPlayed.equals( assetName )) {
			return;
		}
		
		stop();
		
		lastPlayed = assetName;
		this.looping = looping;

		if (!enabled || assetName == null) {
			return;
		}
		
		try {
			
			AssetFileDescriptor afd = Game.instance.getAssets().openFd( assetName );
			
			MediaPlayer mp = new MediaPlayer();
			mp.setAudioStreamType( AudioManager.STREAM_MUSIC );
			mp.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength() );
			mp.prepare();
			player = mp;
			player.start();
			player.setLooping(looping);
			player.setVolume(volume, volume);
			
		} catch (Exception e) {
			
			Game.reportException(e);
			player = null;
			
		}
	}
	
	public void mute() {
		lastPlayed = null;
		stop();
	}
	
	public void pause() {
		if (player != null) {
			player.pause();
		}
	}
	
	public void resume() {
		if (player != null) {
			player.start();
			player.setLooping(looping);
		}
	}
	
	public void stop() {
		if (player != null) {
			try {
				player.stop();
				player.release();
			} catch ( Exception e ){
				Game.reportException(e);
			}
			player = null;
		}
	}
	
	public void volume( float value ) {
		volume = value;
		if (player != null) {
			player.setVolume( value, value );
		}
	}
	
	public boolean isPlaying() {
		return player != null && player.isPlaying();
	}
	
	public void enable( boolean value ) {
		enabled = value;
		if (isPlaying() && !value) {
			stop();
		} else
		if (!isPlaying() && value) {
			play( lastPlayed, looping );
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public static final PhoneStateListener callMute = new PhoneStateListener(){
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			if( state == TelephonyManager.CALL_STATE_RINGING ) {
				INSTANCE.pause();
				
			} else if( state == TelephonyManager.CALL_STATE_IDLE ) {
				if (!Game.instance.isPaused()) {
					INSTANCE.resume();
				}
			}
			
			super.onCallStateChanged(state, incomingNumber);
		}
	};
	
	public static void setMuteListener(){
		//versions lower than this require READ_PHONE_STATE permission
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			TelephonyManager mgr =
					(TelephonyManager) Game.instance.getSystemService(Activity.TELEPHONY_SERVICE);
			mgr.listen(Music.callMute, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
}
