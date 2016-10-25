package mx.peta.inmovilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.peta.inmovilia.validadorWebService.AvaluoValido;
import mx.peta.inmovilia.validadorWebService.AvaluoValidoJsonResult;
import mx.peta.inmovilia.validadorWebService.EndPointsInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.llamarWS).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Touch", Toast.LENGTH_LONG).show();
        /*
            La dirección del servidor es:

            http://valjson.artica.com.mx/validador.svc
            esta liga devuelve la descripción del servicio

         */
        final String BASE_URL = "http://valjson.artica.com.mx/";
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        EndPointsInterface apiService = retrofit.create(EndPointsInterface.class);
        //tipologia=2
        //&CP=4318&delegacion=9003&entidad=9&proximidadUrbana=1&claseInmueble=5&vidautil=720&superTerreno=400&superConstruido=400
        // &valConst=4000000&valConcluido=8000000&revisadoManualmente=0&USER=rayo&PASSWORD=rayo&sensibilidad=3.5
        Call<AvaluoValido> callApiService = apiService.getAvaluo(2.0, 4318.0, 9003.0, 9.0, 1.0, 5.0, 720.0, 400.0, 400.0, 4000000.0,8000000.0,0,"rayo","rayo",3.5);
        callApiService.enqueue(new Callback<AvaluoValido> (){
            @Override
            public void onResponse(Call<AvaluoValido> call, Response<AvaluoValido> response) {
                Toast.makeText(getApplicationContext(), "Datos del avaluo", Toast.LENGTH_LONG).show();
                AvaluoValidoJsonResult res = response.body().getAvaluoValidoJsonResult();
                Toast.makeText(getApplicationContext(),"Avaluo válido " + res.getAvaluoValido().toString() + "\n " +
                                                       "Valor estimado " + res.getValorEstimado().toString() + "\n " +
                                                       "DS " + res.getDesStn().toString() + "\n " +
                                                       res.getTipoModelo() + "\n " +
                                                       res.getErrorStatus(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AvaluoValido> call, Throwable t) {

            }
        });
    }
}
