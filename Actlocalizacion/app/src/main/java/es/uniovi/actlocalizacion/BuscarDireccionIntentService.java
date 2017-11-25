package es.uniovi.actlocalizacion;


import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class BuscaDireccionIntentService extends IntentService {

    private static final String TAG = "BuscaDireccionIS";
    private static final int ERROR = -1;
    private ResultReceiver mReceiver;
    private int estado = 0;
    private String cadResultado;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startIntentService(Context context, ResultReceiver mResultReceiver, Location posicion) {
        // Crea un intent para pasar al intent service la responsabilidad de buscar la driección
        Intent intent = new Intent(context, BuscaDireccionIntentService.class);
        //intent.setAction(ACTION_FOO);

        // Pasa el receptor de resultados como un extra al servicio
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pasa la localización como un estra al servicio
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, posicion);

        /* Lanza el servicio. Si no está listo para ejecutarse, se instancia y comienza
           (creando un proceso si es neceasrio), si está ejecutandose continua la ejecución.
           El servicio termina automáticamente cuando el intent se procesa
        */
        context.startService(intent);
    }

    public BuscaDireccionIntentService() {
        super(TAG);
    }

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a {@link android.os.ResultReceiver} in * MainActivity to process content
     * sent from this service.
     * <p>
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        /****************************************************
         * Recupera los parámetros de llamada al intent
         ****************************************************/

        // Instancia a la que devolvemos el resultado
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Comprueba que el receptor este registrado correctamente
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Obtiene la localización pasada al servicio a través de un extra
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        // Nos aseguramos de que los datos recibidos son correctos.
        // Si no enviamos un error y salimos
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }

        /****************************************************
         * Realiza la llamada a geocoder
         ****************************************************/

        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.
        cadResultado = recuperarDireccion(location);

        /****************************************************
         * Envia resultados a la activity principal
         ****************************************************/
        if (estado == ERROR)
            deliverResultToReceiver(Constants.FAILURE_RESULT, cadResultado);
        else
            deliverResultToReceiver(Constants.SUCCESS_RESULT, cadResultado);

    }

    /**
     * Gestiona la llamada síncrona a Geocoder
     * Hace la petición con el parámetro posición y comprueba los posibles errores
     * Si hay error devuelve el mensaje de error, si no hay error concatena las líneas
     * de la dirección y la devuelve
     * Establece la variable estado a ERROR (-1) si se detecta algún error
     *
     * @param posicion parámetro Location con la posición de la que queremos recuperar la dirección
     * @return Cadena con el mensaje de error, si lo hubo, con la dirección completa
     */
    private String recuperarDireccion(Location posicion) {
        // Instanciamos objeto Geocoder limita las búsquedas a la región definida
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        String direccion = null;

        // Lista de direcciones que devolverá Geocoder.
        List<Address> addresses = null;

        try {
            /* getFromLo0cation() devuelve un array de direcciones que coinciden con el área
               que rodea la latitud / longitud que se proporciona. Los resultados son la
               mejor opción, pero no se asegura la precisión    */
            addresses = geocoder.getFromLocation(
                    posicion.getLatitude(),
                    posicion.getLongitude(),
                    // Aquí limitamos el número de direcciones devueltas
                    1);
        } catch (IOException ioException) {
            // Captura problemas de red y otros problemas de entrada/salida.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
            estado = ERROR;
        } catch (IllegalArgumentException illegalArgumentException) {
            // Captura valores no válidos para la latitud o longitud.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitud = " + posicion.getLatitude() +
                    ", Longitud = " + posicion.getLongitude(), illegalArgumentException);
            estado = ERROR;
        }

        // Gestiona el caso donde no se encontró ninguna dirección.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
                estado = ERROR;
            }
        } else {
            // Tenemos el array de direcciones
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            /* Recupera las líneas de la dirección usando {@code getAddressLine},
               y las une para devolverlas. Se podrían haber utilizado otros métodos para
               para recuperar la dirección completa {@link android.location.address}:
             */
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            // junta todas las líneas utilizando el salto de línea
            direccion = TextUtils.join(System.getProperty("line.separator"), addressFragments);
            Log.i(TAG, getString(R.string.address_found));
        }

        if (estado == ERROR)
            return errorMessage;
        else
            return direccion;
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }


}
