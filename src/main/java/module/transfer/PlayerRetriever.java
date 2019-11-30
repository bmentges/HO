// %1126721330885:hoplugins.transfers.utils%
package module.transfer;

import core.db.DBManager;
import core.model.HOVerwaltung;
import core.model.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.List;

/**
 * Utility to retrieve a player by an id, even if it is an old-player.
 *
 * @author <a href=mailto:nethyperon@users.sourceforge.net>Boy van der Werf</a>
 */
public final class PlayerRetriever {
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Private default constuctor to prevent class instantiation. 
     */
    private PlayerRetriever() {
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Retrieve a player by his ID.
     *
     * @param id Player ID
     *
     * @return ISpieler interface representing the found player or <code>null</code> if no player
     *         could be found.
     */
    public static Player getPlayer(int id) {
        final Player player = HOVerwaltung.instance().getModel().getSpieler(id);

        if (player == null) {
            final List<Player> oldPlayers = HOVerwaltung.instance().getModel().getAllOldSpieler();

            for (final Iterator<Player> iter = oldPlayers.iterator(); iter.hasNext();) {
                final Player oldPlayer = iter.next();

                if (oldPlayer.getSpielerID() == id) {
                    return oldPlayer;
                }
            }

            return null;
        } else {
            return player;
        }
    }
    /**
     * Retrieve a player by his transfer details.
     *
     * @param transfer Transfer information
     *
     * @return ISpieler interface representing the found player or <code>null</code> if no player
     *         could be found.
     */
    public static Player getPlayer(PlayerTransfer transfer) {
        Player player = getPlayer(transfer.getPlayerId());

        if (player != null) return player;

        List<Player> players = new ArrayList<Player>();
        players.addAll(HOVerwaltung.instance().getModel().getAllSpieler());
        players.addAll(HOVerwaltung.instance().getModel().getAllOldSpieler());

        List<Player> matches = new ArrayList<Player>();

        for (final Iterator<Player> iter = players.iterator(); iter.hasNext();) {
            player = iter.next();

            if (Objects.equals(player.getName(), transfer.getPlayerName())) {
                matches.add(player);
            }
        }

        if (matches.size() == 1) return matches.get(0);

        for (final Iterator<Player> iter = matches.iterator(); iter.hasNext();) {
            final Player match = iter.next();

            player = DBManager.instance().getSpielerAtDate(match.getSpielerID(), transfer.getDate());

            if(player == null) {
                iter.remove();
            }
        }

        if (matches.size() == 1) return matches.get(0);

        return null;
    }
}
