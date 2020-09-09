package chess.play;

import cse332.chess.server.Hub;

/**
 * You can use this client to have your bot automatically
 * start a match when run.  This will be particularly useful
 * when you want to have your bot play on a cloud instance. To
 * see how the game is going, you should observe the game using
 * the normal EasyChess interface.
 */
public class CloudClient {
    public static void main(String[] args) {
        String username = "YOUR_TEAMNAME_HERE";
        String password = "YOUR_TEAM_PASSWORD_HERE";
        String botToPlay = "THE_BOT_YOU_WANT_TO_FACE";

        System.out.println("Starting a match against " + botToPlay);
        Hub hub = new Hub(null, botToPlay);
        hub.login(username, password);
    }
}
