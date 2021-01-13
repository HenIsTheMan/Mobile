package sg.diploma.product.currency;

import android.view.SurfaceView;

import sg.diploma.product.load_and_save.SharedPrefsManager;

public final class CurrencyManager{
	private CurrencyManager(){
		view = null;

		amtOfCoins = 0;
	}

	public void Init(SurfaceView _view){
		view = _view;
	}

	public void LoadCurrencyData(){
		final Integer coinData = SharedPrefsManager.Instance.LoadDataInt("CurrencyData", "CoinData");
		amtOfCoins = coinData == null ? 999 : coinData;
	}

	public void SaveCurrencyData(){
		SharedPrefsManager.Instance.SaveDataInt("CurrencyData", "CoinData", amtOfCoins);
	}

	public int GetAmtOfCoins(){
		return amtOfCoins;
	}

	public void SetAmtOfCoins(final int amtOfCoins){
		this.amtOfCoins = amtOfCoins;
	}

	private SurfaceView view;

	private int amtOfCoins;

	public final static CurrencyManager Instance;

	static{
		Instance = new CurrencyManager();
	}
}