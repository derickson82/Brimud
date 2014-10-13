/**
 * 
 */
package com.brimud.command.builder;

import org.easymock.EasyMockSupport;
import org.junit.Test;

import com.brimud.db.PlayerDao;
import com.brimud.db.RoomDao;
import com.brimud.model.World;
import com.brimud.service.MessageService;

/**
 * @author dan
 *
 */
public class TestGotoCommand extends EasyMockSupport {

  private final MessageService messageService = createNiceMock(MessageService.class);

  private final World world = new World();
  
  private final PlayerDao playerDao = createNiceMock(PlayerDao.class);
  
  private final GotoCommand gotoCommand = new GotoCommand(messageService, world, playerDao);

  @Test(expected=NullPointerException.class)
  public void testNullPlayer() {
    gotoCommand.doCommand(null, null, null);
  }
  
//  @Test(expectedExceptions=NullPointerException.class)
//  public void testNullCommand() {
//    Player player = new Player("Dan");
//    gotoCommand.doCommand(player, null, null);
//  }
//  
//  @Test
//  public void testNullArguments() {
//    Player player = new Player("Dan");
//    
//    resetAll();
//    
//    Capture<Player> playerCapture = new Capture<Player>();
//    Capture<String> messageCapture = new Capture<String>();
//    messageService.sendMessage(EasyMock.capture(playerCapture), EasyMock.capture(messageCapture));
//    EasyMock.expectLastCall().once();    
//    replayAll();    
//    
//    gotoCommand.doCommand(player, GotoCommand.GOTO, null);
//    
//    Assert.assertEquals(player, playerCapture.getValue());
//    Assert.assertEquals("Goto where? who?", messageCapture.getValue());
//    
//    verifyAll();
//  }
  
  
}
