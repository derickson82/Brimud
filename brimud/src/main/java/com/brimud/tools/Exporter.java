package com.brimud.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.w3._2001.xinclude.IncludeType;

import com.brimud.command.Direction;
import com.brimud.db.Transacted;
import com.brimud.db.ZoneDao;
import com.brimud.model.Room;
import com.brimud.model.Zone;
import com.brimud.world.WorldType;
import com.brimud.world.ZoneLinksType;
import com.brimud.world.ZoneLinksType.Link;
import com.brimud.zone.ExtraType;
import com.brimud.zone.ExtraType.Extra;
import com.brimud.zone.DirectionType;
import com.brimud.zone.ObjectFactory;
import com.brimud.zone.RoomType;
import com.brimud.zone.ZoneType;
import com.google.inject.Inject;

public class Exporter {
  private final ZoneDao zoneDao;
  private final org.w3._2001.xinclude.ObjectFactory xmlOf = new org.w3._2001.xinclude.ObjectFactory();
  private final com.brimud.world.ObjectFactory worldOf = new com.brimud.world.ObjectFactory();
  private final ObjectFactory of = new ObjectFactory();
  private final List<Link> zoneLinks = new ArrayList<Link>();

  @Inject
  Exporter(ZoneDao zoneDao) {
    this.zoneDao = zoneDao;
  }

  @Transacted
  public void export() {
    List<Zone> zones = zoneDao.getAll();
    
    List<String> zoneFileNames = new ArrayList<String>();
    for (Zone z : zones) {
      zoneFileNames.add(export(z));
    }
    
    export(zoneFileNames);
  }

  private void export(List<String> zoneFileNames) {
    WorldType worldType = worldOf.createWorldType();
    
    ZoneLinksType xmlZoneLinks = worldOf.createZoneLinksType();
    
    worldType.setZonelinks(xmlZoneLinks);
    
    List<Link> link = xmlZoneLinks.getLink();
    link.addAll(zoneLinks);
    
    List<IncludeType> includes = worldType.getInclude();
    
    for (String fileName : zoneFileNames) {
      IncludeType include = xmlOf.createIncludeType();
      include.setHref(fileName);
      includes.add(include);
    }
    
    try {
      FileOutputStream fos = new FileOutputStream("world.xml");
      JAXBContext newInstance = JAXBContext.newInstance(WorldType.class.getPackage().getName());
      Marshaller marshaller = newInstance.createMarshaller();
      
      marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new WorldPrefixMapper());
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(worldOf.createWorld(worldType), fos);
      
    } catch (IOException e) {
      throw new RuntimeException("File could not be created: world.xml", e);
    } 
    catch (JAXBException e) {
      throw new RuntimeException("Jaxb marshalling failure", e);
    }
    
  }
  
  /**
   * 
   * @param zone
   * @return the name of the xml file where the zone was exported
   * 
   */
  private String export(Zone zone) {
    ZoneType xmlZone = buildXml(zone);
    String zoneFileName = zone.getId() + ".xml";
    try {
      FileOutputStream fos = new FileOutputStream(zoneFileName);
      JAXBContext newInstance = JAXBContext.newInstance(ZoneType.class.getPackage().getName());
      Marshaller marshaller = newInstance.createMarshaller();
      
      marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new ZonePrefixMapper());
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(of.createZone(xmlZone), fos);
      
      return zoneFileName;
    } catch (IOException e) {
      throw new RuntimeException("File could not be created: " + zoneFileName, e);
    } 
    catch (JAXBException e) {
      throw new RuntimeException("Jaxb marshalling failure", e);
    }
    
  }

  private ZoneType buildXml(Zone zone) {
    ZoneType xmlZone = of.createZoneType();

    xmlZone.setId(zone.getId());
    xmlZone.setName(zone.getName());
    
    if (zone.getStartingRoom() != null) {
      xmlZone.setStartingRoom(zone.getStartingRoom());
    }
    xmlZone.setStartingZone(zone.isStartingZone());
    xmlZone.setDescription(zone.getDescription());

    List<RoomType> rooms = xmlZone.getRoom();

    for (Room r : zone.getRooms()) {
      rooms.add(buildXml(r));
    }

    return xmlZone;
  }

  /**
   * @param room
   * @return
   */
  private RoomType buildXml(Room room) {
    RoomType roomType = of.createRoomType();

    roomType.setId(room.getId().getRoomId());
    roomType.setName(room.getName());
    roomType.setShort(room.getShortDescription());
    roomType.setDescription(room.getDescription());

    extractDirectionInfo(room, roomType);
    
    roomType.setExtras(of.createExtraType());
    ExtraType extras = roomType.getExtras();
    List<Extra> extraList = extras.getExtra();
    
    for (Map.Entry<String, String> entry : room.getExtraDescriptions().entrySet()) {
      Extra extra = of.createExtraTypeExtra();
      extra.setKeyword(entry.getKey());
      extra.setValue(entry.getValue());
      extraList.add(extra);
    }

    return roomType;
  }

  private void extractDirectionInfo(Room room, RoomType roomType) {
    for (Direction d : Direction.values()) {
      Room exit = room.getExit(d);
      DirectionType dType = null;
      if (exit != null) {
        if (room.getId().getZoneId().equals(exit.getId().getZoneId())) {
          dType = of.createDirectionType();
          dType.setExitToRoom(exit.getId().getRoomId());
        } else {

          Link link = worldOf.createZoneLinksTypeLink();
          link.setFromRoom(room.getId().getRoomId());
          link.setFromZone(room.getId().getZoneId());
          link.setToRoom(exit.getId().getRoomId());
          link.setToZone(exit.getId().getZoneId());
          link.setDirection(d.getName());
          zoneLinks.add(link);
        }
      }
      String scan = room.getScanDescription(d);
      if (scan != null) {
        if (dType == null) {
          dType = of.createDirectionType();
        }
        dType.setValue(scan);
      }
      if (dType != null) {
        switch (d) {
        case NORTH:
          roomType.setNorth(dType);
          break;
        case EAST:
          roomType.setEast(dType);
          break;
        case SOUTH:
          roomType.setSouth(dType);
          break;
        case WEST:
          roomType.setWest(dType);
          break;
        case UP:
          roomType.setUp(dType);
          break;
        case DOWN:
          roomType.setDown(dType);
          break;
        }
      }
    }
  }
}
