package sg.diploma.product.audio;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;

public final class AudioManager{
	private AudioManager(){
		view = null;
		res = null;

		musicMap = new HashMap<>();
		soundMap = new HashMap<>();

		musicVol = 4.0f;
		soundVol = 4.0f;
	}

	public void Init(SurfaceView _view){
		view = _view;
		res = _view.getResources();
	}

	public void PlayAudio(final int ID, final AudioTypes.AudioType type){
		final HashMap<Integer, MediaPlayer> audioMap = type == AudioTypes.AudioType.Music ? musicMap : soundMap;
		if(audioMap.containsKey(ID)){
			audioMap.get(ID).reset();
			audioMap.get(ID).start();
		}

		//* Load audio
		MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), ID);
		audioMap.put(ID, mediaPlayer);

		final float audioVol = type == AudioTypes.AudioType.Music ? musicVol : soundVol;
		mediaPlayer.setVolume(audioVol, audioVol);

		mediaPlayer.start();
		//*/
	}

	public boolean IsPlaying(final int ID, final AudioTypes.AudioType type){
		final HashMap<Integer, MediaPlayer> audioMap = type == AudioTypes.AudioType.Music ? musicMap : soundMap;

		if(!audioMap.containsKey(ID)){
			return false;
		}

		return audioMap.get(ID).isPlaying();
	}

	public void StopAudio(final int ID, final AudioTypes.AudioType type){
		final HashMap<Integer, MediaPlayer> audioMap = type == AudioTypes.AudioType.Music ? musicMap : soundMap;
		audioMap.get(ID).stop();
	}

	public void OnMusicVolChanged(final int amt){
	}

	public void OnSoundVolChanged(final int amt){
	}

	private SurfaceView view;
	private Resources res;

	private final HashMap<Integer, MediaPlayer> musicMap;
	private final HashMap<Integer, MediaPlayer> soundMap;

	private float musicVol;
	private float soundVol;

	public final static AudioManager Instance;

	static{
		Instance = new AudioManager();
	}
}