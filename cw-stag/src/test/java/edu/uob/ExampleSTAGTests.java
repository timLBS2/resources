package edu.uob;

import edu.uob.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ExampleSTAGTests {
  private GameServer server;

  // Create a new server _before_ every @Test
  @BeforeEach
  void setup() {
      File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  String sendCommandToServer(String command) {
      // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
      return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
      "Server took too long to respond (probably stuck in an infinite loop)");
  }

  // A lot of tests will probably check the game state using 'look' - so we better make sure 'look' works well !
  @Test
  void testLook() {
    String response = sendCommandToServer("simon: look");
    response = response.toLowerCase();
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
      System.out.println(response);
      System.out.println(response.contains("cabin"));
    assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
      System.out.println(response);
      System.out.println(response.contains("log cabin"));
    assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
    assertTrue(response.contains("forest"), "Did not see available paths in response to look");
  }

  // Test that we can pick something up and that it appears in our inventory
  @Test
  void testGet()
  {
      String response;
      sendCommandToServer("simon: get potion");
      response = sendCommandToServer("simon: inv");
      response = response.toLowerCase();
      assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
      response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
  }

  // Test that we can goto a different location (we won't get very far if we can't move around the game !)
  @Test
  void testGoto()
  {
      sendCommandToServer("simon: goto forest");
      String response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
  }

  // Add more unit tests or integration tests here.

    // Test that we can drop something and that it is no longer in our inventory
  @Test
  void testDrop()
  {
      sendCommandToServer("simon: drop potion");
      String response = sendCommandToServer("simon: inv");
      response = response.toLowerCase();
      assertFalse(response.contains("potion"), "Potion is still present in the inventory after an attempt was made to drop it");
      response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertTrue(response.contains("potion"), "Did not see the potion in the room after an attempt was made to drop it");
  }
    @Test
    void testUnlock()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        String response = sendCommandToServer("simon: unlock trapdoor");
        response = response.toLowerCase();
        assertTrue(response.contains("leading down into a cellar"), "Failed attempt to use 'unlock' command to unlock the trapdoor");
    }
    @Test
    void testHealth()
    {
        String response = sendCommandToServer("simon: drink potion");
        response = response.toLowerCase();
        assertTrue(response.contains("you drink the potion and your health improves"), "No potion.");
    }
    @Test
    void testFight()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        String response = sendCommandToServer("simon: fight elf");
        response = response.toLowerCase();
        assertTrue(response.contains("you attack the elf, but he fights back and you lose some health"), "No violence.");
    }
    @Test
    void testAfterLife()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: fight elf");
        sendCommandToServer("simon: fight elf");
        sendCommandToServer("simon: fight elf");
        assertEquals(GameMap.getPlayerByName("simon").getHealth(), 3, "Respawned with health.");
    }
    @Test
    void testCut()
    {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: cut down tree");
        response = response.toLowerCase();
        assertTrue(response.contains("cut down the tree with the axe"), "No log here.");
    }
    @Test
    void testProduce()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        String response = sendCommandToServer("simon: pay coin");
        response = response.toLowerCase();
        assertTrue(response.contains("pay the elf your silver coin and he produces a shovel"), "No log here.");
    }
    @Test
    void testBridge()
    {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        String response = sendCommandToServer("simon: bridge river");
        response = response.toLowerCase();
        assertTrue(response.contains("bridge the river with the log and can now reach the other side"), "No log here.");
    }
    @Test
    void testHorn()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        String response = sendCommandToServer("simon: blow");
        response = response.toLowerCase();
        assertFalse(response.contains("blow the horn and as if by magic, a lumberjack appears !"), "No log here.");
    }
    @Test
    void testGold()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: unlock trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: pay coin");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge river");
        sendCommandToServer("simon: goto clearing");
        String response = sendCommandToServer("simon: dig ground");
        response = response.toLowerCase();
        assertTrue(response.contains("dig into the soft ground and unearth a pot of gold"), "No log here.");
    }

    @Test
    void testChop()
    {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: chop tree with axe");
        response = response.toLowerCase();
        assertTrue(response.contains("cut down the tree with the axe"), "No log here.");
    }

    @Test
    void testBlowHorn()
    {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        String response = sendCommandToServer("simon: blow horn");
        response = response.toLowerCase();
        assertTrue(response.contains("blow the horn and as if by magic, a lumberjack appears !"), "No log here.");
    }

    @Test
    void testChopDisorder()
    {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: use axe to chop tree");
        response = response.toLowerCase();
        assertTrue(response.contains("cut down the tree with the axe"), "No log here.");
    }

    @Test
    void testLOOK() {
        String response = sendCommandToServer("simon: LOOK");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
    }
}
