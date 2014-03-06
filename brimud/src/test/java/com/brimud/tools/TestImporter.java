/**
 * 
 */
package com.brimud.tools;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMockSupport;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

import com.brimud.command.Direction;
import com.brimud.db.RoomDao;
import com.brimud.db.ZoneDao;
import com.brimud.zone.DirectionType;
import com.brimud.zone.ExtraType;
import com.brimud.zone.ExtraType.Extra;
import com.brimud.zone.RoomType;
import com.brimud.world.WorldType;
import com.brimud.world.ZoneLinksType;
import com.brimud.world.ZoneLinksType.Link;
import com.brimud.zone.ZoneType;
import com.brimud.model.Room;
import com.brimud.model.Zone;
import com.brimud.service.BuilderService;

//  
// TODO map zone jaxb object to model object 
// TODO map world jaxb objects 
// TODO insert? merge? replace? loaded zone with zone in the database

// DONE:
// read single zone file into jaxb objects
// read world xml and all included zone files into jaxb objects 
// validate the xml when it is parsed

/**
 * 
 * 
 * @author dan
 * 
 */
public class TestImporter extends EasyMockSupport {

  private static final String TEST_ZONE_LOCATION = "src/test/resources/testzone.xml";
  private static final String TEST_WORLD_LOCATION = "src/test/resources/testworld.xml";

  private final ZoneDao zoneDao = createNiceMock(ZoneDao.class);
  private final RoomDao roomDao = createNiceMock(RoomDao.class);
  private final BuilderService builderService = createNiceMock(BuilderService.class);
  
  private final Importer importer = new Importer(zoneDao, roomDao, builderService);

  @Test
  public void testMappingZoneTypeToZone() {
    ZoneType zoneType = new ZoneType();
    zoneType.setId("zoneId");
    zoneType.setName("Zone Name");
    zoneType.setDescription("Zone Description");
    zoneType.setShort("Zone Short Description");
    zoneType.setStartingRoom("room1");
    zoneType.setStartingZone(Boolean.TRUE);
    
    RoomType roomType = new RoomType();
    roomType.setId("room1");
    roomType.setName("Room Name");
    roomType.setDescription("Room Description");
    roomType.setShort("Room Short Description");

    DirectionType north = new DirectionType();
    north.setExitToRoom("room1");
    north.setValue("This room lies to the north");
    roomType.setNorth(north);

    DirectionType south = new DirectionType();
    south.setExitToRoom("room1");
    south.setValue("This room lies to the south");
    roomType.setSouth(south);
    
    DirectionType east = new DirectionType();
    east.setExitToRoom("room1");
    east.setValue("This room lies to the east");
    roomType.setEast(east);

    DirectionType west = new DirectionType();
    west.setExitToRoom("room1");
    west.setValue("This room lies to the west");
    roomType.setWest(west);

    DirectionType up = new DirectionType();
    up.setExitToRoom("room1");
    up.setValue("This room lies above");
    roomType.setUp(up);

    DirectionType down = new DirectionType();
    down.setExitToRoom("room1");
    down.setValue("This room lies below");
    roomType.setDown(down);
    
    zoneType.getRoom().add(roomType);
    
    Zone zone = importer.mapToXml(zoneType);
    
    assertNotNull(zone.getId());
    assertEquals(zone.getId(), zoneType.getId());
    assertZone(zoneType, zone.getId(), zone.getName(), zone.getDescription(), zone.getShortDescription(),zone.getStartingRoom(), zone.isStartingZone());

    Room room = zone.getRooms().iterator().next();        

    assertRoom(roomType, room.getId().getRoomId(), room.getName(), room.getDescription(), room.getShortDescription());
    
    assertDirection(room, Direction.NORTH, roomType.getNorth());
    assertDirection(room, Direction.SOUTH, roomType.getSouth());
    assertDirection(room, Direction.EAST, roomType.getEast());
    assertDirection(room, Direction.WEST, roomType.getWest());
    assertDirection(room, Direction.WEST, roomType.getWest());
    assertDirection(room, Direction.WEST, roomType.getWest());
    
    for (Map.Entry<String, String> entry : room.getExtraDescriptions().entrySet()) {
      assertEquals(entry.getKey(), "extraKey");
      assertEquals(entry.getValue(), "extraValue");
    }
    
  }
  
