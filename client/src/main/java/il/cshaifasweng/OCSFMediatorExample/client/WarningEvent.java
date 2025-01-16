package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

/**
 * Class representing an event triggered when a warning is received from the server.
 */
public class WarningEvent {
	private Warning warning;

	/**
	 * Constructor to initialize the event with a warning.
	 * @param warning The warning object sent by the server.
	 */
	public WarningEvent(Warning warning) {
		this.warning = warning;
	}

	/**
	 * Retrieves the warning associated with the event.
	 * @return The warning object.
	 */
	public Warning getWarning() {
		return this.warning;
	}
}
