package com.kopysoft.MorseMessenger.recieve;

/**
 * 			Copyright (C) 2011 by Ethan Hall
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *  
 */

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.StringMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

/** How all tones will be played
 * 
 * @author Ethan Hall
 */
public class PlayMessage extends BroadcastReceiver {

	private static final String TAG = Defines.TAG + " - PlayMessage";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	@Override
	public void onReceive(Context context, Intent intent) {
		String Message = intent.getExtras().getString("message").toUpperCase();
		int Speed = intent.getExtras().getInt("speed", 75);
		if(printDebugMessages) Log.d(TAG, Message);

		try{
			playMessage(Message, Speed, 100);
		} catch( Exception e){
			Log.d(TAG, e.toString());
		}
	}

	private void playMessage(String message, int delay, int volume) throws InterruptedException{
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,volume);
		char[] charMessage = message.toCharArray();
		StringMap map = new StringMap();
		int timeToPlay = 0;

		Log.d(TAG, Character.toString((char)charMessage[0]));

		for(int i = 0; i < charMessage.length; i++){
			String charToSend = map.getVal(Character.toString((char)charMessage[i]));
			if(charToSend != null){
				byte[] brokenString = charToSend.getBytes();
				for(int j = 0; j < brokenString.length; j++){

					if(((char)brokenString[j]) == '.'){
						timeToPlay = delay;
					} else if(((char)brokenString[j]) == '-') {
						timeToPlay = delay * 3;
					} else if(((char)brokenString[j]) == ' ') {
						timeToPlay = delay * 6;
					}

					if(((char)brokenString[j]) == ' '){
						tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay);
					} else {
						tg.startTone(ToneGenerator.TONE_DTMF_0, timeToPlay);
					}

					Thread.sleep(timeToPlay);

					tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay);

					Thread.sleep(delay);
				}
			}
		}
	}

}
