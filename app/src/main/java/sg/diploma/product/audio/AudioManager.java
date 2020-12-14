package sg.diploma.product.audio;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;

public final class AudioManager{
	private AudioManager(){
		view = null;
		res = null;

		audioMap = new HashMap<Integer, MediaPlayer>();
	}

	public void Init(SurfaceView _view){
		view = _view;
		res = _view.getResources();
	}

	public void PlayAudio(int _id, float _vol){
		if(audioMap.containsKey(_id)){
			audioMap.get(_id).reset();
			audioMap.get(_id).start();
		}

		///Load audio
		MediaPlayer newAudio = MediaPlayer.create(view.getContext(), _id);
		audioMap.put(_id, newAudio);
		newAudio.setVolume(_vol, _vol); //volume value range from 0.0 to 1.0
		newAudio.start();
	}

	public boolean IsPlaying(int _id){
		if(!audioMap.containsKey(_id)){
			return false;
		}

		return audioMap.get(_id).isPlaying();
	}

	public void StopAudio(int _id){
		MediaPlayer newAudio = audioMap.get(_id);
		newAudio.stop();
	}

	private SurfaceView view;
	private Resources res;

	private final HashMap<Integer, MediaPlayer> audioMap;

	public final static AudioManager Instance;

	static{
		Instance = new AudioManager();
	}
}