package hoppers.com.tamir.hoopers.bluetooth;

/**
 * Created by Tamir on 03/03/2018.
 * Event Indicating of a reject or accept dual (via bluetooth)
 */

public interface IOnChallengeResponse {
    void onAccepted();
    void onRejected();
}