  private void assertDirection(Room room, Direction d, DirectionType directionType) {
    Room exit = room.getExit(d);
    assertNotNull(exit);
    assertEquals(exit.getId().getRoomId(), directionType.getExitToRoom());
    
    String scanDescription = room.getScanDescription(d);
    assertEquals(scanDescription, directionType.getValue());
    
  }
  
  @Test
  public void testLoadingWorldFile() throws FileNotFoundException {
    InputStream xmlInputStream = new FileInputStream(TEST_WORLD_LOCATION);
    InputSource inputSource = new InputSource(xmlInputStream);
    inputSource.setSystemId(TEST_WORLD_LOCATION);
    WorldType world = importer.parseWorld(inputSource);
    
    assertNotNull(world);
    
    ZoneLinksType zonelinks = world.getZonelinks();
    assertNotNull(zonelinks);
    assertEquals(zonelinks.getLink().size(), 2);
    for (Link link : zonelinks.getLink()) {

      // the room links to itself
      assertTrue("west".equals(link.getDirection()) || "east".equals(link.getDirection()));
      assertEquals(link.getToRoom(), "room1");
      assertEquals(link.getToZone(), "testzone");
      assertEquals(link.getFromRoom(), "room1");
      assertEquals(link.getFromZone(), "testzone");
    }
    
    // this tests whether the includes are being processed
    assertEquals(world.getZone().size(), 1);
    assertEquals(world.getInclude().size(), 0);
    assertZone(world.getZone().get(0), "testzone", "Test Zone", "Zone description", "Zone short description", "room1", true);
    assertRoom(world.getZone().get(0).getRoom().get(0), "room1", "Room Name", "Room Description", "Room short description");
  }
  
  @Test
  public void testLoadingZoneFile() throws FileNotFoundException {
    InputStream xmlInputStream = new FileInputStream(TEST_ZONE_LOCATION);
    
    // input sources must contain a systemId in order to process includes correctly.
    // TODO maybe create an InputSource builder or something to require a system id?
    InputSource inputSource = new InputSource(xmlInputStream);
    inputSource.setSystemId(TEST_ZONE_LOCATION);

    ZoneType zone = importer.parseZone(inputSource);
    assertZone(zone, "testzone", "Test Zone", "Zone description", "Zone short description", "room1", true);

    // Zone.getRoom() lazily creates the list
    List<RoomType> rooms = zone.getRoom();
    assertEquals(rooms.size(), 1);
    for (RoomType room : rooms) {
      assertRoom(room, "room1", "Room Name", "Room Description", "Room short description");

      DirectionType north = room.getNorth();
      assertNotNull(north);
      assertEquals(north.getExitToRoom(), "room1");
      assertEquals(north.getValue(), "An exit to the north");

      ExtraType extraType = room.getExtras();
      List<Extra> extras = extraType.getExtra();
      assertEquals(extras.size(), 1);
      for (Extra extra : extras) {
        assertEquals(extra.getKeyword(), "extraKey");
        assertEquals(extra.getValue(), "extraValue");
      }
    }
  }

  private void assertZone(ZoneType zone, String id, String name, String description, String shortDescription,
      String startingRoom, Boolean startingZone) {
    assertNotNull(zone);
    assertEquals(zone.getName(), name);
    assertEquals(zone.getDescription(), description);
    assertEquals(zone.getId(), id);
    assertEquals(zone.getShort(), shortDescription);
    assertEquals(zone.getStartingRoom(), startingRoom);
    assertEquals(zone.isStartingZone(), startingZone);
  }

  private void assertRoom(RoomType room, String id, String name, String description, String shortDescription) {
    assertEquals(room.getId(), id);
    assertEquals(room.getName(), name);
    assertEquals(room.getDescription(), description);
    assertEquals(room.getShort(), shortDescription);
  }
}
