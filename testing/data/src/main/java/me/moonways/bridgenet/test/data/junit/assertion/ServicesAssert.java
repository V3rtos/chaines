package me.moonways.bridgenet.test.data.junit.assertion;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.model.message.CreateGame;
import me.moonways.bridgenet.model.service.economy.currency.bank.BankTransaction;
import me.moonways.bridgenet.model.service.games.ActiveGame;
import me.moonways.bridgenet.model.service.games.Game;
import me.moonways.bridgenet.model.service.games.GameState;
import me.moonways.bridgenet.model.service.games.GameStatus;
import me.moonways.bridgenet.model.service.language.Language;
import me.moonways.bridgenet.test.data.TestConst;
import org.junit.Assert;

import java.rmi.RemoteException;

import static junit.framework.TestCase.assertEquals;

@UtilityClass
public class ServicesAssert {

    public void assertGame(Game game, CreateGame.Result subj, GameStatus gameStatus) throws RemoteException {
        Assert.assertNotNull(game);
        Assert.assertNotNull(subj);
        Assert.assertNotNull(gameStatus);

        ActiveGame activeGame = game.getActiveGame(subj.getActiveId()).get();
        GameState state = activeGame.getState();

        Assert.assertEquals(game.getName(), TestConst.Game.NAME);
        Assert.assertEquals(activeGame.getMap(), TestConst.Game.MAP);

        Assert.assertEquals(state.getMaxPlayers(), 2);
        Assert.assertEquals(state.getPlayersInTeam(), 1);
        Assert.assertEquals(state.getPlayers(), 0);
        Assert.assertEquals(state.getSpectators(), 0);
        Assert.assertEquals(state.getStatus(), gameStatus);
    }

    public void assertLanguage(Language expect, Language actual) {
        Assert.assertNotNull(expect);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expect.getId(), actual.getId());
        Assert.assertEquals(expect.getName(), actual.getName());
    }

    public void assertBankTransaction(BankTransaction transaction, int receivedExpect, int spentExpect, int currentExpect) {
        assertEquals(BankTransaction.Result.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(TestConst.Economy.CURRENCY, transaction.getState().getCurrency());
        assertEquals(receivedExpect, transaction.getState().getReceived());
        assertEquals(spentExpect, transaction.getState().getSpent());
        assertEquals(currentExpect, transaction.getState().getCurrent());
    }
}
