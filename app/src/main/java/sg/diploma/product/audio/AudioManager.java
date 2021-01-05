package sg.diploma.product.audio;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Objects;

import sg.diploma.product.load_and_save.SharedPrefsManager;

public final class AudioManager{
	private AudioManager(){
		view = null;
		res = null;

		musicMap = new HashMap<>();
		soundMap = new HashMap<>();

		musicVol = 0.0f;
		soundVol = 0.0f;
	}

	public void Init(SurfaceView _view){
		view = _view;
		res = _view.getResources();
	}

	public void PlayAudio(final int ID, final AudioTypes.AudioType type){
		if(AudioManager.Instance.IsPlaying(ID, type)){
			return;
		}

		final HashMap<Integer, MediaPlayer> audioMap = type == AudioTypes.AudioType.Music ? musicMap : soundMap;
		if(audioMap.containsKey(ID)){
			Objects.requireNonNull(audioMap.get(ID)).reset();
			Objects.requireNonNull(audioMap.get(ID)).start();
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

	public void OnMusicVolChanged(final float amt){
		musicVol = amt;
		for(MediaPlayer mediaPlayer: musicMap.values()){
			mediaPlayer.setVolume(musicVol, musicVol);
		}
	}

	public void OnSoundVolChanged(final float amt){
		soundVol = amt;
		for(MediaPlayer mediaPlayer: soundMap.values()){
			mediaPlayer.setVolume(soundVol, soundVol);
		}
	}

	public void LoadAudioVolData(){
		final Float musicVolFloat = SharedPrefsManager.Instance.LoadDataFloat("AudioVolData", "MusicVolData");
		final Float soundVolFloat = SharedPrefsManager.Instance.LoadDataFloat("AudioVolData", "SoundVolData");
		musicVol = musicVolFloat == null ? 1.0f : musicVolFloat;
		soundVol = soundVolFloat == null ? 1.0f : soundVolFloat;
	}

	public void SaveAudioVolData(){
		SharedPrefsManager.Instance.SaveDataFloat("AudioVolData", "MusicVolData", musicVol);
		SharedPrefsManager.Instance.SaveDataFloat("AudioVolData", "SoundVolData", soundVol);
	}

	public float GetMusicVol(){
		return musicVol;
	}

	public float GetSoundVol(){
		return soundVol;
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